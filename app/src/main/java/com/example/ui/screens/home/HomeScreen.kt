package com.example.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyVersesMetric
import com.example.data.model.LibraryItem
import com.example.data.model.ReadingViewMode
import com.example.data.model.ReciterItem
import com.example.data.model.SurahItem
import com.example.data.model.WeeklyReadingSummary
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.ui.components.HomeHeroBanner
import com.example.ui.components.HomeReciterCarouselCard
import com.example.ui.theme.*
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    activeSubTab: ReadingViewMode,
    onSubTabChange: (ReadingViewMode) -> Unit,
    onSurahClick: (Int, Int) -> Unit,
    onReciterClick: (ReciterItem) -> Unit,
    lastReadList: List<LibraryItem>,
    searchQuery: String,
    weeklyReadingSummary: WeeklyReadingSummary? = null,
    onViewFullStats: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val quickLinks = listOf(
        "AL-MULK" to 67,
        "AS-SAJDAH" to 32,
        "AL-KAHF" to 18,
        "AYATUL KURSI" to 2,
        "YA-SIN" to 36,
        "AR-RAHMAN" to 55,
        "AL-WAQI'AH" to 56,
        "AL-IKHLAS" to 112
    )

    val listState = rememberLazyListState()

    // When tab changes, if user was scrolled past the header, maintain position at sticky tab
    LaunchedEffect(activeSubTab) {
        if (listState.firstVisibleItemIndex > 4) {
            listState.scrollToItem(4)
        }
    }

    val filteredSurahs = remember(QuranData.surahs, searchQuery) {
        QuranData.surahs.filter {
            if (searchQuery.isBlank()) true
            else it.englishName.contains(searchQuery, ignoreCase = true) ||
                    it.banglaTranslation.contains(searchQuery, ignoreCase = true) ||
                    it.number.toString() == searchQuery.trim()
        }
    }

    val displayLastRead = remember(lastReadList) {
        lastReadList.take(10)
    }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(activeSubTab) {
                var totalDragX = 0f
                detectHorizontalDragGestures(
                    onDragStart = { totalDragX = 0f },
                    onDragEnd = {
                        if (totalDragX < -70f) {
                            // Swiped Left -> Next Tab (সূরা -> পৃষ্ঠা -> পারা -> হিযব -> রুকু)
                            val nextIdx = (activeSubTab.ordinal + 1).coerceAtMost(ReadingViewMode.values().size - 1)
                            onSubTabChange(ReadingViewMode.values()[nextIdx])
                        } else if (totalDragX > 70f) {
                            // Swiped Right -> Previous Tab
                            val prevIdx = (activeSubTab.ordinal - 1).coerceAtLeast(0)
                            onSubTabChange(ReadingViewMode.values()[prevIdx])
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        totalDragX += dragAmount
                        if (abs(totalDragX) > 35f) {
                            change.consume()
                        }
                    }
                )
            },
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. New Refined Auto-Sliding Hero Banner (No broken lines, full text displayed)
        item(key = "hero_banner") {
            HomeHeroBanner(
                lastRead = lastReadList.firstOrNull(),
                onResumeRead = { onSurahClick(it.surahNumber, it.ayahNumber) },
                onAyahClick = onSurahClick,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // 2. Featured Reciters (জনপ্রিয় ক্বারীগণ)
        item(key = "featured_reciters") {
            Column(modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.SpatialAudioOff,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "জনপ্রিয় ক্বারীগণ (${RecitersData.recitersList.size} Reciters)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(RecitersData.recitersList) { reciter ->
                        HomeReciterCarouselCard(
                            reciter = reciter,
                            onClick = { onReciterClick(reciter) },
                            modifier = Modifier.testTag("reciter_card_${reciter.name}")
                        )
                    }
                }
            }
        }

        // 3. Quick Access Surahs (Quick Links)
        item(key = "quick_links") {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.FlashOn,
                        contentDescription = null,
                        tint = QuranGold,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Quick Links",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(quickLinks) { (title, surahNum) ->
                        SuggestionChip(
                            onClick = { onSurahClick(surahNum, 1) },
                            label = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                labelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("quick_link_$title")
                        )
                    }
                }
            }
        }

        // 4. Last Read (সর্বশেষ পঠিত)
        if (displayLastRead.isNotEmpty()) {
            item(key = "last_read") {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Last Read",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("last_read_horizontal_row")
                    ) {
                        items(displayLastRead, key = { it.id }) { item ->
                            SuggestionChip(
                                onClick = { onSurahClick(item.surahNumber, item.ayahNumber) },
                                label = {
                                    Text(
                                        text = "${item.surahName.uppercase()} (${item.ayahNumber})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                },
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                    labelColor = MaterialTheme.colorScheme.primary
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("last_read_chip_${item.surahNumber}")
                            )
                        }
                    }
                }
            }
        }

        // 5. STICKY SECONDARY TAB ROW ( সূরা, পৃষ্ঠা, পারা, হিযব, রুকু )
        stickyHeader(key = "sticky_tab_row") {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                SecondaryTabRow(
                    selectedTabIndex = activeSubTab.ordinal,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IslamicEmeraldPrimary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ReadingViewMode.values().forEach { mode ->
                        val isSelected = activeSubTab == mode
                        val label = when (mode) {
                            ReadingViewMode.SURAH -> "সূরা"
                            ReadingViewMode.PAGE -> "পৃষ্ঠা"
                            ReadingViewMode.JUZ -> "পারা"
                            ReadingViewMode.HIZB -> "হিযব"
                            ReadingViewMode.RUKU -> "রুকু"
                        }

                        Tab(
                            selected = isSelected,
                            onClick = { onSubTabChange(mode) },
                            text = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.5.sp
                                )
                            },
                            selectedContentColor = IslamicEmeraldPrimary,
                            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.testTag("home_subtab_$label")
                        )
                    }
                }
            }
        }

        // 6. Complete Lists for Active Tab (No cutoffs, all items render completely)
        when (activeSubTab) {
            ReadingViewMode.SURAH -> {
                items(filteredSurahs, key = { "surah_${it.number}" }) { surah ->
                    ModernSurahCard(
                        surah = surah,
                        onClick = { onSurahClick(surah.number, 1) }
                    )
                }
            }

            ReadingViewMode.PAGE -> {
                items(QuranData.pageList, key = { "page_${it.pageNumber}" }) { page ->
                    PageListItem(
                        page = page,
                        onClick = { onSurahClick(page.startSurah, page.startAyah) }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            ReadingViewMode.JUZ -> {
                items(QuranData.juzList, key = { "juz_${it.number}" }) { juz ->
                    JuzListItem(
                        juz = juz,
                        onClick = { onSurahClick(juz.startSurah, juz.startAyah) }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            ReadingViewMode.HIZB -> {
                itemsIndexed(QuranData.hizbList, key = { index, hizb -> "hizb_${hizb.number}_${hizb.quarter}_$index" }) { _, hizb ->
                    HizbListItem(
                        hizb = hizb,
                        onClick = { onSurahClick(hizb.startSurah, hizb.startAyah) }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            ReadingViewMode.RUKU -> {
                itemsIndexed(QuranData.rukuList, key = { index, ruku -> "ruku_${ruku.rukuNumber}_$index" }) { _, ruku ->
                    RukuListItem(
                        ruku = ruku,
                        onClick = { onSurahClick(ruku.startSurah, ruku.startAyah) }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ModernSurahCard(
    surah: SurahItem,
    onClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("surah_item_${surah.number}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Octagonal / Styled Number Badge
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(IslamicEmeraldContainer, IslamicEmeraldContainer.copy(alpha = 0.4f))
                            )
                        )
                        .border(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${surah.number}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = IslamicEmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = surah.englishName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${surah.banglaTranslation} • ${surah.totalAyahs} আয়াত • ${if (surah.revelationType == "Meccan") "মাক্কী" else "মাদানী"}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                    )
                }
            }

            Text(
                text = surah.arabicName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = IslamicEmeraldPrimary
                )
            )
        }
    }
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun PageListItem(
    page: com.example.data.repository.PageInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Page ${page.pageNumber}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            )
            Text(
                text = page.startSurahName,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        Text(
            text = page.arabicSnippet,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun JuzListItem(
    juz: com.example.data.repository.JuzInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Juz ${juz.number}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            )
            Text(
                text = juz.startSurahName,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        Text(
            text = juz.arabicName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun HizbListItem(
    hizb: com.example.data.repository.HizbInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${hizb.quarter} Hizb ${hizb.number}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            )
            Text(
                text = hizb.startSurahName,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        Text(
            text = hizb.arabicName,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RukuListItem(
    ruku: com.example.data.repository.RukuInfo,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Ruku ${ruku.rukuNumber}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
            )
            Text(
                text = ruku.startSurahName,
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }

        Text(
            text = ruku.arabicSnippet,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

