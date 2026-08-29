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
    // Local staging state for real-time live preview before applying to reader
    var previewFont by remember(settings.selectedFont) { mutableStateOf(settings.selectedFont) }
    var previewScript by remember(settings.selectedScript) { mutableStateOf(settings.selectedScript) }
    var previewFontSize by remember(settings.arabicFontSizeSp) { mutableFloatStateOf(settings.arabicFontSizeSp) }
    var previewLineHeight by remember(settings.arabicLineHeightMultiplier) { mutableFloatStateOf(settings.arabicLineHeightMultiplier) }
    var previewLetterSpacing by remember(settings.arabicLetterSpacingSp) { mutableFloatStateOf(settings.arabicLetterSpacingSp) }
    var previewFontWeight by remember(settings.arabicFontWeight) { mutableStateOf(settings.arabicFontWeight) }

    var selectedCategoryIndex by remember { mutableIntStateOf(0) }
    var selectedPreviewAyahIndex by remember { mutableIntStateOf(0) }
    var hasAppliedFeedback by remember { mutableStateOf(false) }

    val sampleVerses = listOf(
        Pair("Al-Fatihah 1:1-2", "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ ۝١ ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ ۝٢"),
        Pair("Ayatul Kursi 2:255", "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌ وَلَا نَوْمٌ ۝"),
        Pair("Al-Ikhlas 112:1-4", "قُلْ هُوَ ٱللَّهُ أَحَدٌ ۝١ ٱللَّهُ ٱلصَّمَدُ ۝٢ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝٣"),
        Pair("Al-Kahf 18:1", "ٱلْحَمْدُ لِلَّهِ ٱلَّذِىٓ أَنزَلَ عَلَىٰ عَبْدِهِ ٱلْكِتَٰبَ وَلَمْ يَجْعَل لَّهُۥ عِوَجَا ۝")
    )

    val currentFontDetail = QuranTypography.getFontDetail(previewFont)

    val filteredFonts = remember(selectedCategoryIndex, previewScript) {
        when (selectedCategoryIndex) {
            1 -> QuranTypography.availableFonts.filter { it.category == QuranFontCategory.UTHMANIC_MADANI }
            2 -> QuranTypography.availableFonts.filter { it.category == QuranFontCategory.INDOPAK_NASTALEEQ }
            3 -> QuranTypography.availableFonts.filter { it.category == QuranFontCategory.CALLIGRAPHIC }
            else -> QuranTypography.availableFonts
        }
    }

    fun applyPreviewToReader() {
        if (settings.selectedScript != previewScript) {
            onScriptSelected(previewScript)
        }
        onFontSelected(previewFont)
        onFontSizeChanged(previewFontSize)
        onLineHeightMultiplierChanged(previewLineHeight)
        onLetterSpacingChanged(previewLetterSpacing)
        onFontWeightChanged(previewFontWeight)
        hasAppliedFeedback = true
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
                val isMadani = previewScript == QuranScriptType.MADANI_UTHMANI
                Surface(
                    onClick = {
                        previewScript = QuranScriptType.MADANI_UTHMANI
                        previewFont = QuranFontFamily.UTHMANIC_HAFS
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
                                    previewScript = QuranScriptType.MADANI_UTHMANI
                                    previewFont = QuranFontFamily.UTHMANIC_HAFS
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

                val isIndoPak = previewScript == QuranScriptType.INDOPAK
                Surface(
                    onClick = {
                        previewScript = QuranScriptType.INDOPAK
                        previewFont = QuranFontFamily.INDOPAK_NASTALEEQ
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
                                    previewScript = QuranScriptType.INDOPAK
                                    previewFont = QuranFontFamily.INDOPAK_NASTALEEQ
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
                                text = "Live Text Preview (${previewFontSize.toInt()}sp • $previewFontWeight)",
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

                    // Arabic Scripture Rendered with Current Live Preview Typography Settings
                    val currentSample = sampleVerses[selectedPreviewAyahIndex].second
                    val formattedSample = if (previewScript == QuranScriptType.INDOPAK || previewFont.name.startsWith("INDOPAK")) {
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
                            font = previewFont,
                            fontSizeSp = previewFontSize,
                            fontWeight = QuranTypography.parseFontWeight(previewFontWeight),
                            letterSpacingSp = previewLetterSpacing,
                            lineHeightMultiplier = previewLineHeight
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Active Font Details & Live Status Badge
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                                    imageVector = Icons.Default.FontDownload,
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

                        if (settings.selectedFont == previewFont) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = MaterialTheme.colorScheme.primary,
                            ) {
                                Text(
                                    text = "Active in Reader",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
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
                        onClick = {
                            previewFont = QuranFontFamily.UTHMANIC_HAFS
                            previewFontSize = 28f
                            previewLineHeight = 1.65f
                            previewLetterSpacing = 0f
                            previewFontWeight = "Bold"
                            previewScript = QuranScriptType.MADANI_UTHMANI
                        }
                    )
                }
                item {
                    PresetChip(
                        title = "আমিরি ক্যালিগ্রাফি",
                        subtitle = "Amiri 30sp",
                        onClick = {
                            previewFont = QuranFontFamily.UTHMANIC_AMIRI
                            previewFontSize = 30f
                            previewLineHeight = 1.75f
                            previewLetterSpacing = 0.2f
                            previewFontWeight = "Bold"
                            previewScript = QuranScriptType.MADANI_UTHMANI
                        }
                    )
                }
                item {
                    PresetChip(
                        title = "ইন্দোপাক স্পষ্টতা",
                        subtitle = "Noorehidayat 30sp",
                        onClick = {
                            previewFont = QuranFontFamily.INDOPAK_NOOREHIDAYAT
                            previewFontSize = 30f
                            previewLineHeight = 1.8f
                            previewLetterSpacing = 0f
                            previewFontWeight = "Bold"
                            previewScript = QuranScriptType.INDOPAK
                        }
                    )
                }
                item {
                    PresetChip(
                        title = "নাস্তালিক লিপি",
                        subtitle = "Nastaleeq 32sp",
                        onClick = {
                            previewFont = QuranFontFamily.INDOPAK_NASTALEEQ
                            previewFontSize = 32f
                            previewLineHeight = 1.95f
                            previewLetterSpacing = 0f
                            previewFontWeight = "Normal"
                            previewScript = QuranScriptType.INDOPAK
                        }
                    )
                }
                item {
                    PresetChip(
                        title = "বড় হরফ (লার্জ প্রিন্ট)",
                        subtitle = "Bold 36sp",
                        onClick = {
                            previewFont = QuranFontFamily.INDOPAK_NOOREHUDA
                            previewFontSize = 36f
                            previewLineHeight = 1.85f
                            previewLetterSpacing = 0.5f
                            previewFontWeight = "Bold"
                            previewScript = QuranScriptType.INDOPAK
                        }
                    )
                }
                item {
                    PresetChip(
                        title = "কমপ্যাক্ট মুসহাফ",
                        subtitle = "Digitalkhat 24sp",
                        onClick = {
                            previewFont = QuranFontFamily.UTHMANIC_DIGITALKHAT
                            previewFontSize = 24f
                            previewLineHeight = 1.55f
                            previewLetterSpacing = 0f
                            previewFontWeight = "Medium"
                            previewScript = QuranScriptType.MADANI_UTHMANI
                        }
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
                text = "ফন্ট নির্বাচন ও প্রিভিউ (${filteredFonts.size}টি ফন্ট)",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(6.dp))

            filteredFonts.forEach { fontDetail ->
                val isPreviewing = previewFont == fontDetail.fontFamilyEnum
                val isAppliedInReader = settings.selectedFont == fontDetail.fontFamilyEnum

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (isPreviewing) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(
                        width = if (isPreviewing) 1.8.dp else 1.dp,
                        color = if (isPreviewing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            previewFont = fontDetail.fontFamilyEnum
                            hasAppliedFeedback = false
                        }
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
                                    selected = isPreviewing,
                                    onClick = {
                                        previewFont = fontDetail.fontFamilyEnum
                                        hasAppliedFeedback = false
                                    },
                                    colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = fontDetail.displayName,
                                            fontSize = 14.sp,
                                            fontWeight = if (isPreviewing) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isPreviewing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                        )
                                        if (isAppliedInReader) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = QuranGold.copy(alpha = 0.2f)
                                            ) {
                                                Text(
                                                    text = "In Reader",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = QuranGoldDark,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
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
                                color = if (isPreviewing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
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
                        onClick = { previewFontSize = (previewFontSize - 2f).coerceIn(18f, 52f) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = "${previewFontSize.toInt()} sp",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(
                        onClick = { previewFontSize = (previewFontSize + 2f).coerceIn(18f, 52f) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                    }
                }
            }
            Slider(
                value = previewFontSize,
                onValueChange = { previewFontSize = it },
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
                    text = String.format("%.2fx", previewLineHeight),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Slider(
                value = previewLineHeight,
                onValueChange = { previewLineHeight = it },
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
                        selected = previewFontWeight == weight,
                        onClick = { previewFontWeight = weight },
                        label = { Text(weight, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Apply to Quran Reader with prominent UI
            Button(
                onClick = { applyPreviewToReader() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("apply_font_to_reader_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasAppliedFeedback) Color(0xFF16A34A) else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(
                    imageVector = if (hasAppliedFeedback) Icons.Default.Check else Icons.Default.AutoStories,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasAppliedFeedback) "প্রয়োগ সম্পন্ন হয়েছে (Applied to Reader)" else "কুরআন রিডারে প্রয়োগ করুন (Apply to Quran Reader)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Reset & Close Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onResetDefaults()
                        previewFont = QuranFontFamily.UTHMANIC_HAFS
                        previewScript = QuranScriptType.MADANI_UTHMANI
                        previewFontSize = 28f
                        previewLineHeight = 1.7f
                        previewLetterSpacing = 0f
                        previewFontWeight = "Bold"
                        hasAppliedFeedback = false
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("রিসেট (Reset)")
                }

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("বন্ধ করুন (Close)")
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
