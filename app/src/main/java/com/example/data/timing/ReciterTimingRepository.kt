package com.example.data.timing

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.data.repository.QuranData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap

/**
 * Repository to manage, parse, generate, and persist high-precision recitation timings
 * (letter by letter, word by word, ayah by ayah, surah by surah) from JSON data files:
 * - abdul_basit_hafsmujawwad_surah_audiolink.json
 * - abdul_basit_hafsmujawwad_surah_segments.json
 * - abdul_basit_hasf-murattal_ayah.json
 *
 * Extensible for any future reciters added to assets or storage.
 */
class ReciterTimingRepository(private val context: Context) {

    private val tag = "ReciterTimingRepo"

    // Cache of parsed surah timings: key is "${reciterId}_${surahNumber}"
    private val surahTimingCache = ConcurrentHashMap<String, SurahTimingData>()

    // Cache of surah audio links: key is "${reciterId}_${surahNumber}" -> audio_url
    private val surahAudioUrlCache = ConcurrentHashMap<String, String>()

    // Cache of ayah audio links: key is "${reciterId}_${surahNumber}:${ayahNumber}" -> audio_url
    private val ayahAudioUrlCache = ConcurrentHashMap<String, String>()

    // In-memory raw segment JSON caches to avoid re-reading disk repeatedly
    private var mujawwadSegmentsJson: JSONObject? = null
    private var murattalAyahJson: JSONObject? = null
    private var surahAudioLinksJson: JSONObject? = null

    /**
     * Checks if high-precision JSON timing data is available for this reciter
     */
    fun isReciterSupported(reciter: ReciterItem): Boolean {
        val id = reciter.id.lowercase()
        val name = reciter.displayName.lowercase()
        val folder = reciter.serverFolder.lowercase()

        // Abdul Basit variants
        if (id.contains("abdulbaset") || id.contains("abdul_basit") ||
            name.contains("abdul basit") || folder.contains("abdul_basit")) {
            return true
        }

        // Check if custom timing file exists in storage
        val localTimingFile = getLocalTimingFile(reciter)
        if (localTimingFile.exists() && localTimingFile.length() > 500) {
            return true
        }

        // Check if assets has files for this reciter
        return hasAssetForReciter(reciter)
    }

    private fun hasAssetForReciter(reciter: ReciterItem): Boolean {
        val prefix = getReciterFilePrefix(reciter)
        return try {
            val list = context.assets.list("reciters") ?: emptyArray()
            list.any { it.startsWith(prefix, ignoreCase = true) }
        } catch (e: Exception) {
            false
        }
    }

    fun getReciterFilePrefix(reciter: ReciterItem): String {
        val id = reciter.id.lowercase()
        val name = reciter.displayName.lowercase()
        return if (id.contains("abdulbaset") || id.contains("abdul_basit") || name.contains("abdul basit")) {
            "abdul_basit"
        } else {
            reciter.id.lowercase().replace("-", "_")
        }
    }

    /**
     * Get Surah audio URL from JSON metadata
     */
    suspend fun getSurahAudioUrl(reciter: ReciterItem, surahNumber: Int): String? = withContext(Dispatchers.IO) {
        val cacheKey = "${reciter.id}_$surahNumber"
        surahAudioUrlCache[cacheKey]?.let { return@withContext it }

        ensureAudioLinksLoaded(reciter)
        val json = surahAudioLinksJson ?: return@withContext null

        val surahObj = json.optJSONObject(surahNumber.toString()) ?: return@withContext null
        val url = surahObj.optString("audio_url").takeIf { it.isNotBlank() }
        if (url != null) {
            surahAudioUrlCache[cacheKey] = url
        }
        url
    }

