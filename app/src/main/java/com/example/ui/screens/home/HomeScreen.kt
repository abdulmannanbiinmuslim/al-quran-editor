package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LibraryItem
import com.example.data.model.ReadingViewMode
import com.example.data.model.ReciterItem
import com.example.data.model.SurahItem
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.ui.components.HomeReciterCarouselCard
import com.example.ui.components.ReciterAvatarBadge
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    activeSubTab: ReadingViewMode,
    onSubTabChange: (ReadingViewMode) -> Unit,
    onSurahClick: (Int, Int) -> Unit,
    onReciterClick: (ReciterItem) -> Unit,
    lastReadList: List<LibraryItem>,
    searchQuery: String,
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // 1. Material 3 Islamic Hero Greeting Card (Assalamu Alaikum & Last Read)
            item {
                HomeHeroBannerCard(
                    lastRead = lastReadList.firstOrNull(),
                    onResumeRead = { item ->
                        onSurahClick(item.surahNumber, item.ayahNumber)
                    }
                )
            }

            // 2. Featured Reciters (জনপ্রিয় ক্বারীগণ) with Avatar Badges
            item {
                Column(modifier = Modifier.padding(top = 14.dp, bottom = 8.dp)) {
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
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "জনপ্রিয় ক্বারীগণ (Featured Reciters)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(RecitersData.recitersList.take(14)) { reciter ->
                            HomeReciterCarouselCard(
                                reciter = reciter,
                                onClick = { onReciterClick(reciter) },
                                modifier = Modifier.testTag("reciter_card_${reciter.name}")
                            )
                        }
                    }
                }
            }

            // 3. Quick Access Surahs (পছন্দের সূরাসমূহ)
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.FlashOn,
                            contentDescription = null,
                            tint = QuranGold,
                            modifier = Modifier.size(16.dp)
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
                    Spacer(modifier = Modifier.height(6.dp))

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
                                    containerColor = IslamicEmeraldContainer.copy(alpha = 0.45f),
                                    labelColor = IslamicEmeraldPrimary
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    IslamicEmeraldPrimary.copy(alpha = 0.25f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("quick_link_$title")
                            )
                        }
                    }
                }
            }

            // 4. M3 Expressive Secondary Tab Bar (Surah, Page, Juz, Hizb, Ruku)
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
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
                                ReadingViewMode.HIZB -> "হিজব"
                                ReadingViewMode.RUKU -> "রুকু"
                            }

                            Tab(
                                selected = isSelected,
                                onClick = { onSubTabChange(mode) },
                                text = {
                                    Text(
                                        text = label,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 13.sp
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

            // 5. Vertical Content List Based on Selected Sub Tab
            when (activeSubTab) {
                ReadingViewMode.SURAH -> {
                    val filteredSurahs = QuranData.surahs.filter {
                        if (searchQuery.isBlank()) true
                        else it.englishName.contains(searchQuery, ignoreCase = true) ||
                                it.banglaTranslation.contains(searchQuery, ignoreCase = true) ||
                                it.number.toString() == searchQuery.trim()
                    }

                    items(filteredSurahs) { surah ->
                        ModernSurahCard(
                            surah = surah,
                            onClick = { onSurahClick(surah.number, 1) }
                        )
                    }
                }

                ReadingViewMode.PAGE -> {
                    items(QuranData.pageList) { page ->
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
                    items(QuranData.juzList) { juz ->
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
                    items(QuranData.hizbList) { hizb ->
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
                    items(QuranData.rukuList) { ruku ->
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
}

@Composable
private fun HomeHeroBannerCard(
    lastRead: LibraryItem?,
    onResumeRead: (LibraryItem) -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(IslamicEmeraldDark, IslamicEmeraldPrimary, Color(0xFF1E6B4C))
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "আসসালামু আলাইকুম",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "পবিত্র কুরআন পড়ুন, শুনুন ও অন্তরে ধারণ করুন",
                            fontSize = 12.sp,
                            color = QuranGoldLight
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = QuranGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Last read quick resume card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.2f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (lastRead != null) onResumeRead(lastRead)
                            else onResumeRead(
                                LibraryItem(
                                    id = "1",
                                    surahNumber = 1,
                                    ayahNumber = 1,
                                    surahName = "Al-Fatihah",
                                    arabicSnippet = "بِسْمِ ٱللَّهِ",
                                    translationSnippet = "পরম করুণাময় আল্লাহর নামে"
                                )
                            )
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = QuranGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "সর্বশেষ পঠিত (Last Read)",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Text(
                                    text = if (lastRead != null) "${lastRead.surahName} [${lastRead.surahNumber}:${lastRead.ayahNumber}]"
                                    else "Al-Fatihah [1:1]",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        FilledTonalButton(
                            onClick = {
                                if (lastRead != null) onResumeRead(lastRead)
                                else onResumeRead(
                                    LibraryItem(
                                        id = "1",
                                        surahNumber = 1,
                                        ayahNumber = 1,
                                        surahName = "Al-Fatihah",
                                        arabicSnippet = "بِسْمِ ٱللَّهِ",
                                        translationSnippet = "পরম করুণাময় আল্লাহর নামে"
                                    )
                                )
                            },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = QuranGold,
                                contentColor = Color(0xFF2C2200)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("পড়ুন", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
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

