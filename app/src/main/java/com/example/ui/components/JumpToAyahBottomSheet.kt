package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.QuranData
import com.example.ui.theme.IslamicEmeraldPrimary
import kotlinx.coroutines.launch

enum class JumpSectionFilter(val label: String) {
    ALL("All"),
    SURAH_PAGE("Surah / Page"),
    JUZ("Juz"),
    HIZB("Hizb"),
    RUKU("Ruku")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpToAyahBottomSheet(
    onDismiss: () -> Unit,
    onNavigateToSurah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    var selectedFilter by remember { mutableStateOf(JumpSectionFilter.ALL) }

    // 1. Surah, Ayah, Page state
    var selectedSurahNumber by remember { mutableIntStateOf(1) }
    var selectedAyahNumber by remember { mutableIntStateOf(1) }
    var selectedPageNumber by remember { mutableIntStateOf(1) }

    var surahInput by remember { mutableStateOf("1") }
    var ayahInput by remember { mutableStateOf("1") }
    var pageInput by remember { mutableStateOf("1") }

    val currentSurah = remember(selectedSurahNumber) {
        QuranData.surahs.getOrNull(selectedSurahNumber - 1) ?: QuranData.surahs[0]
    }
    val maxAyahs = currentSurah.totalAyahs

    // 2. Juz state
    var selectedJuzNumber by remember { mutableIntStateOf(1) }
    var juzSearchInput by remember { mutableStateOf("") }
    var juzNoInput by remember { mutableStateOf("1") }

    val currentJuzInfo = remember(selectedJuzNumber) {
        QuranData.juzList.getOrNull(selectedJuzNumber - 1) ?: QuranData.juzList[0]
    }

    // 3. Hizb state
    var selectedHizbNumber by remember { mutableIntStateOf(1) }
    var hizbSearchInput by remember { mutableStateOf("") }
    var hizbNoInput by remember { mutableStateOf("1") }

    val currentHizbInfo = remember(selectedHizbNumber) {
        QuranData.hizbList.getOrNull(selectedHizbNumber - 1) ?: QuranData.hizbList[0]
    }

    // 4. Ruku state
    var selectedRukuNumber by remember { mutableIntStateOf(1) }
    var rukuSearchInput by remember { mutableStateOf("") }
    var rukuNoInput by remember { mutableStateOf("1") }

    val currentRukuInfo = remember(selectedRukuNumber) {
        QuranData.rukuList.getOrNull(selectedRukuNumber - 1) ?: QuranData.rukuList[0]
    }

    // Lazy list states for sliders/wheels
    val surahListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val ayahListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val pageListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val juzListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val hizbListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val rukuListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    // Sync scroll when selection changes
    LaunchedEffect(selectedSurahNumber) {
        val targetIdx = (selectedSurahNumber - 1).coerceIn(0, QuranData.surahs.size - 1)
        surahListState.animateScrollToItem((targetIdx - 1).coerceAtLeast(0))
    }
    LaunchedEffect(selectedAyahNumber) {
        val targetIdx = (selectedAyahNumber - 1).coerceIn(0, maxAyahs - 1)
        ayahListState.animateScrollToItem((targetIdx - 1).coerceAtLeast(0))
    }
    LaunchedEffect(selectedPageNumber) {
        val targetIdx = (selectedPageNumber - 1).coerceIn(0, 603)
        pageListState.animateScrollToItem((targetIdx - 1).coerceAtLeast(0))
    }
    LaunchedEffect(selectedJuzNumber) {
        val targetIdx = (selectedJuzNumber - 1).coerceIn(0, QuranData.juzList.size - 1)
        juzListState.animateScrollToItem((targetIdx - 1).coerceAtLeast(0))
    }
    LaunchedEffect(selectedHizbNumber) {
        val targetIdx = (selectedHizbNumber - 1).coerceIn(0, QuranData.hizbList.size - 1)
        hizbListState.animateScrollToItem((targetIdx - 1).coerceAtLeast(0))
    }
    LaunchedEffect(selectedRukuNumber) {
        val targetIdx = (selectedRukuNumber - 1).coerceIn(0, QuranData.rukuList.size - 1)
        rukuListState.animateScrollToItem((targetIdx - 1).coerceAtLeast(0))
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
                .padding(horizontal = 16.dp)
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
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(IslamicEmeraldPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = "Jump",
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Jump to Ayah / Destination",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary,
                                fontSize = 16.sp
                            )
                        )
                        Text(
                            text = "সূরা, আয়াত, পৃষ্ঠা, পারা বা রুকুতে সরাসরি যান",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Filter Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                JumpSectionFilter.values().forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        onClick = { selectedFilter = filter },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier.padding(vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filter.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 10.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =========================================================================
            // SECTION 1: Surah, Ayah, Page (Matching Image 1 & 2 Blueprint)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.ALL || selectedFilter == JumpSectionFilter.SURAH_PAGE) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Section Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Surah / Ayah / Page",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicEmeraldPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${currentSurah.englishName} : $selectedAyahNumber)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            Text(
                                text = currentSurah.arabicName,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Row of 3 Input Boxes: [ Surah ] [ Ayah ] [ Page ]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Surah Input
                            OutlinedTextField(
                                value = surahInput,
                                onValueChange = { input ->
                                    surahInput = input
                                    val num = input.toIntOrNull()
                                    if (num != null && num in 1..114) {
                                        selectedSurahNumber = num
                                        if (selectedAyahNumber > QuranData.surahs[num - 1].totalAyahs) {
                                            selectedAyahNumber = 1
                                            ayahInput = "1"
                                        }
                                        selectedPageNumber = QuranData.surahs[num - 1].startPage
                                        pageInput = selectedPageNumber.toString()
                                    } else {
                                        val match = QuranData.surahs.find {
                                            it.englishName.contains(input, ignoreCase = true) ||
                                            it.banglaTranslation.contains(input, ignoreCase = true) ||
                                            it.arabicName.contains(input)
                                        }
                                        if (match != null) {
                                            selectedSurahNumber = match.number
                                            if (selectedAyahNumber > match.totalAyahs) {
                                                selectedAyahNumber = 1
                                                ayahInput = "1"
                                            }
                                            selectedPageNumber = match.startPage
                                            pageInput = selectedPageNumber.toString()
                                        }
                                    }
                                },
                                placeholder = { Text("|A...", fontSize = 11.sp) },
                                label = { Text("Surah", fontSize = 10.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1.3f)
                                    .testTag("input_surah")
                            )

                            // Ayah Input
                            OutlinedTextField(
                                value = ayahInput,
                                onValueChange = { input ->
                                    ayahInput = input
                                    val aNum = input.toIntOrNull()
                                    if (aNum != null && aNum in 1..maxAyahs) {
                                        selectedAyahNumber = aNum
                                    }
                                },
                                placeholder = { Text("1..2", fontSize = 11.sp) },
                                label = { Text("Ayah", fontSize = 10.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_ayah")
                            )

                            // Page Input
                            OutlinedTextField(
                                value = pageInput,
                                onValueChange = { input ->
                                    pageInput = input
                                    val pNum = input.toIntOrNull()
                                    if (pNum != null && pNum in 1..604) {
                                        selectedPageNumber = pNum
                                    }
                                },
                                placeholder = { Text("1..2", fontSize = 11.sp) },
                                label = { Text("Page", fontSize = 10.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_page")
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 3-Column Scrollable Wheel / Picker View
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Column 1: Surah Name List (Arabic & English)
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(1.4f)
                                    .fillMaxHeight()
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Surah Name",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicEmeraldPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    LazyColumn(
                                        state = surahListState,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        itemsIndexed(QuranData.surahs) { idx, surah ->
                                            val isSelected = surah.number == selectedSurahNumber
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedSurahNumber = surah.number
                                                        surahInput = surah.number.toString()
                                                        if (selectedAyahNumber > surah.totalAyahs) {
                                                            selectedAyahNumber = 1
                                                            ayahInput = "1"
                                                        }
                                                        selectedPageNumber = surah.startPage
                                                        pageInput = surah.startPage.toString()
                                                    }
                                                    .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                    .then(
                                                        if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                        else Modifier
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 5.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = "${surah.number}. ${surah.englishName}",
                                                        fontSize = 11.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface,
                                                        maxLines = 1,
                                                        overflow = TextOverflow.Ellipsis,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Text(
                                                        text = surah.arabicName,
                                                        fontSize = 11.sp,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Column 2: Ayah Number List
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .fillMaxHeight()
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Ayah",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicEmeraldPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    LazyColumn(
                                        state = ayahListState,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        items(maxAyahs) { i ->
                                            val aNum = i + 1
                                            val isSelected = aNum == selectedAyahNumber
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedAyahNumber = aNum
                                                        ayahInput = aNum.toString()
                                                    }
                                                    .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                    .then(
                                                        if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                        else Modifier
                                                    )
                                                    .padding(vertical = 5.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$aNum",
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Column 3: Page Number List
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .fillMaxHeight()
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        text = "Page",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicEmeraldPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    LazyColumn(
                                        state = pageListState,
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        items(604) { p ->
                                            val pNum = p + 1
                                            val isSelected = pNum == selectedPageNumber
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        selectedPageNumber = pNum
                                                        pageInput = pNum.toString()
                                                    }
                                                    .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                    .then(
                                                        if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                        else Modifier
                                                    )
                                                    .padding(vertical = 5.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "$pNum",
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Two Action Buttons: [ Go to Surah ] [ Go to Page ]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    onNavigateToSurah(selectedSurahNumber, selectedAyahNumber)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("button_go_to_surah")
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Go to Surah", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    val pageInfo = QuranData.pageList.getOrNull(selectedPageNumber - 1) ?: QuranData.pageList[0]
                                    onNavigateToSurah(pageInfo.startSurah, pageInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .testTag("button_go_to_page")
                            ) {
                                Icon(Icons.Default.AutoStories, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Go to Page", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION 2: Juz Section (Matching Image 1 & 2 Blueprint)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.ALL || selectedFilter == JumpSectionFilter.JUZ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Juz (পারা)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Top Row: [ Juz ] [ No. ] [ Go to Juz ]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = juzSearchInput,
                                onValueChange = { input ->
                                    juzSearchInput = input
                                    val match = QuranData.juzList.find {
                                        it.arabicName.contains(input) || it.startSurahName.contains(input, ignoreCase = true)
                                    }
                                    if (match != null) {
                                        selectedJuzNumber = match.number
                                        juzNoInput = match.number.toString()
                                    }
                                },
                                placeholder = { Text("Juz", fontSize = 11.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("input_juz_name")
                            )

                            OutlinedTextField(
                                value = juzNoInput,
                                onValueChange = { input ->
                                    juzNoInput = input
                                    val jNum = input.toIntOrNull()
                                    if (jNum != null && jNum in 1..30) {
                                        selectedJuzNumber = jNum
                                    }
                                },
                                placeholder = { Text("1..2", fontSize = 11.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .testTag("input_juz_no")
                            )

                            Button(
                                onClick = {
                                    onNavigateToSurah(currentJuzInfo.startSurah, currentJuzInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                contentPadding = PaddingValues(horizontal = 14.dp),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("button_go_to_juz")
                            ) {
                                Text("Go to Juz", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2-Column Scrollable Picker: [ Arabic Snippet ] [ Juz Number ]
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Column 1: Arabic Snippet
                                LazyColumn(
                                    state = juzListState,
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                ) {
                                    itemsIndexed(QuranData.juzList) { idx, juz ->
                                        val isSelected = juz.number == selectedJuzNumber
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedJuzNumber = juz.number
                                                    juzNoInput = juz.number.toString()
                                                    juzSearchInput = juz.startSurahName
                                                }
                                                .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                .then(
                                                    if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                    else Modifier
                                                )
                                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = juz.arabicName,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = juz.startSurahName,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                                // Column 2: Juz Number
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .fillMaxHeight()
                                ) {
                                    items(30) { j ->
                                        val jNum = j + 1
                                        val isSelected = jNum == selectedJuzNumber
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedJuzNumber = jNum
                                                    juzNoInput = jNum.toString()
                                                }
                                                .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                .then(
                                                    if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                    else Modifier
                                                )
                                                .padding(vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Juz $jNum",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION 3: Hizb Section (Matching Image 1 & 2 Blueprint)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.ALL || selectedFilter == JumpSectionFilter.HIZB) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Hizb (হিযব)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Top Row: [ Hizb ] [ No. ] [ Go to Hizb ]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = hizbSearchInput,
                                onValueChange = { input ->
                                    hizbSearchInput = input
                                    val match = QuranData.hizbList.find {
                                        it.arabicName.contains(input) || it.startSurahName.contains(input, ignoreCase = true)
                                    }
                                    if (match != null) {
                                        selectedHizbNumber = match.number
                                        hizbNoInput = match.number.toString()
                                    }
                                },
                                placeholder = { Text("Hizb", fontSize = 11.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("input_hizb_name")
                            )

                            OutlinedTextField(
                                value = hizbNoInput,
                                onValueChange = { input ->
                                    hizbNoInput = input
                                    val hNum = input.toIntOrNull()
                                    if (hNum != null && hNum in 1..60) {
                                        selectedHizbNumber = hNum
                                    }
                                },
                                placeholder = { Text("1..2", fontSize = 11.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .testTag("input_hizb_no")
                            )

                            Button(
                                onClick = {
                                    onNavigateToSurah(currentHizbInfo.startSurah, currentHizbInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                contentPadding = PaddingValues(horizontal = 14.dp),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("button_go_to_hizb")
                            ) {
                                Text("Go to Hizb", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2-Column Scrollable Picker: [ Arabic Snippet ] [ Hizb Number ]
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Column 1: Arabic Snippet
                                LazyColumn(
                                    state = hizbListState,
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                ) {
                                    itemsIndexed(QuranData.hizbList) { idx, hizb ->
                                        val isSelected = hizb.number == selectedHizbNumber
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedHizbNumber = hizb.number
                                                    hizbNoInput = hizb.number.toString()
                                                    hizbSearchInput = hizb.startSurahName
                                                }
                                                .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                .then(
                                                    if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                    else Modifier
                                                )
                                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = hizb.arabicName,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "${hizb.startSurahName} (${hizb.quarter})",
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                                // Column 2: Hizb Number
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .fillMaxHeight()
                                ) {
                                    items(QuranData.hizbList.size) { h ->
                                        val hItem = QuranData.hizbList[h]
                                        val isSelected = hItem.number == selectedHizbNumber
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedHizbNumber = hItem.number
                                                    hizbNoInput = hItem.number.toString()
                                                }
                                                .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                .then(
                                                    if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                    else Modifier
                                                )
                                                .padding(vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Hizb ${hItem.number}",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION 4: Ruku Section (Matching Image 1 & 2 Blueprint)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.ALL || selectedFilter == JumpSectionFilter.RUKU) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Ruku (রুকু)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Top Row: [ Ruku ] [ No. ] [ Go to Ruku ]
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = rukuSearchInput,
                                onValueChange = { input ->
                                    rukuSearchInput = input
                                    val match = QuranData.rukuList.find {
                                        it.arabicSnippet.contains(input) || it.startSurahName.contains(input, ignoreCase = true)
                                    }
                                    if (match != null) {
                                        selectedRukuNumber = match.rukuNumber
                                        rukuNoInput = match.rukuNumber.toString()
                                    }
                                },
                                placeholder = { Text("Ruku", fontSize = 11.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .testTag("input_ruku_name")
                            )

                            OutlinedTextField(
                                value = rukuNoInput,
                                onValueChange = { input ->
                                    rukuNoInput = input
                                    val rNum = input.toIntOrNull()
                                    if (rNum != null && rNum in 1..556) {
                                        selectedRukuNumber = rNum
                                    }
                                },
                                placeholder = { Text("1..2", fontSize = 11.sp) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(0.8f)
                                    .testTag("input_ruku_no")
                            )

                            Button(
                                onClick = {
                                    onNavigateToSurah(currentRukuInfo.startSurah, currentRukuInfo.startAyah)
                                    onDismiss()
                                },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                contentPadding = PaddingValues(horizontal = 14.dp),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("button_go_to_ruku")
                            ) {
                                Text("Go to Ruku", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2-Column Scrollable Picker: [ Arabic Snippet ] [ Ruku Number ]
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Column 1: Arabic Snippet
                                LazyColumn(
                                    state = rukuListState,
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                ) {
                                    itemsIndexed(QuranData.rukuList) { idx, ruku ->
                                        val isSelected = ruku.rukuNumber == selectedRukuNumber
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedRukuNumber = ruku.rukuNumber
                                                    rukuNoInput = ruku.rukuNumber.toString()
                                                    rukuSearchInput = ruku.startSurahName
                                                }
                                                .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                .then(
                                                    if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                    else Modifier
                                                )
                                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = ruku.arabicSnippet,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = ruku.startSurahName,
                                                    fontSize = 10.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                                // Column 2: Ruku Number
                                LazyColumn(
                                    modifier = Modifier
                                        .weight(0.8f)
                                        .fillMaxHeight()
                                ) {
                                    items(QuranData.rukuList.size) { r ->
                                        val rNum = r + 1
                                        val isSelected = rNum == selectedRukuNumber
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedRukuNumber = rNum
                                                    rukuNoInput = rNum.toString()
                                                }
                                                .background(if (isSelected) IslamicEmeraldPrimary.copy(alpha = 0.14f) else Color.Transparent)
                                                .then(
                                                    if (isSelected) Modifier.border(1.dp, IslamicEmeraldPrimary, RoundedCornerShape(4.dp))
                                                    else Modifier
                                                )
                                                .padding(vertical = 6.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Ruku $rNum",
                                                fontSize = 12.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // Explanatory note matching Image 2
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info",
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "এই খালি box গুলোতে তাদের নির্দিষ্ট name কিংবা number লিখেও ইউজার তাদের গন্তব্যে যেতে পারবেন। slider গুলোর মাধ্যমেও তাদের নির্দিষ্ট name কিংবা number উপরে নিচে slid করে ইউজার তাদের গন্তব্যে যেতে পারবেন।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.5.sp,
                            lineHeight = 15.sp
                        )
                    )
                }
            }
        }
    }
}