    /**
     * Get Ayah audio URL from Murattal Ayah JSON
     */
    suspend fun getAyahAudioUrl(reciter: ReciterItem, surahNumber: Int, ayahNumber: Int): String? = withContext(Dispatchers.IO) {
        val cacheKey = "${reciter.id}_$surahNumber:$ayahNumber"
        ayahAudioUrlCache[cacheKey]?.let { return@withContext it }

        ensureMurattalAyahLoaded(reciter)
        val json = murattalAyahJson ?: return@withContext null

        val ayahKey = "$surahNumber:$ayahNumber"
        val ayahObj = json.optJSONObject(ayahKey) ?: return@withContext null
        val url = ayahObj.optString("audio_url").takeIf { it.isNotBlank() }
        if (url != null) {
            ayahAudioUrlCache[cacheKey] = url
        }
        url
    }

    /**
     * Load full SurahTimingData (Letter, Word, Ayah, Surah) for a given surah.
     * Uses local timing file if saved, otherwise parses the raw assets.
     */
    suspend fun getSurahTiming(reciter: ReciterItem, surahNumber: Int): SurahTimingData? = withContext(Dispatchers.IO) {
        val cacheKey = "${reciter.id}_$surahNumber"
        surahTimingCache[cacheKey]?.let { return@withContext it }

        // 1. Check local saved timing file first (e.g. from previous download)
        val savedSurahTiming = loadSavedSurahTiming(reciter, surahNumber)
        if (savedSurahTiming != null) {
            surahTimingCache[cacheKey] = savedSurahTiming
            return@withContext savedSurahTiming
        }

        // 2. Build from raw JSON assets
        val builtTiming = buildSurahTimingFromAssets(reciter, surahNumber)
        if (builtTiming != null) {
            surahTimingCache[cacheKey] = builtTiming
        }
        builtTiming
    }

    /**
     * Generate the complete 4-format Master Timing File (Letter, Word, Ayah, Surah)
     * for the specified surahs (e.g. Surah 1 Al-Fatihah, or all selected surahs)
     * and save to storage as "Abdul_basit_timing.json" and "Abdul_basit_timing_surah_{num}.json"
     */
    suspend fun generateAndSaveReciterTimingFile(
        reciter: ReciterItem,
        surahNumbers: List<Int>
    ): File = withContext(Dispatchers.IO) {
        val targetSurahs = if (surahNumbers.isEmpty()) (1..114).toList() else surahNumbers
        val rootJson = JSONObject()
        val surahsObj = JSONObject()

        val prefix = getReciterFilePrefix(reciter)
        val friendlyName = if (prefix == "abdul_basit") "Abdul Basit Abdul Samad" else reciter.displayName

        rootJson.put("reciter_id", reciter.id)
        rootJson.put("reciter_name", friendlyName)
        rootJson.put("reciter_style", reciter.style)
        rootJson.put("generated_at", System.currentTimeMillis())
        rootJson.put("timing_formats_supported", JSONArray(listOf("letter_by_letter", "word_by_word", "ayah_by_ayah", "surah_by_surah")))
        rootJson.put("total_surahs_included", targetSurahs.size)

        for (sNum in targetSurahs) {
            val surahTiming = getSurahTiming(reciter, sNum) ?: continue
            val singleSurahJson = convertSurahTimingToJson(surahTiming)
            surahsObj.put(sNum.toString(), singleSurahJson)

            // Also save per-surah timing file for fast individual surah offline loading
            savePerSurahTimingFile(reciter, sNum, singleSurahJson)
        }

        rootJson.put("surahs", surahsObj)

        // Save unified master file: "Abdul_basit_timing.json"
        val timingsDir = getTimingDirectory(reciter)
        val masterFile = File(timingsDir, "${prefix.capitalizeWords()}_timing.json")
        masterFile.writeText(rootJson.toString(2))
        Log.i(tag, "Saved master timing file: ${masterFile.absolutePath} (size: ${masterFile.length()} bytes)")

        // Also duplicate to root timings dir as "Abdul_basit_timing.json"
        val rootMasterFile = File(timingsDir.parentFile, "Abdul_basit_timing.json")
        try {
            masterFile.copyTo(rootMasterFile, overwrite = true)
        } catch (e: Exception) {
            Log.w(tag, "Could not mirror root master file: ${e.message}")
        }

        masterFile
    }

