package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyahItem
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReciterItem
import com.example.data.repository.RecitersData
import com.example.data.timing.TimingGenerator
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleAyahShareBottomSheet(
    surahName: String,
    totalAyahsCount: Int,
    ayahs: List<AyahItem>,
    selectedFont: QuranFontFamily,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isArabicChecked by remember { mutableStateOf(true) }
    var isShareWithLyricsChecked by remember { mutableStateOf(false) }
    var isShareWithSrtChecked by remember { mutableStateOf(false) }
    var isEnglishChecked by remember { mutableStateOf(false) }
    var isBanglaChecked by remember { mutableStateOf(false) }
    var isTafsirChecked by remember { mutableStateOf(false) }

    var startAyah by remember { mutableStateOf(1f) }
    var endAyah by remember { mutableStateOf(totalAyahsCount.coerceAtLeast(1).coerceAtMost(7).toFloat()) }

    val reciter: ReciterItem = RecitersData.recitersList[2] // Abdul Basit Mujawwad sample default

    // Filter ayahs within selected slider range
    val selectedAyahs = ayahs.filter {
        it.ayahNumberInSurah in startAyah.toInt()..endAyah.toInt()
    }

    // Generated text computation based on active checkboxes
    val generatedText = remember(
        isArabicChecked, isShareWithLyricsChecked, isShareWithSrtChecked,
        isEnglishChecked, isBanglaChecked, isTafsirChecked,
        startAyah, endAyah, selectedFont
    ) {
        when {
            isShareWithLyricsChecked && isBanglaChecked -> {
                TimingGenerator.generateArabicWithTranslationLrc(surahName, reciter, selectedAyahs, selectedFont, "Bangla")
            }
            isShareWithLyricsChecked && isEnglishChecked -> {
                TimingGenerator.generateArabicWithTranslationLrc(surahName, reciter, selectedAyahs, selectedFont, "English")
            }
            isShareWithLyricsChecked -> {
                TimingGenerator.generateArabicLrc(surahName, reciter, selectedAyahs, selectedFont)
            }
            isShareWithSrtChecked && isBanglaChecked -> {
                TimingGenerator.generateArabicWithTranslationSrt(selectedAyahs, selectedFont, "Bangla")
            }
            isShareWithSrtChecked && isEnglishChecked -> {
                TimingGenerator.generateArabicWithTranslationSrt(selectedAyahs, selectedFont, "English")
            }
            isShareWithSrtChecked -> {
                TimingGenerator.generateArabicSrt(selectedAyahs, selectedFont)
            }
            isBanglaChecked || isEnglishChecked -> {
                val title = if (isBanglaChecked) "Bangla - Islamic Foundation" else "English - Sahih International"
                TimingGenerator.generateArabicWithTranslationText(surahName, selectedAyahs, selectedFont, title)
            }
            else -> {
                TimingGenerator.generateOnlyArabic(selectedAyahs, selectedFont)
            }
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
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Multiple Ayahs",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Checkbox List (Exact Match to Pages 28, 30, 31, 32, 33)
            ShareCheckbox(
                label = "Arabic",
                checked = isArabicChecked,
                onCheckedChange = { isArabicChecked = it }
            )

            ShareCheckbox(
                label = "Share with Lyrics (LRC Format)",
                checked = isShareWithLyricsChecked,
                onCheckedChange = {
                    isShareWithLyricsChecked = it
                    if (it) isShareWithSrtChecked = false
                }
            )

            ShareCheckbox(
                label = "Share with SRT (Subtitles Format)",
                checked = isShareWithSrtChecked,
                onCheckedChange = {
                    isShareWithSrtChecked = it
                    if (it) isShareWithLyricsChecked = false
                }
            )

            ShareCheckbox(
                label = "English - Mohsin Khan / Sahih",
                checked = isEnglishChecked,
                onCheckedChange = {
                    isEnglishChecked = it
                    if (it) isBanglaChecked = false
                }
            )

            ShareCheckbox(
                label = "Bangla - Bayaan Foundation",
                checked = isBanglaChecked,
                onCheckedChange = {
                    isBanglaChecked = it
                    if (it) isEnglishChecked = false
                }
            )

            ShareCheckbox(
                label = "Bangla - Tafsir Ibn Kathir",
                checked = isTafsirChecked,
                onCheckedChange = { isTafsirChecked = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ayah Range Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Start: Ayah ${startAyah.toInt()}",
                    fontWeight = FontWeight.Bold,
                    color = IslamicEmeraldPrimary,
                    fontSize = 13.sp
                )
                Text(
                    text = "End: Ayah ${endAyah.toInt()}",
                    fontWeight = FontWeight.Bold,
                    color = IslamicEmeraldPrimary,
                    fontSize = 13.sp
                )
            }

            val maxRange = totalAyahsCount.coerceAtLeast(1).toFloat()
            RangeSlider(
                value = startAyah..endAyah,
                onValueChange = { range ->
                    startAyah = range.start
                    endAyah = range.endInclusive
                },
                valueRange = 1f..maxRange,
                steps = (maxRange.toInt() - 2).coerceAtLeast(0),
                colors = SliderDefaults.colors(
                    thumbColor = IslamicEmeraldPrimary,
                    activeTrackColor = IslamicEmeraldPrimary
                ),
                modifier = Modifier.testTag("ayah_range_slider")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Live Preview of Formatted Output
            Text("Preview:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 160.dp)
            ) {
                Text(
                    text = generatedText,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(10.dp)
                        .verticalScroll(rememberScrollState())
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Copy & Share Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Quran Text", generatedText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("copy_multiple_ayahs_button")
                ) {
                    Icon(Icons.Outlined.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy")
                }

                Button(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Surah $surahName")
                            putExtra(Intent.EXTRA_TEXT, generatedText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Ayahs via"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("share_multiple_ayahs_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ShareCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = IslamicEmeraldPrimary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
