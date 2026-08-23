package com.example.data.timing

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.data.model.AyahItem
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReciterItem
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

/**
 * Data class representing a parsed LRC line for sync operations.
 */
data class ParsedLrcLine(
    val timestampMs: Long,
    val timeFormatted: String,
    val text: String,
    val ayahNumber: Int? = null
)

/**
 * Timing File Formats supported by the service.
 */
enum class TimingFileFormat(val extension: String, val mimeType: String, val displayName: String) {
    LRC(".lrc", "text/plain", "LRC (Lyrics / Music Players)"),
    SRT(".srt", "application/x-subrip", "SRT (Subtitles / Video Players)"),
    VTT(".vtt", "text/vtt", "WebVTT (Web / HTML5 Players)")
}

/**
 * Options for generating timing files.
 */
data class TimingSyncOptions(
    val format: TimingFileFormat = TimingFileFormat.LRC,
    val surahNumber: Int = 1,
    val surahName: String = "Al-Fatihah",
    val reciter: ReciterItem,
    val ayahs: List<AyahItem>,
    val font: QuranFontFamily = QuranFontFamily.UTHMANIC_HAFS,
    val includeArabic: Boolean = true,
    val includeBanglaTranslation: Boolean = false,
    val includeEnglishTranslation: Boolean = false,
    val includeRosette: Boolean = true,
    val offsetMs: Long = 0L, // Custom global offset in milliseconds (+ or -)
    val includeMetadataHeaders: Boolean = true
)

/**
 * Comprehensive Service to generate, parse, export, and sync .lrc, .srt, and .vtt timing files
 * for local audio-text synchronization in music players, video players, and media apps.
 */
object QuranTimingSyncService {

    fun formatLrcTimestamp(ms: Long): String {
        val safeMs = ms.coerceAtLeast(0L)
        val totalSeconds = safeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val hundredths = (safeMs % 1000) / 10
        return String.format(Locale.US, "%02d:%02d.%02d", minutes, seconds, hundredths)
    }

    fun formatSrtTimestamp(ms: Long): String {
        val safeMs = ms.coerceAtLeast(0L)
        val totalSeconds = safeMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val millis = safeMs % 1000
        return String.format(Locale.US, "%02d:%02d:%02d,%03d", hours, minutes, seconds, millis)
    }

    fun formatVttTimestamp(ms: Long): String {
        val safeMs = ms.coerceAtLeast(0L)
        val totalSeconds = safeMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val millis = safeMs % 1000
        return String.format(Locale.US, "%02d:%02d:%02d.%03d", hours, minutes, seconds, millis)
    }