    /**
     * Export the master timing file directly to user's Download directory
     * so user can locate "Abdul basit timing.json" in Android File Manager
     */
    suspend fun exportTimingFileToDownloads(
        reciter: ReciterItem,
        surahNumbers: List<Int>
    ): File? = withContext(Dispatchers.IO) {
        try {
            val masterFile = generateAndSaveReciterTimingFile(reciter, surahNumbers)
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            if (downloadsDir != null && (downloadsDir.exists() || downloadsDir.mkdirs())) {
                val prefix = getReciterFilePrefix(reciter)
                val exportName = if (prefix == "abdul_basit") "Abdul basit timing.json" else "${reciter.displayName} timing.json"
                val exportFile = File(downloadsDir, exportName)
                masterFile.copyTo(exportFile, overwrite = true)
                Log.i(tag, "Exported timing file to Downloads: ${exportFile.absolutePath}")
                exportFile
            } else {
                masterFile
            }
        } catch (e: Exception) {
            Log.e(tag, "Failed to export timing file to Downloads: ${e.message}", e)
            null
        }
    }

    /**
     * Get root directory for timings:
     * App internal storage: <filesDir>/quran_timings/<reciter_prefix>/
     */
    fun getTimingDirectory(reciter: ReciterItem): File {
        val baseDir = context.getExternalFilesDir(null) ?: context.filesDir
        val prefix = getReciterFilePrefix(reciter)
        val dir = File(baseDir, "quran_timings/$prefix")
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir
    }

    fun getLocalTimingFile(reciter: ReciterItem): File {
        val dir = getTimingDirectory(reciter)
        val prefix = getReciterFilePrefix(reciter)
        return File(dir, "${prefix.capitalizeWords()}_timing.json")
    }

    fun getPerSurahTimingFile(reciter: ReciterItem, surahNumber: Int): File {
        val dir = getTimingDirectory(reciter)
        val prefix = getReciterFilePrefix(reciter)
        return File(dir, "${prefix}_timing_surah_${surahNumber}.json")
    }

    private fun savePerSurahTimingFile(reciter: ReciterItem, surahNumber: Int, json: JSONObject) {
        try {
            val file = getPerSurahTimingFile(reciter, surahNumber)
            file.writeText(json.toString(2))
        } catch (e: Exception) {
            Log.w(tag, "Failed to save per-surah timing: ${e.message}")
        }
    }

    private fun loadSavedSurahTiming(reciter: ReciterItem, surahNumber: Int): SurahTimingData? {
        // Check per-surah file first
        val perSurahFile = getPerSurahTimingFile(reciter, surahNumber)
        if (perSurahFile.exists() && perSurahFile.length() > 200) {
            try {
                val content = perSurahFile.readText()
                val obj = JSONObject(content)
                return parseSurahTimingFromJson(obj)
            } catch (e: Exception) {
                Log.w(tag, "Error reading perSurahFile: ${e.message}")
            }
        }

        // Check master timing file
        val masterFile = getLocalTimingFile(reciter)
        if (masterFile.exists() && masterFile.length() > 500) {
            try {
                val content = masterFile.readText()
                val root = JSONObject(content)
                val surahsObj = root.optJSONObject("surahs")
                val surahObj = surahsObj?.optJSONObject(surahNumber.toString())
                if (surahObj != null) {
                    return parseSurahTimingFromJson(surahObj)
                }
            } catch (e: Exception) {
                Log.w(tag, "Error reading masterFile: ${e.message}")
            }
        }

        return null
    }

