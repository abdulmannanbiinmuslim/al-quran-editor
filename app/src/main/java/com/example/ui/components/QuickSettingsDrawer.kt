package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranFontFamily
import com.example.data.model.ReadingSettings
import com.example.data.model.ReadingViewMode
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickSettingsModalSheet(
    settings: ReadingSettings,
    onSettingsChange: (ReadingSettings) -> Unit,
    onOpenTajweedGuide: () -> Unit,
    onOpenFontSettings: () -> Unit = {},
    onOpenThemeSelector: () -> Unit = {},
    onOpenMainSettings: () -> Unit = {},
    onOpenAudioManager: () -> Unit = {},
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    text = "Quick Settings (দ্রুত সেটিংস)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 0. Theme & Night Mode Controls
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "থিম ও নাইট মোড (${settings.appColorTheme.displayName})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(
                            onClick = {
                                onDismiss()
                                onOpenThemeSelector()
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("সবগুলো দেখুন", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 4 Night Mode Quick Selector Pills
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        com.example.data.model.NightModeOption.values().forEach { option ->
                            val isSelected = settings.nightModeOption == option
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onSettingsChange(settings.copy(nightModeOption = option)) }
                            ) {
                                Text(
                                    text = when (option) {
                                        com.example.data.model.NightModeOption.LIGHT -> "দিন"
                                        com.example.data.model.NightModeOption.NIGHT -> "রাত"
                                        com.example.data.model.NightModeOption.OLED_BLACK -> "OLED"
                                        com.example.data.model.NightModeOption.SYSTEM -> "Auto"
                                    },
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. View Mode (দেখুন)
            Text("দেখুন (View Mode)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ReadingViewMode.values().forEach { mode ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onSettingsChange(settings.copy(viewMode = mode)) }
                            .padding(end = 4.dp)
                    ) {
                        RadioButton(
                            selected = settings.viewMode == mode,
                            onClick = { onSettingsChange(settings.copy(viewMode = mode)) },
                            colors = RadioButtonDefaults.colors(selectedColor = IslamicEmeraldPrimary)
                        )
                        Text(
                            text = when (mode) {
                                ReadingViewMode.SURAH -> "সূরা"
                                ReadingViewMode.PAGE -> "পৃষ্ঠা"
                                ReadingViewMode.JUZ -> "পারা"
                                ReadingViewMode.HIZB -> "হিজব"
                                ReadingViewMode.RUKU -> "রুকু"
                            },
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // 2. Content Toggles (কনটেন্ট)
            Text("কনটেন্ট (Content Display)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CheckboxItem(
                        label = "আরবি (Arabic)",
                        checked = settings.showArabic,
                        onCheckedChange = { onSettingsChange(settings.copy(showArabic = it)) }
                    )
                    CheckboxItem(
                        label = "অনুবাদ (Translation)",
                        checked = settings.showTranslation,
                        onCheckedChange = { onSettingsChange(settings.copy(showTranslation = it)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CheckboxItem(
                        label = "শব্দে শব্দে (Word by Word)",
                        checked = settings.showWordByWord,
                        onCheckedChange = { onSettingsChange(settings.copy(showWordByWord = it)) }
                    )
                    CheckboxItem(
                        label = "তাফসীর (Tafsir)",
                        checked = settings.showTafsir,
                        onCheckedChange = { onSettingsChange(settings.copy(showTafsir = it)) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CheckboxItem(
                        label = "তাজবীদ (Tajweed Colors)",
                        checked = settings.showTajweed,
                        onCheckedChange = { onSettingsChange(settings.copy(showTajweed = it)) }
                    )

                    TextButton(onClick = onOpenTajweedGuide) {
                        Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("তাজবীদের নিয়ম", fontSize = 12.sp, color = IslamicEmeraldPrimary)
                    }
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // 3. Translation & Tafsir Selection
            Text("অনুবাদ নির্বাচন (Translations)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            var translationExpanded by remember { mutableStateOf(false) }
            val translations = listOf(
                "Bangla - Islamic Foundation",
                "Bangla - Bayaan Foundation",
                "Bangla - Taisirul Quran",
                "Bangla - Mujibur Rahman",
                "English - Sahih International",
                "English - The Clear Quran (Mustafa Khattab)"
            )
            OutlinedButton(
                onClick = { translationExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(settings.selectedTranslation, color = IslamicEmeraldPrimary)
            }
            DropdownMenu(
                expanded = translationExpanded,
                onDismissRequest = { translationExpanded = false }
            ) {
                translations.forEach { t ->
                    DropdownMenuItem(
                        text = { Text(t) },
                        onClick = {
                            onSettingsChange(settings.copy(selectedTranslation = t))
                            translationExpanded = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text("তাফসীর নির্বাচন (Tafsir)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            var tafsirExpanded by remember { mutableStateOf(false) }
            val tafsirs = listOf(
                "Bangla - Tafsir Ibn Kathir",
                "Bangla - Tafsir Abu Bakr Zakaria",
                "Bangla - Tafsir Ahsanul Bayaan",
                "English - Tafsir Ibn Kathir (Abridged)"
            )
            OutlinedButton(
                onClick = { tafsirExpanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(settings.selectedTafsir, color = IslamicEmeraldPrimary)
            }
            DropdownMenu(
                expanded = tafsirExpanded,
                onDismissRequest = { tafsirExpanded = false }
            ) {
                tafsirs.forEach { tf ->
                    DropdownMenuItem(
                        text = { Text(tf) },
                        onClick = {
                            onSettingsChange(settings.copy(selectedTafsir = tf))
                            tafsirExpanded = false
                        }
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // 4. Mushaf Type (মুসহাফের ধরণ)
            Text("মুসহাফের ধরণ (Mushaf Type)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = settings.selectedMushafType == "Mushaf Unicode Text",
                    onClick = { onSettingsChange(settings.copy(selectedMushafType = "Mushaf Unicode Text")) },
                    label = { Text("Mushaf Unicode Text") }
                )
                FilterChip(
                    selected = settings.selectedMushafType == "Classic Madani Mushaf",
                    onClick = { onSettingsChange(settings.copy(selectedMushafType = "Classic Madani Mushaf")) },
                    label = { Text("Classic Madani Mushaf") }
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // 5. Font Settings & Live Preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Font Settings (ফন্ট ও টাইপোগ্রাফি)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                TextButton(
                    onClick = {
                        onDismiss()
                        onOpenFontSettings()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Font Studio", fontSize = 12.sp, color = IslamicEmeraldPrimary, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Live Preview Card with Dynamic Typography
            Surface(
                color = IslamicEmeraldContainer.copy(alpha = 0.4f),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = com.example.ui.theme.QuranTypography.getFontSampleText(settings.selectedFont),
                        style = com.example.ui.theme.QuranTypography.getArabicTextStyle(
                            font = settings.selectedFont,
                            fontSizeSp = settings.arabicFontSizeSp
                        ),
                        textAlign = TextAlign.Center,
                        color = IslamicEmeraldPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "সকল প্রশংসা জগতসমূহের প্রতিপালক আল্লাহরই।",
                        fontSize = settings.translationFontSizeSp.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ফন্ট: ${com.example.ui.theme.QuranTypography.getFontDisplayName(settings.selectedFont)}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = QuranGold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Arabic Font Size", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("${settings.arabicFontSizeSp.toInt()} sp", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            }
            Slider(
                value = settings.arabicFontSizeSp,
                onValueChange = { onSettingsChange(settings.copy(arabicFontSizeSp = it)) },
                valueRange = 20f..44f,
                colors = SliderDefaults.colors(
                    thumbColor = IslamicEmeraldPrimary,
                    activeTrackColor = IslamicEmeraldPrimary
                )
            )

            // Translation Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Translation Font Size", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("${settings.translationFontSizeSp.toInt()} sp", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            }
            Slider(
                value = settings.translationFontSizeSp,
                onValueChange = { onSettingsChange(settings.copy(translationFontSizeSp = it)) },
                valueRange = 12f..24f,
                colors = SliderDefaults.colors(
                    thumbColor = IslamicEmeraldPrimary,
                    activeTrackColor = IslamicEmeraldPrimary
                )
            )

            // Tafsir Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tafsir Font Size", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text("${settings.tafsirFontSizeSp.toInt()} sp", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            }
            Slider(
                value = settings.tafsirFontSizeSp,
                onValueChange = { onSettingsChange(settings.copy(tafsirFontSizeSp = it)) },
                valueRange = 11f..20f,
                colors = SliderDefaults.colors(
                    thumbColor = IslamicEmeraldPrimary,
                    activeTrackColor = IslamicEmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Font Family Selector (Uthmani vs Indopak)
            Text("Quran Font Family (ফন্ট নির্বাচন করুন)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(6.dp))

            var selectedFontCategory by remember { mutableStateOf(0) } // 0: Uthmani/Madani, 1: Indopak/Asian
            TabRow(
                selectedTabIndex = selectedFontCategory,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = IslamicEmeraldPrimary
            ) {
                Tab(
                    selected = selectedFontCategory == 0,
                    onClick = { selectedFontCategory = 0 },
                    text = { Text("Uthmani / Madani (মাদানী)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedFontCategory == 1,
                    onClick = { selectedFontCategory = 1 },
                    text = { Text("IndoPak / Asian (ইন্দোপাক)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            val uthmaniFonts = listOf(
                QuranFontFamily.UTHMANIC_HAFS,
                QuranFontFamily.UTHMANIC_AMIRI,
                QuranFontFamily.UTHMANIC_SCHEHERAZADE,
                QuranFontFamily.UTHMANIC_DIGITALKHAT,
                QuranFontFamily.ME_QURAN
            )

            val indopakFonts = listOf(
                QuranFontFamily.INDOPAK_NASTALEEQ,
                QuranFontFamily.INDOPAK_NOOREHUDA,
                QuranFontFamily.INDOPAK_NOOREHIDAYAT,
                QuranFontFamily.INDOPAK_PDMS_SALEEM
            )

            val displayedFonts = if (selectedFontCategory == 0) uthmaniFonts else indopakFonts

            displayedFonts.forEach { font ->
                val isSelected = settings.selectedFont == font
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) IslamicEmeraldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSettingsChange(settings.copy(selectedFont = font)) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { onSettingsChange(settings.copy(selectedFont = font)) },
                                colors = RadioButtonDefaults.colors(selectedColor = IslamicEmeraldPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = com.example.ui.theme.QuranTypography.getFontDisplayName(font),
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (selectedFontCategory == 0) "Madani Naskh Script" else "Subcontinental Nastaleeq/Asian",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Text(
                            text = if (font == QuranFontFamily.INDOPAK_NASTALEEQ) "القرآن" else "ٱلْقُرْءَانُ",
                            style = com.example.ui.theme.QuranTypography.getArabicTextStyle(
                                font = font,
                                fontSizeSp = 20f
                            ),
                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Navigation Shortcuts to Audio Manager & Main Settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onDismiss()
                        onOpenAudioManager()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IslamicEmeraldPrimary)
                ) {
                    Icon(Icons.Default.Headphones, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Audio Manager", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onDismiss()
                        onOpenMainSettings()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
                ) {
                    Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Main Settings", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun CheckboxItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onCheckedChange(!checked) }
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = IslamicEmeraldPrimary)
        )
        Text(label, fontSize = 13.sp)
    }
}
