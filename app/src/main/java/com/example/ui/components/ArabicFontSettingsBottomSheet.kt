package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranFontFamily
import com.example.data.model.QuranScriptType
import com.example.data.model.ReadingSettings
import com.example.ui.theme.*

/**
 * Dedicated Arabic Fonts & Typography Studio Settings Panel
 * Allows users to dynamically switch between Arabic fonts loaded from the assets folder,
 * adjust line-height, letter-spacing, font weight, script type, and preview scriptures in real time.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArabicFontSettingsBottomSheet(
    settings: ReadingSettings,
    onScriptSelected: (QuranScriptType) -> Unit = {},
    onFontSelected: (QuranFontFamily) -> Unit,
    onFontSizeChanged: (Float) -> Unit,
    onLineHeightMultiplierChanged: (Float) -> Unit,
    onLetterSpacingChanged: (Float) -> Unit,
    onFontWeightChanged: (String) -> Unit,
    onApplyPreset: (String) -> Unit,
    onResetDefaults: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryIndex by remember { mutableStateOf(0) }
    var selectedPreviewAyahIndex by remember { mutableStateOf(0) }

    val sampleVerses = listOf(
        Pair("Al-Fatihah 1:1-2", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ ۝١ ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ ۝٢"),
        Pair("Ayatul Kursi 2:255", "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌ وَلَا نَوْمٌ ۝"),
        Pair("Al-Ikhlas 112:1-4", "قُلْ هُوَ ٱللَّهُ أَحَدٌ ۝١ ٱللَّهُ ٱلصَّمَدُ ۝٢ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝٣"),
        Pair("Al-Kahf 18:1", "ٱلْحَمْدُ لِلَّهِ ٱلَّذِىٓ أَنزَلَ عَلَىٰ عَبْدِهِ ٱلْكِتَٰبَ وَلَمْ يَجْعَل لَّهُۥ عِوَجَا ۝")
    )

    val currentFontDetail = QuranTypography.getFontDetail(settings.selectedFont)

    val filteredFonts = remember(selectedCategoryIndex, settings.selectedScript) {
        when (selectedCategoryIndex) {
            1 -> QuranTypography.availableFonts.filter { it.category == QuranFontCategory.UTHMANIC_MADANI }
            2 -> QuranTypography.availableFonts.filter { it.category == QuranFontCategory.INDOPAK_NASTALEEQ }
            3 -> QuranTypography.availableFonts.filter { it.category == QuranFontCategory.CALLIGRAPHIC }
            else -> QuranTypography.availableFonts
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
                .verticalScroll(rememberScrollState())
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Arabic Fonts & Typography",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "লিপি, ফন্ট ও ক্যালিগ্রাফি স্টুডিও (Real-time Customization)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // DUAL SCRIPT SELECTOR (মাদানী/উসমানী ও ইন্দোপাক)
            // ==========================================
            Text(
                text = "কুরআন স্ক্রিপ্ট নির্বাচন (Quran Script Selection):",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val isMadani = settings.selectedScript == QuranScriptType.MADANI_UTHMANI
                Surface(
                    onClick = {
                        onScriptSelected(QuranScriptType.MADANI_UTHMANI)
                        selectedCategoryIndex = 1
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isMadani) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                        width = if (isMadani) 2.dp else 1.dp,
                        color = if (isMadani) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("script_button_madani")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isMadani,
                                onClick = {
                                    onScriptSelected(QuranScriptType.MADANI_UTHMANI)
                                    selectedCategoryIndex = 1
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "উসমানী / মাদানী",
                                fontWeight = if (isMadani) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (isMadani) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "মদীনা মুসহাফ স্ট্যান্ডার্ড",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                val isIndoPak = settings.selectedScript == QuranScriptType.INDOPAK
                Surface(
                    onClick = {
                        onScriptSelected(QuranScriptType.INDOPAK)
                        selectedCategoryIndex = 2
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = if (isIndoPak) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                        width = if (isIndoPak) 2.dp else 1.dp,
                        color = if (isIndoPak) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("script_button_indopak")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = isIndoPak,
                                onClick = {
                                    onScriptSelected(QuranScriptType.INDOPAK)
                                    selectedCategoryIndex = 2
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Text(
                                text = "ইন্দোপাক লিপি",
                                fontWeight = if (isIndoPak) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (isIndoPak) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "সাবকন্টিনেন্টাল এশিয়ান লিপি",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Live Interactive Scripture Preview Card
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Preview Top Info Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Live Preview (${settings.arabicFontSizeSp.toInt()}sp • ${settings.arabicFontWeight})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        // Sample Verse Switcher
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = sampleVerses[selectedPreviewAyahIndex].first,
                                fontSize = 11.sp,
                                color = QuranGoldDark,
                                fontWeight = FontWeight.SemiBold
                            )
                            IconButton(
                                onClick = {
                                    selectedPreviewAyahIndex = (selectedPreviewAyahIndex + 1) % sampleVerses.size
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Switch verse",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Arabic Scripture Rendered with Current Typography Settings
                    val currentSample = sampleVerses[selectedPreviewAyahIndex].second
                    val formattedSample = if (settings.selectedScript == QuranScriptType.INDOPAK || settings.selectedFont.name.startsWith("INDOPAK")) {
                        currentSample
                            .replace("ٱ", "ا")
                            .replace("ٰ", "")
                            .replace("لَّهِ", "للَّهِ")
                    } else {
                        currentSample
                    }

                    Text(
                        text = formattedSample,
                        style = QuranTypography.getArabicTextStyle(
                            font = settings.selectedFont,
                            fontSizeSp = settings.arabicFontSizeSp,
                            fontWeight = QuranTypography.parseFontWeight(settings.arabicFontWeight),
                            letterSpacingSp = settings.arabicLetterSpacingSp,
                            lineHeightMultiplier = settings.arabicLineHeightMultiplier
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Active Font Details Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(0.5.dp, QuranGold.copy(alpha = 0.5f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${currentFontDetail.displayName} • ${currentFontDetail.scriptType}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Typography Presets Bar
            Text(
                text = "জনপ্রিয় প্রেসেট (Presets)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    PresetChip(
                        title = "মাদানী স্ট্যান্ডার্ড",
                        subtitle = "Uthmanic 28sp",
                        onClick = { onApplyPreset("madani_standard") }
                    )
                }
                item {
                    PresetChip(
                        title = "আমিরি ক্যালিগ্রাফি",
                        subtitle = "Amiri 30sp",
                        onClick = { onApplyPreset("amiri_classical") }
                    )
                }
                item {
                    PresetChip(
                        title = "ইন্দোপাক স্পষ্টতা",
                        subtitle = "Noorehidayat 30sp",
                        onClick = { onApplyPreset("indopak_clarity") }
                    )
                }
                item {
                    PresetChip(
                        title = "নাস্তালিক লিপি",
                        subtitle = "Nastaleeq 32sp",
                        onClick = { onApplyPreset("indopak_nastaleeq") }
                    )
                }
                item {
                    PresetChip(
                        title = "বড় হরফ (লার্জ প্রিন্ট)",
                        subtitle = "Bold 36sp",
                        onClick = { onApplyPreset("elder_large_print") }
                    )
                }
                item {
                    PresetChip(
                        title = "কমপ্যাক্ট মুসহাফ",
                        subtitle = "Digitalkhat 24sp",
                        onClick = { onApplyPreset("compact_mushaf") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Category Tab Filter
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryIndex,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Tab(
                    selected = selectedCategoryIndex == 0,
                    onClick = { selectedCategoryIndex = 0 },
                    text = { Text("সকল ফন্ট (9)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedCategoryIndex == 1,
                    onClick = { selectedCategoryIndex = 1 },
                    text = { Text("মাদানী / উসমানী", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedCategoryIndex == 2,
                    onClick = { selectedCategoryIndex = 2 },
                    text = { Text("ইন্দোপাক / এশিয়ান", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
                Tab(
                    selected = selectedCategoryIndex == 3,
                    onClick = { selectedCategoryIndex = 3 },
                    text = { Text("নসখ ও ক্লাসিক্যাল", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // List of Available Arabic Fonts from Assets
            Text(
                text = "ফন্ট নির্বাচন করুন (${filteredFonts.size}টি ফন্ট)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            filteredFonts.forEach { fontDetail ->
                val isSelected = settings.selectedFont == fontDetail.fontFamilyEnum

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = if (isSelected) 1.8.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onFontSelected(fontDetail.fontFamilyEnum) }
                        .testTag("font_card_${fontDetail.fontFamilyEnum.name.lowercase()}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onFontSelected(fontDetail.fontFamilyEnum) },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = fontDetail.displayName,
                                        fontSize = 14.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${fontDetail.banglaName} • ${fontDetail.scriptType}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            // Asset indicator badge
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = "assets/${fontDetail.assetFileName.substringAfterLast("/")}",
                                    fontSize = 9.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Rendered Arabic Snippet in this Specific Font
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = fontDetail.sampleAyahSnippet,
                                style = QuranTypography.getArabicTextStyle(
                                    font = fontDetail.fontFamilyEnum,
                                    fontSizeSp = 22f,
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Right,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = fontDetail.description,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(14.dp))

            // Typography Fine-Tuning Section
            Text(
                text = "টাইপোগ্রাফি ফাইন-টিউনিং (Fine-Tuning Controls)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // 1. Arabic Font Size Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Arabic Font Size (হরফের আকার)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onFontSizeChanged(settings.arabicFontSizeSp - 2f) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = "${settings.arabicFontSizeSp.toInt()} sp",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(
                        onClick = { onFontSizeChanged(settings.arabicFontSizeSp + 2f) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                    }
                }
            }
            Slider(
                value = settings.arabicFontSizeSp,
                onValueChange = onFontSizeChanged,
                valueRange = 20f..46f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Line Height Multiplier Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Line Spacing Multiplier (লাইন ব্যবধান)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                Text(
                    text = String.format("%.2fx", settings.arabicLineHeightMultiplier),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = settings.arabicLineHeightMultiplier,
                onValueChange = onLineHeightMultiplierChanged,
                valueRange = 1.3f..2.4f,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Font Weight Selector
            Text("Font Weight (হরফের স্থূলতা)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val weights = listOf("Normal", "Medium", "SemiBold", "Bold")
                weights.forEach { weight ->
                    FilterChip(
                        selected = settings.arabicFontWeight == weight,
                        onClick = { onFontWeightChanged(weight) },
                        label = { Text(weight, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Reset & Done Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onResetDefaults,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("রিসেট (Reset)")
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("সম্পন্ন (Done)", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun PresetChip(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
        modifier = Modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = subtitle,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