    /**
     * Generate standard LRC timing file string.
     */
    fun generateLrc(options: TimingSyncOptions): String {
        val sb = StringBuilder()
        val surah = options.surahName
        val reciter = options.reciter

        if (options.includeMetadataHeaders) {
            sb.append("[ti:Surah $surah (${options.surahNumber})]\n")
            sb.append("[ar:${reciter.displayName}]\n")
            sb.append("[al:Holy Quran Recitation]\n")
            sb.append("[by:Al Quran Editor]\n")
            if (options.offsetMs != 0L) {
                sb.append("[offset:${options.offsetMs}]\n")
            }
            sb.append("\n")
        }

        var runningMs = 6060L + options.offsetMs
        options.ayahs.forEach { ayah ->
            val startMs = ((if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningMs) + options.offsetMs).coerceAtLeast(0L)
            val timeTag = "[${formatLrcTimestamp(startMs)}]"

            val textBuilder = StringBuilder()
            if (options.includeArabic) {
                val arabic = if (options.font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
                textBuilder.append(arabic)
                if (options.includeRosette) {
                    textBuilder.append(" ۝${TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)}")
                }
            }

            if (options.includeBanglaTranslation && ayah.banglaTranslation.isNotBlank()) {
                if (textBuilder.isNotEmpty()) textBuilder.append("\n")
                textBuilder.append("(${ayah.ayahNumberInSurah}) ${ayah.banglaTranslation}")
            }

            if (options.includeEnglishTranslation && ayah.englishTranslation.isNotBlank()) {
                if (textBuilder.isNotEmpty()) textBuilder.append("\n")
                textBuilder.append("(${ayah.ayahNumberInSurah}) ${ayah.englishTranslation}")
            }

            sb.append("$timeTag ${textBuilder.toString()}\n")
            runningMs += (ayah.textUthmani.length * 350L).coerceAtLeast(6000L)
        }

        return sb.toString().trimEnd()
    }

    /**
     * Generate standard SRT subtitle timing file string.
     */
    fun generateSrt(options: TimingSyncOptions): String {
        val sb = StringBuilder()
        var runningStartMs = 6064L + options.offsetMs

        options.ayahs.forEachIndexed { index, ayah ->
            val duration = (ayah.textUthmani.length * 350L).coerceAtLeast(6500L)
            val rawStart = if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningStartMs
            val rawEnd = if (ayah.defaultEndMs > 0) ayah.defaultEndMs else (rawStart + duration)

            val startMs = (rawStart + options.offsetMs).coerceAtLeast(0L)
            val endMs = (rawEnd + options.offsetMs).coerceAtLeast(startMs + 1000L)
            runningStartMs = endMs + 250L

            sb.append("${index + 1}\n")
            sb.append("${formatSrtTimestamp(startMs)} --> ${formatSrtTimestamp(endMs)}\n")

            val lines = mutableListOf<String>()
            if (options.includeArabic) {
                val arabic = if (options.font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
                val rosette = if (options.includeRosette) " ۝${TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)}" else ""
                lines.add("$arabic$rosette")
            }
            if (options.includeBanglaTranslation && ayah.banglaTranslation.isNotBlank()) {
                lines.add(ayah.banglaTranslation)
            }
            if (options.includeEnglishTranslation && ayah.englishTranslation.isNotBlank()) {
                lines.add(ayah.englishTranslation)
            }

            sb.append(lines.joinToString("\n"))
            sb.append("\n\n")
        }

        return sb.toString().trimEnd()
    }

    /**
     * Generate WebVTT timing file string.
     */
    fun generateVtt(options: TimingSyncOptions): String {
        val sb = StringBuilder()
        sb.append("WEBVTT - Surah ${options.surahName} (${options.reciter.displayName})\n\n")

        var runningStartMs = 6064L + options.offsetMs

        options.ayahs.forEachIndexed { index, ayah ->
            val duration = (ayah.textUthmani.length * 350L).coerceAtLeast(6500L)
            val rawStart = if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningStartMs
            val rawEnd = if (ayah.defaultEndMs > 0) ayah.defaultEndMs else (rawStart + duration)

            val startMs = (rawStart + options.offsetMs).coerceAtLeast(0L)
            val endMs = (rawEnd + options.offsetMs).coerceAtLeast(startMs + 1000L)
            runningStartMs = endMs + 250L

            sb.append("${index + 1}\n")
            sb.append("${formatVttTimestamp(startMs)} --> ${formatVttTimestamp(endMs)}\n")

            val lines = mutableListOf<String>()
            if (options.includeArabic) {
                val arabic = if (options.font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
                val rosette = if (options.includeRosette) " ۝${TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)}" else ""
                lines.add("$arabic$rosette")
            }
            if (options.includeBanglaTranslation && ayah.banglaTranslation.isNotBlank()) {
                lines.add(ayah.banglaTranslation)
            }
            if (options.includeEnglishTranslation && ayah.englishTranslation.isNotBlank()) {
                lines.add(ayah.englishTranslation)
            }

            sb.append(lines.joinToString("\n"))
            sb.append("\n\n")
        }

        return sb.toString().trimEnd()
    }

    /**
     * Master generation dispatcher based on chosen format.
     */
    fun generateTimingContent(options: TimingSyncOptions): String {
        return when (options.format) {
            TimingFileFormat.LRC -> generateLrc(options)
            TimingFileFormat.SRT -> generateSrt(options)
            TimingFileFormat.VTT -> generateVtt(options)
        }
    }

    /**
     * Save generated timing content directly to local storage file on device.
     */
    fun saveTimingFileLocally(
        context: Context,
        filenameWithoutExt: String,
        format: TimingFileFormat,
        content: String
    ): Result<File> {
        return try {
            val safeName = filenameWithoutExt.replace(Regex("[^a-zA-Z0-9._-]"), "_")
            val fullFilename = "$safeName${format.extension}"

            // 1. Save in app's external files directory (Documents/QuranTimings)
            val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "QuranTimings")
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val targetFile = File(dir, fullFilename)
            FileOutputStream(targetFile).use { fos ->
                fos.write(content.toByteArray(Charsets.UTF_8))
                fos.flush()
            }

            // 2. Also register in MediaStore Downloads on Android 10+ for direct user visibility if supported
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    val values = ContentValues().apply {
                        put(MediaStore.Downloads.DISPLAY_NAME, fullFilename)
                        put(MediaStore.Downloads.MIME_TYPE, format.mimeType)
                        put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/QuranTimings")
                    }
                    val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                    if (uri != null) {
                        context.contentResolver.openOutputStream(uri)?.use { out ->
                            out.write(content.toByteArray(Charsets.UTF_8))
                            out.flush()
                        }
                    }
                } catch (ignored: Throwable) {
                    // Fallback to targetFile in Documents directory
                }
            }

            Result.success(targetFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Share timing file with external audio/video player apps via FileProvider Intent.
     */
    fun shareTimingFile(context: Context, file: File, format: TimingFileFormat, title: String) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = format.mimeType
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, title)
                putExtra(Intent.EXTRA_TITLE, title)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Share $title via...")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: Exception) {
            // Fallback to plain text share
            val textIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, file.readText())
                putExtra(Intent.EXTRA_SUBJECT, title)
            }
            context.startActivity(Intent.createChooser(textIntent, "Share Timing Text"))
        }
    }

    /**
     * Parse raw LRC string into structured parsed lines for local synchronization.
     */
    fun parseLrcText(lrcContent: String): List<ParsedLrcLine> {
        val lines = lrcContent.lines()
        val result = mutableListOf<ParsedLrcLine>()
        val lrcRegex = Regex("\\[(\\d{2}):(\\d{2})\\.(\\d{2,3})\\](.*)")

        lines.forEach { line ->
            val match = lrcRegex.matchEntire(line.trim())
            if (match != null) {
                val min = match.groupValues[1].toLongOrNull() ?: 0L
                val sec = match.groupValues[2].toLongOrNull() ?: 0L
                val fracStr = match.groupValues[3]
                val frac = if (fracStr.length == 2) (fracStr.toLongOrNull() ?: 0L) * 10 else fracStr.toLongOrNull() ?: 0L

                val totalMs = (min * 60 * 1000) + (sec * 1000) + frac
                val text = match.groupValues[4].trim()
                val ayahMatch = Regex(".*?(\\d+).*").find(text)
                val ayahNum = ayahMatch?.groupValues?.get(1)?.toIntOrNull()

                result.add(
                    ParsedLrcLine(
                        timestampMs = totalMs,
                        timeFormatted = String.format(Locale.US, "%02d:%02d.%02d", min, sec, frac / 10),
                        text = text,
                        ayahNumber = ayahNum
                    )
                )
            }
        }
        return result
    }
}
