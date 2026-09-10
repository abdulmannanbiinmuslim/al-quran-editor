package com.example.data.timing

/**
 * Letter-by-letter timing representation for tajweed and pronunciation learning.
 */
data class LetterSegmentData(
    val letterIndex: Int,
    val letter: String, // The Arabic letter (with attached harakah/shaddah)
    val startMs: Long,
    val endMs: Long,
    val durationMs: Long
)

/**
 * Word-by-word timing segment.
 */
data class WordSegmentData(
    val wordIndex: Int,
    val wordText: String = "",
    val banglaMeaning: String = "",
    val startMs: Long,
    val endMs: Long,
    val durationMs: Long,
    val letters: List<LetterSegmentData> = emptyList()
)

/**
 * Ayah-by-ayah timing data within a surah.
 */
data class AyahTimingData(
    val surahNumber: Int,
    val ayahNumber: Int,
    val startMs: Long,
    val endMs: Long,
    val durationMs: Long,
    val audioUrl: String? = null,
    val words: List<WordSegmentData> = emptyList()
)

/**
 * Surah-by-surah timing data.
 */
data class SurahTimingData(
    val surahNumber: Int,
    val surahName: String = "",
    val audioUrl: String = "",
    val durationSec: Int = 0,
    val durationMs: Long = 0L,
    val surahStartMs: Long = 0L,
    val surahEndMs: Long = 0L,
    val ayahs: Map<Int, AyahTimingData> = emptyMap()
)

/**
 * Reciter Master Timing format saved as e.g. "Abdul_basit_timing.json"
 * Contains all 4 recitation timing modes:
 * 1. surah_by_surah
 * 2. ayah_by_ayah
 * 3. word_by_word
 * 4. letter_by_letter
 */
data class ReciterMasterTimingFile(
    val reciterId: String,
    val reciterName: String,
    val reciterStyle: String,
    val generatedAt: Long = System.currentTimeMillis(),
    val totalSurahsIncluded: Int = 0,
    val surahs: Map<Int, SurahTimingData> = emptyMap()
)
