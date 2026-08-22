package com.example.data.model

data class SurahItem(
    val number: Int,
    val englishName: String,
    val arabicName: String,
    val englishTranslation: String,
    val banglaTranslation: String,
    val totalAyahs: Int,
    val revelationType: String, // "Meccan" or "Medinan"
    val startPage: Int,
    val startJuz: Int,
    val startHizb: Int,
    val startRuku: Int
)

data class WordItem(
    val position: Int,
    val arabic: String,
    val english: String,
    val bangla: String
)

data class AyahItem(
    val surahNumber: Int,
    val ayahNumberInSurah: Int,
    val ayahNumberInQuran: Int,
    val textUthmani: String,
    val textIndopak: String,
    val englishTranslation: String,
    val banglaTranslation: String,
    val banglaTafsir: String,
    val words: List<WordItem> = emptyList(),
    val pageNumber: Int,
    val juzNumber: Int,
    val hizbNumber: Int,
    val rukuNumber: Int,
    val isSajdah: Boolean = false,
    val defaultStartMs: Long = 0L,
    val defaultEndMs: Long = 0L
)

data class ReciterItem(
    val id: String,
    val name: String,
    val displayName: String,
    val subStyle: String = "Murattal", // "Murattal" or "Mujawwad"
    val serverFolder: String,
    val style: String = "Murattal",
    val country: String = "Saudi Arabia",
    val bitRate: String = "128kbps",
    val isDownloaded: Boolean = false,
    val downloadedSurahsCount: Int = 0,
    val totalSurahsCount: Int = 114,
    val spaceUsedKb: Long = 0L
)

data class TimingEntry(
    val ayahIndex: Int,
    val startMs: Long,
    val endMs: Long,
    val durationMs: Long = endMs - startMs
)

data class PlannerItem(
    val id: String,
    val title: String,
    val totalDays: Int,
    val completedDays: Int = 0,
    val targetVersesPerDay: Int = 20,
    val isCustom: Boolean = false,
    val isCompleted: Boolean = false,
    val category: String = "Read Complete Quran"
)

data class LibraryItem(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahName: String,
    val arabicSnippet: String,
    val translationSnippet: String,
    val noteText: String = "",
    val type: LibraryType = LibraryType.COLLECTION,
    val timestamp: Long = System.currentTimeMillis()
)

enum class LibraryType {
    COLLECTION,
    PIN,
    NOTE,
    LAST_READ
}

data class TopicItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val iconName: String,
    val versesCount: Int,
    val verses: List<TopicVerseRef>
)

data class TopicVerseRef(
    val surahNumber: Int,
    val ayahNumber: Int,
    val title: String,
    val arabicSnippet: String,
    val translationSnippet: String
)

enum class ReadingViewMode {
    SURAH,
    PAGE,
    JUZ,
    HIZB,
    RUKU
}

enum class ReadingLayoutMode {
    LYRICS_AYAH_BY_AYAH,
    PAGE_MUSHAF
}

enum class QuranFontFamily {
    UTHMANIC_HAFS,
    UTHMANIC_DIGITALKHAT,
    UTHMANIC_AMIRI,
    UTHMANIC_SCHEHERAZADE,
    ME_QURAN,
    INDOPAK_NASTALEEQ,
    INDOPAK_NOOREHUDA,
    INDOPAK_NOOREHIDAYAT,
    INDOPAK_PDMS_SALEEM
}

data class ReadingSettings(
    val viewMode: ReadingViewMode = ReadingViewMode.SURAH,
    val layoutMode: ReadingLayoutMode = ReadingLayoutMode.LYRICS_AYAH_BY_AYAH,
    val showArabic: Boolean = true,
    val showTranslation: Boolean = true,
    val showWordByWord: Boolean = false,
    val showTafsir: Boolean = false,
    val showTajweed: Boolean = true,
    val arabicFontSizeSp: Float = 28f,
    val translationFontSizeSp: Float = 16f,
    val tafsirFontSizeSp: Float = 14f,
    val selectedFont: QuranFontFamily = QuranFontFamily.UTHMANIC_HAFS,
    val selectedTranslation: String = "Bangla - Islamic Foundation",
    val selectedTafsir: String = "Bangla - Tafsir Ibn Kathir",
    val selectedMushafType: String = "Mushaf Unicode Text" // or "Classic Madani Mushaf"
)
