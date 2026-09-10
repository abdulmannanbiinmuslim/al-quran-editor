package com.example.data.audio

import android.content.Context
import android.util.Log
import com.example.data.model.ReciterItem
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.data.timing.ReciterTimingRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

enum class DownloadMode(val title: String, val banglaTitle: String, val description: String) {
    AYAH_BY_AYAH("Ayah by Ayah", "আয়াত ভিত্তিক (Ayah by Ayah)", "প্রতিটি আয়াত আলাদা আলাদা অডিও ফাইল হিসেবে ডাউনলোড"),
    SURAH_BY_SURAH("Surah by Surah", "সূরা ভিত্তিক (Surah by Surah)", "সম্পূর্ণ সূরা একটি একক অডিও ফাইল হিসেবে ডাউনলোড")
}

sealed class SurahDownloadStatus {
    object NotDownloaded : SurahDownloadStatus()
    data class Downloading(
        val progress: Float = 0f,
        val downloadedAyahs: Int = 0,
        val totalAyahs: Int = 0,
        val downloadedBytes: Long = 0L,
        val totalBytes: Long = 0L
    ) : SurahDownloadStatus()
    data class Downloaded(val fileSizeBytes: Long) : SurahDownloadStatus()
    data class Error(val message: String) : SurahDownloadStatus()
}

