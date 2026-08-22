package com.example.ui.screens.reading

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioPlayerState
import com.example.data.model.AyahItem
import com.example.data.model.ReadingLayoutMode
import com.example.data.model.ReadingSettings
import com.example.data.model.SurahItem
import com.example.data.timing.TimingGenerator
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QuranReadingScreen(
    surah: SurahItem,
    ayahs: List<AyahItem>,
    settings: ReadingSettings,
    audioState: AudioPlayerState,
    isAutoScrollActive: Boolean,
    autoScrollSpeed: Int,
    onBack: () -> Unit,
    onTitleClick: () -> Unit,
    onToggleLayoutMode: () -> Unit,
    onOpenQuickSettings: () -> Unit,
    onOpenContents: () -> Unit,
    onToggleAutoScroll: () -> Unit,
    onAutoScrollSpeedChange: (Int) -> Unit,
    onOpenAudioEditor: () -> Unit,
    onOpenPlayerBottomSheet: () -> Unit = {},
    onOpenPlanner: () -> Unit,
    onAyahOptionsClick: (AyahItem) -> Unit,
    onPlaySingleAyah: (AyahItem) -> Unit,
    onAddBookmark: (AyahItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Smooth auto scroll effect
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
                listState.animateScrollToItem(idx + 1)
            }
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = IslamicEmeraldPrimary,
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Back Arrow
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        // Center Surah Title (Clickable -> Jump to Ayah)
                        Surface(
                            onClick = onTitleClick,
                            color = Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("reading_title_clickable")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${surah.number}. ${surah.englishName} ▾",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        // Right action buttons: Layout Mode Switch & Quick Settings
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Layout mode toggle (Lyrics vs Mushaf Page)
                            IconButton(
                                onClick = onToggleLayoutMode,
                                modifier = Modifier.testTag("toggle_reading_mode_button")
                            ) {
                                Icon(
                                    imageVector = if (settings.layoutMode == ReadingLayoutMode.LYRICS_AYAH_BY_AYAH)
                                        Icons.Default.MenuBook
                                    else
                                        Icons.Default.FormatAlignLeft,
                                    contentDescription = "Toggle Reading Mode",
                                    tint = Color.White
                                )
                            }

                            // Quick Settings Drawer Button
                            IconButton(
                                onClick = onOpenQuickSettings,
                                modifier = Modifier.testTag("reading_quick_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Quick Settings",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Column {
                // Auto Scroll Speed Bar (Floating if active)
                AnimatedVisibility(visible = isAutoScrollActive) {
                    Surface(
                        color = IslamicEmeraldDark,
                        contentColor = Color.White,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { onAutoScrollSpeedChange(autoScrollSpeed - 1) }) {
                                    Icon(Icons.Default.RemoveCircleOutline, contentDescription = "Slower", tint = Color.White)
                                }
                                Text(
                                    text = "Auto Scroll Speed: ${autoScrollSpeed}x",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranGold
                                )
                                IconButton(onClick = { onAutoScrollSpeedChange(autoScrollSpeed + 1) }) {
                                    Icon(Icons.Default.AddCircleOutline, contentDescription = "Faster", tint = Color.White)
                                }
                            }

                            IconButton(onClick = onToggleAutoScroll) {
                                Icon(Icons.Default.Close, contentDescription = "Stop", tint = Color.White)
                            }
                        }
                    }
                }

                // Bottom Navigation Bar (Contents, Auto Scroll, Play Audio, Planner)
                Surface(
                    color = IslamicEmeraldPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .height(60.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavItem(
                            icon = Icons.Outlined.Layers,
                            label = "Contents",
                            onClick = onOpenContents
                        )
                        BottomNavItem(
                            icon = if (isAutoScrollActive) Icons.Default.PauseCircle else Icons.Outlined.SwapVert,
                            label = "Auto Scroll",
                            onClick = onToggleAutoScroll
                        )
                        BottomNavItem(
                            icon = if (audioState.isPlaying) Icons.Default.PauseCircle else Icons.Outlined.PlayCircleOutline,
                            label = if (audioState.isPlaying) "Playing (${audioState.currentAyahNumber})" else "Audio Player",
                            onClick = onOpenPlayerBottomSheet
                        )
                        BottomNavItem(
                            icon = Icons.Outlined.EventNote,
                            label = "Planner",
                            onClick = onOpenPlanner
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Header Info Bar (Juz, Hizb, Page, Ruku info)
            item {
                ReadingHeaderInfoCard(surah = surah)
            }

            // Bismillah Banner (for surahs other than At-Tawbah 9)
            if (surah.number != 9) {
                item {
                    BismillahBanner(fontFamily = settings.selectedFont)
                }
            }

            // Ayah List
            if (settings.layoutMode == ReadingLayoutMode.LYRICS_AYAH_BY_AYAH) {
                items(ayahs) { ayah ->
                    val isCurrentlyPlaying = audioState.isPlaying &&
                            audioState.currentSurahNumber == surah.number &&
                            audioState.currentAyahNumber == ayah.ayahNumberInSurah

                    AyahCardItem(
                        ayah = ayah,
                        settings = settings,
                        isPlaying = isCurrentlyPlaying,
                        onOptionsClick = { onAyahOptionsClick(ayah) },
                        onPlayClick = { onPlaySingleAyah(ayah) },
                        onBookmarkClick = { onAddBookmark(ayah) }
                    )
                }
            } else {
                // Continuous Mushaf Mode
                item {
                    MushafPageView(
                        ayahs = ayahs,
                        settings = settings,
                        activeAyahNumber = if (audioState.isPlaying) audioState.currentAyahNumber else null,
                        onAyahClick = { onAyahOptionsClick(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomNavItem(
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
        Icon(imageVector = icon, contentDescription = label, tint = Color.White, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = label, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ReadingHeaderInfoCard(surah: SurahItem) {
    Surface(
        color = IslamicEmeraldContainer.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Juz ${surah.startJuz} • Hizb ${surah.startHizb} • Page ${surah.startPage} • Ruku ${surah.startRuku}",
                fontSize = 12.sp,
                color = IslamicEmeraldPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${surah.englishName} (${surah.totalAyahs} Ayahs, ${surah.revelationType})",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun BismillahBanner(fontFamily: com.example.data.model.QuranFontFamily) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = IslamicEmeraldContainer.copy(alpha = 0.35f),
            border = androidx.compose.foundation.BorderStroke(1.dp, QuranGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                style = com.example.ui.theme.QuranTypography.getArabicTextStyle(
                    font = fontFamily,
                    fontSizeSp = 24f
                ),
                color = IslamicEmeraldPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AyahCardItem(
    ayah: AyahItem,
    settings: ReadingSettings,
    isPlaying: Boolean,
    onOptionsClick: () -> Unit,
    onPlayClick: () -> Unit,
    onBookmarkClick: () -> Unit
) {
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
        // Top Ayah action bar (Ayah number rosette, play, bookmark, options)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Golden Ayah Rosette / Badge
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

        // Dynamic Arabic Text with Selected Typography
        if (settings.showArabic) {
            val arabicText = if (settings.selectedFont.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani
            Text(
                text = "$arabicText ۝${TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)}",
                style = com.example.ui.theme.QuranTypography.getArabicTextStyle(
                    font = settings.selectedFont,
                    fontSizeSp = settings.arabicFontSizeSp
                ),
                color = if (isPlaying) IslamicEmeraldDark else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Right,
                modifier = Modifier.fillMaxWidth()
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
                                style = com.example.ui.theme.QuranTypography.getArabicTextStyle(
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
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Tafsir Text
        if (settings.showTafsir && ayah.banglaTafsir.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "তাফসীর (${settings.selectedTafsir})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = IslamicEmeraldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ayah.banglaTafsir,
                        fontSize = settings.tafsirFontSizeSp.sp,
                        lineHeight = (settings.tafsirFontSizeSp * 1.45f).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
}

@Composable
private fun MushafPageView(
    ayahs: List<AyahItem>,
    settings: ReadingSettings,
    activeAyahNumber: Int?,
    onAyahClick: (AyahItem) -> Unit
) {
    // PDF / Classical Mushaf Page Layout with Ornate Islamic Border
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(2.dp, QuranGold.copy(alpha = 0.75f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Mushaf Page Top Frame Header
            Surface(
                color = IslamicEmeraldContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuranGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val firstAyah = ayahs.firstOrNull()
                    Text(
                        text = "পারা ${firstAyah?.juzNumber ?: 1}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                    Text(
                        text = "পৃষ্ঠা ${firstAyah?.pageNumber ?: 1}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuranGoldDark
                    )
                    Text(
                        text = "হিজব ${firstAyah?.hizbNumber ?: 1}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Continuous Justified Mushaf Ayahs
            ayahs.forEach { ayah ->
                val isActive = activeAyahNumber == ayah.ayahNumberInSurah
                val arabicText = if (settings.selectedFont.name.startsWith("INDOPAK")) ayah.textIndopak else ayah.textUthmani

                Surface(
                    color = if (isActive) IslamicEmeraldContainer.copy(alpha = 0.5f) else Color.Transparent,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAyahClick(ayah) }
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "$arabicText ۝${TimingGenerator.toArabicNumber(ayah.ayahNumberInSurah)}",
                        style = com.example.ui.theme.QuranTypography.getArabicTextStyle(
                            font = settings.selectedFont,
                            fontSizeSp = settings.arabicFontSizeSp
                        ),
                        color = if (isActive) IslamicEmeraldDark else MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Justify,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mushaf Bottom Footer
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "— ۝ —",
                    color = QuranGold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
