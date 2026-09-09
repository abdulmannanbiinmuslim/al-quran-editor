package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.QuranData
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

enum class JumpSectionFilter(val label: String) {
    ALL("All"),
    SURAH_PAGE("Surah / Page"),
    JUZ("Juz"),
    HIZB("Hizb"),
    RUKU("Ruku")
}

/**
 * Reusable Sticky Wheel Picker with a fixed central sticky highlight band,
 * smooth snapping fling behavior, and subtle top/bottom fade mask.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> StickyWheelPicker(
    items: List<T>,
    selectedIndex: Int,
    onItemSelected: (index: Int, item: T) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = selectedIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0))
    ),
    itemHeight: Dp = 36.dp,
    visibleCount: Int = 5,
    itemContent: @Composable (item: T, isSelected: Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val padHeight = itemHeight * ((visibleCount - 1) / 2) // 36 * 2 = 72.dp

    // Sync scroll when selectedIndex changes externally
    LaunchedEffect(selectedIndex) {
        if (selectedIndex in items.indices && listState.firstVisibleItemIndex != selectedIndex && !listState.isScrollInProgress) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

    // Detect when user scrolls item into sticky center
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .distinctUntilChanged()
            .collect { index ->
                if (index in items.indices && index != selectedIndex) {
                    onItemSelected(index, items[index])
                }
            }
    }

    Box(
        modifier = modifier
            .height(itemHeight * visibleCount)
    ) {
        // Sticky Center Line / Band: Aligned in the exact vertical center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .align(Alignment.Center)
                .padding(horizontal = 2.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(IslamicEmeraldPrimary.copy(alpha = 0.12f))
                .border(1.2.dp, IslamicEmeraldPrimary.copy(alpha = 0.55f), RoundedCornerShape(6.dp))
        )

        // Snapping scroll list
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = padHeight),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { idx, item ->
                val isSelected = idx == selectedIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight)
                        .clickable {
                            coroutineScope.launch {
                                listState.animateScrollToItem(idx)
                            }
                            onItemSelected(idx, item)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    itemContent(item, isSelected)
                }
            }
        }

        // Top subtle gradient fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(padHeight)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.05f)
                        )
                    )
                )
        )

        // Bottom subtle gradient fade
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(padHeight)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        )
    }
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

    // Direct number quick jump inputs for the ALL tab (as demonstrated in user video)
    var allSurahInput by remember { mutableStateOf("1") }
    var allAyahInput by remember { mutableStateOf("1") }
    var allPageInput by remember { mutableStateOf("1") }
    var allJuzInput by remember { mutableStateOf("1") }
    var allHizbInput by remember { mutableStateOf("1") }
    var allRukuInput by remember { mutableStateOf("1") }

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
    val juzNoListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val hizbListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val hizbNoListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val rukuListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)
    val rukuNoListState = rememberLazyListState(initialFirstVisibleItemIndex = 0)

    // Sync scroll when Juz selection changes
    LaunchedEffect(selectedJuzNumber) {
        val targetIdx = (selectedJuzNumber - 1).coerceIn(0, QuranData.juzList.size - 1)
        if (juzListState.firstVisibleItemIndex != targetIdx && !juzListState.isScrollInProgress) {
            juzListState.animateScrollToItem(targetIdx)
        }
        if (juzNoListState.firstVisibleItemIndex != targetIdx && !juzNoListState.isScrollInProgress) {
            juzNoListState.animateScrollToItem(targetIdx)
        }
    }

    // Sync scroll when Hizb selection changes
    LaunchedEffect(selectedHizbNumber) {
        val targetIdx = (selectedHizbNumber - 1).coerceIn(0, QuranData.hizbList.size - 1)
        if (hizbListState.firstVisibleItemIndex != targetIdx && !hizbListState.isScrollInProgress) {
            hizbListState.animateScrollToItem(targetIdx)
        }
        if (hizbNoListState.firstVisibleItemIndex != targetIdx && !hizbNoListState.isScrollInProgress) {
            hizbNoListState.animateScrollToItem(targetIdx)
        }
    }

    // Sync scroll when Ruku selection changes
    LaunchedEffect(selectedRukuNumber) {
        val targetIdx = (selectedRukuNumber - 1).coerceIn(0, QuranData.rukuList.size - 1)
        if (rukuListState.firstVisibleItemIndex != targetIdx && !rukuListState.isScrollInProgress) {
            rukuListState.animateScrollToItem(targetIdx)
        }
        if (rukuNoListState.firstVisibleItemIndex != targetIdx && !rukuNoListState.isScrollInProgress) {
            rukuNoListState.animateScrollToItem(targetIdx)
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
                            text = "সূরা, আয়াত, পৃষ্ঠা, পারা, হিযব বা রুকুতে সরাসরি যান",
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

            // Quick Filter Pills: All, Surah / Page, Juz, Hizb, Ruku
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
            // TAB: ALL (Compact Direct-Number Quick Jump Layout as shown in user video)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.ALL) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 1. Surah & Ayah
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Surah & Ayah",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary,
                                    fontSize = 14.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = allSurahInput,
                                    onValueChange = { allSurahInput = it },
                                    label = { Text("Surah (1-114)", fontSize = 10.5.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1.1f)
                                        .testTag("all_input_surah")
                                )

                                OutlinedTextField(
                                    value = allAyahInput,
                                    onValueChange = { allAyahInput = it },
                                    label = { Text("Ayah", fontSize = 10.5.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .testTag("all_input_ayah")
                                )

                                Button(
                                    onClick = {
                                        val sNum = (allSurahInput.toIntOrNull() ?: 1).coerceIn(1, 114)
                                        val totalAyahs = QuranData.surahs[sNum - 1].totalAyahs
                                        val aNum = (allAyahInput.toIntOrNull() ?: 1).coerceIn(1, totalAyahs)
                                        onNavigateToSurah(sNum, aNum)
                                        onDismiss()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                    contentPadding = PaddingValues(horizontal = 14.dp),
                                    modifier = Modifier
                                        .height(52.dp)
                                        .testTag("all_button_go_to_surah")
                                ) {
                                    Text("Go to Surah", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                                }
                            }
                        }
                    }

                    // 2. Page (1-604)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = allPageInput,
                                    onValueChange = { allPageInput = it },
                                    label = { Text("Page (1-604)", fontSize = 10.5.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("all_input_page")
                                )

                                Button(
                                    onClick = {
                                        val pNum = (allPageInput.toIntOrNull() ?: 1).coerceIn(1, 604)
                                        val pageInfo = QuranData.pageList.getOrNull(pNum - 1) ?: QuranData.pageList[0]
                                        onNavigateToSurah(pageInfo.startSurah, pageInfo.startAyah)
                                        onDismiss()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    modifier = Modifier
                                        .height(52.dp)
                                        .testTag("all_button_go_to_page")
                                ) {
                                    Text("Go to Page", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                                }
                            }
                        }
                    }

                    // 3. Juz (1-30)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = allJuzInput,
                                    onValueChange = { allJuzInput = it },
                                    label = { Text("Juz (1-30)", fontSize = 10.5.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("all_input_juz")
                                )

                                Button(
                                    onClick = {
                                        val jNum = (allJuzInput.toIntOrNull() ?: 1).coerceIn(1, 30)
                                        val juzInfo = QuranData.juzList.getOrNull(jNum - 1) ?: QuranData.juzList[0]
                                        onNavigateToSurah(juzInfo.startSurah, juzInfo.startAyah)
                                        onDismiss()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                    contentPadding = PaddingValues(horizontal = 18.dp),
                                    modifier = Modifier
                                        .height(52.dp)
                                        .testTag("all_button_go_to_juz")
                                ) {
                                    Text("Go to Juz", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                                }
                            }
                        }
                    }

                    // 4. Hizb (1-60)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = allHizbInput,
                                    onValueChange = { allHizbInput = it },
                                    label = { Text("Hizb (1-60)", fontSize = 10.5.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("all_input_hizb")
                                )

                                Button(
                                    onClick = {
                                        val hNum = (allHizbInput.toIntOrNull() ?: 1).coerceIn(1, 60)
                                        val hizbInfo = QuranData.hizbList.firstOrNull { it.number == hNum } ?: QuranData.hizbList[0]
                                        onNavigateToSurah(hizbInfo.startSurah, hizbInfo.startAyah)
                                        onDismiss()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    modifier = Modifier
                                        .height(52.dp)
                                        .testTag("all_button_go_to_hizb")
                                ) {
                                    Text("Go to Hizb", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                                }
                            }
                        }
                    }

                    // 5. Ruku (1-556)
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = allRukuInput,
                                    onValueChange = { allRukuInput = it },
                                    label = { Text("Ruku (1-556)", fontSize = 10.5.sp) },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("all_input_ruku")
                                )

                                Button(
                                    onClick = {
                                        val rNum = (allRukuInput.toIntOrNull() ?: 1).coerceIn(1, 556)
                                        val rukuInfo = QuranData.rukuList.getOrNull(rNum - 1) ?: QuranData.rukuList[0]
                                        onNavigateToSurah(rukuInfo.startSurah, rukuInfo.startAyah)
                                        onDismiss()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    modifier = Modifier
                                        .height(52.dp)
                                        .testTag("all_button_go_to_ruku")
                                ) {
                                    Text("Go to Ruku", fontWeight = FontWeight.Bold, fontSize = 12.5.sp)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION 1: Surah, Ayah, Page (3-Column Sticky Wheel Slider)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.SURAH_PAGE) {
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
                                        val surahAtPage = QuranData.surahs.findLast { it.startPage <= pNum } ?: QuranData.surahs[0]
                                        if (selectedSurahNumber != surahAtPage.number) {
                                            selectedSurahNumber = surahAtPage.number
                                            surahInput = surahAtPage.number.toString()
                                            if (selectedAyahNumber > surahAtPage.totalAyahs) {
                                                selectedAyahNumber = 1
                                                ayahInput = "1"
                                            }
                                        }
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

                        // 3-Column Wheel Slider with Central Sticky Indicator
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            // Column 1: Surah Name Sticky Picker
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(1.5f)
                                    .fillMaxHeight()
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Surah Name",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                        Text(
                                            text = "اسم السورة",
                                            fontSize = 10.sp,
                                            color = IslamicEmeraldPrimary.copy(alpha = 0.8f)
                                        )
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    StickyWheelPicker(
                                        items = QuranData.surahs,
                                        selectedIndex = (selectedSurahNumber - 1).coerceIn(0, QuranData.surahs.size - 1),
                                        listState = surahListState,
                                        onItemSelected = { idx, surah ->
                                            selectedSurahNumber = surah.number
                                            surahInput = surah.number.toString()
                                            if (selectedAyahNumber > surah.totalAyahs) {
                                                selectedAyahNumber = 1
                                                ayahInput = "1"
                                            }
                                            selectedPageNumber = surah.startPage
                                            pageInput = surah.startPage.toString()
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) { surah, isSelected ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 6.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${surah.number}. ${surah.englishName}",
                                                fontSize = if (isSelected) 12.sp else 10.5.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = surah.arabicName,
                                                fontSize = if (isSelected) 12.5.sp else 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.55f)
                                            )
                                        }
                                    }
                                }
                            }

                            // Column 2: Ayah Sticky Picker
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(0.75f)
                                    .fillMaxHeight()
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Ayah",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    val ayahsList = remember(maxAyahs) { (1..maxAyahs).toList() }
                                    StickyWheelPicker(
                                        items = ayahsList,
                                        selectedIndex = (selectedAyahNumber - 1).coerceIn(0, (maxAyahs - 1).coerceAtLeast(0)),
                                        listState = ayahListState,
                                        onItemSelected = { idx, ayahNum ->
                                            selectedAyahNumber = ayahNum
                                            ayahInput = ayahNum.toString()
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) { ayahNum, isSelected ->
                                        Text(
                                            text = "$ayahNum",
                                            fontSize = if (isSelected) 14.sp else 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }

                            // Column 3: Page Sticky Picker
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .weight(0.75f)
                                    .fillMaxHeight()
                            ) {
                                Column(modifier = Modifier.fillMaxSize()) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Page",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                    }
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                                    val pagesList = remember { (1..604).toList() }
                                    StickyWheelPicker(
                                        items = pagesList,
                                        selectedIndex = (selectedPageNumber - 1).coerceIn(0, 603),
                                        listState = pageListState,
                                        onItemSelected = { idx, pageNum ->
                                            selectedPageNumber = pageNum
                                            pageInput = pageNum.toString()
                                            val surahAtPage = QuranData.surahs.findLast { it.startPage <= pageNum } ?: QuranData.surahs[0]
                                            if (selectedSurahNumber != surahAtPage.number) {
                                                selectedSurahNumber = surahAtPage.number
                                                surahInput = surahAtPage.number.toString()
                                                if (selectedAyahNumber > surahAtPage.totalAyahs) {
                                                    selectedAyahNumber = 1
                                                    ayahInput = "1"
                                                }
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) { pageNum, isSelected ->
                                        Text(
                                            text = "$pageNum",
                                            fontSize = if (isSelected) 14.sp else 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                        )
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
            // SECTION 2: Juz Section (2-Column Sticky Wheel Slider)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.JUZ) {
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
                                        juzSearchInput = QuranData.juzList[jNum - 1].startSurahName
                                    }
                                },
                                placeholder = { Text("1..30", fontSize = 11.sp) },
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

                        // 2-Column Sticky Wheel Slider: [ Arabic Snippet ] [ Juz Number ]
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Column 1: Arabic Snippet & Surah Name
                                StickyWheelPicker(
                                    items = QuranData.juzList,
                                    selectedIndex = (selectedJuzNumber - 1).coerceIn(0, QuranData.juzList.size - 1),
                                    listState = juzListState,
                                    onItemSelected = { idx, juz ->
                                        selectedJuzNumber = juz.number
                                        juzNoInput = juz.number.toString()
                                        juzSearchInput = juz.startSurahName
                                    },
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                ) { juz, isSelected ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = juz.arabicName,
                                            fontSize = if (isSelected) 13.sp else 11.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = juz.startSurahName,
                                            fontSize = 10.sp,
                                            color = if (isSelected) QuranGold else Color.Gray
                                        )
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                                // Column 2: Juz Number (1..30)
                                val juzNumbers = remember { (1..30).toList() }
                                StickyWheelPicker(
                                    items = juzNumbers,
                                    selectedIndex = (selectedJuzNumber - 1).coerceIn(0, 29),
                                    listState = juzNoListState,
                                    onItemSelected = { idx, jNum ->
                                        selectedJuzNumber = jNum
                                        juzNoInput = jNum.toString()
                                        juzSearchInput = QuranData.juzList[jNum - 1].startSurahName
                                    },
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .fillMaxHeight()
                                ) { jNum, isSelected ->
                                    Text(
                                        text = "Juz $jNum",
                                        fontSize = if (isSelected) 13.sp else 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION 3: Hizb Section (2-Column Sticky Wheel Slider)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.HIZB) {
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
                                        hizbSearchInput = QuranData.hizbList[hNum - 1].startSurahName
                                    }
                                },
                                placeholder = { Text("1..60", fontSize = 11.sp) },
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

                        // 2-Column Sticky Wheel Slider: [ Arabic Snippet ] [ Hizb Number ]
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Column 1: Arabic Snippet & Surah Name
                                StickyWheelPicker(
                                    items = QuranData.hizbList,
                                    selectedIndex = (selectedHizbNumber - 1).coerceIn(0, QuranData.hizbList.size - 1),
                                    listState = hizbListState,
                                    onItemSelected = { idx, hizb ->
                                        selectedHizbNumber = hizb.number
                                        hizbNoInput = hizb.number.toString()
                                        hizbSearchInput = hizb.startSurahName
                                    },
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                ) { hizb, isSelected ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = hizb.arabicName,
                                            fontSize = if (isSelected) 13.sp else 11.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "${hizb.startSurahName} (${hizb.quarter})",
                                            fontSize = 9.5.sp,
                                            color = if (isSelected) QuranGold else Color.Gray
                                        )
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                                // Column 2: Hizb Number (1..60)
                                val hizbNumbers = remember { (1..60).toList() }
                                StickyWheelPicker(
                                    items = hizbNumbers,
                                    selectedIndex = (selectedHizbNumber - 1).coerceIn(0, 59),
                                    listState = hizbNoListState,
                                    onItemSelected = { idx, hNum ->
                                        selectedHizbNumber = hNum
                                        hizbNoInput = hNum.toString()
                                        hizbSearchInput = QuranData.hizbList[hNum - 1].startSurahName
                                    },
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .fillMaxHeight()
                                ) { hNum, isSelected ->
                                    Text(
                                        text = "Hizb $hNum",
                                        fontSize = if (isSelected) 13.sp else 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            // =========================================================================
            // SECTION 4: Ruku Section (2-Column Sticky Wheel Slider)
            // =========================================================================
            if (selectedFilter == JumpSectionFilter.RUKU) {
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
                                        rukuSearchInput = QuranData.rukuList[rNum - 1].startSurahName
                                    }
                                },
                                placeholder = { Text("1..556", fontSize = 11.sp) },
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

                        // 2-Column Sticky Wheel Slider: [ Arabic Snippet ] [ Ruku Number ]
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                // Column 1: Arabic Snippet & Surah Name
                                StickyWheelPicker(
                                    items = QuranData.rukuList,
                                    selectedIndex = (selectedRukuNumber - 1).coerceIn(0, QuranData.rukuList.size - 1),
                                    listState = rukuListState,
                                    onItemSelected = { idx, ruku ->
                                        selectedRukuNumber = ruku.rukuNumber
                                        rukuNoInput = ruku.rukuNumber.toString()
                                        rukuSearchInput = ruku.startSurahName
                                    },
                                    modifier = Modifier
                                        .weight(2f)
                                        .fillMaxHeight()
                                ) { ruku, isSelected ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = ruku.arabicSnippet,
                                            fontSize = if (isSelected) 13.sp else 11.5.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.55f),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = ruku.startSurahName,
                                            fontSize = 10.sp,
                                            color = if (isSelected) QuranGold else Color.Gray
                                        )
                                    }
                                }

                                VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

                                // Column 2: Ruku Number (1..556)
                                val rukuNumbers = remember { (1..556).toList() }
                                StickyWheelPicker(
                                    items = rukuNumbers,
                                    selectedIndex = (selectedRukuNumber - 1).coerceIn(0, 555),
                                    listState = rukuNoListState,
                                    onItemSelected = { idx, rNum ->
                                        selectedRukuNumber = rNum
                                        rukuNoInput = rNum.toString()
                                        rukuSearchInput = QuranData.rukuList[rNum - 1].startSurahName
                                    },
                                    modifier = Modifier
                                        .weight(0.9f)
                                        .fillMaxHeight()
                                ) { rNum, isSelected ->
                                    Text(
                                        text = "Ruku $rNum",
                                        fontSize = if (isSelected) 13.sp else 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(14.dp))
            }

            if (selectedFilter != JumpSectionFilter.ALL) {
                // Explanatory note matching user's design blueprint
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
                            text = "এই খালি box গুলোতে তাদের নির্দিষ্ট name কিংবা number লিখেও ইউজার তাদের গন্তব্যে যেতে পারবেন। slider গুলোর মাঝে থাকা স্টিকি লাইনে স্ক্রোল করে নির্দিষ্ট নাম বা নম্বর সিলেক্ট করেও সরাসরি গন্তব্যে যেতে পারবেন।",
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
}