class QuranDownloadManager(private val context: Context) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val activeDownloadJobs = ConcurrentHashMap<Int, Job>()
    private var bulkJob: Job? = null

    val timingRepository = ReciterTimingRepository(context)

    private val _downloadMode = MutableStateFlow(DownloadMode.AYAH_BY_AYAH)
    val downloadMode = _downloadMode.asStateFlow()

    private val _surahStatusMap = MutableStateFlow<Map<Int, SurahDownloadStatus>>(emptyMap())
    val surahStatusMap = _surahStatusMap.asStateFlow()

    private val _downloadedCount = MutableStateFlow(0)
    val downloadedCount = _downloadedCount.asStateFlow()

    private val _usedStorageBytes = MutableStateFlow(0L)
    val usedStorageBytes = _usedStorageBytes.asStateFlow()

    private val _isBulkDownloading = MutableStateFlow(false)
    val isBulkDownloading = _isBulkDownloading.asStateFlow()

    private val _bulkProgress = MutableStateFlow(0f)
    val bulkProgress = _bulkProgress.asStateFlow()

    private val _bulkCurrentSurahNumber = MutableStateFlow(1)
    val bulkCurrentSurahNumber = _bulkCurrentSurahNumber.asStateFlow()

    private var currentReciter: ReciterItem? = null

    fun setDownloadMode(mode: DownloadMode, reciter: ReciterItem) {
        _downloadMode.value = mode
        refreshStatuses(reciter)
    }

    /**
     * Get root directory for audio files:
     * App internal/external storage: <filesDir>/quran_audio/<reciter_id>/<mode>
     */
    private fun getAudioDirectory(reciter: ReciterItem, mode: DownloadMode): File {
        val baseDir = context.getExternalFilesDir(null) ?: context.filesDir
        val modeDirName = if (mode == DownloadMode.SURAH_BY_SURAH) "surah_full" else "ayah_by_ayah"
        val dir = File(baseDir, "quran_audio/${reciter.id}/$modeDirName")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun getLocalAudioFile(reciter: ReciterItem, surahNumber: Int, ayahNumber: Int? = null): File? {
        val s = surahNumber.toString().padStart(3, '0')
        // Check Ayah by Ayah file first
        if (ayahNumber != null) {
            val a = ayahNumber.toString().padStart(3, '0')
            val ayahDir = getAudioDirectory(reciter, DownloadMode.AYAH_BY_AYAH)
            val ayahFile = File(ayahDir, "${s}${a}.mp3")
            if (ayahFile.exists() && ayahFile.length() > 1024) {
                return ayahFile
            }
        }

        // Check Surah file
        val surahDir = getAudioDirectory(reciter, DownloadMode.SURAH_BY_SURAH)
        val surahFile = File(surahDir, "${s}.mp3")
        if (surahFile.exists() && surahFile.length() > 1024) {
            return surahFile
        }

        return null
    }

    fun refreshStatuses(reciter: ReciterItem) {
        this.currentReciter = reciter
        scope.launch {
            val mode = _downloadMode.value
            val dir = getAudioDirectory(reciter, mode)
            val statusMap = mutableMapOf<Int, SurahDownloadStatus>()
            var totalDownloaded = 0
            var totalBytes = 0L

            for (surah in QuranData.surahs) {
                val surahNumber = surah.number
                val s = surahNumber.toString().padStart(3, '0')

                if (mode == DownloadMode.SURAH_BY_SURAH) {
                    val file = File(dir, "$s.mp3")
                    if (file.exists() && file.length() > 1024) {
                        val size = file.length()
                        statusMap[surahNumber] = SurahDownloadStatus.Downloaded(size)
                        totalDownloaded++
                        totalBytes += size
                    } else if (activeDownloadJobs.containsKey(surahNumber)) {
                        statusMap[surahNumber] = _surahStatusMap.value[surahNumber] ?: SurahDownloadStatus.Downloading()
                    } else {
                        statusMap[surahNumber] = SurahDownloadStatus.NotDownloaded
                    }
                } else {
                    // Ayah by Ayah mode: Check all ayahs in this surah
                    val ayahsCount = surah.totalAyahs
                    var existingAyahsCount = 0
                    var surahAyahsBytes = 0L

                    for (a in 1..ayahsCount) {
                        val aStr = a.toString().padStart(3, '0')
                        val file = File(dir, "$s$aStr.mp3")
                        if (file.exists() && file.length() > 512) {
                            existingAyahsCount++
                            surahAyahsBytes += file.length()
                        }
                    }

                    if (existingAyahsCount == ayahsCount && ayahsCount > 0) {
                        statusMap[surahNumber] = SurahDownloadStatus.Downloaded(surahAyahsBytes)
                        totalDownloaded++
                        totalBytes += surahAyahsBytes
                    } else if (activeDownloadJobs.containsKey(surahNumber)) {
                        statusMap[surahNumber] = _surahStatusMap.value[surahNumber] ?: SurahDownloadStatus.Downloading()
                    } else if (existingAyahsCount > 0) {
                        // Partially downloaded
                        statusMap[surahNumber] = SurahDownloadStatus.Downloading(
                            progress = existingAyahsCount.toFloat() / ayahsCount,
                            downloadedAyahs = existingAyahsCount,
                            totalAyahs = ayahsCount,
                            downloadedBytes = surahAyahsBytes
                        )
                        totalBytes += surahAyahsBytes
                    } else {
                        statusMap[surahNumber] = SurahDownloadStatus.NotDownloaded
                    }
                }
            }

            _surahStatusMap.value = statusMap
            _downloadedCount.value = totalDownloaded
            _usedStorageBytes.value = totalBytes
        }
    }

    /**
     * Download an individual Surah
     */
    fun downloadSurah(reciter: ReciterItem, surahNumber: Int) {
        if (activeDownloadJobs.containsKey(surahNumber)) return

        val job = scope.launch {
            try {
                val mode = _downloadMode.value
                val surah = QuranData.surahs.find { it.number == surahNumber } ?: return@launch
                val dir = getAudioDirectory(reciter, mode)
                val s = surahNumber.toString().padStart(3, '0')

                if (mode == DownloadMode.SURAH_BY_SURAH) {
                    // Update status
                    updateSurahStatus(surahNumber, SurahDownloadStatus.Downloading(0.05f))
                    val destinationFile = File(dir, "$s.mp3")
                    val tempFile = File(dir, "$s.mp3.tmp")

                    // Attempt primary URL: check JSON metadata first (e.g. tarteel audio-cdn URL), then EveryAyah
                    val jsonSurahUrl = timingRepository.getSurahAudioUrl(reciter, surahNumber)
                    val primaryUrl = jsonSurahUrl ?: RecitersData.getSurahFullAudioUrl(reciter, surahNumber)
                    val success = downloadSingleFile(primaryUrl, tempFile) { progress, downloadedBytes, totalBytes ->
                        updateSurahStatus(
                            surahNumber,
                            SurahDownloadStatus.Downloading(
                                progress = progress,
                                downloadedBytes = downloadedBytes,
                                totalBytes = totalBytes
                            )
                        )
                    }

                    if (success && tempFile.exists() && tempFile.length() > 1024) {
                        tempFile.renameTo(destinationFile)
                        updateSurahStatus(surahNumber, SurahDownloadStatus.Downloaded(destinationFile.length()))
                        try {
                            timingRepository.generateAndSaveReciterTimingFile(reciter, listOf(surahNumber))
                        } catch (e: Exception) {
                            Log.w("QuranDownloadManager", "Could not compile timing file: ${e.message}")
                        }
                    } else {
                        // Try fallback URL if needed
                        val fallbackUrl = "https://server8.mp3quran.net/afs/$s.mp3"
                        val fallbackSuccess = downloadSingleFile(fallbackUrl, tempFile) { progress, downloadedBytes, totalBytes ->
                            updateSurahStatus(
                                surahNumber,
                                SurahDownloadStatus.Downloading(
                                    progress = progress,
                                    downloadedBytes = downloadedBytes,
                                    totalBytes = totalBytes
                                )
                            )
                        }
                        if (fallbackSuccess && tempFile.exists() && tempFile.length() > 1024) {
                            tempFile.renameTo(destinationFile)
                            updateSurahStatus(surahNumber, SurahDownloadStatus.Downloaded(destinationFile.length()))
                            try {
                                timingRepository.generateAndSaveReciterTimingFile(reciter, listOf(surahNumber))
                            } catch (e: Exception) {
                                Log.w("QuranDownloadManager", "Could not compile timing file: ${e.message}")
                            }
                        } else {
                            tempFile.delete()
                            updateSurahStatus(surahNumber, SurahDownloadStatus.Error("ডাউনলোড ব্যর্থ হয়েছে"))
                        }
                    }
                } else {
                    // Ayah by Ayah mode
                    val ayahsCount = surah.totalAyahs
                    var totalSurahBytes = 0L
                    var successfullyDownloadedAyahs = 0

                    updateSurahStatus(
                        surahNumber,
                        SurahDownloadStatus.Downloading(0.01f, 0, ayahsCount)
                    )

                    for (a in 1..ayahsCount) {
                        if (!isActive) break
                        val aStr = a.toString().padStart(3, '0')
                        val ayahFile = File(dir, "$s$aStr.mp3")
                        val tempFile = File(dir, "$s$aStr.mp3.tmp")

                        if (ayahFile.exists() && ayahFile.length() > 512) {
                            successfullyDownloadedAyahs++
                            totalSurahBytes += ayahFile.length()
                            continue
                        }

                        val jsonAyahUrl = timingRepository.getAyahAudioUrl(reciter, surahNumber, a)
                        val ayahAudioUrl = jsonAyahUrl ?: RecitersData.getAudioUrl(reciter, surahNumber, a)
                        val ok = downloadSingleFile(ayahAudioUrl, tempFile) { _, _, _ -> }
                        if (ok && tempFile.exists() && tempFile.length() > 200) {
                            tempFile.renameTo(ayahFile)
                            successfullyDownloadedAyahs++
                            totalSurahBytes += ayahFile.length()
                        } else {
                            tempFile.delete()
                        }

                        val currentProgress = successfullyDownloadedAyahs.toFloat() / ayahsCount.coerceAtLeast(1)
                        updateSurahStatus(
                            surahNumber,
                            SurahDownloadStatus.Downloading(
                                progress = currentProgress,
                                downloadedAyahs = successfullyDownloadedAyahs,
                                totalAyahs = ayahsCount,
                                downloadedBytes = totalSurahBytes
                            )
                        )
                    }

                    if (successfullyDownloadedAyahs >= ayahsCount) {
                        updateSurahStatus(surahNumber, SurahDownloadStatus.Downloaded(totalSurahBytes))
                        try {
                            timingRepository.generateAndSaveReciterTimingFile(reciter, listOf(surahNumber))
                        } catch (e: Exception) {
                            Log.w("QuranDownloadManager", "Could not compile timing file: ${e.message}")
                        }
                    } else if (isActive) {
                        updateSurahStatus(surahNumber, SurahDownloadStatus.Error("কিছু আয়াত ডাউনলোড হতে ব্যর্থ হয়েছে"))
                    }
                }
            } catch (e: CancellationException) {
                // Cancelled
                Log.d("QuranDownloadManager", "Download cancelled for surah $surahNumber")
            } catch (e: Exception) {
                Log.e("QuranDownloadManager", "Error downloading surah $surahNumber: ${e.message}")
                updateSurahStatus(surahNumber, SurahDownloadStatus.Error(e.localizedMessage ?: "ত্রুটি"))
            } finally {
                activeDownloadJobs.remove(surahNumber)
                refreshStatuses(reciter)
            }
        }
        activeDownloadJobs[surahNumber] = job
    }

    private fun downloadSingleFile(
        fileUrl: String,
        destinationFile: File,
        onProgress: (progress: Float, downloadedBytes: Long, totalBytes: Long) -> Unit
    ): Boolean {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL(fileUrl)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 15000
            connection.readTimeout = 20000
            connection.requestMethod = "GET"
            connection.setRequestProperty("User-Agent", "QuranAndroidApp/1.0")
            connection.connect()

            if (connection.responseCode !in 200..299) {
                Log.w("QuranDownloadManager", "HTTP response not OK: ${connection.responseCode} for $fileUrl")
                return false
            }

            val fileLength = connection.contentLength.toLong()
            val input = connection.inputStream
            val output = FileOutputStream(destinationFile)
            val buffer = ByteArray(8192)
            var total: Long = 0
            var count: Int

            while (input.read(buffer).also { count = it } != -1) {
                output.write(buffer, 0, count)
                total += count
                if (fileLength > 0) {
                    val progress = (total.toFloat() / fileLength).coerceIn(0f, 1f)
                    onProgress(progress, total, fileLength)
                }
            }

            output.flush()
            output.close()
            input.close()
            true
        } catch (e: Exception) {
            Log.w("QuranDownloadManager", "Download file exception for $fileUrl: ${e.message}")
            try { destinationFile.delete() } catch (_: Exception) {}
            false
        } finally {
            connection?.disconnect()
        }
    }

    private fun updateSurahStatus(surahNumber: Int, status: SurahDownloadStatus) {
        val current = _surahStatusMap.value.toMutableMap()
        current[surahNumber] = status
        _surahStatusMap.value = current
    }

    fun cancelSurahDownload(surahNumber: Int, reciter: ReciterItem) {
        activeDownloadJobs[surahNumber]?.cancel()
        activeDownloadJobs.remove(surahNumber)
        updateSurahStatus(surahNumber, SurahDownloadStatus.NotDownloaded)
        refreshStatuses(reciter)
    }

    /**
     * Delete an individual downloaded Surah
     */
    fun deleteSurahAudio(reciter: ReciterItem, surahNumber: Int) {
        cancelSurahDownload(surahNumber, reciter)
        scope.launch {
            val mode = _downloadMode.value
            val dir = getAudioDirectory(reciter, mode)
            val s = surahNumber.toString().padStart(3, '0')

            if (mode == DownloadMode.SURAH_BY_SURAH) {
                val file = File(dir, "$s.mp3")
                if (file.exists()) file.delete()
                val tmp = File(dir, "$s.mp3.tmp")
                if (tmp.exists()) tmp.delete()
            } else {
                val surah = QuranData.surahs.find { it.number == surahNumber }
                val count = surah?.totalAyahs ?: 300
                for (a in 1..count) {
                    val aStr = a.toString().padStart(3, '0')
                    val f = File(dir, "$s$aStr.mp3")
                    if (f.exists()) f.delete()
                    val tmp = File(dir, "$s$aStr.mp3.tmp")
                    if (tmp.exists()) tmp.delete()
                }
            }
            updateSurahStatus(surahNumber, SurahDownloadStatus.NotDownloaded)
            refreshStatuses(reciter)
        }
    }

    /**
     * Download all 114 Surahs sequentially
     */
    fun downloadAllSurahs(reciter: ReciterItem) {
        if (_isBulkDownloading.value) return
        _isBulkDownloading.value = true

        bulkJob = scope.launch {
            try {
                val surahs = QuranData.surahs
                val totalCount = surahs.size
                var completedSurahs = 0

                for (surah in surahs) {
                    if (!isActive) break
                    val currentStatus = _surahStatusMap.value[surah.number]
                    if (currentStatus is SurahDownloadStatus.Downloaded) {
                        completedSurahs++
                        continue
                    }

                    _bulkCurrentSurahNumber.value = surah.number
                    _bulkProgress.value = completedSurahs.toFloat() / totalCount

                    downloadSurah(reciter, surah.number)
                    // Wait for this surah's job to complete
                    val job = activeDownloadJobs[surah.number]
                    job?.join()

                    completedSurahs++
                    _bulkProgress.value = completedSurahs.toFloat() / totalCount
                }
                if (completedSurahs > 0) {
                    try {
                        timingRepository.generateAndSaveReciterTimingFile(reciter, emptyList())
                    } catch (e: Exception) {
                        Log.w("QuranDownloadManager", "Could not generate bulk timing file: ${e.message}")
                    }
                }
            } catch (e: CancellationException) {
                Log.d("QuranDownloadManager", "Bulk download cancelled")
            } finally {
                _isBulkDownloading.value = false
                refreshStatuses(reciter)
            }
        }
    }

    fun isReciterTimingSupported(reciter: ReciterItem): Boolean {
        return timingRepository.isReciterSupported(reciter)
    }

    suspend fun generateAndExportTimingFile(reciter: ReciterItem, surahNumbers: List<Int> = emptyList()): File? {
        return timingRepository.exportTimingFileToDownloads(reciter, surahNumbers)
    }

    fun getLocalTimingFile(reciter: ReciterItem): File {
        return timingRepository.getLocalTimingFile(reciter)
    }

    fun cancelBulkDownload(reciter: ReciterItem) {
        bulkJob?.cancel()
        _isBulkDownloading.value = false
        activeDownloadJobs.forEach { (_, job) -> job.cancel() }
        activeDownloadJobs.clear()
        refreshStatuses(reciter)
    }

    /**
     * Remove / Delete all downloaded surahs for this reciter and mode
     */
    fun deleteAllSurahs(reciter: ReciterItem) {
        cancelBulkDownload(reciter)
        scope.launch {
            val mode = _downloadMode.value
            val dir = getAudioDirectory(reciter, mode)
            if (dir.exists() && dir.isDirectory) {
                dir.listFiles()?.forEach { it.delete() }
            }
            refreshStatuses(reciter)
        }
    }

    fun formatBytes(bytes: Long): String {
        return when {
            bytes <= 0 -> "0 B"
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            else -> String.format("%.1f MB", bytes.toDouble() / (1024 * 1024))
        }
    }
}