    private suspend fun buildSurahTimingFromAssets(reciter: ReciterItem, surahNumber: Int): SurahTimingData? {
        ensureAudioLinksLoaded(reciter)
        ensureMujawwadSegmentsLoaded(reciter)
        ensureMurattalAyahLoaded(reciter)

        val surahInfo = QuranData.surahs.find { it.number == surahNumber }
        val surahName = surahInfo?.englishName ?: "Surah $surahNumber"
        val totalAyahs = surahInfo?.totalAyahs ?: 7

        val surahAudioUrl = getSurahAudioUrl(reciter, surahNumber) ?: ""
        val surahDurationSec = surahAudioLinksJson?.optJSONObject(surahNumber.toString())?.optInt("duration", 0) ?: 0
        val surahDurationMs = surahDurationSec * 1000L

        val segmentsJson = mujawwadSegmentsJson ?: return null
        val murattalJson = murattalAyahJson

        val ayahsMap = mutableMapOf<Int, AyahTimingData>()
        val allAyahsList = QuranData.getAyahsForSurah(surahNumber)

        for (aNum in 1..totalAyahs) {
            val key = "$surahNumber:$aNum"
            val segmentObj = segmentsJson.optJSONObject(key)
            val murattalObj = murattalJson?.optJSONObject(key)

            val ayahModel = allAyahsList.find { it.ayahNumberInSurah == aNum }
            val rawWords = ayahModel?.words ?: emptyList()
            val textUthmani = ayahModel?.textUthmani ?: ""

            val timestampFrom = segmentObj?.optLong("timestamp_from", 0L) ?: 0L
            val timestampTo = segmentObj?.optLong("timestamp_to", 0L) ?: 0L
            val durationMs = segmentObj?.optLong("duration_ms", 0L)?.takeIf { it > 0 } ?: (timestampTo - timestampFrom).coerceAtLeast(0L)
            val ayahAudioUrl = murattalObj?.optString("audio_url")?.takeIf { it.isNotBlank() }

            // Parse word segments from `segments`: [[1, start, end], [2, start, end], ...]
            val rawSegmentsArray = segmentObj?.optJSONArray("segments")
            val wordsList = mutableListOf<WordSegmentData>()

            if (rawSegmentsArray != null && rawSegmentsArray.length() > 0) {
                for (wIdx in 0 until rawSegmentsArray.length()) {
                    val tuple = rawSegmentsArray.optJSONArray(wIdx) ?: continue
                    val wordNumber = tuple.optInt(0, wIdx + 1)
                    val wStart = tuple.optLong(1, 0L)
                    val wEnd = tuple.optLong(2, 0L)
                    val wDuration = (wEnd - wStart).coerceAtLeast(0L)

                    val wordModel = rawWords.getOrNull(wIdx)
                    val wordText = wordModel?.arabic ?: ""
                    val wordBangla = wordModel?.bangla ?: ""

                    // Synthesize letter-by-letter timing for this word
                    val letterSegments = splitArabicWordIntoLetters(
                        wordText = if (wordText.isNotBlank()) wordText else textUthmani,
                        wordStartMs = wStart,
                        wordEndMs = wEnd
                    )

                    wordsList.add(
                        WordSegmentData(
                            wordIndex = wordNumber,
                            wordText = wordText,
                            banglaMeaning = wordBangla,
                            startMs = wStart,
                            endMs = wEnd,
                            durationMs = wDuration,
                            letters = letterSegments
                        )
                    )
                }
            }

            ayahsMap[aNum] = AyahTimingData(
                surahNumber = surahNumber,
                ayahNumber = aNum,
                startMs = timestampFrom,
                endMs = timestampTo,
                durationMs = durationMs,
                audioUrl = ayahAudioUrl,
                words = wordsList
            )
        }

        val firstAyahStart = ayahsMap[1]?.startMs ?: 0L
        val lastAyahEnd = ayahsMap[totalAyahs]?.endMs ?: surahDurationMs

        return SurahTimingData(
            surahNumber = surahNumber,
            surahName = surahName,
            audioUrl = surahAudioUrl,
            durationSec = surahDurationSec,
            durationMs = surahDurationMs,
            surahStartMs = firstAyahStart,
            surahEndMs = lastAyahEnd,
            ayahs = ayahsMap
        )
    }

