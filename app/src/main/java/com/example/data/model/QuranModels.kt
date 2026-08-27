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

data class BookmarkFolder(
    val id: String,
    val name: String,
    val colorHex: String = "#1E563F", // Islamic Emerald by default
    val iconType: String = "bookmark", // "clock", "bookmark", "star", "heart"
    val iconName: String = "Bookmark",
    val itemCount: Int = 0,
    val isSystem: Boolean = false,
    val savedAyahs: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
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
    val folderId: String? = null,
    val folderName: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

enum class LibraryType {
    COLLECTION,
    PIN,
    NOTE,
    LAST_READ,
    FAVORITE
}

data class DailyReminderSettings(
    val isEnabled: Boolean = true,
    val hour: Int = 8,
    val minute: Int = 0,
    val reminderText: String = "সময় হয়েছে আজকের কুরআন তিলাওয়াতের! আপনার রুটিন বজায় রাখুন ✨"
)

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

enum class QuranScriptType(val id: String, val displayName: String, val banglaName: String) {
    MADANI_UTHMANI("madani_uthmani", "Madani / Uthmani Script", "মাদানী / উসমানী লিপি"),
    INDOPAK("indopak", "IndoPak / Asian Script", "ইন্দোপাক / উপমহাদেশীয় লিপি")
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

enum class AppColorTheme(
    val id: String,
    val displayName: String,
    val banglaName: String,
    val description: String
) {
    EMERALD(
        id = "emerald",
        displayName = "Emerald Oasis",
        banglaName = "সবুজ মরূদ্যান (Emerald)",
        description = "ঐতিহ্যবাহী ইসলামিক সবুজ ও সোনালী আভা"
    ),
    SAPPHIRE(
        id = "sapphire",
        displayName = "Royal Sapphire",
        banglaName = "রাজকীয় নীল (Sapphire)",
        description = "গভীর আকাশী নীল ও রূপালী প্রশান্তি"
    ),
    AMBER_DESERT(
        id = "amber",
        displayName = "Desert Sunset",
        banglaName = "মরুভূমির স্বর্ণাভ (Amber)",
        description = "উষ্ণ টেরাকোটা ও সূর্যাস্তের সোনালী আবেশ"
    ),
    AMETHYST(
        id = "amethyst",
        displayName = "Velvet Amethyst",
        banglaName = "মর্যাদাপূর্ণ পার্পল (Amethyst)",
        description = "রাজকীয় বাইজেন্টাইন বেগুনি ও রোজ গোল্ড"
    ),
    SEPIA_MUSHAF(
        id = "sepia",
        displayName = "Antique Manuscript",
        banglaName = "প্রাচীন পাণ্ডুলিপি (Sepia)",
        description = "ঐতিহাসিক মুসহাফের ভিন্টেজ পার্চমেন্ট ও দারুচিনি"
    )
}

enum class NightModeOption(
    val id: String,
    val title: String,
    val banglaTitle: String
) {
    LIGHT("light", "Light Mode", "দিন মোড (Light)"),
    NIGHT("night", "Night Mode", "নাইট মোড (Night)"),
    OLED_BLACK("oled", "OLED Pure Black", "পিচ ব্ল্যাক (OLED Black)"),
    SYSTEM("system", "System Default", "সিস্টেম অনুযায়ী (Auto)")
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
    val arabicLineHeightMultiplier: Float = 1.7f,
    val arabicLetterSpacingSp: Float = 0f,
    val arabicFontWeight: String = "Bold", // "Normal", "Medium", "SemiBold", "Bold"
    val translationFontSizeSp: Float = 16f,
    val tafsirFontSizeSp: Float = 14f,
    val selectedScript: QuranScriptType = QuranScriptType.MADANI_UTHMANI,
    val selectedFont: QuranFontFamily = QuranFontFamily.UTHMANIC_HAFS,
    val selectedTranslation: String = "Bangla - Islamic Foundation",
    val selectedTafsir: String = "Bangla - Tafsir Ibn Kathir",
    val selectedMushafType: String = "Mushaf Unicode Text", // or "Classic Madani Mushaf"
    val appColorTheme: AppColorTheme = AppColorTheme.EMERALD,
    val nightModeOption: NightModeOption = NightModeOption.LIGHT,
    val highContrastNightText: Boolean = true,
    val showSessionSummary: Boolean = true
)

data class DailyVersesMetric(
    val dayOfWeek: String, // "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    val dateLabel: String, // "Aug 17", "Aug 18", etc.
    val versesCount: Int,
    val minutesSpent: Int = 15,
    val goalVerses: Int = 30,
    val isCompleted: Boolean = versesCount >= goalVerses
)

data class WeeklyReadingSummary(
    val metrics: List<DailyVersesMetric>,
    val streakDays: Int = 6,
    val totalVersesThisWeek: Int = 304,
    val averageVersesPerDay: Int = 43,
    val totalMinutesThisWeek: Int = 174,
    val goalVersesDaily: Int = 30
)

data class ReadingSessionSummary(
    val surahNumber: Int,
    val surahName: String,
    val surahArabicName: String,
    val durationSeconds: Long,
    val formattedDuration: String,
    val totalAyahs: Int
)


