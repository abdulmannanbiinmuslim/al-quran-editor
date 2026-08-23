package com.example.data.timing

import com.example.data.model.AyahItem
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReciterItem
import java.util.Locale

object TimingGenerator {

    private fun formatLrcTimestamp(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val hundredths = (ms % 1000) / 10
        return String.format(Locale.US, "%02d:%02d.%02d", minutes, seconds, hundredths)
    }

    private fun formatSrtTimestamp(ms: Long): String {
        val totalSeconds = ms / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        val millis = ms % 1000
        return String.format(Locale.US, "%02d:%02d:%02d,%03d", hours, minutes, seconds, millis)
    }

    fun toArabicNumber(number: Int): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        val numStr = number.toString()
        val builder = StringBuilder()
        for (ch in numStr) {
            if (ch in '0'..'9') {
                builder.append(arabicDigits[ch - '0'])
            } else {
                builder.append(ch)
            }
        }
        return builder.toString()
    }

    // 1. Only Arabic
    fun generateOnlyArabic(ayahs: List<AyahItem>, font: QuranFontFamily): String {
        val sb = StringBuilder()
        ayahs.forEach { ayah ->
            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            sb.append(arabicText)
            sb.append(" ۝${toArabicNumber(ayah.ayahNumberInSurah)} ")
        }
        return sb.toString().trim()
    }

    // 2. Arabic with Lyrics (LRC)
    fun generateArabicLrc(
        surahName: String,
        reciter: ReciterItem,
        ayahs: List<AyahItem>,
        font: QuranFontFamily
    ): String {
        val sb = StringBuilder()
        sb.append("[timing: Surah $surahName]\n")
        sb.append("[artist: ${reciter.displayName}]\n")
        sb.append("[Created by Al Quran Editor]\n\n")

        var runningMs = 6060L
        ayahs.forEach { ayah ->
            val startMs = if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningMs
            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            val timestamp = formatLrcTimestamp(startMs)
            sb.append("[$timestamp] $arabicText ۝${toArabicNumber(ayah.ayahNumberInSurah)}\n")
            runningMs += (ayah.textUthmani.length * 350L).coerceAtLeast(6000L)
        }
        return sb.toString().trimEnd()
    }

    // 3. Arabic with Subtitles (SRT)
    fun generateArabicSrt(
        ayahs: List<AyahItem>,
        font: QuranFontFamily
    ): String {
        val sb = StringBuilder()
        var runningStartMs = 6064L

        ayahs.forEachIndexed { index, ayah ->
            val duration = (ayah.textUthmani.length * 350L).coerceAtLeast(6500L)
            val startMs = if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningStartMs
            val endMs = if (ayah.defaultEndMs > 0) ayah.defaultEndMs else (startMs + duration)
            runningStartMs = endMs + 300L

            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani

            sb.append("${index + 1}\n")
            sb.append("${formatSrtTimestamp(startMs)} --> ${formatSrtTimestamp(endMs)}\n")
            sb.append("$arabicText ۝${toArabicNumber(ayah.ayahNumberInSurah)}\n\n")
        }
        return sb.toString().trimEnd()
    }

    // 4. Arabic with Translation with LRC Format
    fun generateArabicWithTranslationLrc(
        surahName: String,
        reciter: ReciterItem,
        ayahs: List<AyahItem>,
        font: QuranFontFamily,
        language: String = "Bangla"
    ): String {
        val sb = StringBuilder()
        sb.append("[ti: Surah $surahName with $language Translation]\n")
        sb.append("[ar: ${reciter.displayName}]\n\n")

        var runningMs = 6060L
        ayahs.forEach { ayah ->
            val startMs = if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningMs
            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            val translation = if (language == "English") ayah.englishTranslation else ayah.banglaTranslation
            val timestamp = formatLrcTimestamp(startMs)
            sb.append("[$timestamp] $arabicText ۝${toArabicNumber(ayah.ayahNumberInSurah)}\n")
            sb.append("$translation\n\n")
            runningMs += (ayah.textUthmani.length * 350L).coerceAtLeast(6000L)
        }
        return sb.toString().trimEnd()
    }

    // 5. Arabic with Translation with SRT Format
    fun generateArabicWithTranslationSrt(
        ayahs: List<AyahItem>,
        font: QuranFontFamily,
        language: String = "Bangla"
    ): String {
        val sb = StringBuilder()
        var runningStartMs = 6064L

        ayahs.forEachIndexed { index, ayah ->
            val duration = (ayah.textUthmani.length * 350L).coerceAtLeast(6500L)
            val startMs = if (ayah.defaultStartMs > 0) ayah.defaultStartMs else runningStartMs
            val endMs = if (ayah.defaultEndMs > 0) ayah.defaultEndMs else (startMs + duration)
            runningStartMs = endMs + 300L

            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            val translation = if (language == "English") ayah.englishTranslation else ayah.banglaTranslation

            sb.append("${index + 1}\n")
            sb.append("${formatSrtTimestamp(startMs)} --> ${formatSrtTimestamp(endMs)}\n")
            sb.append("$arabicText ۝${toArabicNumber(ayah.ayahNumberInSurah)}\n")
            sb.append("$translation\n\n")
        }
        return sb.toString().trimEnd()
    }

    // 6. Arabic with Translation Plain Text
    fun generateArabicWithTranslationText(
        surahName: String,
        ayahs: List<AyahItem>,
        font: QuranFontFamily,
        translationTitle: String = "Bangla - Islamic Foundation"
    ): String {
        val sb = StringBuilder()
        sb.append("Surah $surahName\n")
        sb.append("$translationTitle\n\n")

        ayahs.forEach { ayah ->
            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            val translation = if (translationTitle.contains("English")) ayah.englishTranslation else ayah.banglaTranslation
            sb.append("$arabicText ۝${toArabicNumber(ayah.ayahNumberInSurah)}\n")
            sb.append("${ayah.ayahNumberInSurah}. $translation\n\n")
        }
        return sb.toString().trimEnd()
    }

    // 7. Single Ayah Audio Download Link
    fun generateSingleAyahDownloadLink(reciter: ReciterItem, surahNumber: Int, ayahNumber: Int): String {
        val s = surahNumber.toString().padStart(3, '0')
        val a = ayahNumber.toString().padStart(3, '0')
        return "https://everyayah.com/data/${reciter.serverFolder}/$s$a.mp3"
    }

    // 8. Multiple Ayahs Audio Download Links List
    fun generateMultipleAyahDownloadLinks(reciter: ReciterItem, surahNumber: Int, ayahs: List<AyahItem>): String {
        val sb = StringBuilder()
        sb.append("# Quran Audio Download Links (Qari: ${reciter.displayName})\n")
        val s = surahNumber.toString().padStart(3, '0')
        ayahs.forEach { ayah ->
            val a = ayah.ayahNumberInSurah.toString().padStart(3, '0')
            val url = "https://everyayah.com/data/${reciter.serverFolder}/$s$a.mp3"
            sb.append("Ayah ${ayah.ayahNumberInSurah}: $url\n")
        }
        return sb.toString().trimEnd()
    }

    // 9. Full Arabic + Translation + Direct Audio Download Links
    fun generateAyahsWithDownloadLinksText(
        surahName: String,
        reciter: ReciterItem,
        surahNumber: Int,
        ayahs: List<AyahItem>,
        font: QuranFontFamily,
        includeTranslation: Boolean = true,
        language: String = "Bangla"
    ): String {
        val sb = StringBuilder()
        sb.append("Surah $surahName (Qari: ${reciter.displayName})\n")
        val s = surahNumber.toString().padStart(3, '0')
        ayahs.forEach { ayah ->
            val a = ayah.ayahNumberInSurah.toString().padStart(3, '0')
            val url = "https://everyayah.com/data/${reciter.serverFolder}/$s$a.mp3"
            val arabicText = if (font.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            sb.append("$arabicText ۝${toArabicNumber(ayah.ayahNumberInSurah)}\n")
            if (includeTranslation) {
                val translation = if (language == "English") ayah.englishTranslation else ayah.banglaTranslation
                sb.append("${ayah.ayahNumberInSurah}. $translation\n")
            }
            sb.append("🎵 Audio Link: $url\n\n")
        }
        return sb.toString().trimEnd()
    }
}
