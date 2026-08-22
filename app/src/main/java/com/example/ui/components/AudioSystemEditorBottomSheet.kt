package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioSystemEditorBottomSheet(
    surahName: String,
    surahNumber: Int,
    totalAyahsCount: Int,
    currentReciter: ReciterItem,
    onOpenReciterSelector: () -> Unit,
    onPlay: (startAyah: Int, endAyah: Int, reciter: ReciterItem, repeatAyah: Int) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var startAyah by remember { mutableStateOf(1) }
    var endAyah by remember { mutableStateOf(totalAyahsCount.coerceAtLeast(1)) }

    var repeatSelection by remember { mutableStateOf("None") }
    var repeatAyah by remember { mutableStateOf("None") }

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

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
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Audio System Editor",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = IslamicEmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 1. Ayah Start Range
            Card(
                onClick = { showStartPicker = !showStartPicker },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.SkipPrevious, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Start Range", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    Text(
                        "$surahName $surahNumber:$startAyah ▾",
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }
            }

            if (showStartPicker) {
                Slider(
                    value = startAyah.toFloat(),
                    onValueChange = { startAyah = it.toInt().coerceAtMost(endAyah) },
                    valueRange = 1f..totalAyahsCount.toFloat(),
                    steps = (totalAyahsCount - 2).coerceAtLeast(0),
                    colors = SliderDefaults.colors(
                        thumbColor = IslamicEmeraldPrimary,
                        activeTrackColor = IslamicEmeraldPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Ayah End Range
            Card(
                onClick = { showEndPicker = !showEndPicker },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.SkipNext, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("End Range", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    Text(
                        "$surahName $surahNumber:$endAyah ▾",
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }
            }

            if (showEndPicker) {
                Slider(
                    value = endAyah.toFloat(),
                    onValueChange = { endAyah = it.toInt().coerceAtLeast(startAyah) },
                    valueRange = 1f..totalAyahsCount.toFloat(),
                    steps = (totalAyahsCount - 2).coerceAtLeast(0),
                    colors = SliderDefaults.colors(
                        thumbColor = IslamicEmeraldPrimary,
                        activeTrackColor = IslamicEmeraldPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Reciter Selector
            Card(
                onClick = onOpenReciterSelector,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.RecordVoiceOver, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Reciter", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    Text(
                        "${currentReciter.displayName} >",
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Repeat Selection Dropdown
            var repeatSelectionExpanded by remember { mutableStateOf(false) }
            Card(
                onClick = { repeatSelectionExpanded = true },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Repeat, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Repeat Selection", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    Text("$repeatSelection ▾", color = Color.Gray)
                }
                DropdownMenu(
                    expanded = repeatSelectionExpanded,
                    onDismissRequest = { repeatSelectionExpanded = false }
                ) {
                    listOf("None", "1 time", "2 times", "3 times", "Continuous Loop").forEach { r ->
                        DropdownMenuItem(
                            text = { Text(r) },
                            onClick = {
                                repeatSelection = r
                                repeatSelectionExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Repeat Ayah Dropdown
            var repeatAyahExpanded by remember { mutableStateOf(false) }
            Card(
                onClick = { repeatAyahExpanded = true },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.RepeatOne, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Repeat Ayah", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    }
                    Text("$repeatAyah ▾", color = Color.Gray)
                }
                DropdownMenu(
                    expanded = repeatAyahExpanded,
                    onDismissRequest = { repeatAyahExpanded = false }
                ) {
                    listOf("None", "1 time", "2 times", "3 times", "5 times").forEach { r ->
                        DropdownMenuItem(
                            text = { Text(r) },
                            onClick = {
                                repeatAyah = r
                                repeatAyahExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Bottom Buttons: Cancel & Play
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        val repAyahInt = when (repeatAyah) {
                            "1 time" -> 1
                            "2 times" -> 2
                            "3 times" -> 3
                            "5 times" -> 5
                            else -> 0
                        }
                        onPlay(startAyah, endAyah, currentReciter, repAyahInt)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("audio_editor_play_button")
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Play")
                }
            }
        }
    }
}
