package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioPlayerState
import com.example.data.model.AyahItem
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReciterItem
import com.example.data.repository.QuranData
import com.example.ui.theme.*

/**
 * Format milliseconds into MM:SS format
 */
fun formatTimeMs(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterAudioPlayerBottomSheet(
    audioState: AudioPlayerState,
    selectedFont: QuranFontFamily = QuranFontFamily.UTHMANIC_HAFS,
    onPlayPauseToggle: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSeekForward: () -> Unit,
    onSeekBackward: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onSpeedChange: (Float) -> Unit,
    onRepeatCountChange: (Int) -> Unit,
    onOpenReciterSelector: () -> Unit,
    onOpenAudioEditor: () -> Unit,
    onAddBookmark: (AyahItem) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val surah = QuranData.surahs.find { it.number == audioState.currentSurahNumber } ?: QuranData.surahs[0]
    val currentAyah = audioState.currentAyah ?: QuranData.getAyahsForSurah(audioState.currentSurahNumber).find {
        it.ayahNumberInSurah == audioState.currentAyahNumber
    } ?: QuranData.fatihahAyahs[0]

    var isDraggingSlider by remember { mutableStateOf(false) }
    var sliderDraggingPosition by remember { mutableStateOf(0f) }

    val currentPositionMs = if (isDraggingSlider) {
        sliderDraggingPosition.toLong()
    } else {
        audioState.currentPositionMs
    }

    val totalDurationMs = audioState.durationMs.coerceAtLeast(1L)
    val progressFraction = (currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)

    var showSpeedDialog by remember { mutableStateOf(false) }
    var showRepeatDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = IslamicEmeraldPrimary.copy(alpha = 0.5f),
                width = 44.dp,
                height = 4.dp
            )
        },
        modifier = Modifier.testTag("reciter_audio_player_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sheet Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = IslamicEmeraldContainer.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${surah.englishName} (${surah.arabicName}) • আয়াত ${audioState.currentAyahNumber}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicEmeraldPrimary
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse Player",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Reciter Info Card
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        ReciterAvatarBadge(
                            reciter = audioState.currentReciter,
                            size = 52.dp,
                            showBorder = true,
                            showPlayingBadge = audioState.isPlaying
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = audioState.currentReciter.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = audioState.currentReciter.displayName,
                                fontSize = 13.sp,
                                color = QuranGoldDark,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${audioState.currentReciter.style} • ${audioState.currentReciter.country} • ${audioState.currentReciter.bitRate}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Change Reciter Button
                    OutlinedButton(
                        onClick = onOpenReciterSelector,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = IslamicEmeraldPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.5f)),
                        modifier = Modifier.testTag("player_switch_reciter_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapHoriz,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("ক্বারী", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Active Ayah Preview Card (Arabic + Translation)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = IslamicEmeraldContainer.copy(alpha = 0.35f),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuranGold.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currentAyah.textUthmani,
                        style = QuranTypography.getArabicTextStyle(
                            font = selectedFont,
                            fontSizeSp = 21f
                        ),
                        textAlign = TextAlign.Center,
                        color = IslamicEmeraldDark,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${currentAyah.ayahNumberInSurah}. ${currentAyah.banglaTranslation}",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress Slider & Timestamps
            Column(modifier = Modifier.fillMaxWidth()) {
                Slider(
                    value = progressFraction,
                    onValueChange = { fraction ->
                        isDraggingSlider = true
                        sliderDraggingPosition = fraction * totalDurationMs
                    },
                    onValueChangeFinished = {
                        isDraggingSlider = false
                        onSeekTo(sliderDraggingPosition.toLong())
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = IslamicEmeraldPrimary,
                        activeTrackColor = IslamicEmeraldPrimary,
                        inactiveTrackColor = IslamicEmeraldContainer.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("audio_progress_seek_slider")
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatTimeMs(currentPositionMs),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (audioState.isBuffering) {
                        Text(
                            text = "বাফারিং হচ্ছে...",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldDark
                        )
                    }

                    Text(
                        text = formatTimeMs(audioState.durationMs),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary Playback Controls Row (Previous, -10s, Play/Pause, +10s, Next)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Skip to Previous Ayah
                IconButton(
                    onClick = onSkipPrevious,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("player_skip_previous_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Ayah",
                        tint = if (audioState.hasPrevious) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Seek 10s Backward
                IconButton(
                    onClick = onSeekBackward,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("player_seek_backward_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Rewind 10 seconds",
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Main Play / Pause Button with Gradient & Buffering state
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    shadowElevation = 6.dp,
                    modifier = Modifier.size(68.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.linearGradient(
                                    listOf(IslamicEmeraldPrimary, IslamicEmeraldDark)
                                )
                            )
                            .border(2.dp, QuranGoldLight, CircleShape)
                            .clickable(onClick = onPlayPauseToggle)
                            .testTag("player_play_pause_fab"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (audioState.isBuffering) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 3.dp,
                                modifier = Modifier.size(32.dp)
                            )
                        } else {
                            Icon(
                                imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (audioState.isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                }

                // Seek 10s Forward
                IconButton(
                    onClick = onSeekForward,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("player_seek_forward_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward 10 seconds",
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Skip to Next Ayah
                IconButton(
                    onClick = onSkipNext,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag("player_skip_next_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Ayah",
                        tint = if (audioState.hasNext) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Secondary Controls (Speed, Repeat Ayah, Loop Range, Bookmark)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Playback Speed Chip
                AssistChip(
                    onClick = { showSpeedDialog = true },
                    label = {
                        Text(
                            text = "${audioState.playbackSpeed}x গতি",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = IslamicEmeraldPrimary
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("player_speed_chip")
                )

                // Repeat Mode Chip
                AssistChip(
                    onClick = { showRepeatDialog = true },
                    label = {
                        Text(
                            text = if (audioState.repeatAyahTimes == 0) "রিপিট: বন্ধ" else "রিপিট: ${audioState.repeatAyahTimes} বার",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (audioState.repeatAyahTimes > 0) QuranGoldDark else IslamicEmeraldPrimary
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = if (audioState.repeatAyahTimes > 0) IslamicEmeraldContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("player_repeat_chip")
                )

                // Audio Range Editor Button
                IconButton(
                    onClick = {
                        onDismiss()
                        onOpenAudioEditor()
                    },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Audio Range Editor",
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Bookmark Ayah Button
                IconButton(
                    onClick = { onAddBookmark(currentAyah) },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder,
                        contentDescription = "Bookmark Verse",
                        tint = QuranGoldDark,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }

    // Playback Speed Dialog
    if (showSpeedDialog) {
        AlertDialog(
            onDismissRequest = { showSpeedDialog = false },
            title = { Text("প্লেব্যাক গতি নির্বাচন করুন", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    val speeds = listOf(0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f)
                    speeds.forEach { speed ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSpeedChange(speed)
                                    showSpeedDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = audioState.playbackSpeed == speed,
                                onClick = {
                                    onSpeedChange(speed)
                                    showSpeedDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = IslamicEmeraldPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${speed}x ${if (speed == 1.0f) "(স্বাভাবিক)" else ""}",
                                fontSize = 15.sp,
                                fontWeight = if (audioState.playbackSpeed == speed) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSpeedDialog = false }) {
                    Text("বাতিল", color = IslamicEmeraldPrimary)
                }
            }
        )
    }

    // Repeat Ayah Dialog
    if (showRepeatDialog) {
        AlertDialog(
            onDismissRequest = { showRepeatDialog = false },
            title = { Text("আয়াত পুনরাবৃত্তি (Repeat Ayah)", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    val repeatOptions = listOf(0, 1, 2, 3, 5, 10)
                    repeatOptions.forEach { count ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onRepeatCountChange(count)
                                    showRepeatDialog = false
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = audioState.repeatAyahTimes == count,
                                onClick = {
                                    onRepeatCountChange(count)
                                    showRepeatDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = IslamicEmeraldPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (count == 0) "বন্ধ (একবার বাজবে)" else "$count বার পুনরাবৃত্তি",
                                fontSize = 15.sp,
                                fontWeight = if (audioState.repeatAyahTimes == count) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRepeatDialog = false }) {
                    Text("বাতিল", color = IslamicEmeraldPrimary)
                }
            }
        )
    }
}

/**
 * Compact Floating Mini Player Bar displayed when audio is playing or paused
 */
@Composable
fun MiniAudioPlayerBar(
    audioState: AudioPlayerState,
    onExpandPlayer: () -> Unit,
    onPlayPauseToggle: () -> Unit,
    onClosePlayer: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (audioState.currentAyah == null && !audioState.isPlaying && !audioState.isBuffering) {
        return
    }

    val surah = QuranData.surahs.find { it.number == audioState.currentSurahNumber } ?: QuranData.surahs[0]
    val totalDurationMs = audioState.durationMs.coerceAtLeast(1L)
    val progressFraction = (audioState.currentPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onExpandPlayer)
            .testTag("mini_audio_player_bar"),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Slim Progress Bar
            LinearProgressIndicator(
                progress = { progressFraction },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = IslamicEmeraldPrimary,
                trackColor = IslamicEmeraldContainer.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Reciter Avatar & Surah / Ayah info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    ReciterAvatarBadge(
                        reciter = audioState.currentReciter,
                        size = 38.dp,
                        showBorder = true,
                        showPlayingBadge = audioState.isPlaying
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "${surah.englishName} • আয়াত ${audioState.currentAyahNumber}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${audioState.currentReciter.name} (${formatTimeMs(audioState.currentPositionMs)} / ${formatTimeMs(audioState.durationMs)})",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Controls
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilledTonalIconButton(
                        onClick = onPlayPauseToggle,
                        modifier = Modifier.size(36.dp),
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = IslamicEmeraldPrimary,
                            contentColor = Color.White
                        )
                    ) {
                        if (audioState.isBuffering) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Icon(
                                imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    IconButton(
                        onClick = onClosePlayer,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Player",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