    /**
     * Splits an Arabic word into pronounced letter-units with diacritics
     * and assigns proportional start and end millisecond timestamps.
     */
    fun splitArabicWordIntoLetters(
        wordText: String,
        wordStartMs: Long,
        wordEndMs: Long
    ): List<LetterSegmentData> {
        val units = mutableListOf<String>()
        var currentUnit = StringBuilder()

        for (ch in wordText) {
            val code = ch.code
            // Combining marks (tashkeel/harakat: 064B-065F, 0670, 06D6-06ED)
            val isCombiningMark = (code in 0x064B..0x065F) || code == 0x0670 || (code in 0x06D6..0x06ED)
            if (isCombiningMark) {
                currentUnit.append(ch)
            } else if (ch.isWhitespace()) {
                // skip
            } else {
                if (currentUnit.isNotEmpty()) {
                    units.add(currentUnit.toString())
                    currentUnit = StringBuilder()
                }
                currentUnit.append(ch)
            }
        }
        if (currentUnit.isNotEmpty()) {
            units.add(currentUnit.toString())
        }

        if (units.isEmpty()) {
            return emptyList()
        }

        val wordDuration = (wordEndMs - wordStartMs).coerceAtLeast(1L)
        val perLetterMs = wordDuration.toDouble() / units.size.toDouble()

        return units.mapIndexed { idx, letter ->
            val lStart = (wordStartMs + (idx * perLetterMs)).toLong()
            val lEnd = if (idx == units.size - 1) wordEndMs else (wordStartMs + ((idx + 1) * perLetterMs)).toLong()
            LetterSegmentData(
                letterIndex = idx,
                letter = letter,
                startMs = lStart,
                endMs = lEnd,
                durationMs = (lEnd - lStart).coerceAtLeast(0L)
            )
        }
    }

    /**
     * Convert SurahTimingData into a rich JSON Object containing all 4 modes
     */
    fun convertSurahTimingToJson(surahTiming: SurahTimingData): JSONObject {
        val obj = JSONObject()
        obj.put("surah_number", surahTiming.surahNumber)
        obj.put("surah_name", surahTiming.surahName)
        obj.put("audio_url", surahTiming.audioUrl)
        obj.put("duration_sec", surahTiming.durationSec)
        obj.put("duration_ms", surahTiming.durationMs)

        // 1. surah_by_surah mode
        val surahModeObj = JSONObject().apply {
            put("start_ms", surahTiming.surahStartMs)
            put("end_ms", surahTiming.surahEndMs)
            put("duration_ms", surahTiming.durationMs)
        }
        obj.put("surah_by_surah", surahModeObj)

        // 2. ayah_by_ayah mode
        val ayahsArray = JSONArray()
        // 3. word_by_word mode
        val wordsArray = JSONArray()
        // 4. letter_by_letter mode
        val lettersArray = JSONArray()

        val sortedAyahs = surahTiming.ayahs.values.sortedBy { it.ayahNumber }
        for (ayah in sortedAyahs) {
            val ayahJson = JSONObject().apply {
                put("ayah_number", ayah.ayahNumber)
                put("start_ms", ayah.startMs)
                put("end_ms", ayah.endMs)
                put("duration_ms", ayah.durationMs)
                if (ayah.audioUrl != null) {
                    put("audio_url", ayah.audioUrl)
                }
            }
            ayahsArray.put(ayahJson)

            for (word in ayah.words) {
                val wordJson = JSONObject().apply {
                    put("ayah_number", ayah.ayahNumber)
                    put("word_number", word.wordIndex)
                    put("word_text", word.wordText)
                    put("bangla", word.banglaMeaning)
                    put("start_ms", word.startMs)
                    put("end_ms", word.endMs)
                    put("duration_ms", word.durationMs)
                }
                wordsArray.put(wordJson)

                for (letter in word.letters) {
                    val letterJson = JSONObject().apply {
                        put("ayah_number", ayah.ayahNumber)
                        put("word_number", word.wordIndex)
                        put("letter_index", letter.letterIndex)
                        put("letter", letter.letter)
                        put("start_ms", letter.startMs)
                        put("end_ms", letter.endMs)
                        put("duration_ms", letter.durationMs)
                    }
                    lettersArray.put(letterJson)
                }
            }
        }

        obj.put("ayah_by_ayah", ayahsArray)
        obj.put("word_by_word", wordsArray)
        obj.put("letter_by_letter", lettersArray)

        return obj
    }

