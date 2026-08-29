package com.example.ui.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainSettingsSheet(
    settings: ReadingSettings,
    onSettingsChange: (ReadingSettings) -> Unit,
    onOpenAudioManager: () -> Unit,
    onOpenFontStudio: () -> Unit,
    onOpenTajweedGuide: () -> Unit,
    onOpenThemeSelector: () -> Unit,
    onSyncWithCloud: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var showResetConfirm by remember { mutableStateOf(false) }

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
                .fillMaxHeight(0.94f)
                .padding(horizontal = 20.dp)
        ) {
            // Top Bar
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
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Main Settings (প্রধান সেটিংস)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "কুরআন পাঠ, অনুবাদ, অডিও ও অ্যাপের পূর্ণ নিয়ন্ত্রণ",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrollable Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 36.dp)
            ) {
                // ==========================================
                // Section 1: Quran Reading & Typography
                // ==========================================
                SettingsSectionHeader(
                    icon = Icons.Default.MenuBook,
                    title = "কুরআন তিলাওয়াত ও টাইপোগ্রাফি",
                    subtitle = "লিপি, ফন্ট সাইজ ও ডিসপ্লে অপশন"
                )

                // Live Typography Preview Card
                Surface(
                    color = IslamicEmeraldContainer.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = QuranTypography.getFontSampleText(settings.selectedFont),
                            style = QuranTypography.getArabicTextStyle(
                                font = settings.selectedFont,
                                fontSizeSp = settings.arabicFontSizeSp
                            ),
                            textAlign = TextAlign.Center,
                            color = IslamicEmeraldPrimary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু।",
                            fontSize = settings.translationFontSizeSp.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Arabic Script Selector (Madani vs Indopak)
                Text("আরবি লিপির ধরণ (Script Type):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuranScriptType.values().forEach { script ->
                        val isSelected = settings.selectedScript == script
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    val defaultFont = if (script == QuranScriptType.MADANI_UTHMANI) QuranFontFamily.UTHMANIC_HAFS else QuranFontFamily.INDOPAK_NOOREHUDA
                                    onSettingsChange(settings.copy(selectedScript = script, selectedFont = defaultFont))
                                }
                        ) {
                            Text(
                                text = script.banglaName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Font Family Quick Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("আরবি ফন্ট (Arabic Font Family):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    TextButton(
                        onClick = {
                            onDismiss()
                            onOpenFontStudio()
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Font Studio", fontSize = 11.sp, color = IslamicEmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                val availableFonts = if (settings.selectedScript == QuranScriptType.MADANI_UTHMANI) {
                    listOf(QuranFontFamily.UTHMANIC_HAFS, QuranFontFamily.UTHMANIC_AMIRI, QuranFontFamily.UTHMANIC_SCHEHERAZADE, QuranFontFamily.ME_QURAN)
                } else {
                    listOf(QuranFontFamily.INDOPAK_NOOREHUDA, QuranFontFamily.INDOPAK_NASTALEEQ, QuranFontFamily.INDOPAK_PDMS_SALEEM)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    availableFonts.forEach { font ->
                        val isSelected = settings.selectedFont == font
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) IslamicEmeraldContainer else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSettingsChange(settings.copy(selectedFont = font)) }
                        ) {
                            Text(
                                text = QuranTypography.getFontDisplayName(font).split(" ")[0],
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sliders: Arabic Font Size
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("আরবি ফন্ট সাইজ (Arabic Font Size)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text("${settings.arabicFontSizeSp.toInt()} sp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                }
                Slider(
                    value = settings.arabicFontSizeSp,
                    onValueChange = { onSettingsChange(settings.copy(arabicFontSizeSp = it)) },
                    valueRange = 20f..48f,
                    colors = SliderDefaults.colors(thumbColor = IslamicEmeraldPrimary, activeTrackColor = IslamicEmeraldPrimary)
                )

                // Sliders: Bangla Translation Font Size
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("বাংলা অনুবাদ ফন্ট সাইজ (Translation Size)", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    Text("${settings.translationFontSizeSp.toInt()} sp", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                }
                Slider(
                    value = settings.translationFontSizeSp,
                    onValueChange = { onSettingsChange(settings.copy(translationFontSizeSp = it)) },
                    valueRange = 12f..24f,
                    colors = SliderDefaults.colors(thumbColor = IslamicEmeraldPrimary, activeTrackColor = IslamicEmeraldPrimary)
                )

                // Content Toggles
                Text("প্রদর্শন উপাদান (Display Components):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SettingsSwitchRow("আরবি টেক্সট", settings.showArabic) { onSettingsChange(settings.copy(showArabic = it)) }
                    SettingsSwitchRow("বাংলা অনুবাদ", settings.showTranslation) { onSettingsChange(settings.copy(showTranslation = it)) }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SettingsSwitchRow("শব্দে শব্দে অর্থ", settings.showWordByWord) { onSettingsChange(settings.copy(showWordByWord = it)) }
                    SettingsSwitchRow("তাফসীর নোট", settings.showTafsir) { onSettingsChange(settings.copy(showTafsir = it)) }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    SettingsSwitchRow("তাজবীদ কালার", settings.showTajweed) { onSettingsChange(settings.copy(showTajweed = it)) }
                    TextButton(onClick = {
                        onDismiss()
                        onOpenTajweedGuide()
                    }) {
                        Text("তাজবীদ গাইড", fontSize = 11.sp, color = QuranGold, fontWeight = FontWeight.Bold)
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 14.dp))

                // ==========================================
                // Section 2: Translations & Tafsir Sources
                // ==========================================
                SettingsSectionHeader(
                    icon = Icons.Default.Translate,
                    title = "অনুবাদ ও তাফসীর গ্রন্থ",
                    subtitle = "পছন্দের বাংলা ও ইংরেজি অনুবাদক"
                )

                var translationExpanded by remember { mutableStateOf(false) }
                val translationOptions = listOf(
                    "Bangla - Islamic Foundation",
                    "Bangla - Bayaan Foundation",
                    "Bangla - Taisirul Quran",
                    "Bangla - Mujibur Rahman",
                    "English - Sahih International",
                    "English - The Clear Quran (Dr. Mustafa Khattab)"
                )
                Text("বাংলা/ইংরেজি অনুবাদ নির্বাচন:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { translationExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(settings.selectedTranslation, color = IslamicEmeraldPrimary, fontSize = 13.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = IslamicEmeraldPrimary)
                    }
                }
                DropdownMenu(expanded = translationExpanded, onDismissRequest = { translationExpanded = false }) {
                    translationOptions.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt, fontSize = 13.sp) },
                            onClick = {
                                onSettingsChange(settings.copy(selectedTranslation = opt))
                                translationExpanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                var tafsirExpanded by remember { mutableStateOf(false) }
                val tafsirOptions = listOf(
                    "Bangla - Tafsir Ibn Kathir",
                    "Bangla - Tafsir Abu Bakr Zakaria",
                    "Bangla - Tafsir Ahsanul Bayaan",
                    "English - Tafsir Ibn Kathir (Abridged)"
                )
                Text("তাফসীর গ্রন্থ নির্বাচন:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedButton(
                    onClick = { tafsirExpanded = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(settings.selectedTafsir, color = IslamicEmeraldPrimary, fontSize = 13.sp)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = IslamicEmeraldPrimary)
                    }
                }
                DropdownMenu(expanded = tafsirExpanded, onDismissRequest = { tafsirExpanded = false }) {
                    tafsirOptions.forEach { opt ->
                        DropdownMenuItem(
                            text = { Text(opt, fontSize = 13.sp) },
                            onClick = {
                                onSettingsChange(settings.copy(selectedTafsir = opt))
                                tafsirExpanded = false
                            }
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 14.dp))

                // ==========================================
                // Section 3: Themes & Night Mode
                // ==========================================
                SettingsSectionHeader(
                    icon = Icons.Default.Palette,
                    title = "থিম ও নাইট মোড",
                    subtitle = "কালার প্যালেট ও ডিসপ্লে মোড"
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("বর্তমান থিম: ${settings.appColorTheme.banglaName}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    TextButton(onClick = {
                        onDismiss()
                        onOpenThemeSelector()
                    }) {
                        Text("সকল থিম দেখুন", fontSize = 11.sp, color = IslamicEmeraldPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Night Mode 4-Pill Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    NightModeOption.values().forEach { mode ->
                        val isSelected = settings.nightModeOption == mode
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { onSettingsChange(settings.copy(nightModeOption = mode)) }
                        ) {
                            Text(
                                text = when (mode) {
                                    NightModeOption.LIGHT -> "দিন"
                                    NightModeOption.NIGHT -> "রাত"
                                    NightModeOption.OLED_BLACK -> "OLED"
                                    NightModeOption.SYSTEM -> "Auto"
                                },
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reading Layout Mode
                Text("ডিফল্ট রিডিং লেআউট:", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (settings.layoutMode == ReadingLayoutMode.LYRICS_AYAH_BY_AYAH) IslamicEmeraldContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (settings.layoutMode == ReadingLayoutMode.LYRICS_AYAH_BY_AYAH) IslamicEmeraldPrimary else Color.Transparent),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSettingsChange(settings.copy(layoutMode = ReadingLayoutMode.LYRICS_AYAH_BY_AYAH)) }
                    ) {
                        Text("আয়াত বাই আয়াত (Lyrics)", fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (settings.layoutMode == ReadingLayoutMode.PAGE_MUSHAF) IslamicEmeraldContainer else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (settings.layoutMode == ReadingLayoutMode.PAGE_MUSHAF) IslamicEmeraldPrimary else Color.Transparent),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onSettingsChange(settings.copy(layoutMode = ReadingLayoutMode.PAGE_MUSHAF)) }
                    ) {
                        Text("পৃষ্ঠা মুসহাফ (Page Mushaf)", fontSize = 11.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.padding(8.dp))
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 14.dp))

                // ==========================================
                // Section 4: Audio & Recitation Management
                // ==========================================
                SettingsSectionHeader(
                    icon = Icons.Default.Headphones,
                    title = "অডিও ও ক্বারী সেটিংস",
                    subtitle = "অডিও প্লেয়ার, অফলাইন ও রিপিট নিয়ন্ত্রণ"
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("পূর্ণাঙ্গ অডিও ম্যানেজার", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text("সকল ক্বারী, অফলাইন ডাউনলোড ও প্লেব্যাক স্পিড", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        FilledTonalButton(
                            onClick = {
                                onDismiss()
                                onOpenAudioManager()
                            }
                        ) {
                            Text("Open Manager", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 14.dp))

                // ==========================================
                // Section 5: Cloud Sync & Storage
                // ==========================================
                SettingsSectionHeader(
                    icon = Icons.Default.CloudSync,
                    title = "ক্লাউড সিঙ্ক ও ব্যাকআপ",
                    subtitle = "বুকমার্ক ও পড়ার হিস্ট্রি সংরক্ষণ"
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IslamicEmeraldContainer.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("ফায়ারবেস ক্লাউড সিঙ্ক", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                            Text("আপনার পিন, নোট ও প্ল্যানার ক্লাউডে ব্যাকআপ রাখুন", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = {
                                onSyncWithCloud()
                                Toast.makeText(context, "ক্লাউড সিঙ্ক সম্পন্ন হয়েছে ✨", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
                        ) {
                            Text("Sync Now", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Reset Settings Button
                OutlinedButton(
                    onClick = { showResetConfirm = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("সেটিংস রিসেট করুন (Reset to Defaults)", fontSize = 12.sp)
                }

                Divider(modifier = Modifier.padding(vertical = 14.dp))

                // ==========================================
                // Section 6: About App & Community
                // ==========================================
                SettingsSectionHeader(
                    icon = Icons.Default.Info,
                    title = "অ্যাপ পরিচিতি ও তথ্য",
                    subtitle = "ভার্সন ও সোর্স তথ্য"
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Al Quran App v1.0.0", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("ডেটা সোর্স: Tarteel Qul, Tanzil.net & EveryAyah", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Al Quran App")
                                    putExtra(Intent.EXTRA_TEXT, "Read and listen to the Holy Quran with Al Quran App: https://quran.com")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share App"))
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = {
                                Toast.makeText(context, "Thank you for rating 5 Stars! ⭐⭐⭐⭐⭐", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = QuranGold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Rate 5★", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    if (showResetConfirm) {
        AlertDialog(
            onDismissRequest = { showResetConfirm = false },
            title = { Text("সেটিংস রিসেট করবেন?") },
            text = { Text("সকল ফন্ট সাইজ, লিপি, অনুবাদ ও থিম ডিফল্ট মানে ফিরিয়ে নেওয়া হবে।") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSettingsChange(ReadingSettings())
                        showResetConfirm = false
                        Toast.makeText(context, "সেটিংস ডিফল্ট মানে রিসেট করা হয়েছে ✨", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Text("হ্যাঁ, রিসেট করুন", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetConfirm = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = IslamicEmeraldPrimary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
private fun SettingsSwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = IslamicEmeraldPrimary)
        )
        Text(label, fontSize = 12.sp)
    }
}
