package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.audio.AudioPlayerState
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioManagerSheet(
    audioState: AudioPlayerState,
    onSelectReciter: (ReciterItem) -> Unit,
    onPlayPauseToggle: () -> Unit,
    onSeekTo: (Long) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onRepeatCountChange: (Int) -> Unit,
    onOpenDownloadManager: (ReciterItem) -> Unit,
    onOpenTimingGenerator: () -> Unit,
    onOpenRecitationModeMenu: () -> Unit = {},
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedCategoryTab by remember { mutableStateOf(0) } // 0: All, 1: Murattal, 2: Mujawwad, 3: Translation
    var selectedBitrate by remember { mutableStateOf("128kbps") }
    var interAyahDelaySec by remember { mutableStateOf(0) }
    var continuousPlay by remember { mutableStateOf(true) }
    var autoScrollWithAudio by remember { mutableStateOf(true) }
    var audioCacheSizeMb by remember { mutableStateOf(48.6f) }
    var showClearCacheConfirm by remember { mutableStateOf(false) }

    val allReciters: List<ReciterItem> = remember { RecitersData.recitersList }
    val filteredReciters = remember(selectedCategoryTab, allReciters) {
        when (selectedCategoryTab) {
            1 -> allReciters.filter { it.style.contains("Murattal", ignoreCase = true) || it.subStyle.contains("Murattal", ignoreCase = true) }
            2 -> allReciters.filter { it.style.contains("Mujawwad", ignoreCase = true) || it.subStyle.contains("Mujawwad", ignoreCase = true) }
            3 -> allReciters.filter { it.name.contains("translation", ignoreCase = true) || it.displayName.contains("বাংলা", ignoreCase = true) || it.displayName.contains("English", ignoreCase = true) }
            else -> allReciters
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(horizontal = 20.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(IslamicEmeraldContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Headphones,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Audio Manager (অডিও ম্যানেজার)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "ক্বারী নির্বাচন, অফলাইন ডাউনলোড ও প্লেব্যাক সেটিংস",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 32.dp)
            ) {
                // 1. Currently Active Player Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                ReciterAvatarBadge(
                                    reciter = audioState.currentReciter,
                                    size = 44.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = audioState.currentReciter.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${audioState.currentReciter.style} • ${audioState.currentReciter.bitRate} • ${if (audioState.isPlaying) "Playing (চলছে)" else "Paused"}",
                                        fontSize = 11.sp,
                                        color = if (audioState.isPlaying) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Play/Pause Action
                            FilledIconButton(
                                onClick = onPlayPauseToggle,
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = IslamicEmeraldPrimary,
                                    contentColor = Color.White
                                ),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = if (audioState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Play/Pause"
                                )
                            }
                        }

                        // Progress slider if duration > 0
                        if (audioState.durationMs > 0) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Slider(
                                value = audioState.currentPositionMs.toFloat(),
                                onValueChange = { onSeekTo(it.toLong()) },
                                valueRange = 0f..audioState.durationMs.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = IslamicEmeraldPrimary,
                                    activeTrackColor = IslamicEmeraldPrimary
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = formatAudioTime(audioState.currentPositionMs),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = formatAudioTime(audioState.durationMs),
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Playback Speed & Repeat Controls
                Text(
                    text = "প্লেব্যাক ও পুনরাবৃত্তি (Playback & Loop Settings)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Speed Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("গতি (Playback Speed):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(0.75f, 1.0f, 1.25f, 1.5f, 2.0f).forEach { speed ->
                            val isSelected = audioState.playbackSpeed == speed
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { onSpeedChange(speed) }
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${speed}x",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Repeat Count Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("আয়াত রিপিট (Repeat):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(
                            1 to "1x",
                            2 to "2x",
                            3 to "3x",
                            5 to "5x",
                            -1 to "∞"
                        ).forEach { (count, label) ->
                            val isSelected = audioState.repeatAyahTimes == count
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { onRepeatCountChange(count) }
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Recitation Mode Setting
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("তিলাওয়াত মোড (Recitation Mode):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        Text(
                            text = "${audioState.recitationMode.banglaTitle} (${audioState.recitationMode.title})",
                            fontSize = 11.sp,
                            color = IslamicEmeraldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    FilledTonalButton(
                        onClick = {
                            onDismiss()
                            onOpenRecitationModeMenu()
                        },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = IslamicEmeraldContainer)
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp), tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("মোড পরিবর্তন", fontSize = 11.sp, color = IslamicEmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Delay between Ayahs (Hifz Memorization)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("আয়াত বিরতি (Delay between Ayahs):", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf(0, 1, 2, 3, 5).forEach { sec ->
                            val isSelected = interAyahDelaySec == sec
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) QuranGold else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .clickable { interAyahDelaySec = sec }
                                    .padding(vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (sec == 0) "None" else "${sec}s",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggles for Continuous Play & Auto-Scroll
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ধারাবাহিক সূরা তিলাওয়াত (Continuous Play)", fontSize = 12.sp)
                    Switch(
                        checked = continuousPlay,
                        onCheckedChange = { continuousPlay = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicEmeraldPrimary, checkedTrackColor = IslamicEmeraldContainer)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("অডিও তিলাওয়াতের সাথে অটো স্ক্রল (Auto Scroll)", fontSize = 12.sp)
                    Switch(
                        checked = autoScrollWithAudio,
                        onCheckedChange = { autoScrollWithAudio = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicEmeraldPrimary, checkedTrackColor = IslamicEmeraldContainer)
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // 3. Audio Quality & Cache Management
                Text(
                    text = "স্টোরেজ ও অডিও কোয়ালিটি (Storage & Bitrate)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("64kbps (Data Saver)", "128kbps (Standard HQ)", "192kbps (Ultra HQ)").forEach { br ->
                        val code = br.split(" ")[0]
                        val isSelected = selectedBitrate == code
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IslamicEmeraldContainer else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedBitrate = code }
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = code,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (code == "64kbps") "সেভার" else if (code == "128kbps") "স্ট্যান্ডার্ড" else "এইচডি",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Cache Cleanup Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "অডিও ক্যাশ স্টোরেজ: ${String.format("%.1f", audioCacheSizeMb)} MB",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "দ্রুত অফলাইন প্লেব্যাকের জন্য সাময়িক মেমোরি",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        OutlinedButton(
                            onClick = { showClearCacheConfirm = true },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear Cache", fontSize = 11.sp)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 12.dp))

                // 4. Reciters Directory with Category Tabs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ক্বারী ডিরেক্টরি (Reciters Catalog)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    TextButton(onClick = onOpenTimingGenerator) {
                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(14.dp), tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync LRC", fontSize = 12.sp, color = IslamicEmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Category Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedCategoryTab,
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    contentColor = IslamicEmeraldPrimary,
                    divider = {}
                ) {
                    listOf("সকল ক্বারী", "মুরাত্তাল", "মুজাওয়াদ", "অনুবাদ অডিও").forEachIndexed { index, label ->
                        Tab(
                            selected = selectedCategoryTab == index,
                            onClick = { selectedCategoryTab = index },
                            text = { Text(label, fontSize = 12.sp, fontWeight = if (selectedCategoryTab == index) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reciters List
                filteredReciters.forEach { reciter ->
                    val isCurrent = audioState.currentReciter.id == reciter.id
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isCurrent) IslamicEmeraldContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isCurrent) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectReciter(reciter) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                ReciterAvatarBadge(
                                    reciter = reciter,
                                    size = 40.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = reciter.displayName,
                                        fontSize = 13.sp,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrent) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${reciter.style} • ${reciter.country} • ${reciter.bitRate}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                // Download Manager Button
                                IconButton(
                                    onClick = { onOpenDownloadManager(reciter) },
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudDownload,
                                        contentDescription = "Download Offline",
                                        tint = QuranGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                // Play / Select Button
                                FilledTonalIconButton(
                                    onClick = { onSelectReciter(reciter) },
                                    modifier = Modifier.size(34.dp),
                                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                                        containerColor = if (isCurrent) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                                        contentColor = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                ) {
                                    Icon(
                                        imageVector = if (isCurrent && audioState.isPlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                                        contentDescription = "Select & Play",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showClearCacheConfirm) {
        AlertDialog(
            onDismissRequest = { showClearCacheConfirm = false },
            title = { Text("অডিও ক্যাশ মুছে ফেলবেন?") },
            text = { Text("সাময়িক অডিও ফাইল মুছে ফেলা হবে। অফলাইন ডাউনলোড করা সম্পূর্ণ সূরা ফাইলগুলো অক্ষত থাকবে।") },
            confirmButton = {
                TextButton(
                    onClick = {
                        audioCacheSizeMb = 0.0f
                        showClearCacheConfirm = false
                        Toast.makeText(context, "অডিও ক্যাশ সফলভাবে মুছে ফেলা হয়েছে ✨", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("হ্যাঁ, মুছুন", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheConfirm = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

private fun formatAudioTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