    private fun parseSurahTimingFromJson(obj: JSONObject): SurahTimingData {
        val surahNum = obj.optInt("surah_number", 1)
        val surahName = obj.optString("surah_name", "Surah $surahNum")
        val audioUrl = obj.optString("audio_url", "")
        val durationSec = obj.optInt("duration_sec", 0)
        val durationMs = obj.optLong("duration_ms", 0L)

        val surahModeObj = obj.optJSONObject("surah_by_surah")
        val surahStart = surahModeObj?.optLong("start_ms", 0L) ?: 0L
        val surahEnd = surahModeObj?.optLong("end_ms", durationMs) ?: durationMs

        val ayahsArray = obj.optJSONArray("ayah_by_ayah") ?: JSONArray()
        val wordsArray = obj.optJSONArray("word_by_word") ?: JSONArray()
        val lettersArray = obj.optJSONArray("letter_by_letter") ?: JSONArray()

        // Index letters by "${ayahNumber}_${wordNumber}"
        val lettersMap = mutableMapOf<String, MutableList<LetterSegmentData>>()
        for (i in 0 until lettersArray.length()) {
            val lObj = lettersArray.optJSONObject(i) ?: continue
            val aNum = lObj.optInt("ayah_number")
            val wNum = lObj.optInt("word_number")
            val key = "${aNum}_$wNum"
            val list = lettersMap.getOrPut(key) { mutableListOf() }
            list.add(
                LetterSegmentData(
                    letterIndex = lObj.optInt("letter_index"),
                    letter = lObj.optString("letter"),
                    startMs = lObj.optLong("start_ms"),
                    endMs = lObj.optLong("end_ms"),
                    durationMs = lObj.optLong("duration_ms")
                )
            )
        }

        // Index words by ayahNumber
        val wordsMap = mutableMapOf<Int, MutableList<WordSegmentData>>()
        for (i in 0 until wordsArray.length()) {
            val wObj = wordsArray.optJSONObject(i) ?: continue
            val aNum = wObj.optInt("ayah_number")
            val wNum = wObj.optInt("word_number")
            val list = wordsMap.getOrPut(aNum) { mutableListOf() }
            val letters = lettersMap["${aNum}_$wNum"] ?: emptyList()
            list.add(
                WordSegmentData(
                    wordIndex = wNum,
                    wordText = wObj.optString("word_text"),
                    banglaMeaning = wObj.optString("bangla"),
                    startMs = wObj.optLong("start_ms"),
                    endMs = wObj.optLong("end_ms"),
                    durationMs = wObj.optLong("duration_ms"),
                    letters = letters
                )
            )
        }

        val ayahsMap = mutableMapOf<Int, AyahTimingData>()
        for (i in 0 until ayahsArray.length()) {
            val aObj = ayahsArray.optJSONObject(i) ?: continue
            val aNum = aObj.optInt("ayah_number")
            ayahsMap[aNum] = AyahTimingData(
                surahNumber = surahNum,
                ayahNumber = aNum,
                startMs = aObj.optLong("start_ms"),
                endMs = aObj.optLong("end_ms"),
                durationMs = aObj.optLong("duration_ms"),
                audioUrl = aObj.optString("audio_url").takeIf { it.isNotBlank() },
                words = wordsMap[aNum] ?: emptyList()
            )
        }

        return SurahTimingData(
            surahNumber = surahNum,
            surahName = surahName,
            audioUrl = audioUrl,
            durationSec = durationSec,
            durationMs = durationMs,
            surahStartMs = surahStart,
            surahEndMs = surahEnd,
            ayahs = ayahsMap
        )
    }

