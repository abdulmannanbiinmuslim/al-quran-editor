package com.example.ui.screens.reading

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioPlayerState
import com.example.data.model.AyahItem
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReadingLayoutMode
import com.example.data.model.ReadingSettings
import com.example.data.model.SurahItem
import com.example.data.timing.TimingGenerator
import com.example.ui.components.ExpandableAyahTafsirSection
import com.example.ui.theme.*
import com.example.ui.util.TajweedAnnotator
import com.example.ui.util.rememberAppHaptics
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuranReadingScreen(
    surah: SurahItem,
    ayahs: List<AyahItem>,
    settings: ReadingSettings,
    audioState: AudioPlayerState,
    isAutoScrollActive: Boolean,
    autoScrollSpeed: Int,
    isFavoriteAyah: (surahNum: Int, ayahNum: Int) -> Boolean = { _, _ -> false },
    isSurahCompleted: Boolean = false,
    dontShowCelebrationAgain: Boolean = false,
    onToggleSurahCompleted: () -> Unit = {},
    onSetDontShowCelebration: (Boolean) -> Unit = {},
    onToggleFavoriteAyah: (AyahItem) -> Unit = {},
    onBack: () -> Unit,
    onTitleClick: () -> Unit,
    onToggleLayoutMode: () -> Unit,
    onOpenQuickSettings: () -> Unit,
    onOpenFontSettings: () -> Unit = {},
    onOpenContents: () -> Unit,
    onToggleAutoScroll: () -> Unit,
    onAutoScrollSpeedChange: (Int) -> Unit,
    onOpenAudioEditor: () -> Unit,
    onOpenPlayerBottomSheet: () -> Unit = {},
    onOpenPlanner: () -> Unit,
    onAyahOptionsClick: (AyahItem) -> Unit,
    onPlaySingleAyah: (AyahItem) -> Unit,
    onPlayPauseAudio: () -> Unit = {},
    onSeekAudio: (Long) -> Unit = {},
    onNextAyahAudio: () -> Unit = {},
    onPreviousAyahAudio: () -> Unit = {},
    onAddBookmark: (AyahItem) -> Unit,
    onAddNoteClick: (AyahItem) -> Unit = {},
    onFontSizeChange: (Float) -> Unit = {},
    onSelectScriptStyle: (QuranFontFamily) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val haptics = rememberAppHaptics()
    var expandedAyahNumbers by remember { mutableStateOf(setOf<Int>()) }

    // Dynamic Pinch-To-Zoom local state
    var currentArabicSize by remember(settings.arabicFontSizeSp) { mutableFloatStateOf(settings.arabicFontSizeSp) }
    var currentTranslationSize by remember(settings.translationFontSizeSp) { mutableFloatStateOf(settings.translationFontSizeSp) }
    var showZoomHud by remember { mutableStateOf(false) }
    var lastZoomChangeTime by remember { mutableLongStateOf(0L) }
    var showJuzInfoDialog by remember { mutableStateOf(false) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    // Auto-hide Zoom HUD after 1.5 seconds of gesture inactivity
    LaunchedEffect(lastZoomChangeTime) {
        if (showZoomHud) {
            delay(1500L)
            showZoomHud = false
        }
    }

    // Auto scroll effect
    LaunchedEffect(isAutoScrollActive, autoScrollSpeed) {
        if (isAutoScrollActive) {
            while (isAutoScrollActive) {
                val delayTime = (400L / autoScrollSpeed).coerceAtLeast(40L)
                delay(delayTime)
                listState.scrollBy(4f)
            }
        }
    }

    // Auto scroll to active recited ayah
    LaunchedEffect(audioState.currentAyahNumber) {
        if (audioState.isPlaying) {
            val idx = ayahs.indexOfFirst { it.ayahNumberInSurah == audioState.currentAyahNumber }
            if (idx >= 0) {
                listState.animateScrollToItem((idx + 1).coerceAtMost(ayahs.size))
            }
        }
    }

    // Group ayahs by page for continuous Mushaf rendering
    val ayahsByPage = remember(ayahs) {
        ayahs.groupBy { it.pageNumber }
    }

    Scaffold(
        topBar = {
            Surface(
                color = IslamicEmeraldPrimary,
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    // 1. Main Navigation App Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Left: Back Arrow
                        IconButton(
                            onClick = {
                                haptics.tap()
                                onBack()
                            },
                            modifier = Modifier.testTag("reading_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        // Center: Surah Title Dropdown
                        Surface(
                            onClick = {
                                haptics.tap()
                                onTitleClick()
                            },
                            color = Color.White.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.testTag("reading_title_clickable")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "${surah.number}. ${surah.banglaTranslation} (${surah.englishName})",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Select Surah",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Right Action Buttons: Layout, Font studio, Quick Settings
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    haptics.tap()
                                    onToggleLayoutMode()
                                },
                                modifier = Modifier.testTag("toggle_reading_mode_button")
                            ) {
                                Icon(
                                    imageVector = if (settings.layoutMode == ReadingLayoutMode.LYRICS_AYAH_BY_AYAH)
                                        Icons.Default.MenuBook
                                    else
                                        Icons.Default.FormatAlignLeft,
                                    contentDescription = "Toggle Mode",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = {
                                    haptics.tap()
                                    onOpenFontSettings()
                                },
                                modifier = Modifier.testTag("reading_font_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Tune & Font Studio",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = {
                                    haptics.tap()
                                    onOpenQuickSettings()
                                },
                                modifier = Modifier.testTag("reading_quick_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu / Quick Settings",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    // 2. Sub-Header Information Strip
                    val firstAyah = ayahs.firstOrNull()
                    val juzNum = firstAyah?.juzNumber ?: surah.startJuz
                    val hizbNum = firstAyah?.hizbNumber ?: surah.startHizb
                    val pageNum = firstAyah?.pageNumber ?: surah.startPage
                    val rukuNum = firstAyah?.rukuNumber ?: surah.startRuku

                    Surface(
                        onClick = {
                            haptics.tap()
                            onTitleClick()
                        },
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reading_subheader_jump")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "পারা $juzNum  •  Hizb $hizbNum  •  পৃষ্ঠা $pageNum  •  রকু $rukuNum",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Jump",
                                    tint = IslamicEmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Surface(
                                onClick = {
                                    haptics.tap()
                                    showJuzInfoDialog = true
                                },
                                color = Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Juz info",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column {
                // Floating Auto Scroll Speed Controller
                AnimatedVisibility(
                    visible = isAutoScrollActive,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    Surface(
                        color = IslamicEmeraldDark,
                        contentColor = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    haptics.fontSizeTick()
                                    onAutoScrollSpeedChange(autoScrollSpeed - 1)
                                }) {
                                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Slower", tint = Color.White)
                                }
                                Text(
                                    text = "অটো স্ক্রল: ${autoScrollSpeed}x",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranGold
                                )
                                IconButton(onClick = {
                                    haptics.fontSizeTick()
                                    onAutoScrollSpeedChange(autoScrollSpeed + 1)
                                }) {
                                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Faster", tint = Color.White)
                                }
                            }

                            IconButton(onClick = {
                                haptics.tap()
                                onToggleAutoScroll()
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Stop", tint = Color.White)
                            }
                        }
                    }
                }

                // Bottom Navigation Bar Actions
                Surface(
                    color = IslamicEmeraldPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .height(56.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomBarActionButton(
                            icon = Icons.Outlined.Translate,
                            label = "অনুবাদ",
                            onClick = {
                                haptics.tap()
                                onOpenContents()
                            }
                        )

                        BottomBarActionButton(
                            icon = if (isAutoScrollActive) Icons.Default.PauseCircle else Icons.Outlined.SwapVert,
                            label = "অটোস্ক্রল",
                            onClick = {
                                haptics.tap()
                                onToggleAutoScroll()
                            }
                        )

                        BottomBarActionButton(
                            icon = if (audioState.isPlaying) Icons.Default.PauseCircle else Icons.Outlined.PlayCircleOutline,
                            label = if (audioState.isPlaying) "চলছে (${audioState.currentAyahNumber})" else "অডিও",
                            onClick = {
                                haptics.tap()
                                onOpenPlayerBottomSheet()
                            }
                        )

                        BottomBarActionButton(
                            icon = Icons.Outlined.CalendarMonth,
                            label = "প্ল্যানার",
                            onClick = {
                                haptics.tap()
                                onOpenPlanner()
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(Unit) {
                    detectTransformGestures { _, _, zoom, _ ->
                        if (zoom != 1f) {
                            val newArabic = (currentArabicSize * zoom).coerceIn(18f, 54f)
                            val newTrans = (currentTranslationSize * zoom).coerceIn(12f, 32f)

                            if (kotlin.math.abs(newArabic - currentArabicSize) > 0.25f) {
                                currentArabicSize = newArabic
                                currentTranslationSize = newTrans
                                onFontSizeChange(newArabic)
                                showZoomHud = true
                                lastZoomChangeTime = System.currentTimeMillis()
                                haptics.fontSizeTick()
                            }
                        }
                    }
                }
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // Layout Mode 1: CONTINUOUS MUSHAF READING
                if (settings.layoutMode == ReadingLayoutMode.PAGE_MUSHAF) {
                    ayahsByPage.forEach { (pageNumber, pageAyahs) ->
                        if (pageAyahs.any { it.ayahNumberInSurah == 1 }) {
                            item(key = "surah_header_${surah.number}") {
                                SurahHeaderCard(surah = surah, settings = settings)
                            }
                        } else {
                            item(key = "page_separator_$pageNumber") {
                                PageSeparatorStrip(
                                    pageNumber = pageNumber,
                                    rukuNumber = pageAyahs.firstOrNull()?.rukuNumber ?: 1
                                )
                            }
                        }

                        item(key = "mushaf_page_$pageNumber") {
                            ContinuousMushafParagraphCard(
                                ayahs = pageAyahs,
                                settings = settings.copy(
                                    arabicFontSizeSp = currentArabicSize,
                                    translationFontSizeSp = currentTranslationSize
                                ),
                                activeAyahNumber = if (audioState.isPlaying && audioState.currentSurahNumber == surah.number)
                                    audioState.currentAyahNumber else null,
                                onAyahClick = { onAyahOptionsClick(it) }
                            )
                        }
                    }
                } else {
                    // Layout Mode 2: VERSE BY VERSE (LYRICS)
                    item(key = "surah_header_lyrics") {
                        SurahHeaderCard(surah = surah, settings = settings)
                    }

                    items(ayahs, key = { it.ayahNumberInSurah }) { ayah ->
                        val isCurrentlyPlaying = audioState.isPlaying &&
                                audioState.currentSurahNumber == surah.number &&
                                audioState.currentAyahNumber == ayah.ayahNumberInSurah
                        val isTafsirExpanded = expandedAyahNumbers.contains(ayah.ayahNumberInSurah)
                        val isFav = isFavoriteAyah(ayah.surahNumber, ayah.ayahNumberInSurah)

                        AyahCardItem(
                            ayah = ayah,
                            settings = settings.copy(
                                arabicFontSizeSp = currentArabicSize,
                                translationFontSizeSp = currentTranslationSize
                            ),
                            isPlaying = isCurrentlyPlaying,
                            isFavorite = isFav,
                            isTafsirExpanded = isTafsirExpanded,
                            onToggleFavorite = {
                                haptics.celebration()
                                onToggleFavoriteAyah(ayah)
                            },
                            onToggleTafsir = {
                                haptics.toggleTafsir()
                                expandedAyahNumbers = if (expandedAyahNumbers.contains(ayah.ayahNumberInSurah)) {
                                    expandedAyahNumbers - ayah.ayahNumberInSurah
                                } else {
                                    expandedAyahNumbers + ayah.ayahNumberInSurah
                                }
                            },
                            onOptionsClick = {
                                haptics.tap()
                                onAyahOptionsClick(ayah)
                            },
                            onPlayClick = {
                                haptics.toggleTafsir()
                                onPlaySingleAyah(ayah)
                            },
                            onBookmarkClick = {
                                haptics.tap()
                                onAddBookmark(ayah)
                            },
                            onAddNoteClick = {
                                haptics.tap()
                                onAddNoteClick(ayah)
                            }
                        )
                    }
                }
            }

            // Pinch-To-Zoom Visual HUD Indicator
            AnimatedVisibility(
                visible = showZoomHud,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            ) {
                Surface(
                    color = IslamicEmeraldDark.copy(alpha = 0.92f),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(24.dp),
                    shadowElevation = 8.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.FormatSize, contentDescription = null, tint = QuranGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "আরবি সাইজ: ${currentArabicSize.toInt()}sp • অনুবাদ: ${currentTranslationSize.toInt()}sp",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }

    // Surah Completion Celebration Dialog
    if (showCompletionDialog) {
        AlertDialog(
            onDismissRequest = { showCompletionDialog = false },
            icon = {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(IslamicEmeraldPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(38.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "🎉 মাশাআল্লাহ! সূরা সমাপ্ত",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = IslamicEmeraldPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "আলহামদুলিল্লাহ! আপনি সফলভাবে সূরা ${surah.banglaTranslation} (${surah.englishName}) তিলাওয়াত সমাপ্ত করেছেন।",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "আল্লাহ সুবহানাহু ওয়া তা'আলা আপনার এই তিলাওয়াতকে কবুল ও মঞ্জুর করুন এবং জ্ঞানের আলোয় জীবনকে ভরিয়ে দিন।",
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
                        // 1. Alhamdulillah / Ameen Primary Button
                        Button(
                            onClick = {
                                haptics.tap()
                                showCompletionDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("btn_celebration_alhamdulillah")
                        ) {
                            Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("আলহামদুলিল্লাহ (আমীন)", fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // 2. Remind Me Later Button
                            OutlinedButton(
                                onClick = {
                                    haptics.tap()
                                    showCompletionDialog = false
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .testTag("btn_celebration_remind_later")
                            ) {
                                Text("পরে মনে করান", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }

                            // 3. Don't Show Again Button
                            OutlinedButton(
                                onClick = {
                                    haptics.tap()
                                    onSetDontShowCelebration(true)
                                    showCompletionDialog = false
                                },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(40.dp)
                                    .testTag("btn_celebration_dont_show_again")
                            ) {
                                Text("আর দেখাবেন না", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    // Juz Info Dialog
    if (showJuzInfoDialog) {
        AlertDialog(
            onDismissRequest = { showJuzInfoDialog = false },
            title = {
                Text("পারা ও সূরা পরিচিতি", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("• সূরা: ${surah.number}. ${surah.englishName} (${surah.arabicName})", fontWeight = FontWeight.Bold)
                    Text("• বাংলা অর্থ: ${surah.banglaTranslation}")
                    Text("• মোট আয়াত: ${surah.totalAyahs} টি")
                    Text("• অবতীর্ণ স্থান: ${surah.revelationType}")
                    Text("• পারা নম্বর: ${surah.startJuz}")
                }
            },
            confirmButton = {
                TextButton(onClick = { showJuzInfoDialog = false }) {
                    Text("ঠিক আছে", color = IslamicEmeraldPrimary)
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AyahCardItem(
    ayah: AyahItem,
    settings: ReadingSettings,
    isPlaying: Boolean,
    isFavorite: Boolean,
    isTafsirExpanded: Boolean,
    onToggleFavorite: () -> Unit,
    onToggleTafsir: () -> Unit,
    onOptionsClick: () -> Unit,
    onPlayClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onAddNoteClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme() || settings.nightModeOption != com.example.data.model.NightModeOption.LIGHT
    val bgModifier = if (isPlaying) {
        Modifier.background(IslamicEmeraldContainer.copy(alpha = 0.45f))
    } else {
        Modifier.background(MaterialTheme.colorScheme.surface)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(bgModifier)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("ayah_item_${ayah.ayahNumberInSurah}")
    ) {
        // Top Ayah Header (Ayah number, play, favorite star, bookmark, options)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Golden Ayah Rosette / Number Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                if (isPlaying) listOf(QuranGold, QuranGoldDark)
                                else listOf(IslamicEmeraldPrimary, IslamicEmeraldDark)
                            )
                        )
                        .border(1.dp, QuranGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${ayah.ayahNumberInSurah}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                FilledTonalIconButton(
                    onClick = onPlayClick,
                    modifier = Modifier.size(32.dp),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = if (isPlaying) QuranGold else IslamicEmeraldContainer
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = if (isPlaying) Color.White else IslamicEmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Favorite Star Icon Button
                IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) QuranGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Bookmark Pin Button
                IconButton(onClick = onBookmarkClick, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            IconButton(onClick = onOptionsClick, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Dynamic Arabic Text with Tajweed Coloring
        if (settings.showArabic) {
            val rawArabic = if (settings.selectedScript == com.example.data.model.QuranScriptType.INDOPAK || settings.selectedFont.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            val baseTextColor = MaterialTheme.colorScheme.onSurface
            val tajweedText = TajweedAnnotator.buildTajweedText(
                text = rawArabic,
                baseTextColor = baseTextColor,
                isDark = isDark,
                enabled = settings.showTajweed
            )

            val fullText = buildAnnotatedString {
                append(tajweedText)
                val arabicNum = TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)
                withStyle(
                    SpanStyle(
                        color = QuranGoldDark,
                        fontWeight = FontWeight.Bold,
                        fontSize = (settings.arabicFontSizeSp * 0.85f).sp
                    )
                ) {
                    append(" ۝$arabicNum")
                }
            }

            Text(
                text = fullText,
                style = QuranTypography.getArabicTextStyle(
                    font = settings.selectedFont,
                    fontSizeSp = settings.arabicFontSizeSp,
                    fontWeight = QuranTypography.parseFontWeight(settings.arabicFontWeight),
                    letterSpacingSp = settings.arabicLetterSpacingSp,
                    lineHeightMultiplier = settings.arabicLineHeightMultiplier
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleTafsir() }
            )
        }

        // Word by Word Layout
        if (settings.showWordByWord && ayah.words.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ayah.words.forEach { word ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicEmeraldContainer.copy(alpha = 0.35f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, IslamicEmeraldPrimary.copy(alpha = 0.2f)),
                        modifier = Modifier.padding(3.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = word.arabic,
                                style = QuranTypography.getArabicTextStyle(
                                    font = settings.selectedFont,
                                    fontSizeSp = 16f
                                ),
                                color = IslamicEmeraldPrimary
                            )
                            Text(
                                text = word.bangla,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }

        // Translation Text
        if (settings.showTranslation) {
            Spacer(modifier = Modifier.height(10.dp))
            val translation = if (settings.selectedTranslation.contains("English")) {
                ayah.englishTranslation
            } else {
                ayah.banglaTranslation
            }
            Text(
                text = "${ayah.ayahNumberInSurah}. $translation",
                fontSize = settings.translationFontSizeSp.sp,
                lineHeight = (settings.translationFontSizeSp * 1.45f).sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable { onToggleTafsir() }
            )
        }

        // Expandable Tafsir Section
        Spacer(modifier = Modifier.height(12.dp))
        ExpandableAyahTafsirSection(
            ayah = ayah,
            isExpanded = isTafsirExpanded || settings.showTafsir,
            onToggleExpand = onToggleTafsir,
            onAddNoteClick = onAddNoteClick
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
}

@Composable
private fun SurahHeaderCard(
    surah: SurahItem,
    settings: ReadingSettings
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = surah.arabicName,
                style = QuranTypography.getArabicTextStyle(
                    font = settings.selectedFont,
                    fontSizeSp = 32f,
                    fontWeight = FontWeight.Bold
                ),
                color = IslamicEmeraldPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "সূরা ${surah.banglaTranslation} (${surah.englishName})",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = "${surah.revelationType} • ${surah.totalAyahs} আয়াত",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (surah.number != 9) { // At-Tawbah does not have Bismillah
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                    style = QuranTypography.getArabicTextStyle(
                        font = settings.selectedFont,
                        fontSizeSp = 22f,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun PageSeparatorStrip(pageNumber: Int, rukuNumber: Int) {
    Surface(
        color = IslamicEmeraldContainer.copy(alpha = 0.4f),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("পৃষ্ঠা $pageNumber", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            Text("রকু $rukuNumber", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = IslamicEmeraldPrimary)
        }
    }
}

@Composable
private fun ContinuousMushafParagraphCard(
    ayahs: List<AyahItem>,
    settings: ReadingSettings,
    activeAyahNumber: Int?,
    onAyahClick: (AyahItem) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            val isDark = isSystemInDarkTheme() || settings.nightModeOption != com.example.data.model.NightModeOption.LIGHT
            val baseTextColor = MaterialTheme.colorScheme.onSurface
            val fullParagraph = buildAnnotatedString {
                ayahs.forEach { ayah ->
                    val rawArabic = if (settings.selectedScript == com.example.data.model.QuranScriptType.INDOPAK || settings.selectedFont.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
                    val tajweedText = TajweedAnnotator.buildTajweedText(
                        text = rawArabic,
                        baseTextColor = baseTextColor,
                        isDark = isDark,
                        enabled = settings.showTajweed
                    )
                    val isPlaying = activeAyahNumber == ayah.ayahNumberInSurah

                    if (isPlaying) {
                        withStyle(SpanStyle(background = MaterialTheme.colorScheme.primaryContainer)) {
                            append(tajweedText)
                        }
                    } else {
                        append(tajweedText)
                    }

                    val arabicNum = TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)
                    withStyle(
                        SpanStyle(
                            color = QuranGoldDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = (settings.arabicFontSizeSp * 0.8f).sp
                        )
                    ) {
                        append(" ۝$arabicNum ")
                    }
                }
            }

            Text(
                text = fullParagraph,
                style = QuranTypography.getArabicTextStyle(
                    font = settings.selectedFont,
                    fontSizeSp = settings.arabicFontSizeSp,
                    lineHeightMultiplier = settings.arabicLineHeightMultiplier
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun BottomBarActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .testTag("reading_bottom_${label.lowercase().replace(" ", "_")}")
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}
