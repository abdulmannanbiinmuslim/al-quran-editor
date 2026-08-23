package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.audio.AudioPlayerManager
import com.example.data.auth.FirebaseAuthService
import com.example.data.firestore.QuranFirestoreSyncService
import com.example.data.model.*
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.data.repository.TopicsData
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class QuranViewModel(application: Application) : AndroidViewModel(application) {

    val audioPlayer = AudioPlayerManager(application)
    val authService = FirebaseAuthService(application)
    val firestoreService = QuranFirestoreSyncService(application)

    // Firebase Auth & Firestore State
    val currentUser = authService.currentUser
    val cloudSyncStatus = firestoreService.syncStatus
    val cloudUserData = firestoreService.cloudUserData

    // Main Navigation
    private val _currentTab = MutableStateFlow(0) // 0: Home, 1: Planner, 2: Topics, 3: Library, 4: Stats
    val currentTab = _currentTab.asStateFlow()

    // Home Sub Tab
    private val _homeSubTab = MutableStateFlow(ReadingViewMode.SURAH)
    val homeSubTab = _homeSubTab.asStateFlow()

    // Planner Sub Tab
    private val _plannerSubTab = MutableStateFlow(0) // 0: My Planners, 1: Find Planners, 2: Completed
    val plannerSubTab = _plannerSubTab.asStateFlow()

    // Library Sub Tab
    private val _librarySubTab = MutableStateFlow(0) // 0: Collections, 1: Pins, 2: Notes
    val librarySubTab = _librarySubTab.asStateFlow()

    // Reading Screen State
    private val _isReadingMode = MutableStateFlow(false)
    val isReadingMode = _isReadingMode.asStateFlow()

    private val _currentSurah = MutableStateFlow(QuranData.surahs[0])
    val currentSurah = _currentSurah.asStateFlow()

    private val _currentAyahs = MutableStateFlow(QuranData.getAyahsForSurah(1))
    val currentAyahs = _currentAyahs.asStateFlow()

    private val _readingSettings = MutableStateFlow(ReadingSettings())
    val readingSettings = _readingSettings.asStateFlow()

    // Search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearchActive = MutableStateFlow(false)
    val isSearchActive = _isSearchActive.asStateFlow()

    // Drawers & Dialogs
    private val _isDrawerOpen = MutableStateFlow(false)
    val isDrawerOpen = _isDrawerOpen.asStateFlow()

    private val _isJumpToAyahOpen = MutableStateFlow(false)
    val isJumpToAyahOpen = _isJumpToAyahOpen.asStateFlow()

    private val _isQuickSettingsOpen = MutableStateFlow(false)
    val isQuickSettingsOpen = _isQuickSettingsOpen.asStateFlow()

    private val _isAyahOptionsOpen = MutableStateFlow(false)
    val isAyahOptionsOpen = _isAyahOptionsOpen.asStateFlow()
    val selectedAyahForOptions = MutableStateFlow<AyahItem?>(null)

    private val _isShareMenuOpen = MutableStateFlow(false)
    val isShareMenuOpen = _isShareMenuOpen.asStateFlow()

    private val _isMultipleAyahShareOpen = MutableStateFlow(false)
    val isMultipleAyahShareOpen = _isMultipleAyahShareOpen.asStateFlow()

    private val _isAudioEditorOpen = MutableStateFlow(false)
    val isAudioEditorOpen = _isAudioEditorOpen.asStateFlow()

    private val _isTajweedGuideOpen = MutableStateFlow(false)
    val isTajweedGuideOpen = _isTajweedGuideOpen.asStateFlow()

    private val _isReciterSelectorOpen = MutableStateFlow(false)
    val isReciterSelectorOpen = _isReciterSelectorOpen.asStateFlow()

    private val _isPlayerBottomSheetOpen = MutableStateFlow(false)
    val isPlayerBottomSheetOpen = _isPlayerBottomSheetOpen.asStateFlow()

    private val _isTimingGeneratorOpen = MutableStateFlow(false)
    val isTimingGeneratorOpen = _isTimingGeneratorOpen.asStateFlow()

    private val _isFontSettingsOpen = MutableStateFlow(false)
    val isFontSettingsOpen = _isFontSettingsOpen.asStateFlow()

    private val _isThemeSelectorOpen = MutableStateFlow(false)
    val isThemeSelectorOpen = _isThemeSelectorOpen.asStateFlow()

    private val _isDownloadManagerOpen = MutableStateFlow(false)
    val isDownloadManagerOpen = _isDownloadManagerOpen.asStateFlow()
    val selectedReciterForDownload = MutableStateFlow<ReciterItem?>(null)

    // Auto Scroll
    private val _isAutoScrollActive = MutableStateFlow(false)
    val isAutoScrollActive = _isAutoScrollActive.asStateFlow()

    private val _autoScrollSpeed = MutableStateFlow(3) // 1..10
    val autoScrollSpeed = _autoScrollSpeed.asStateFlow()

    private var autoScrollJob: Job? = null

    // Collections / Pins / Notes
    private val _lastReadList = MutableStateFlow(
        listOf(
            LibraryItem("lr1", 1, 1, "Al-Fatihah", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ", "শুরু করছি আল্লাহর নামে...", type = LibraryType.LAST_READ),
            LibraryItem("lr2", 2, 1, "Al-Baqarah", "الم ۝ ذَٰلِكَ ٱلْكِتَٰبُ", "আলিফ-লাম-মীম...", type = LibraryType.LAST_READ),
            LibraryItem("lr3", 7, 1, "Al-A'raf", "الٓمصٓ", "আলিফ-লাম-মীম-সোয়াদ...", type = LibraryType.LAST_READ)
        )
    )
    val lastReadList = _lastReadList.asStateFlow()

    private val _pinnedAyahs = MutableStateFlow(
        listOf(
            LibraryItem("pin1", 2, 255, "Al-Baqarah", "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ", "আয়াতুল কুরসী", type = LibraryType.PIN),
            LibraryItem("pin2", 67, 1, "Al-Mulk", "تَبَٰرَكَ ٱلَّذِى بِيَدِهِ ٱلْمُلْكُ", "বরকতময় তিনি যাঁর হাতে রাজত্ব...", type = LibraryType.PIN)
        )
    )
    val pinnedAyahs = _pinnedAyahs.asStateFlow()

    private val _userNotes = MutableStateFlow(
        listOf(
            LibraryItem(
                "note1",
                1,
                5,
                "Al-Fatihah 1:5",
                "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
                "ইবাদত এবং সাহায্য কেবল আল্লাহর কাছে চাওয়ার অঙ্গীকার।",
                noteText = "দৈনিক প্রতিটি সালাতে অন্তরের গভীর থেকে অনুধাবন করা কর্তব্য।",
                type = LibraryType.NOTE
            )
        )
    )
    val userNotes = _userNotes.asStateFlow()

    // Planner items
    private val _activePlanners = MutableStateFlow(
        listOf(
            PlannerItem("plan1", "Quran in Ramadan", 29, 6, 20, isCustom = false),
            PlannerItem("plan2", "Daily Juz Review", 30, 12, 15, isCustom = true)
        )
    )
    val activePlanners = _activePlanners.asStateFlow()

    val findPlannersList = listOf(
        PlannerItem("f1", "Quran in Ramadan", 29, 0, 20),
        PlannerItem("f2", "Quran in a Month", 30, 0, 20),
        PlannerItem("f3", "Quran in 4 Months", 120, 0, 5),
        PlannerItem("f4", "Quran in a Year", 365, 0, 2)
    )

    private val _completedPlanners = MutableStateFlow<List<PlannerItem>>(emptyList())
    val completedPlanners = _completedPlanners.asStateFlow()

    // Stats
    val currentStreakDays = MutableStateFlow(6)
    val readTodayMinutes = MutableStateFlow(0)
    val readTargetMinutes = MutableStateFlow(1)
    val weeklyStats = listOf(
        "S" to 1,
        "M" to 16,
        "Tu" to 0,
        "W" to 12,
        "Th" to 8,
        "F" to 25,
        "S" to 5
    )

    private val _weeklyReadingSummary = MutableStateFlow(
        WeeklyReadingSummary(
            metrics = listOf(
                DailyVersesMetric(dayOfWeek = "Sun", dateLabel = "17 Aug", versesCount = 28, minutesSpent = 15, goalVerses = 30),
                DailyVersesMetric(dayOfWeek = "Mon", dateLabel = "18 Aug", versesCount = 45, minutesSpent = 28, goalVerses = 30),
                DailyVersesMetric(dayOfWeek = "Tue", dateLabel = "19 Aug", versesCount = 18, minutesSpent = 10, goalVerses = 30),
                DailyVersesMetric(dayOfWeek = "Wed", dateLabel = "20 Aug", versesCount = 56, minutesSpent = 35, goalVerses = 30),
                DailyVersesMetric(dayOfWeek = "Thu", dateLabel = "21 Aug", versesCount = 38, minutesSpent = 22, goalVerses = 30),
                DailyVersesMetric(dayOfWeek = "Fri", dateLabel = "22 Aug", versesCount = 85, minutesSpent = 50, goalVerses = 30),
                DailyVersesMetric(dayOfWeek = "Sat", dateLabel = "23 Aug", versesCount = 42, minutesSpent = 26, goalVerses = 30)
            ),
            streakDays = 6,
            totalVersesThisWeek = 312,
            averageVersesPerDay = 44,
            totalMinutesThisWeek = 186,
            goalVersesDaily = 30
        )
    )
    val weeklyReadingSummary = _weeklyReadingSummary.asStateFlow()

    fun setTab(tab: Int) {
        _currentTab.value = tab
    }

    fun setHomeSubTab(mode: ReadingViewMode) {
        _homeSubTab.value = mode
    }

    fun setPlannerSubTab(index: Int) {
        _plannerSubTab.value = index
    }

    fun setLibrarySubTab(index: Int) {
        _librarySubTab.value = index
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSearchActive(active: Boolean) {
        _isSearchActive.value = active
    }

    fun setDrawerOpen(open: Boolean) {
        _isDrawerOpen.value = open
    }

    fun setJumpToAyahOpen(open: Boolean) {
        _isJumpToAyahOpen.value = open
    }

    fun setQuickSettingsOpen(open: Boolean) {
        _isQuickSettingsOpen.value = open
    }

    fun setAyahOptionsOpen(open: Boolean, ayah: AyahItem? = null) {
        selectedAyahForOptions.value = ayah
        _isAyahOptionsOpen.value = open
    }

    fun setShareMenuOpen(open: Boolean, ayah: AyahItem? = null) {
        if (ayah != null) selectedAyahForOptions.value = ayah
        _isShareMenuOpen.value = open
    }

    fun setMultipleAyahShareOpen(open: Boolean) {
        _isMultipleAyahShareOpen.value = open
    }

    fun setAudioEditorOpen(open: Boolean) {
        _isAudioEditorOpen.value = open
    }

    fun setTajweedGuideOpen(open: Boolean) {
        _isTajweedGuideOpen.value = open
    }

    fun setReciterSelectorOpen(open: Boolean) {
        _isReciterSelectorOpen.value = open
    }

    fun setPlayerBottomSheetOpen(open: Boolean) {
        _isPlayerBottomSheetOpen.value = open
    }

    fun setTimingGeneratorOpen(open: Boolean) {
        _isTimingGeneratorOpen.value = open
    }

    fun setFontSettingsOpen(open: Boolean) {
        _isFontSettingsOpen.value = open
    }

    fun setThemeSelectorOpen(open: Boolean) {
        _isThemeSelectorOpen.value = open
    }

    fun setAppColorTheme(theme: AppColorTheme) {
        _readingSettings.value = _readingSettings.value.copy(appColorTheme = theme)
    }

    fun setNightModeOption(option: NightModeOption) {
        _readingSettings.value = _readingSettings.value.copy(nightModeOption = option)
    }

    fun toggleNightMode() {
        val current = _readingSettings.value.nightModeOption
        val next = when (current) {
            NightModeOption.LIGHT -> NightModeOption.NIGHT
            NightModeOption.NIGHT -> NightModeOption.LIGHT
            NightModeOption.OLED_BLACK -> NightModeOption.LIGHT
            NightModeOption.SYSTEM -> NightModeOption.NIGHT
        }
        _readingSettings.value = _readingSettings.value.copy(nightModeOption = next)
    }

    fun setHighContrastNightText(enabled: Boolean) {
        _readingSettings.value = _readingSettings.value.copy(highContrastNightText = enabled)
    }

    fun setFontFamily(font: QuranFontFamily) {
        _readingSettings.value = _readingSettings.value.copy(selectedFont = font)
    }

    fun setArabicFontSize(sizeSp: Float) {
        _readingSettings.value = _readingSettings.value.copy(arabicFontSizeSp = sizeSp.coerceIn(18f, 52f))
    }

    fun setArabicLineHeightMultiplier(multiplier: Float) {
        _readingSettings.value = _readingSettings.value.copy(arabicLineHeightMultiplier = multiplier.coerceIn(1.2f, 2.6f))
    }

    fun setArabicLetterSpacing(spacingSp: Float) {
        _readingSettings.value = _readingSettings.value.copy(arabicLetterSpacingSp = spacingSp.coerceIn(-1.5f, 4f))
    }

    fun setArabicFontWeight(weight: String) {
        _readingSettings.value = _readingSettings.value.copy(arabicFontWeight = weight)
    }

    fun resetTypographySettings() {
        _readingSettings.value = _readingSettings.value.copy(
            selectedFont = QuranFontFamily.UTHMANIC_HAFS,
            arabicFontSizeSp = 28f,
            arabicLineHeightMultiplier = 1.7f,
            arabicLetterSpacingSp = 0f,
            arabicFontWeight = "Bold"
        )
    }

    fun applyTypographyPreset(presetKey: String) {
        when (presetKey) {
            "madani_standard" -> {
                _readingSettings.value = _readingSettings.value.copy(
                    selectedFont = QuranFontFamily.UTHMANIC_HAFS,
                    arabicFontSizeSp = 28f,
                    arabicLineHeightMultiplier = 1.65f,
                    arabicLetterSpacingSp = 0f,
                    arabicFontWeight = "Bold"
                )
            }
            "amiri_classical" -> {
                _readingSettings.value = _readingSettings.value.copy(
                    selectedFont = QuranFontFamily.UTHMANIC_AMIRI,
                    arabicFontSizeSp = 30f,
                    arabicLineHeightMultiplier = 1.75f,
                    arabicLetterSpacingSp = 0.2f,
                    arabicFontWeight = "Bold"
                )
            }
            "indopak_clarity" -> {
                _readingSettings.value = _readingSettings.value.copy(
                    selectedFont = QuranFontFamily.INDOPAK_NOOREHIDAYAT,
                    arabicFontSizeSp = 30f,
                    arabicLineHeightMultiplier = 1.8f,
                    arabicLetterSpacingSp = 0f,
                    arabicFontWeight = "Bold"
                )
            }
            "indopak_nastaleeq" -> {
                _readingSettings.value = _readingSettings.value.copy(
                    selectedFont = QuranFontFamily.INDOPAK_NASTALEEQ,
                    arabicFontSizeSp = 32f,
                    arabicLineHeightMultiplier = 1.95f,
                    arabicLetterSpacingSp = 0f,
                    arabicFontWeight = "Normal"
                )
            }
            "elder_large_print" -> {
                _readingSettings.value = _readingSettings.value.copy(
                    selectedFont = QuranFontFamily.INDOPAK_NOOREHUDA,
                    arabicFontSizeSp = 36f,
                    arabicLineHeightMultiplier = 1.85f,
                    arabicLetterSpacingSp = 0.5f,
                    arabicFontWeight = "Bold"
                )
            }
            "compact_mushaf" -> {
                _readingSettings.value = _readingSettings.value.copy(
                    selectedFont = QuranFontFamily.UTHMANIC_DIGITALKHAT,
                    arabicFontSizeSp = 24f,
                    arabicLineHeightMultiplier = 1.55f,
                    arabicLetterSpacingSp = 0f,
                    arabicFontWeight = "Medium"
                )
            }
        }
    }

    fun setDownloadManagerOpen(open: Boolean, reciter: ReciterItem? = null) {
        selectedReciterForDownload.value = reciter
        _isDownloadManagerOpen.value = open
    }

    fun openSurah(surahNumber: Int, targetAyahNumber: Int = 1) {
        val surah = QuranData.surahs.find { it.number == surahNumber } ?: QuranData.surahs[0]
        _currentSurah.value = surah
        _currentAyahs.value = QuranData.getAyahsForSurah(surahNumber)
        _isReadingMode.value = true

        // Automatically add to Last Read
        val existing = _lastReadList.value.toMutableList()
        val firstAyah = _currentAyahs.value.firstOrNull() ?: QuranData.fatihahAyahs[0]
        existing.removeAll { it.surahNumber == surahNumber }
        existing.add(
            0,
            LibraryItem(
                id = "lr_${System.currentTimeMillis()}",
                surahNumber = surahNumber,
                ayahNumber = targetAyahNumber,
                surahName = surah.englishName,
                arabicSnippet = firstAyah.textUthmani,
                translationSnippet = firstAyah.banglaTranslation,
                type = LibraryType.LAST_READ
            )
        )
        _lastReadList.value = existing
    }

    fun closeReadingMode() {
        _isReadingMode.value = false
        audioPlayer.stopAudio()
        stopAutoScroll()
    }

    fun updateSettings(transform: (ReadingSettings) -> ReadingSettings) {
        _readingSettings.value = transform(_readingSettings.value)
    }

    fun toggleAutoScroll() {
        if (_isAutoScrollActive.value) {
            stopAutoScroll()
        } else {
            startAutoScroll()
        }
    }

    fun setAutoScrollSpeed(speed: Int) {
        _autoScrollSpeed.value = speed.coerceIn(1, 10)
    }

    private fun startAutoScroll() {
        _isAutoScrollActive.value = true
    }

    fun stopAutoScroll() {
        _isAutoScrollActive.value = false
        autoScrollJob?.cancel()
        autoScrollJob = null
    }

    fun addPin(ayah: AyahItem) {
        val list = _pinnedAyahs.value.toMutableList()
        val exists = list.any { it.surahNumber == ayah.surahNumber && it.ayahNumber == ayah.ayahNumberInSurah }
        if (!exists) {
            val surah = QuranData.surahs.find { it.number == ayah.surahNumber }
            list.add(
                0,
                LibraryItem(
                    id = "pin_${System.currentTimeMillis()}",
                    surahNumber = ayah.surahNumber,
                    ayahNumber = ayah.ayahNumberInSurah,
                    surahName = surah?.englishName ?: "Surah ${ayah.surahNumber}",
                    arabicSnippet = ayah.textUthmani,
                    translationSnippet = ayah.banglaTranslation,
                    type = LibraryType.PIN
                )
            )
            _pinnedAyahs.value = list
        }
    }

    fun removePin(item: LibraryItem) {
        _pinnedAyahs.value = _pinnedAyahs.value.filter { it.id != item.id }
    }

    fun addNote(ayah: AyahItem, noteText: String) {
        val list = _userNotes.value.toMutableList()
        val surah = QuranData.surahs.find { it.number == ayah.surahNumber }
        list.add(
            0,
            LibraryItem(
                id = "note_${System.currentTimeMillis()}",
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumberInSurah,
                surahName = "${surah?.englishName ?: "Surah"} ${ayah.surahNumber}:${ayah.ayahNumberInSurah}",
                arabicSnippet = ayah.textUthmani,
                translationSnippet = ayah.banglaTranslation,
                noteText = noteText,
                type = LibraryType.NOTE
            )
        )
        _userNotes.value = list
    }

    fun deleteNote(item: LibraryItem) {
        _userNotes.value = _userNotes.value.filter { it.id != item.id }
    }

    fun createCustomPlanner(title: String, totalDays: Int, versesPerDay: Int) {
        val list = _activePlanners.value.toMutableList()
        list.add(
            PlannerItem(
                id = "plan_${System.currentTimeMillis()}",
                title = title,
                totalDays = totalDays,
                completedDays = 0,
                targetVersesPerDay = versesPerDay,
                isCustom = true
            )
        )
        _activePlanners.value = list
    }

    fun startFindPlanner(item: PlannerItem) {
        val list = _activePlanners.value.toMutableList()
        if (!list.any { it.title == item.title }) {
            list.add(item.copy(id = "plan_${System.currentTimeMillis()}"))
            _activePlanners.value = list
        }
    }

    // Firebase Authentication & Cloud Firestore Persistence Methods
    fun signInWithGoogle(activityContext: Context) {
        viewModelScope.launch {
            val result = authService.signInWithGoogle(activityContext)
            result.onSuccess { user ->
                syncWithFirestore(user)
            }
        }
    }

    fun quickConnectAccount(email: String = "abdulmannan.biinmuslim@gmail.com", name: String = "Abdul Mannan") {
        authService.signInWithAccount(email, name)
        currentUser.value?.let { user ->
            syncWithFirestore(user)
        }
    }

    fun signOutFromFirebase() {
        authService.signOut()
    }

    fun syncWithFirestore(forcedUser: UserProfile? = null) {
        val user = forcedUser ?: currentUser.value ?: return
        viewModelScope.launch {
            firestoreService.syncUserData(
                user = user,
                streakDays = currentStreakDays.value,
                readTodayMinutes = readTodayMinutes.value,
                readTargetMinutes = readTargetMinutes.value,
                totalVersesRead = 427,
                surahsCompleted = 4,
                totalListeningMinutes = 185,
                weeklyStats = weeklyStats,
                readingSettings = _readingSettings.value,
                pinnedCount = _pinnedAyahs.value.size,
                notesCount = _userNotes.value.size,
                lastReadSurah = _currentSurah.value.number,
                lastReadAyah = 1
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayer.release()
    }
}