    /**
     * Finds the active ayah, word index, and letter index for a given playback position in milliseconds.
     * Works seamlessly in both Surah-by-Surah continuous audio and Ayah-by-Ayah individual audio!
     */
    fun findActiveElements(
        surahTiming: SurahTimingData,
        positionMs: Long,
        currentAyahNumber: Int? = null,
        isSurahFullAudio: Boolean = true
    ): Triple<Int, Int, Int> {
        val targetAyah = if (isSurahFullAudio) {
            // Find which ayah has startMs <= positionMs < endMs
            surahTiming.ayahs.values.find {
                positionMs >= it.startMs && (positionMs < it.endMs || it.ayahNumber == surahTiming.ayahs.size)
            } ?: (currentAyahNumber?.let { surahTiming.ayahs[it] } ?: surahTiming.ayahs[1])
        } else {
            // Ayah audio playback: currentAyahNumber is known, positionMs is relative to ayah
            currentAyahNumber?.let { surahTiming.ayahs[it] } ?: surahTiming.ayahs[1]
        } ?: return Triple(1, -1, -1)

        val ayahNum = targetAyah.ayahNumber

        // For ayah audio, offset matches relative pos; for surah audio, absolute pos
        val effectivePos = if (isSurahFullAudio) positionMs else (targetAyah.startMs + positionMs)

        var activeWordIdx = -1
        var activeLetterIdx = -1

        targetAyah.words.forEachIndexed { idx, word ->
            if (effectivePos >= word.startMs && effectivePos <= word.endMs) {
                activeWordIdx = idx
                // Find matching letter within this word
                word.letters.forEachIndexed { lIdx, letter ->
                    if (effectivePos >= letter.startMs && effectivePos <= letter.endMs) {
                        activeLetterIdx = lIdx
                    }
                }
            }
        }

        return Triple(ayahNum, activeWordIdx, activeLetterIdx)
    }

    // Lazy file loaders for assets
    private fun ensureAudioLinksLoaded(reciter: ReciterItem) {
        if (surahAudioLinksJson != null) return
        val prefix = getReciterFilePrefix(reciter)
        val fileName = if (prefix == "abdul_basit") "abdul_basit_hafsmujawwad_surah_audiolink.json" else "${prefix}_surah_audiolink.json"
        surahAudioLinksJson = loadJsonFromAssetsOrStorage(fileName)
    }

    private fun ensureMujawwadSegmentsLoaded(reciter: ReciterItem) {
        if (mujawwadSegmentsJson != null) return
        val prefix = getReciterFilePrefix(reciter)
        val fileName = if (prefix == "abdul_basit") "abdul_basit_hafsmujawwad_surah_segments.json" else "${prefix}_surah_segments.json"
        mujawwadSegmentsJson = loadJsonFromAssetsOrStorage(fileName)
    }

    private fun ensureMurattalAyahLoaded(reciter: ReciterItem) {
        if (murattalAyahJson != null) return
        val prefix = getReciterFilePrefix(reciter)
        val fileName = if (prefix == "abdul_basit") "abdul_basit_hasf-murattal_ayah.json" else "${prefix}_ayah.json"
        murattalAyahJson = loadJsonFromAssetsOrStorage(fileName)
    }

    private fun loadJsonFromAssetsOrStorage(fileName: String): JSONObject? {
        // Try local storage first
        val storageFile = File(context.filesDir, "reciters/$fileName")
        if (storageFile.exists()) {
            return try {
                JSONObject(storageFile.readText())
            } catch (e: Exception) {
                Log.e(tag, "Failed to parse JSON from storage: $fileName", e)
                null
            }
        }

        // Try assets
        return try {
            val assetPath = "reciters/$fileName"
            val jsonStr = context.assets.open(assetPath).bufferedReader().use { it.readText() }
            JSONObject(jsonStr)
        } catch (e: Exception) {
            // Try root asset
            try {
                val jsonStr = context.assets.open(fileName).bufferedReader().use { it.readText() }
                JSONObject(jsonStr)
            } catch (e2: Exception) {
                Log.w(tag, "Could not load asset $fileName: ${e.message}")
                null
            }
        }
    }

    private fun String.capitalizeWords(): String {
        return split("_").joinToString("_") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }
    }
}
