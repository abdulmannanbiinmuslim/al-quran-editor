package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyahItem
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReciterItem
import com.example.data.repository.RecitersData
import com.example.data.timing.QuranTimingSyncService
import com.example.data.timing.TimingFileFormat
import com.example.data.timing.TimingSyncOptions
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimingFileGeneratorBottomSheet(
    surahName: String,
    surahNumber: Int,
    totalAyahsCount: Int,
    ayahs: List<AyahItem>,
    selectedFont: QuranFontFamily = QuranFontFamily.UTHMANIC_HAFS,
    initialReciter: ReciterItem = RecitersData.recitersList[0],
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var selectedFormat by remember { mutableStateOf(TimingFileFormat.LRC) }
    var startAyah by remember { mutableStateOf(1f) }
    var endAyah by remember { mutableStateOf(totalAyahsCount.coerceAtLeast(1).toFloat()) }
    var selectedReciterIndex by remember { mutableStateOf(RecitersData.recitersList.indexOfFirst { it.id == initialReciter.id }.takeIf { it >= 0 } ?: 0) }
    var showReciterDropdown by remember { mutableStateOf(false) }

    var offsetMs by remember { mutableStateOf(0L) }
    var includeArabic by remember { mutableStateOf(true) }
    var includeBangla by remember { mutableStateOf(false) }
    var includeEnglish by remember { mutableStateOf(false) }
    var includeRosette by remember { mutableStateOf(true) }
    var includeHeaders by remember { mutableStateOf(true) }

    val reciter = RecitersData.recitersList.getOrElse(selectedReciterIndex) { RecitersData.recitersList[0] }

    val selectedAyahs = remember(ayahs, startAyah, endAyah) {
        ayahs.filter { it.ayahNumberInSurah in startAyah.toInt()..endAyah.toInt() }
    }

    val options = remember(
        selectedFormat, surahNumber, surahName, reciter,
        selectedAyahs, selectedFont, includeArabic,
        includeBangla, includeEnglish, includeRosette,
        offsetMs, includeHeaders
    ) {
        TimingSyncOptions(
            format = selectedFormat,
            surahNumber = surahNumber,
            surahName = surahName,
            reciter = reciter,
            ayahs = selectedAyahs,
            font = selectedFont,
            includeArabic = includeArabic,
            includeBanglaTranslation = includeBangla,
            includeEnglishTranslation = includeEnglish,
            includeRosette = includeRosette,
            offsetMs = offsetMs,
            includeMetadataHeaders = includeHeaders
        )
    }

    val generatedContent = remember(options) {
        QuranTimingSyncService.generateTimingContent(options)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Timing Sync Service (.lrc / .srt)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicEmeraldPrimary
                        )
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }

            Text(
                text = "অডিও প্লেয়ার এবং ভিডিও প্লেয়ারে স্থানীয়ভাবে লিরিক্স ও সাবটাইটেল সিঙ্ক করার ফাইল তৈরি করুন।",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Format Switcher Tabs (.LRC, .SRT, .VTT)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TimingFileFormat.values().forEach { fmt ->
                        val isSelected = selectedFormat == fmt
                        Surface(
                            onClick = { selectedFormat = fmt },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) IslamicEmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = fmt.name,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Reciter Selection
            Card(
                onClick = { showReciterDropdown = true },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("ক্বারী (Reciter Timing)", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(reciter.displayName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                    Text("পরিবর্তন ▾", color = IslamicEmeraldPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                DropdownMenu(
                    expanded = showReciterDropdown,
                    onDismissRequest = { showReciterDropdown = false }
                ) {
                    RecitersData.recitersList.forEachIndexed { index, r ->
                        DropdownMenuItem(
                            text = { Text(r.displayName) },
                            onClick = {
                                selectedReciterIndex = index
                                showReciterDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Ayah Range Selector
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("আয়াত সীমা", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text(
                            "$surahName $surahNumber:${startAyah.toInt()} - $endAyah.toInt() (${selectedAyahs.size} আয়াত)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicEmeraldPrimary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("শুরু: ${startAyah.toInt()}", fontSize = 11.sp, modifier = Modifier.width(55.dp))
                        Slider(
                            value = startAyah,
                            onValueChange = { startAyah = it.coerceAtMost(endAyah) },
                            valueRange = 1f..totalAyahsCount.toFloat(),
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(thumbColor = IslamicEmeraldPrimary, activeTrackColor = IslamicEmeraldPrimary)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("শেষ: ${endAyah.toInt()}", fontSize = 11.sp, modifier = Modifier.width(55.dp))
                        Slider(
                            value = endAyah,
                            onValueChange = { endAyah = it.coerceAtLeast(startAyah) },
                            valueRange = 1f..totalAyahsCount.toFloat(),
                            modifier = Modifier.weight(1f),
                            colors = SliderDefaults.colors(thumbColor = IslamicEmeraldPrimary, activeTrackColor = IslamicEmeraldPrimary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. Timing Calibration / Offset Adjuster (+/- ms)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("টাইমিং অফসেট ক্যালিব্রেশন", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("${if (offsetMs >= 0) "+$offsetMs" else "$offsetMs"} ms", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IslamicEmeraldPrimary)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf(-500L, -100L, 0L, 100L, 500L).forEach { delta ->
                            FilterChip(
                                selected = (delta == 0L && offsetMs == 0L) || (delta != 0L && offsetMs == delta),
                                onClick = {
                                    offsetMs = if (delta == 0L) 0L else (offsetMs + delta)
                                },
                                label = { Text(if (delta == 0L) "Reset" else "${if (delta > 0) "+" else ""}${delta}ms", fontSize = 10.sp) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Content Toggles (Arabic, Bangla, English, Rosette)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = includeArabic,
                    onClick = { includeArabic = !includeArabic },
                    label = { Text("আরবি টেক্সট") },
                    leadingIcon = { if (includeArabic) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                )
                FilterChip(
                    selected = includeBangla,
                    onClick = { includeBangla = !includeBangla },
                    label = { Text("বাংলা অনুবাদ") },
                    leadingIcon = { if (includeBangla) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                )
                FilterChip(
                    selected = includeEnglish,
                    onClick = { includeEnglish = !includeEnglish },
                    label = { Text("English") },
                    leadingIcon = { if (includeEnglish) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                )
                FilterChip(
                    selected = includeRosette,
                    onClick = { includeRosette = !includeRosette },
                    label = { Text("রোজेट ۝") }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Live Syntax Preview Box
            Text("ফাইল প্রিভিউ (${selectedFormat.name})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp, max = 220.dp)
            ) {
                Text(
                    text = generatedContent,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    modifier = Modifier
                        .padding(12.dp)
                        .verticalScroll(rememberScrollState())
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7. Action Buttons (Save File, Share File, Copy)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Save to Storage Button
                Button(
                    onClick = {
                        val sPad = surahNumber.toString().padStart(3, '0')
                        val filename = "Surah_${sPad}_${surahName}_${reciter.id}"
                        val res = QuranTimingSyncService.saveTimingFileLocally(
                            context = context,
                            filenameWithoutExt = filename,
                            format = selectedFormat,
                            content = generatedContent
                        )
                        if (res.isSuccess) {
                            val savedFile = res.getOrNull()
                            Toast.makeText(context, "সংরক্ষিত হয়েছে: ${savedFile?.name}\n(Downloads/QuranTimings/)", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "সেভ করতে ত্রুটি হয়েছে: ${res.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1.2f).testTag("save_timing_file_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
                ) {
                    Icon(Icons.Outlined.SaveAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("সেভ করুন (${selectedFormat.name})", fontSize = 12.sp)
                }

                // Share Button
                FilledTonalButton(
                    onClick = {
                        val sPad = surahNumber.toString().padStart(3, '0')
                        val filename = "Surah_${sPad}_${surahName}_${reciter.id}"
                        val res = QuranTimingSyncService.saveTimingFileLocally(
                            context = context,
                            filenameWithoutExt = filename,
                            format = selectedFormat,
                            content = generatedContent
                        )
                        if (res.isSuccess) {
                            val file = res.getOrThrow()
                            QuranTimingSyncService.shareTimingFile(
                                context = context,
                                file = file,
                                format = selectedFormat,
                                title = "Surah $surahName (${selectedFormat.name})"
                            )
                        } else {
                            Toast.makeText(context, "শেয়ার করতে সমস্যা হয়েছে", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.weight(1f).testTag("share_timing_file_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("শেয়ার", fontSize = 12.sp)
                }

                // Copy Button
                OutlinedIconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Quran Timing ${selectedFormat.name}", generatedContent)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "${selectedFormat.name} টেক্সট কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy Text", tint = IslamicEmeraldPrimary)
                }
            }
        }
    }
}
