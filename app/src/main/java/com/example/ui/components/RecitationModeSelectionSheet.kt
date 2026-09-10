package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RecitationMode
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldDark
import com.example.ui.theme.QuranGoldLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Bottom sheet allowing users to toggle and customize recitation modes:
 * 1. Letter-by-Letter (বর্ণভিত্তিক / হরফ-বাই-হরফ)
 * 2. Word-by-Word (শব্দভিত্তিক / লফজ-বাই-লফজ)
 * 3. Ayah-by-Ayah (আয়াতভিত্তিক / আয়াত-বাই-আয়াত)
 * 4. Surah-by-Surah (সূরাভিত্তিক / পূর্ণ সূরা প্রবাহ)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecitationModeSelectionSheet(
    currentMode: RecitationMode,
    onSelectMode: (RecitationMode) -> Unit,
    wordPauseDurationMs: Long = 500L,
    onWordPauseDurationChange: (Long) -> Unit = {},
    letterPauseDurationMs: Long = 350L,
    onLetterPauseDurationChange: (Long) -> Unit = {},
    autoAdvanceSurah: Boolean = true,
    onAutoAdvanceSurahChange: (Boolean) -> Unit = {},
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var selectedMode by remember(currentMode) { mutableStateOf(currentMode) }
    var wordPause by remember(wordPauseDurationMs) { mutableLongStateOf(wordPauseDurationMs) }
    var letterPause by remember(letterPauseDurationMs) { mutableLongStateOf(letterPauseDurationMs) }
    var autoAdvance by remember(autoAdvanceSurah) { mutableStateOf(autoAdvanceSurah) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = null,
        modifier = Modifier.testTag("recitation_mode_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Sheet Header with Islamic Decorative Styling
            RecitationSheetHeader(onDismiss = onDismiss)

            // Segmented Top Tab Quick Switcher
            RecitationModeQuickTabs(
                selectedMode = selectedMode,
                onModeSelected = { selectedMode = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mode Cards List
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RecitationMode.values().forEach { mode ->
                    RecitationModeItemCard(
                        mode = mode,
                        isSelected = selectedMode == mode,
                        onClick = { selectedMode = mode }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Live Preview Simulation Box
            RecitationLiveDemonstrator(
                mode = selectedMode,
                wordPauseMs = wordPause,
                letterPauseMs = letterPause
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Fine-Tuning Controls for Selected Mode
            RecitationModeFineTuning(
                mode = selectedMode,
                wordPause = wordPause,
                onWordPauseChange = {
                    wordPause = it
                    onWordPauseDurationChange(it)
                },
                letterPause = letterPause,
                onLetterPauseChange = {
                    letterPause = it
                    onLetterPauseDurationChange(it)
                },
                autoAdvance = autoAdvance,
                onAutoAdvanceChange = {
                    autoAdvance = it
                    onAutoAdvanceSurahChange(it)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        onSelectMode(selectedMode)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("btn_apply_recitation_mode")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = QuranGoldLight,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "মোড সক্রিয় করুন (${selectedMode.banglaTitle.substringBefore(' ')})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

/**
 * Top Ornamental Header for the Recitation Sheet
 */
@Composable
private fun RecitationSheetHeader(onDismiss: () -> Unit) {
    Surface(
        color = IslamicEmeraldPrimary,
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(listOf(QuranGold, QuranGoldDark))
                        )
                        .border(1.dp, QuranGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.RecordVoiceOver,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Recitation Mode (তিলাওয়াত মোড)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "হরফ, শব্দ, আয়াত ও সূরা ভিত্তিক তিলাওয়াত বিন্যাস",
                        fontSize = 11.sp,
                        color = QuranGoldLight
                    )
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
                    .testTag("btn_close_recitation_sheet")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Top Quick Tabs for instant mode switching
 */
@Composable
private fun RecitationModeQuickTabs(
    selectedMode: RecitationMode,
    onModeSelected: (RecitationMode) -> Unit
) {
    val modes = RecitationMode.values()
    val selectedIndex = modes.indexOf(selectedMode)

    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        contentColor = IslamicEmeraldPrimary,
        indicator = { tabPositions ->
            if (selectedIndex in tabPositions.indices) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 3.dp,
                    color = IslamicEmeraldPrimary
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        modes.forEachIndexed { index, mode ->
            val isSelected = selectedMode == mode
            val shortLabel = when (mode) {
                RecitationMode.LETTER_BY_LETTER -> "হরফ"
                RecitationMode.WORD_BY_WORD -> "শব্দ"
                RecitationMode.AYAH_BY_AYAH -> "আয়াত"
                RecitationMode.SURAH_BY_SURAH -> "সূরা"
            }

            Tab(
                selected = isSelected,
                onClick = { onModeSelected(mode) },
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = getModeIcon(mode),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = shortLabel,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                modifier = Modifier.testTag("tab_recitation_${mode.id}")
            )
        }
    }
}

/**
 * Individual selectable card for each recitation mode
 */
@Composable
private fun RecitationModeItemCard(
    mode: RecitationMode,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
        label = "border_color"
    )
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) IslamicEmeraldContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface,
        label = "container_color"
    )

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_recitation_mode_${mode.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Mode Icon Badge
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getModeIcon(mode),
                            contentDescription = null,
                            tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = mode.banglaTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = mode.banglaSubtitle,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = IslamicEmeraldPrimary,
                        unselectedColor = MaterialTheme.colorScheme.outline
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Recommended Tag Chip
            Surface(
                color = if (isSelected) QuranGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(0.5.dp, if (isSelected) QuranGold else Color.Transparent)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "উপযুক্ত: ${mode.recommendedFor}",
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) QuranGoldDark else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(
                visible = isSelected,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    HorizontalDivider(
                        color = IslamicEmeraldPrimary.copy(alpha = 0.2f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = mode.banglaDescription,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Live Demonstration and Simulated Audio Visualizer for the selected mode
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RecitationLiveDemonstrator(
    mode: RecitationMode,
    wordPauseMs: Long,
    letterPauseMs: Long
) {
    var isSimulating by remember(mode) { mutableStateOf(false) }
    var activeStepIndex by remember(mode) { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .testTag("recitation_live_preview_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "লাইভ প্রিভিউ ও ডেমো (Interactive Sample)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }

                Surface(
                    onClick = {
                        if (isSimulating) {
                            isSimulating = false
                            activeStepIndex = 0
                        } else {
                            isSimulating = true
                            activeStepIndex = 0
                            scope.launch {
                                val totalSteps = when (mode) {
                                    RecitationMode.LETTER_BY_LETTER -> 8
                                    RecitationMode.WORD_BY_WORD -> 4
                                    RecitationMode.AYAH_BY_AYAH -> 2
                                    RecitationMode.SURAH_BY_SURAH -> 6
                                }
                                val delayTime = when (mode) {
                                    RecitationMode.LETTER_BY_LETTER -> letterPauseMs.coerceAtLeast(300L)
                                    RecitationMode.WORD_BY_WORD -> wordPauseMs.coerceAtLeast(500L)
                                    RecitationMode.AYAH_BY_AYAH -> 1200L
                                    RecitationMode.SURAH_BY_SURAH -> 400L
                                }
                                for (i in 0 until totalSteps) {
                                    if (!isSimulating) break
                                    activeStepIndex = i
                                    delay(delayTime)
                                }
                                isSimulating = false
                                activeStepIndex = 0
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSimulating) QuranGold else IslamicEmeraldPrimary,
                    contentColor = Color.White
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isSimulating) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isSimulating) "থামুন" else "প্রিভিউ শুনুন",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (mode) {
                RecitationMode.LETTER_BY_LETTER -> {
                    // Letter-by-letter visual breakdown of "بِسْمِ ٱللَّهِ"
                    val letters = listOf(
                        Triple("بِ", "Bi", "ঠোঁটের ভেজা অংশ (কাসরাহ)"),
                        Triple("سْ", "s", "শিস ধ্বনি (সাফির)"),
                        Triple("مِ", "mi", "দুই ঠোঁট মিলিত হয়ে"),
                        Triple("ٱ", "a", "হামযাতুল ওয়াসল"),
                        Triple("لَّ", "llā", "লাম মুফাখখাম (মোটা)"),
                        Triple("ـٰ", "a", "মাদ-এ আসলী"),
                        Triple("هِ", "hi", "হলকের শেষ ভাগ"),
                        Triple("۝১", "Ayah 1", "ওয়াকফে তাম")
                    )

                    Column {
                        Text(
                            text = "হরফ ও মাখরাজ বিশ্লেষণ (Phonetic Segmentation):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            letters.forEachIndexed { index, (letter, roman, makhraj) ->
                                val isHighlighted = isSimulating && activeStepIndex == index
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isHighlighted) QuranGold else IslamicEmeraldContainer.copy(alpha = 0.5f),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isHighlighted) QuranGoldDark else IslamicEmeraldPrimary.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .clickable { activeStepIndex = index }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = letter,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isHighlighted) Color.White else IslamicEmeraldPrimary
                                        )
                                        Text(
                                            text = roman,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = if (isHighlighted) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        // Makhraj tooltip for highlighted letter
                        val activeInfo = letters.getOrNull(activeStepIndex)
                        if (activeInfo != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(0.5.dp, IslamicEmeraldPrimary.copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        tint = IslamicEmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "হরফ '${activeInfo.first}': ${activeInfo.third}",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                RecitationMode.WORD_BY_WORD -> {
                    // Word-by-word visual breakdown
                    val words = listOf(
                        Pair("بِسْمِ", "নামে"),
                        Pair("ٱللَّهِ", "আল্লাহর"),
                        Pair("ٱلرَّحْمَٰنِ", "পরম দয়ালু"),
                        Pair("ٱلرَّحِيمِ", "অতি মেহেরবান")
                    )

                    Column {
                        Text(
                            text = "শব্দে শব্দে তিলাওয়াত ও অর্থ (Word Tiles Flow):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            words.forEachIndexed { index, (arabic, bangla) ->
                                val isWordActive = isSimulating && activeStepIndex == index
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isWordActive) IslamicEmeraldPrimary else IslamicEmeraldContainer.copy(alpha = 0.45f),
                                    border = BorderStroke(
                                        if (isWordActive) 1.5.dp else 0.5.dp,
                                        if (isWordActive) QuranGold else IslamicEmeraldPrimary.copy(alpha = 0.25f)
                                    ),
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .clickable { activeStepIndex = index }
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = arabic,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isWordActive) Color.White else IslamicEmeraldPrimary
                                        )
                                        Text(
                                            text = bangla,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isWordActive) QuranGoldLight else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                RecitationMode.AYAH_BY_AYAH -> {
                    Column {
                        Text(
                            text = "পূর্ণ আয়াত ও ওয়াকফ বিরতি (Verse & Pause Stream):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val ayahs = listOf(
                            "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ ۝১",
                            "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ ۝২"
                        )

                        ayahs.forEachIndexed { index, verseText ->
                            val isVersePlaying = isSimulating && activeStepIndex == index
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isVersePlaying) IslamicEmeraldContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    1.dp,
                                    if (isVersePlaying) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = if (isVersePlaying) "তিলাওয়াত হচ্ছে..." else "আয়াত ${index + 1}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isVersePlaying) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = verseText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isVersePlaying) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }

                RecitationMode.SURAH_BY_SURAH -> {
                    Column {
                        Text(
                            text = "অবিচ্ছিন্ন সূরা প্রবাহ (Continuous Audio Stream):",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = IslamicEmeraldDark,
                            contentColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    // Animated Wave Pulsing Visualizer
                                    repeat(7) { i ->
                                        val height = if (isSimulating) {
                                            (14 + (i * 5) % 18).dp
                                        } else {
                                            8.dp
                                        }
                                        Box(
                                            modifier = Modifier
                                                .width(4.dp)
                                                .height(height)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(if (isSimulating) QuranGold else Color.White.copy(alpha = 0.5f))
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "আয়াত থেকে আয়াতে কোনো থামাবাঁধা নেই — পূর্ণ সূরা একটানা প্রবাহিত হয়",
                                    fontSize = 11.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Mode Fine-Tuning Controls (Pacing, Intervals, and Auto-Advance)
 */
@Composable
private fun RecitationModeFineTuning(
    mode: RecitationMode,
    wordPause: Long,
    onWordPauseChange: (Long) -> Unit,
    letterPause: Long,
    onLetterPauseChange: (Long) -> Unit,
    autoAdvance: Boolean,
    onAutoAdvanceChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Tune,
                    contentDescription = null,
                    tint = IslamicEmeraldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "গতি ও বিরতি সমন্বয় (Pacing & Timing)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (mode) {
                RecitationMode.LETTER_BY_LETTER -> {
                    Text(
                        text = "হরফ বিরতি ব্যবধান (Letter Pause): ${letterPause}ms",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Slider(
                        value = letterPause.toFloat(),
                        onValueChange = { onLetterPauseChange(it.toLong()) },
                        valueRange = 200f..800f,
                        steps = 5,
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicEmeraldPrimary,
                            activeTrackColor = IslamicEmeraldPrimary
                        )
                    )
                }

                RecitationMode.WORD_BY_WORD -> {
                    Text(
                        text = "শব্দ বিরতি ব্যবধান (Word Pause): ${wordPause}ms",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Slider(
                        value = wordPause.toFloat(),
                        onValueChange = { onWordPauseChange(it.toLong()) },
                        valueRange = 300f..1200f,
                        steps = 8,
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicEmeraldPrimary,
                            activeTrackColor = IslamicEmeraldPrimary
                        )
                    )
                }

                RecitationMode.AYAH_BY_AYAH -> {
                    Text(
                        text = "আয়াত শেষে স্বাভাবিক ওয়াকফ বিরতি বজায় থাকে",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                RecitationMode.SURAH_BY_SURAH -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "পরবর্তী সূরায় স্বয়ংক্রিয় গমন",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "এক সূরা শেষ হলে স্বয়ংক্রিয়ভাবে পরের সূরা শুরু হবে",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = autoAdvance,
                            onCheckedChange = onAutoAdvanceChange,
                            colors = SwitchDefaults.colors(checkedThumbColor = IslamicEmeraldPrimary)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Helper to get distinctive icon for each RecitationMode
 */
fun getModeIcon(mode: RecitationMode): ImageVector {
    return when (mode) {
        RecitationMode.LETTER_BY_LETTER -> Icons.Default.Spellcheck
        RecitationMode.WORD_BY_WORD -> Icons.Default.Translate
        RecitationMode.AYAH_BY_AYAH -> Icons.Default.Audiotrack
        RecitationMode.SURAH_BY_SURAH -> Icons.Default.LibraryBooks
    }
}

/**
 * Compact embedded selector card suitable for MainSettingsSheet or QuickSettingsDrawer
 */
@Composable
fun RecitationModeQuickSelectorCard(
    currentMode: RecitationMode,
    onSelectMode: (RecitationMode) -> Unit,
    onOpenFullSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(IslamicEmeraldPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RecordVoiceOver,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "তিলাওয়াত মোড (Recitation Mode)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "বর্তমান: ${currentMode.banglaTitle}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IslamicEmeraldPrimary
                        )
                    }
                }

                Surface(
                    onClick = onOpenFullSettings,
                    shape = RoundedCornerShape(8.dp),
                    color = IslamicEmeraldContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "কনফিগার ⚙",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4-pill selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                RecitationMode.values().forEach { mode ->
                    val isSelected = currentMode == mode
                    val shortName = when (mode) {
                        RecitationMode.LETTER_BY_LETTER -> "হরফ"
                        RecitationMode.WORD_BY_WORD -> "শব্দ"
                        RecitationMode.AYAH_BY_AYAH -> "আয়াত"
                        RecitationMode.SURAH_BY_SURAH -> "সূরা"
                    }

                    Surface(
                        onClick = { onSelectMode(mode) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            1.dp,
                            if (isSelected) QuranGold else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("quick_mode_pill_${mode.id}")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                        ) {
                            Icon(
                                imageVector = getModeIcon(mode),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = shortName,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
