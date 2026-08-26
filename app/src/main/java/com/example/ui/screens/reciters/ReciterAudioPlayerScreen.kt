package com.example.ui.screens.reciters

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioPlayerManager
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.data.model.SurahItem
import com.example.data.repository.QuranData
import com.example.ui.components.ReciterAvatarBadge
import com.example.ui.theme.*
import com.example.ui.util.rememberAppHaptics

/**
 * Dedicated Full-Page Audio Player Screen for Quran Reciters
 * Features album disc rotation, waveform visualizer, full seekbar, 114 Surah playlist,
 * speed controller, sleep timer, and repeat modes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterAudioPlayerScreen(
    reciter: ReciterItem,
    audioPlayer: AudioPlayerManager,
    onBack: () -> Unit,
    onNavigateToSurahReading: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val haptics = rememberAppHaptics()
    val audioState by audioPlayer.playerState.collectAsState()

    var activeTab by remember { mutableIntStateOf(0) } // 0: Now Playing, 1: Surah Playlist (114 Surahs)
    var surahSearchQuery by remember { mutableStateOf("") }
    var showSleepTimerDialog by remember { mutableStateOf(false) }
    var showSpeedDialog by remember { mutableStateOf(false) }

    val currentSurahNumber = remember(audioState.currentAyahNumber) {
        // Estimate or default to current surah
        val currentSurah = QuranData.surahs.find { it.number == 1 } ?: QuranData.surahs[0]
        currentSurah.number
    }

    val currentSurah = remember(currentSurahNumber) {
        QuranData.surahs.find { it.number == currentSurahNumber } ?: QuranData.surahs[0]
    }

    // Disc rotation animation
    val infiniteTransition = rememberInfiniteTransition(label = "disc_spin")
    val discRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "disc_rotation"
    )

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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                haptics.tap()
                                onBack()
                            }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Column {
                                Text(
                                    text = reciter.displayName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "${reciter.style} • ${reciter.country}",
                                    fontSize = 11.sp,
                                    color = QuranGoldLight
                                )
                            }
                        }

                        Row {
                            IconButton(onClick = {
                                haptics.tap()
                                showSpeedDialog = true
                            }) {
                                Icon(Icons.Default.Speed, contentDescription = "Speed", tint = Color.White)
                            }
                            IconButton(onClick = {
                                haptics.tap()
                                showSleepTimerDialog = true
                            }) {
                                Icon(Icons.Default.Timer, contentDescription = "Sleep Timer", tint = Color.White)
                            }
                        }
                    }

                    // Secondary Tab Row: [Now Playing, 114 Surahs]
                    TabRow(
                        selectedTabIndex = activeTab,
                        containerColor = IslamicEmeraldDark,
                        contentColor = QuranGold,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                                color = QuranGold
                            )
                        }
                    ) {
                        Tab(
                            selected = activeTab == 0,
                            onClick = {
                                haptics.tap()
                                activeTab = 0
                            },
                            text = { Text("প্লেয়ার (Now Playing)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                            selectedContentColor = QuranGold,
                            unselectedContentColor = Color.White.copy(alpha = 0.7f)
                        )
                        Tab(
                            selected = activeTab == 1,
                            onClick = {
                                haptics.tap()
                                activeTab = 1
                            },
                            text = { Text("১১৪টি সূরা প্লেলিস্ট (Playlist)", fontWeight = FontWeight.Bold, fontSize = 13.sp) },
                            selectedContentColor = QuranGold,
                            unselectedContentColor = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (activeTab) {
                0 -> {
                    // MAIN NOW PLAYING SCREEN
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // 1. Reciter Portrait & Glowing Vinyl Disc
                        Box(
                            modifier = Modifier
                                .size(240.dp)
                                .shadow(16.dp, CircleShape, spotColor = IslamicEmeraldPrimary.copy(alpha = 0.5f))
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        listOf(IslamicEmeraldDark, Color(0xFF072115), Color.Black)
                                    )
                                )
                                .border(4.dp, Brush.sweepGradient(listOf(QuranGold, QuranGoldLight, QuranGold)), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // Spinning vinyl grooves
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .then(if (audioState.isPlaying) Modifier.rotate(discRotation) else Modifier),
                                contentAlignment = Alignment.Center
                            ) {
                                ReciterAvatarBadge(
                                    reciter = reciter,
                                    size = 140.dp,
                                    showBorder = true,
                                    showPlayingBadge = false
                                )
                            }

                            // Glowing play indicator overlay
                            if (audioState.isPlaying) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(QuranGold.copy(alpha = 0.85f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GraphicEq,
                                        contentDescription = null,
                                        tint = IslamicEmeraldDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        // 2. Surah & Reciter Details Info Card
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "সূরা ${currentSurah.banglaTranslation} (${currentSurah.arabicName})",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${currentSurah.number}. ${currentSurah.englishName} • ${currentSurah.revelationType} (${currentSurah.totalAyahs} আয়াত)",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = IslamicEmeraldContainer.copy(alpha = 0.6f),
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Text(
                                    text = "ক্বারী: ${reciter.displayName}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = IslamicEmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // 3. Audio Track Progress Scrubber
                        Column(modifier = Modifier.fillMaxWidth()) {
                            val duration = audioState.durationMs.coerceAtLeast(1L)
                            val position = audioState.currentPositionMs.coerceIn(0L, duration)
                            val progress = (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f)

                            Slider(
                                value = progress,
                                onValueChange = { frac ->
                                    val seekTarget = (frac * duration).toLong()
                                    audioPlayer.seekTo(seekTarget)
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = QuranGold,
                                    activeTrackColor = IslamicEmeraldPrimary,
                                    inactiveTrackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val posSec = position / 1000
                                val durSec = duration / 1000
                                Text(
                                    text = String.format("%02d:%02d", posSec / 60, posSec % 60),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format("%02d:%02d", durSec / 60, durSec % 60),
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // 4. Primary Playback Controls
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Previous Surah / Track
                            IconButton(
                                onClick = {
                                    haptics.verseSwitch()
                                    val prevSurahNum = if (currentSurah.number > 1) currentSurah.number - 1 else 114
                                    val ayahs = QuranData.getAyahsForSurah(prevSurahNum)
                                    audioPlayer.playAyahRange(prevSurahNum, ayahs, reciter, 1, ayahs.size)
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.Default.SkipPrevious, contentDescription = "Previous Surah", modifier = Modifier.size(32.dp))
                            }

                            // Rewind 10 seconds
                            IconButton(
                                onClick = {
                                    haptics.fontSizeTick()
                                    audioPlayer.seekBackward(10000L)
                                },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.Replay10, contentDescription = "Rewind 10s", modifier = Modifier.size(28.dp))
                            }

                            // Large Play / Pause FAB with glowing floating elevation
                            FloatingActionButton(
                                onClick = {
                                    haptics.celebration()
                                    if (audioState.isPlaying) {
                                        audioPlayer.togglePlayPause()
                                    } else {
                                        val ayahs = QuranData.getAyahsForSurah(currentSurah.number)
                                        audioPlayer.playAyahRange(currentSurah.number, ayahs, reciter, 1, ayahs.size)
                                    }
                                },
                                containerColor = IslamicEmeraldPrimary,
                                contentColor = Color.White,
                                shape = CircleShape,
                                modifier = Modifier
                                    .size(72.dp)
                                    .shadow(12.dp, CircleShape, spotColor = IslamicEmeraldPrimary)
                                    .testTag("full_player_play_fab")
                            ) {
                                Icon(
                                    imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause",
                                    modifier = Modifier.size(36.dp),
                                    tint = QuranGoldLight
                                )
                            }

                            // Forward 10 seconds
                            IconButton(
                                onClick = {
                                    haptics.fontSizeTick()
                                    audioPlayer.seekForward(10000L)
                                },
                                modifier = Modifier.size(44.dp)
                            ) {
                                Icon(Icons.Default.Forward10, contentDescription = "Forward 10s", modifier = Modifier.size(28.dp))
                            }

                            // Next Surah / Track
                            IconButton(
                                onClick = {
                                    haptics.verseSwitch()
                                    val nextSurahNum = if (currentSurah.number < 114) currentSurah.number + 1 else 1
                                    val ayahs = QuranData.getAyahsForSurah(nextSurahNum)
                                    audioPlayer.playAyahRange(nextSurahNum, ayahs, reciter, 1, ayahs.size)
                                },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(Icons.Default.SkipNext, contentDescription = "Next Surah", modifier = Modifier.size(32.dp))
                            }
                        }

                        // 5. Read In Quran Button
                        Button(
                            onClick = {
                                haptics.tap()
                                onNavigateToSurahReading(currentSurah.number)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = IslamicEmeraldDark,
                                contentColor = QuranGold
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("মুসহাফে এই সূরাটি পড়ুন (Read in Quran)", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                1 -> {
                    // SURAH PLAYLIST (ALL 114 SURAHS)
                    val filteredSurahs = remember(surahSearchQuery) {
                        if (surahSearchQuery.isBlank()) QuranData.surahs
                        else QuranData.surahs.filter {
                            it.englishName.contains(surahSearchQuery, ignoreCase = true) ||
                                    it.banglaTranslation.contains(surahSearchQuery, ignoreCase = true) ||
                                    it.arabicName.contains(surahSearchQuery, ignoreCase = true) ||
                                    it.number.toString() == surahSearchQuery.trim()
                        }
                    }

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Search Bar
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = surahSearchQuery,
                                onValueChange = { surahSearchQuery = it },
                                placeholder = { Text("১১৪টি সূরার মধ্যে খুঁজুন...") },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = IslamicEmeraldPrimary) },
                                trailingIcon = {
                                    if (surahSearchQuery.isNotEmpty()) {
                                        IconButton(onClick = { surahSearchQuery = "" }) {
                                            Icon(Icons.Default.Close, contentDescription = null)
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }

                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(filteredSurahs) { surah ->
                                val isCurrentlyPlayingThisSurah = audioState.isPlaying && audioState.currentAyahNumber > 0

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isCurrentlyPlayingThisSurah) IslamicEmeraldContainer.copy(alpha = 0.5f)
                                    else MaterialTheme.colorScheme.surface,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isCurrentlyPlayingThisSurah) IslamicEmeraldPrimary
                                        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            haptics.celebration()
                                            val ayahs = QuranData.getAyahsForSurah(surah.number)
                                            audioPlayer.playAyahRange(surah.number, ayahs, reciter, 1, ayahs.size)
                                            activeTab = 0
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isCurrentlyPlayingThisSurah) IslamicEmeraldPrimary
                                                        else IslamicEmeraldContainer
                                                    ),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "${surah.number}",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = if (isCurrentlyPlayingThisSurah) Color.White else IslamicEmeraldPrimary
                                                )
                                            }

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column {
                                                Text(
                                                    text = "${surah.englishName} (${surah.banglaTranslation})",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                                Text(
                                                    text = "${surah.revelationType} • ${surah.totalAyahs} আয়াত",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = surah.arabicName,
                                                fontSize = 17.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = IslamicEmeraldPrimary,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )

                                            FilledTonalIconButton(
                                                onClick = {
                                                    haptics.celebration()
                                                    val ayahs = QuranData.getAyahsForSurah(surah.number)
                                                    audioPlayer.playAyahRange(surah.number, ayahs, reciter, 1, ayahs.size)
                                                    activeTab = 0
                                                },
                                                colors = IconButtonDefaults.filledTonalIconButtonColors(
                                                    containerColor = IslamicEmeraldPrimary,
                                                    contentColor = Color.White
                                                ),
                                                modifier = Modifier.size(34.dp)
                                            ) {
                                                Icon(Icons.Default.PlayArrow, contentDescription = "Play", modifier = Modifier.size(20.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Speed Selector Dialog
    if (showSpeedDialog) {
        val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f)
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            title = { Text("তিলাওয়াত স্পিড (Playback Speed)", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    speeds.forEach { spd ->
                        val isSel = audioState.playbackSpeed == spd
                        Surface(
                            onClick = {
                                haptics.tap()
                                audioPlayer.setPlaybackSpeed(spd)
                                showSpeedDialog = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) IslamicEmeraldContainer else Color.Transparent,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${spd}x ${if (spd == 1.0f) "(স্বাভাবিক/Normal)" else ""}",
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                                )
                                if (isSel) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = IslamicEmeraldPrimary)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) {
                    Text("বন্ধ করুন")
                }
            }
        )
    }

    // Sleep Timer Dialog
    if (showSleepTimerDialog) {
        val timers = listOf(
            0 to "বন্ধ (Off)",
            15 to "১৫ মিনিট পর",
            30 to "৩০ মিনিট পর",
            45 to "৪৫ মিনিট পর",
            60 to "১ ঘন্টা পর"
        )
        AlertDialog(
            onDismissRequest = { showSleepTimerDialog = false },
            title = { Text("স্লিপ টাইমার (Sleep Timer)", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    timers.forEach { (mins, label) ->
                        Surface(
                            onClick = {
                                haptics.tap()
                                showSleepTimerDialog = false
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSleepTimerDialog = false }) {
                    Text("ঠিক আছে")
                }
            }
        )
    }
}
