package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.QuranData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

enum class JumpSubject(val title: String, val banglaTitle: String) {
    SURAH("Surah & Ayah", "সূরা ও আয়াত"),
    PAGE("Page", "পৃষ্ঠা"),
    JUZ("Juz", "পারা"),
    HIZB("Hizb", "হিযব"),
    RUKU("Ruku", "রুকু")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpToAyahBottomSheet(
    onDismiss: () -> Unit,
    onNavigateToSurah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var selectedSubject by remember { mutableStateOf(JumpSubject.SURAH) }

    // 1. Surah & Ayah state
    var selectedSurahNumber by remember { mutableStateOf(1) }
    var selectedAyahNumber by remember { mutableStateOf(1) }
    var surahSearchInput by remember { mutableStateOf("1") }
    var ayahInput by remember { mutableStateOf("1") }

    val currentSurah = remember(selectedSurahNumber) {
        QuranData.surahs.getOrNull(selectedSurahNumber - 1) ?: QuranData.surahs[0]
    }
    val maxAyahsInCurrentSurah = currentSurah.totalAyahs

    // 2. Page state
    var selectedPageNumber by remember { mutableStateOf(1) }
    var pageInput by remember { mutableStateOf("1") }
    val currentPageInfo = remember(selectedPageNumber) {
        QuranData.pageList.getOrNull(selectedPageNumber - 1) ?: QuranData.pageList[0]
    }

    // 3. Juz state
    var selectedJuzNumber by remember { mutableStateOf(1) }
    var juzInput by remember { mutableStateOf("1") }
    val currentJuzInfo = remember(selectedJuzNumber) {
        QuranData.juzList.getOrNull(selectedJuzNumber - 1) ?: QuranData.juzList[0]
    }

    // 4. Hizb state
    var selectedHizbNumber by remember { mutableStateOf(1) }
    var hizbInput by remember { mutableStateOf("1") }
    val currentHizbInfo = remember(selectedHizbNumber) {
        QuranData.hizbList.getOrNull(selectedHizbNumber - 1) ?: QuranData.hizbList[0]
    }

    // 5. Ruku state
    var selectedRukuNumber by remember { mutableStateOf(1) }
    var rukuInput by remember { mutableStateOf("1") }
    val currentRukuInfo = remember(selectedRukuNumber) {
        QuranData.rukuList.getOrNull(selectedRukuNumber - 1) ?: QuranData.rukuList[0]
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
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IslamicEmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Navigation",
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Jump to Destination",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                        )
                        Text(
                            text = "সূরা, আয়াত, পৃষ্ঠা, পারা বা রুকুতে সরাসরি যান",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subject Navigation Pills / Tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                JumpSubject.values().forEach { subject ->
                    val isSelected = selectedSubject == subject
                    Surface(
                        onClick = { selectedSubject = subject },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("jump_tab_${subject.name.lowercase()}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = subject.title,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                            Text(
                                text = subject.banglaTitle,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 9.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Content Box based on Selected Subject
            when (selectedSubject) {
                JumpSubject.SURAH -> {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // Section Banner
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${currentSurah.number}. ${currentSurah.englishName}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    )
                                    Text(
                                        text = "${currentSurah.banglaTranslation} • ${currentSurah.revelationType} • ${currentSurah.totalAyahs} আয়াত",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                Text(
                                    text = currentSurah.arabicName,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = IslamicEmeraldPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 22.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(14.dp))

                            // 1. Text Inputs (লিখার মাধ্যমে সিলেক্ট)
                            Text(
                                text = "লিখার মাধ্যমে নির্বাচন করুন (Type to Select)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = surahSearchInput,
                                    onValueChange = { input ->
                                        surahSearchInput = input
                                        val num = input.toIntOrNull()
                                        if (num != null && num in 1..114) {
                                            selectedSurahNumber = num
                                            if (selectedAyahNumber > currentSurah.totalAyahs) {
                                                selectedAyahNumber = 1
                                                ayahInput = "1"
                                            }
                                        } else {
                                            // Search by surah name
                                            val found = QuranData.surahs.find {
                                                it.englishName.contains(input, ignoreCase = true) ||
                                                it.banglaTranslation.contains(input, ignoreCase = true) ||
                                                it.arabicName.contains(input)
                                            }
                                            if (found != null) {
                                                selectedSurahNumber = found.number
                                                if (selectedAyahNumber > found.totalAyahs) {
                                                    selectedAyahNumber = 1
                                                    ayahInput = "1"
                                                }
                                            }
                                        }
                                    },
                                    label = { Text("সূরা নাম / নম্বর (১-১১৪)") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                                    modifier = Modifier
                                        .weight(1.3f)
                                        .testTag("jump_surah_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                OutlinedTextField(
                                    value = ayahInput,
                                    onValueChange = { input ->
                                        ayahInput = input
                                        val aNum = input.toIntOrNull()
                                        if (aNum != null && aNum in 1..maxAyahsInCurrentSurah) {
                                            selectedAyahNumber = aNum
                                        }
                                    },
                                    label = { Text("আয়াত (১-$maxAyahsInCurrentSurah)") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .testTag("jump_ayah_input"),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // 2. Sliders (স্লাইডারের মাধ্যমে সিলেক্ট)
                            Text(
                                text = "স্লাইডারের মাধ্যমে নির্বাচন করুন (Slider Selection)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Surah Slider (সূরা নির্বাচন):",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "$selectedSurahNumber / 114 (${currentSurah.englishName})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary
                                )
                            }

                            Slider(
                                value = selectedSurahNumber.toFloat(),
                                onValueChange = { value ->
                                    selectedSurahNumber = value.toInt().coerceIn(1, 114)
                                    surahSearchInput = selectedSurahNumber.toString()
                                    if (selectedAyahNumber > currentSurah.totalAyahs) {
                                        selectedAyahNumber = 1
                                        ayahInput = "1"
                                    }
                                },
                                valueRange = 1f..114f,
                                steps = 113,
                                colors = SliderDefaults.colors(
                                    thumbColor = IslamicEmeraldPrimary,
                                    activeTrackColor = IslamicEmeraldPrimary,
                                    inactiveTrackColor = IslamicEmeraldContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_surah_slider")
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Ayah Slider (আয়াত নির্বাচন):",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "Ayah $selectedAyahNumber of $maxAyahsInCurrentSurah",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary
                                )
                            }

                            Slider(
                                value = selectedAyahNumber.toFloat().coerceIn(1f, maxAyahsInCurrentSurah.toFloat()),
                                onValueChange = { value ->
                                    selectedAyahNumber = value.toInt().coerceIn(1, maxAyahsInCurrentSurah)
                                    ayahInput = selectedAyahNumber.toString()
                                },
                                valueRange = 1f..maxAyahsInCurrentSurah.toFloat(),
                                steps = if (maxAyahsInCurrentSurah > 1) maxAyahsInCurrentSurah - 1 else 0,
                                colors = SliderDefaults.colors(
                                    thumbColor = QuranGold,
                                    activeTrackColor = QuranGold,
                                    inactiveTrackColor = QuranGold.copy(alpha = 0.25f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_ayah_slider")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 3. Dedicated Action Button (আলাদা বাটন)
                            Button(
                                onClick = {
                                    onNavigateToSurah(selectedSurahNumber, selectedAyahNumber)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("go_to_surah_button")
                            ) {
                                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Go to Surah & Ayah (সূরা ও আয়াতে যান)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                JumpSubject.PAGE -> {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, QuranGold.copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Page $selectedPageNumber of 604",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    )
                                    Text(
                                        text = "Starts at: ${currentPageInfo.startSurahName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                Text(
                                    text = "صفحة $selectedPageNumber",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = QuranGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(14.dp))

                            // Text Input
                            Text(
                                text = "পৃষ্ঠা নম্বর লিখুন (Type Page Number):",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = pageInput,
                                onValueChange = { input ->
                                    pageInput = input
                                    val pNum = input.toIntOrNull()
                                    if (pNum != null && pNum in 1..604) {
                                        selectedPageNumber = pNum
                                    }
                                },
                                label = { Text("পৃষ্ঠা নম্বর (১ - ৬০৪)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_page_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "স্লাইডার দিয়ে পৃষ্ঠা নির্বাচন:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "পৃষ্ঠা $selectedPageNumber",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranGold
                                )
                            }

                            Slider(
                                value = selectedPageNumber.toFloat(),
                                onValueChange = { value ->
                                    selectedPageNumber = value.toInt().coerceIn(1, 604)
                                    pageInput = selectedPageNumber.toString()
                                },
                                valueRange = 1f..604f,
                                steps = 603,
                                colors = SliderDefaults.colors(
                                    thumbColor = QuranGold,
                                    activeTrackColor = QuranGold,
                                    inactiveTrackColor = QuranGold.copy(alpha = 0.25f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_page_slider")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Dedicated Button
                            Button(
                                onClick = {
                                    onNavigateToSurah(currentPageInfo.startSurah, currentPageInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = QuranGold),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("go_to_page_button")
                            ) {
                                Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Go to Page (পৃষ্ঠায় যান)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                JumpSubject.JUZ -> {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Juz $selectedJuzNumber (পারা $selectedJuzNumber)",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    )
                                    Text(
                                        text = "Starts: ${currentJuzInfo.startSurahName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                Text(
                                    text = currentJuzInfo.arabicName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = IslamicEmeraldPrimary,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.End
                                    ),
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(14.dp))

                            // Text Input
                            Text(
                                text = "পারা নম্বর বা নাম লিখুন (Type Juz Number):",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = juzInput,
                                onValueChange = { input ->
                                    juzInput = input
                                    val jNum = input.toIntOrNull()
                                    if (jNum != null && jNum in 1..30) {
                                        selectedJuzNumber = jNum
                                    } else {
                                        val found = QuranData.juzList.find {
                                            it.arabicName.contains(input) || it.startSurahName.contains(input, ignoreCase = true)
                                        }
                                        if (found != null) {
                                            selectedJuzNumber = found.number
                                        }
                                    }
                                },
                                label = { Text("পারা নম্বর (১ - ৩০)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_juz_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "স্লাইডার দিয়ে পারা নির্বাচন:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "পারা $selectedJuzNumber / 30",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary
                                )
                            }

                            Slider(
                                value = selectedJuzNumber.toFloat(),
                                onValueChange = { value ->
                                    selectedJuzNumber = value.toInt().coerceIn(1, 30)
                                    juzInput = selectedJuzNumber.toString()
                                },
                                valueRange = 1f..30f,
                                steps = 29,
                                colors = SliderDefaults.colors(
                                    thumbColor = IslamicEmeraldPrimary,
                                    activeTrackColor = IslamicEmeraldPrimary,
                                    inactiveTrackColor = IslamicEmeraldContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_juz_slider")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Dedicated Button
                            Button(
                                onClick = {
                                    onNavigateToSurah(currentJuzInfo.startSurah, currentJuzInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("go_to_juz_button")
                            ) {
                                Icon(imageVector = Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Go to Juz (পারায় যান)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                JumpSubject.HIZB -> {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${currentHizbInfo.quarter} Hizb ${currentHizbInfo.number}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    )
                                    Text(
                                        text = "Starts: ${currentHizbInfo.startSurahName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                Text(
                                    text = "حزب $selectedHizbNumber",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = IslamicEmeraldPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(14.dp))

                            // Text Input
                            Text(
                                text = "হিযব নম্বর লিখুন (Type Hizb Number):",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = hizbInput,
                                onValueChange = { input ->
                                    hizbInput = input
                                    val hNum = input.toIntOrNull()
                                    if (hNum != null && hNum in 1..60) {
                                        selectedHizbNumber = hNum
                                    }
                                },
                                label = { Text("হিযব নম্বর (১ - ৬০)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_hizb_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "স্লাইডার দিয়ে হিযব নির্বাচন:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "হিযব $selectedHizbNumber / 60",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary
                                )
                            }

                            Slider(
                                value = selectedHizbNumber.toFloat(),
                                onValueChange = { value ->
                                    selectedHizbNumber = value.toInt().coerceIn(1, 60)
                                    hizbInput = selectedHizbNumber.toString()
                                },
                                valueRange = 1f..60f,
                                steps = 59,
                                colors = SliderDefaults.colors(
                                    thumbColor = IslamicEmeraldPrimary,
                                    activeTrackColor = IslamicEmeraldPrimary,
                                    inactiveTrackColor = IslamicEmeraldContainer
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_hizb_slider")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Dedicated Button
                            Button(
                                onClick = {
                                    onNavigateToSurah(currentHizbInfo.startSurah, currentHizbInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("go_to_hizb_button")
                            ) {
                                Icon(imageVector = Icons.Default.Bookmarks, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Go to Hizb (হিযবে যান)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }

                JumpSubject.RUKU -> {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Ruku $selectedRukuNumber (রুকু $selectedRukuNumber)",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    )
                                    Text(
                                        text = "Starts: ${currentRukuInfo.startSurahName}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                                Text(
                                    text = "ركوع $selectedRukuNumber",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = QuranGold,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(14.dp))

                            // Text Input
                            Text(
                                text = "রুকু নম্বর লিখুন (Type Ruku Number):",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = rukuInput,
                                onValueChange = { input ->
                                    rukuInput = input
                                    val rNum = input.toIntOrNull()
                                    if (rNum != null && rNum in 1..556) {
                                        selectedRukuNumber = rNum
                                    }
                                },
                                label = { Text("রুকু নম্বর (১ - ৫৫৬)") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_ruku_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "স্লাইডার দিয়ে রুকু নির্বাচন:",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "রুকু $selectedRukuNumber / 556",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuranGold
                                )
                            }

                            Slider(
                                value = selectedRukuNumber.toFloat(),
                                onValueChange = { value ->
                                    selectedRukuNumber = value.toInt().coerceIn(1, 556)
                                    rukuInput = selectedRukuNumber.toString()
                                },
                                valueRange = 1f..556f,
                                steps = 555,
                                colors = SliderDefaults.colors(
                                    thumbColor = QuranGold,
                                    activeTrackColor = QuranGold,
                                    inactiveTrackColor = QuranGold.copy(alpha = 0.25f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("jump_ruku_slider")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Dedicated Button
                            Button(
                                onClick = {
                                    onNavigateToSurah(currentRukuInfo.startSurah, currentRukuInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = QuranGold),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .testTag("go_to_ruku_button")
                            ) {
                                Icon(imageVector = Icons.Default.FormatListNumbered, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Go to Ruku (রুকুতে যান)",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
