package com.example

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AyahItem
import com.example.data.model.LibraryItem
import com.example.data.model.ReadingLayoutMode
import com.example.ui.components.*
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.library.LibraryScreen
import com.example.ui.screens.planner.PlannerScreen
import com.example.ui.screens.reading.QuranReadingScreen
import com.example.ui.screens.reciters.DownloadManagerDialog
import com.example.ui.screens.reciters.ReciterSelectorScreen
import com.example.ui.screens.stats.StatsScreen
import com.example.ui.screens.topics.TopicsScreen
import com.example.ui.theme.AlQuranEditorTheme
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.viewmodel.QuranViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: QuranViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val readingSettings by viewModel.readingSettings.collectAsStateWithLifecycle()
            AlQuranEditorTheme(
                theme = readingSettings.appColorTheme,
                nightModeOption = readingSettings.nightModeOption
            ) {
                QuranAppRoot(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun QuranAppRoot(viewModel: QuranViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val isReadingMode by viewModel.isReadingMode.collectAsStateWithLifecycle()
    val currentSurah by viewModel.currentSurah.collectAsStateWithLifecycle()
    val currentAyahs by viewModel.currentAyahs.collectAsStateWithLifecycle()
    val readingSettings by viewModel.readingSettings.collectAsStateWithLifecycle()
    val audioState by viewModel.audioPlayer.playerState.collectAsStateWithLifecycle()

    val isSearchActive by viewModel.isSearchActive.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val homeSubTab by viewModel.homeSubTab.collectAsStateWithLifecycle()
    val plannerSubTab by viewModel.plannerSubTab.collectAsStateWithLifecycle()
    val librarySubTab by viewModel.librarySubTab.collectAsStateWithLifecycle()

    val isJumpToAyahOpen by viewModel.isJumpToAyahOpen.collectAsStateWithLifecycle()
    val isQuickSettingsOpen by viewModel.isQuickSettingsOpen.collectAsStateWithLifecycle()
    val isAyahOptionsOpen by viewModel.isAyahOptionsOpen.collectAsStateWithLifecycle()
    val selectedAyahForOptions by viewModel.selectedAyahForOptions.collectAsStateWithLifecycle()

    val isShareMenuOpen by viewModel.isShareMenuOpen.collectAsStateWithLifecycle()
    val isMultipleAyahShareOpen by viewModel.isMultipleAyahShareOpen.collectAsStateWithLifecycle()
    val isAudioEditorOpen by viewModel.isAudioEditorOpen.collectAsStateWithLifecycle()
    val isTajweedGuideOpen by viewModel.isTajweedGuideOpen.collectAsStateWithLifecycle()
    val isReciterSelectorOpen by viewModel.isReciterSelectorOpen.collectAsStateWithLifecycle()
    val isPlayerBottomSheetOpen by viewModel.isPlayerBottomSheetOpen.collectAsStateWithLifecycle()
    val isTimingGeneratorOpen by viewModel.isTimingGeneratorOpen.collectAsStateWithLifecycle()
    val isFontSettingsOpen by viewModel.isFontSettingsOpen.collectAsStateWithLifecycle()
    val isThemeSelectorOpen by viewModel.isThemeSelectorOpen.collectAsStateWithLifecycle()
    val isDownloadManagerOpen by viewModel.isDownloadManagerOpen.collectAsStateWithLifecycle()
    val selectedReciterForDownload by viewModel.selectedReciterForDownload.collectAsStateWithLifecycle()
    val sessionSummary by viewModel.sessionSummary.collectAsStateWithLifecycle()
    val sessionSummaryToast by viewModel.sessionSummaryToast.collectAsStateWithLifecycle()

    LaunchedEffect(sessionSummaryToast) {
        sessionSummaryToast?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
            viewModel.clearSessionSummaryToast()
        }
    }

    val isAutoScrollActive by viewModel.isAutoScrollActive.collectAsStateWithLifecycle()
    val autoScrollSpeed by viewModel.autoScrollSpeed.collectAsStateWithLifecycle()

    val lastReadList by viewModel.lastReadList.collectAsStateWithLifecycle()
    val pinnedAyahs by viewModel.pinnedAyahs.collectAsStateWithLifecycle()
    val favoriteAyahs by viewModel.favoriteAyahs.collectAsStateWithLifecycle()
    val bookmarkFolders by viewModel.bookmarkFolders.collectAsStateWithLifecycle()
    val dontShowCelebrationAgain by viewModel.dontShowCelebrationAgain.collectAsStateWithLifecycle()
    val completedSurahs by viewModel.completedSurahs.collectAsStateWithLifecycle()
    val dailyReminderSettings by viewModel.dailyReminderSettings.collectAsStateWithLifecycle()
    val userNotes by viewModel.userNotes.collectAsStateWithLifecycle()
    val activePlanners by viewModel.activePlanners.collectAsStateWithLifecycle()
    val completedPlanners by viewModel.completedPlanners.collectAsStateWithLifecycle()

    val streakDays by viewModel.currentStreakDays.collectAsStateWithLifecycle()
    val readTodayMin by viewModel.readTodayMinutes.collectAsStateWithLifecycle()
    val readTargetMin by viewModel.readTargetMinutes.collectAsStateWithLifecycle()

    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val cloudSyncStatus by viewModel.cloudSyncStatus.collectAsStateWithLifecycle()
    val cloudUserData by viewModel.cloudUserData.collectAsStateWithLifecycle()
    val weeklyReadingSummary by viewModel.weeklyReadingSummary.collectAsStateWithLifecycle()

    var showNoteDialogForAyah by remember { mutableStateOf<AyahItem?>(null) }
    var selectedAyahForBookmarkFolders by remember { mutableStateOf<AyahItem?>(null) }

    // Back handling
    BackHandler(enabled = isReadingMode || isReciterSelectorOpen || drawerState.isOpen) {
        when {
            drawerState.isOpen -> scope.launch { drawerState.close() }
            isReciterSelectorOpen -> viewModel.setReciterSelectorOpen(false)
            isReadingMode -> viewModel.closeReadingMode()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            RightNavigationDrawerContent(
                onItemClick = { itemId ->
                    scope.launch { drawerState.close() }
                    when (itemId) {
                        "theme_night_mode" -> viewModel.setThemeSelectorOpen(true)
                        "jump_to_ayah" -> viewModel.setJumpToAyahOpen(true)
                        "font_studio" -> viewModel.setFontSettingsOpen(true)
                        "timing_sync" -> viewModel.setTimingGeneratorOpen(true)
                        "settings" -> viewModel.setQuickSettingsOpen(true)
                        "view_tutorials" -> viewModel.setTajweedGuideOpen(true)
                        "share_app" -> {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Al Quran Editor App")
                                putExtra(Intent.EXTRA_TEXT, "Read and listen to the Holy Quran with Al Quran Editor app.")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share App"))
                        }
                        else -> {
                            Toast.makeText(context, "$itemId selected", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onSocialClick = { socialType ->
                    Toast.makeText(context, "Opening $socialType channel", Toast.LENGTH_SHORT).show()
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                if (!isReadingMode && !isReciterSelectorOpen) {
                    TopToolBar(
                        title = when (currentTab) {
                            0 -> "Al Quran"
                            1 -> "Quran Planner"
                            2 -> "Quran Topics"
                            3 -> "Quran Library"
                            else -> "Quran Stats"
                        },
                        isSearchActive = isSearchActive,
                        searchQuery = searchQuery,
                        isNightMode = readingSettings.nightModeOption == com.example.data.model.NightModeOption.NIGHT || readingSettings.nightModeOption == com.example.data.model.NightModeOption.OLED_BLACK,
                        onToggleNightMode = { viewModel.toggleNightMode() },
                        onOpenThemeSelector = { viewModel.setThemeSelectorOpen(true) },
                        onSearchQueryChange = { viewModel.setSearchQuery(it) },
                        onSearchToggle = { viewModel.setSearchActive(!isSearchActive) },
                        onTitleClick = { viewModel.setJumpToAyahOpen(true) },
                        onMenuClick = { scope.launch { drawerState.open() } }
                    )
                }
            },
            bottomBar = {
                if (!isReadingMode && !isReciterSelectorOpen) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = currentTab == 0,
                            onClick = { viewModel.setTab(0) },
                            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Home") },
                            label = { Text("Home", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IslamicEmeraldPrimary,
                                selectedTextColor = IslamicEmeraldPrimary,
                                indicatorColor = IslamicEmeraldPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_tab_home")
                        )

                        NavigationBarItem(
                            selected = currentTab == 1,
                            onClick = { viewModel.setTab(1) },
                            icon = { Icon(Icons.Default.EventNote, contentDescription = "Planner") },
                            label = { Text("Planner", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IslamicEmeraldPrimary,
                                selectedTextColor = IslamicEmeraldPrimary,
                                indicatorColor = IslamicEmeraldPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_tab_planner")
                        )

                        NavigationBarItem(
                            selected = currentTab == 2,
                            onClick = { viewModel.setTab(2) },
                            icon = { Icon(Icons.Default.Category, contentDescription = "Topics") },
                            label = { Text("Topics", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IslamicEmeraldPrimary,
                                selectedTextColor = IslamicEmeraldPrimary,
                                indicatorColor = IslamicEmeraldPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_tab_topics")
                        )

                        NavigationBarItem(
                            selected = currentTab == 3,
                            onClick = { viewModel.setTab(3) },
                            icon = { Icon(Icons.Default.Bookmarks, contentDescription = "Library") },
                            label = { Text("Library", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IslamicEmeraldPrimary,
                                selectedTextColor = IslamicEmeraldPrimary,
                                indicatorColor = IslamicEmeraldPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_tab_library")
                        )

                        NavigationBarItem(
                            selected = currentTab == 4,
                            onClick = { viewModel.setTab(4) },
                            icon = { Icon(Icons.Default.Insights, contentDescription = "Stats") },
                            label = { Text("Stats", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = IslamicEmeraldPrimary,
                                selectedTextColor = IslamicEmeraldPrimary,
                                indicatorColor = IslamicEmeraldPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("nav_tab_stats")
                        )
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isReciterSelectorOpen) {
                    ReciterSelectorScreen(
                        currentReciter = audioState.currentReciter,
                        onSelectReciter = { reciter ->
                            viewModel.audioPlayer.playSingleAyah(
                                surahNumber = currentSurah.number,
                                ayah = currentAyahs.firstOrNull() ?: com.example.data.repository.QuranData.fatihahAyahs[0],
                                reciter = reciter
                            )
                            viewModel.setReciterSelectorOpen(false)
                        },
                        onOpenDownloadManager = { reciter ->
                            viewModel.setDownloadManagerOpen(true, reciter)
                        },
                        onBack = { viewModel.setReciterSelectorOpen(false) }
                    )
                } else if (isReadingMode) {
                    QuranReadingScreen(
                        surah = currentSurah,
                        ayahs = currentAyahs,
                        settings = readingSettings,
                        audioState = audioState,
                        isAutoScrollActive = isAutoScrollActive,
                        autoScrollSpeed = autoScrollSpeed,
                        isFavoriteAyah = { s, a -> viewModel.isAyahFavorite(s, a) },
                        isSurahCompleted = viewModel.isSurahCompleted(currentSurah.number),
                        dontShowCelebrationAgain = dontShowCelebrationAgain,
                        onToggleSurahCompleted = { viewModel.toggleSurahCompleted(currentSurah.number) },
                        onSetDontShowCelebration = { viewModel.setDontShowCelebrationAgain(it) },
                        onToggleFavoriteAyah = { viewModel.toggleFavorite(it) },
                        onBack = { viewModel.closeReadingMode() },
                        onTitleClick = { viewModel.setJumpToAyahOpen(true) },
                        onToggleLayoutMode = {
                            viewModel.updateSettings {
                                it.copy(
                                    layoutMode = if (it.layoutMode == ReadingLayoutMode.LYRICS_AYAH_BY_AYAH)
                                        ReadingLayoutMode.PAGE_MUSHAF
                                    else
                                        ReadingLayoutMode.LYRICS_AYAH_BY_AYAH
                                )
                            }
                        },
                        onOpenQuickSettings = { viewModel.setQuickSettingsOpen(true) },
                        onOpenFontSettings = { viewModel.setFontSettingsOpen(true) },
                        onOpenContents = { viewModel.setQuickSettingsOpen(true) },
                        onToggleAutoScroll = { viewModel.toggleAutoScroll() },
                        onAutoScrollSpeedChange = { viewModel.setAutoScrollSpeed(it) },
                        onOpenAudioEditor = { viewModel.setAudioEditorOpen(true) },
                        onOpenPlayerBottomSheet = { viewModel.setPlayerBottomSheetOpen(true) },
                        onPlayPauseAudio = { viewModel.audioPlayer.togglePlayPause() },
                        onSeekAudio = { viewModel.audioPlayer.seekTo(it) },
                        onNextAyahAudio = { viewModel.audioPlayer.skipNext() },
                        onPreviousAyahAudio = { viewModel.audioPlayer.skipPrevious() },
                        onOpenPlanner = {
                            viewModel.closeReadingMode()
                            viewModel.setTab(1)
                        },
                        onAyahOptionsClick = { ayah ->
                            viewModel.setAyahOptionsOpen(true, ayah)
                        },
                        onPlaySingleAyah = { ayah ->
                            viewModel.audioPlayer.playSingleAyah(currentSurah.number, ayah, audioState.currentReciter)
                            viewModel.setPlayerBottomSheetOpen(true)
                        },
                        onAddBookmark = { ayah ->
                            selectedAyahForBookmarkFolders = ayah
                        },
                        onAddNoteClick = { ayah ->
                            showNoteDialogForAyah = ayah
                        },
                        onFontSizeChange = { newSize ->
                            viewModel.updateSettings { it.copy(arabicFontSizeSp = newSize) }
                        }
                    )
                } else {
                    when (currentTab) {
                        0 -> HomeScreen(
                            activeSubTab = homeSubTab,
                            onSubTabChange = { viewModel.setHomeSubTab(it) },
                            onSurahClick = { surahNum, ayahNum -> viewModel.openSurah(surahNum, ayahNum) },
                            onReciterClick = { reciter ->
                                viewModel.openSurah(1, 1)
                                viewModel.audioPlayer.playSingleAyah(1, com.example.data.repository.QuranData.fatihahAyahs[0], reciter)
                                viewModel.setPlayerBottomSheetOpen(true)
                            },
                            lastReadList = lastReadList,
                            searchQuery = searchQuery,
                            weeklyReadingSummary = weeklyReadingSummary,
                            onViewFullStats = { viewModel.setTab(4) }
                        )
                        1 -> PlannerScreen(
                            activeSubTab = plannerSubTab,
                            onSubTabChange = { viewModel.setPlannerSubTab(it) },
                            activePlanners = activePlanners,
                            findPlanners = viewModel.findPlannersList,
                            completedPlanners = completedPlanners,
                            onCreatePlanner = { t, d, v -> viewModel.createCustomPlanner(t, d, v) },
                            onStartPlanner = { item -> viewModel.startFindPlanner(item) }
                        )
                        2 -> TopicsScreen(
                            onNavigateToAyah = { s, a -> viewModel.openSurah(s, a) }
                        )
                        3 -> LibraryScreen(
                            activeSubTab = librarySubTab,
                            onSubTabChange = { viewModel.setLibrarySubTab(it) },
                            lastReadList = lastReadList,
                            favoriteList = favoriteAyahs,
                            pinnedList = pinnedAyahs,
                            notesList = userNotes,
                            onNavigateToAyah = { s, a -> viewModel.openSurah(s, a) },
                            onDeletePin = { viewModel.removePin(it) },
                            onDeleteFavorite = { viewModel.removeFavorite(it) },
                            onDeleteNote = { viewModel.deleteNote(it) }
                        )
                        4 -> StatsScreen(
                            currentStreakDays = streakDays,
                            readTodayMinutes = readTodayMin,
                            readTargetMinutes = readTargetMin,
                            weeklyStats = viewModel.weeklyStats,
                            weeklyReadingSummary = weeklyReadingSummary,
                            dailyReminderSettings = dailyReminderSettings,
                            onUpdateDailyReminder = { viewModel.updateDailyReminderSettings(it) },
                            onSendTestReminder = { viewModel.sendTestDailyReminder() },
                            currentUser = currentUser,
                            cloudSyncStatus = cloudSyncStatus,
                            cloudUserData = cloudUserData,
                            onSignInWithGoogle = { viewModel.signInWithGoogle(context) },
                            onQuickSignIn = { viewModel.quickConnectAccount() },
                            onSyncNow = { viewModel.syncWithFirestore() },
                            onSignOut = { viewModel.signOutFromFirebase() }
                        )
                    }
                }

                // Mini / Persistent Floating Player Bar (shown on main screens when audio is active)
                if (!isReadingMode && !isReciterSelectorOpen && (audioState.isPlaying || audioState.isBuffering || audioState.currentAyah != null)) {
                    PersistentAudioPlayerBar(
                        audioState = audioState,
                        onExpandPlayer = { viewModel.setPlayerBottomSheetOpen(true) },
                        onPlayPauseToggle = { viewModel.audioPlayer.togglePlayPause() },
                        onSkipNext = { viewModel.audioPlayer.skipNext() },
                        onSkipPrevious = { viewModel.audioPlayer.skipPrevious() },
                        onClosePlayer = { viewModel.audioPlayer.stopAudio() },
                        modifier = Modifier.align(Alignment.BottomCenter)
                    )
                }
            }
        }
    }

    // Jump To Ayah Bottom Sheet
    if (isJumpToAyahOpen) {
        JumpToAyahBottomSheet(
            onDismiss = { viewModel.setJumpToAyahOpen(false) },
            onNavigateToSurah = { surahNum, ayahNum ->
                viewModel.openSurah(surahNum, ayahNum)
            }
        )
    }

    // Quick Settings Drawer / Bottom Sheet
    if (isQuickSettingsOpen) {
        QuickSettingsModalSheet(
            settings = readingSettings,
            onSettingsChange = { newSettings -> viewModel.updateSettings { newSettings } },
            onOpenTajweedGuide = {
                viewModel.setQuickSettingsOpen(false)
                viewModel.setTajweedGuideOpen(true)
            },
            onOpenFontSettings = {
                viewModel.setQuickSettingsOpen(false)
                viewModel.setFontSettingsOpen(true)
            },
            onOpenThemeSelector = {
                viewModel.setQuickSettingsOpen(false)
                viewModel.setThemeSelectorOpen(true)
            },
            onDismiss = { viewModel.setQuickSettingsOpen(false) }
        )
    }

    // Tajweed Rules Dialog
    if (isTajweedGuideOpen) {
        TajweedRulesDialog(
            onDismiss = { viewModel.setTajweedGuideOpen(false) }
        )
    }

    // Ayah Options Bottom Sheet (1st Level)
    if (isAyahOptionsOpen && selectedAyahForOptions != null) {
        val ayah = selectedAyahForOptions!!
        val isFav = viewModel.isAyahFavorite(ayah.surahNumber, ayah.ayahNumberInSurah)
        AyahOptionsBottomSheet(
            ayah = ayah,
            surahNumber = currentSurah.number,
            currentReciter = audioState.currentReciter,
            isFavorite = isFav,
            onToggleFavorite = {
                viewModel.toggleFavorite(ayah)
                Toast.makeText(
                    context,
                    if (isFav) "Removed Ayah ${ayah.ayahNumberInSurah} from Favorites" else "Added Ayah ${ayah.ayahNumberInSurah} to Favorites ⭐",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onCopy = {
                val clip = ClipData.newPlainText(
                    "Ayah ${ayah.surahNumber}:${ayah.ayahNumberInSurah}",
                    "${ayah.textUthmani}\n${ayah.banglaTranslation}"
                )
                (context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(clip)
                Toast.makeText(context, "Copied Ayah ${ayah.ayahNumberInSurah}", Toast.LENGTH_SHORT).show()
            },
            onShare = {
                viewModel.setAyahOptionsOpen(false)
                viewModel.setShareMenuOpen(true, ayah)
            },
            onAddToPlanner = {
                viewModel.createCustomPlanner("Review Surah ${currentSurah.englishName} Ayah ${ayah.ayahNumberInSurah}", 7, 5)
                Toast.makeText(context, "Added to Planner", Toast.LENGTH_SHORT).show()
            },
            onTafsirNoteView = {
                viewModel.setAyahOptionsOpen(false)
                showNoteDialogForAyah = ayah
            },
            onPlayAyah = {
                viewModel.setAyahOptionsOpen(false)
                viewModel.audioPlayer.playSingleAyah(currentSurah.number, ayah, audioState.currentReciter)
                viewModel.setPlayerBottomSheetOpen(true)
            },
            onOpenTimingSync = {
                viewModel.setAyahOptionsOpen(false)
                viewModel.setTimingGeneratorOpen(true)
            },
            onAddBookmark = {
                val targetAyah = ayah
                viewModel.setAyahOptionsOpen(false)
                selectedAyahForBookmarkFolders = targetAyah
            },
            onDismiss = { viewModel.setAyahOptionsOpen(false) }
        )
    }

    // Interactive Bookmark & Folder Management Bottom Sheet
    if (selectedAyahForBookmarkFolders != null) {
        val ayah = selectedAyahForBookmarkFolders!!
        val currentFolderIds = remember(ayah, bookmarkFolders) {
            viewModel.getFoldersForAyah(ayah.surahNumber, ayah.ayahNumberInSurah).toSet()
        }
        BookmarkBottomSheet(
            ayah = ayah,
            folders = bookmarkFolders,
            selectedFolderIds = currentFolderIds,
            onCreateFolder = { folderName, colorHex, _ ->
                viewModel.createBookmarkFolder(folderName, colorHex)
            },
            onSaveSelection = { selectedIds ->
                viewModel.saveAyahToFolders(ayah, selectedIds.toList())
                Toast.makeText(context, "বুকমার্ক সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                selectedAyahForBookmarkFolders = null
            },
            onDismiss = { selectedAyahForBookmarkFolders = null }
        )
    }

    // Ayah Share Options Bottom Sheet (2nd Level)
    if (isShareMenuOpen && selectedAyahForOptions != null) {
        val ayah = selectedAyahForOptions!!
        AyahShareBottomSheet(
            onShareImage = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Quran Verse")
                    putExtra(Intent.EXTRA_TEXT, "✨ ${currentSurah.englishName} [${ayah.surahNumber}:${ayah.ayahNumberInSurah}]\n\n${ayah.textUthmani}\n\n${ayah.banglaTranslation}\n\n— Shared via Al Quran Editor")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Verse Image/Text"))
            },
            onShareText = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Ayah ${ayah.surahNumber}:${ayah.ayahNumberInSurah}")
                    putExtra(Intent.EXTRA_TEXT, "${ayah.textUthmani}\n\n${ayah.banglaTranslation}\n\n(${currentSurah.englishName} ${ayah.surahNumber}:${ayah.ayahNumberInSurah})")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Verse Text"))
            },
            onShareMultipleAyahs = {
                viewModel.setShareMenuOpen(false)
                viewModel.setMultipleAyahShareOpen(true)
            },
            onShareAudioDownloadLink = {
                val s = currentSurah.number.toString().padStart(3, '0')
                val a = ayah.ayahNumberInSurah.toString().padStart(3, '0')
                val downloadUrl = "https://everyayah.com/data/${audioState.currentReciter.serverFolder}/$s$a.mp3"
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Ayah ${ayah.surahNumber}:${ayah.ayahNumberInSurah} Audio Download Link")
                    putExtra(Intent.EXTRA_TEXT, "Surah ${currentSurah.englishName} Ayah ${ayah.ayahNumberInSurah} (${audioState.currentReciter.displayName})\nDirect MP3: $downloadUrl")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Audio Download Link"))
                viewModel.setShareMenuOpen(false)
            },
            onDismiss = { viewModel.setShareMenuOpen(false) }
        )
    }

    // Multiple Ayah Share Bottom Sheet (3rd Level - LRC / SRT / Translations / Audio Download Links)
    if (isMultipleAyahShareOpen) {
        MultipleAyahShareBottomSheet(
            surahName = currentSurah.englishName,
            surahNumber = currentSurah.number,
            totalAyahsCount = currentSurah.totalAyahs,
            ayahs = currentAyahs,
            selectedFont = readingSettings.selectedFont,
            onDismiss = { viewModel.setMultipleAyahShareOpen(false) }
        )
    }

    // Audio System Editor Bottom Sheet (Pages 40-41)
    if (isAudioEditorOpen) {
        AudioSystemEditorBottomSheet(
            surahName = currentSurah.englishName,
            surahNumber = currentSurah.number,
            totalAyahsCount = currentSurah.totalAyahs,
            currentReciter = audioState.currentReciter,
            onOpenReciterSelector = {
                viewModel.setAudioEditorOpen(false)
                viewModel.setReciterSelectorOpen(true)
            },
            onPlay = { startAyah, endAyah, reciter, repeatAyah ->
                viewModel.audioPlayer.playAyahRange(
                    surahNumber = currentSurah.number,
                    ayahs = currentAyahs,
                    reciter = reciter,
                    startAyah = startAyah,
                    endAyah = endAyah,
                    repeatAyahCount = repeatAyah
                )
                viewModel.setPlayerBottomSheetOpen(true)
            },
            onDismiss = { viewModel.setAudioEditorOpen(false) }
        )
    }

    // Reciter Audio Player Bottom Sheet (Play / Pause & Progress Seek Controls)
    if (isPlayerBottomSheetOpen) {
        ReciterAudioPlayerBottomSheet(
            audioState = audioState,
            selectedFont = readingSettings.selectedFont,
            onPlayPauseToggle = { viewModel.audioPlayer.togglePlayPause() },
            onSeekTo = { posMs -> viewModel.audioPlayer.seekTo(posMs) },
            onSeekForward = { viewModel.audioPlayer.seekForward(10000L) },
            onSeekBackward = { viewModel.audioPlayer.seekBackward(10000L) },
            onSkipNext = { viewModel.audioPlayer.skipNext() },
            onSkipPrevious = { viewModel.audioPlayer.skipPrevious() },
            onSpeedChange = { speed -> viewModel.audioPlayer.setPlaybackSpeed(speed) },
            onRepeatCountChange = { count -> viewModel.audioPlayer.setRepeatAyahTimes(count) },
            onOpenReciterSelector = {
                viewModel.setPlayerBottomSheetOpen(false)
                viewModel.setReciterSelectorOpen(true)
            },
            onOpenAudioEditor = {
                viewModel.setPlayerBottomSheetOpen(false)
                viewModel.setAudioEditorOpen(true)
            },
            onOpenTimingGenerator = {
                viewModel.setPlayerBottomSheetOpen(false)
                viewModel.setTimingGeneratorOpen(true)
            },
            onAddBookmark = { ayah ->
                viewModel.addPin(ayah)
                Toast.makeText(context, "Added Ayah ${ayah.ayahNumberInSurah} to Pins", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { viewModel.setPlayerBottomSheetOpen(false) }
        )
    }

    // Audio Timing File Generator Bottom Sheet (.lrc / .srt / .vtt)
    if (isTimingGeneratorOpen) {
        TimingFileGeneratorBottomSheet(
            surahName = currentSurah.englishName,
            surahNumber = currentSurah.number,
            totalAyahsCount = currentSurah.totalAyahs,
            ayahs = currentAyahs,
            selectedFont = readingSettings.selectedFont,
            initialReciter = audioState.currentReciter,
            onDismiss = { viewModel.setTimingGeneratorOpen(false) }
        )
    }

    // Arabic Fonts & Typography Studio Bottom Sheet
    if (isFontSettingsOpen) {
        ArabicFontSettingsBottomSheet(
            settings = readingSettings,
            onFontSelected = { font -> viewModel.setFontFamily(font) },
            onFontSizeChanged = { size -> viewModel.setArabicFontSize(size) },
            onLineHeightMultiplierChanged = { mult -> viewModel.setArabicLineHeightMultiplier(mult) },
            onLetterSpacingChanged = { spacing -> viewModel.setArabicLetterSpacing(spacing) },
            onFontWeightChanged = { weight -> viewModel.setArabicFontWeight(weight) },
            onApplyPreset = { preset -> viewModel.applyTypographyPreset(preset) },
            onResetDefaults = { viewModel.resetTypographySettings() },
            onDismiss = { viewModel.setFontSettingsOpen(false) }
        )
    }

    // Download Manager Dialog
    if (isDownloadManagerOpen && selectedReciterForDownload != null) {
        DownloadManagerDialog(
            reciter = selectedReciterForDownload!!,
            onDismiss = { viewModel.setDownloadManagerOpen(false) }
        )
    }

    // Note / Tafsir Reflection Dialog
    if (showNoteDialogForAyah != null) {
        val ayah = showNoteDialogForAyah!!
        var noteInput by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showNoteDialogForAyah = null }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Tafsir & Notes • Ayah ${ayah.ayahNumberInSurah}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = IslamicEmeraldPrimary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = ayah.textUthmani,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = ayah.banglaTranslation,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = noteInput,
                        onValueChange = { noteInput = it },
                        label = { Text("Write personal reflection or note...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showNoteDialogForAyah = null }) {
                            Text("Close")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (noteInput.isNotBlank()) {
                                    viewModel.addNote(ayah, noteInput)
                                    Toast.makeText(context, "Saved note!", Toast.LENGTH_SHORT).show()
                                }
                                showNoteDialogForAyah = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
                        ) {
                            Text("Save Note")
                        }
                    }
                }
            }
        }
    }

    // Theme & Night Mode Selector Bottom Sheet
    if (isThemeSelectorOpen) {
        ThemeSelectionBottomSheet(
            settings = readingSettings,
            onColorThemeSelected = { viewModel.setAppColorTheme(it) },
            onNightModeSelected = { viewModel.setNightModeOption(it) },
            onToggleHighContrast = { viewModel.setHighContrastNightText(it) },
            onDismiss = { viewModel.setThemeSelectorOpen(false) }
        )
    }

    // Reading Session Summary Dialog / Notification with 3 distinct functional buttons
    if (sessionSummary != null) {
        val summary = sessionSummary!!
        AlertDialog(
            onDismissRequest = { viewModel.dismissSessionSummary() },
            icon = {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "সূরা তিলাওয়াত সম্পন্ন",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "সূরা ${summary.surahName} (${summary.surahArabicName})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "তিলাওয়াত সময়কাল:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = summary.formattedDuration,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "মোট আয়াত:",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${summary.totalAyahs} টি",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "আপনার দৈনিক তিলাওয়াত রেকর্ড ও স্ট্রীক পরিসংখ্যানে এই সময়কাল সফলভাবে যুক্ত হয়েছে।",
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 3 Functional Buttons Layout
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Button 1: আলহামদুলিল্লাহ (Primary filled)
                        Button(
                            onClick = { viewModel.confirmSessionSummary() },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("আলহামদুলিল্লাহ", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        // Button 2: পরে মনে করান (Outlined)
                        OutlinedButton(
                            onClick = { viewModel.remindSessionSummaryLater() },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(42.dp)
                        ) {
                            Text("পরে মনে করান", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }

                        // Button 3: আর দেখাবেন না (Outlined button with border)
                        OutlinedButton(
                            onClick = { viewModel.neverShowSessionSummaryAgain() },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Text("আর দেখাবেন না", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = null
        )
    }
}
