package com.example.ui.screens.library

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.LibraryItem
import com.example.data.model.LibraryType
import com.example.data.repository.QuranData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.LightDivider
import com.example.ui.theme.QuranGold
import com.example.ui.util.rememberAppHaptics

@Composable
fun LibraryScreen(
    activeSubTab: Int,
    onSubTabChange: (Int) -> Unit,
    lastReadList: List<LibraryItem>,
    favoriteList: List<LibraryItem> = emptyList(),
    pinnedList: List<LibraryItem>,
    notesList: List<LibraryItem>,
    onNavigateToAyah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    onDeletePin: (LibraryItem) -> Unit,
    onDeleteFavorite: (LibraryItem) -> Unit = {},
    onDeleteNote: (LibraryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    val haptics = rememberAppHaptics()

    // Helper filter function for items by query (surah name English/Bangla or number)
    fun filterLibraryItems(list: List<LibraryItem>): List<LibraryItem> {
        if (searchQuery.isBlank()) return list
        val query = searchQuery.trim()
        return list.filter { item ->
            val surah = QuranData.surahs.find { it.number == item.surahNumber }
            item.surahName.contains(query, ignoreCase = true) ||
                    item.surahNumber.toString() == query ||
                    item.ayahNumber.toString() == query ||
                    (surah != null && (
                            surah.englishName.contains(query, ignoreCase = true) ||
                                    surah.banglaTranslation.contains(query, ignoreCase = true) ||
                                    surah.arabicName.contains(query, ignoreCase = true)
                            )) ||
                    item.arabicSnippet.contains(query, ignoreCase = true) ||
                    item.translationSnippet.contains(query, ignoreCase = true) ||
                    item.noteText.contains(query, ignoreCase = true)
        }
    }

    // Filter Surahs for Surah Directory tab
    val filteredSurahList = remember(searchQuery) {
        if (searchQuery.isBlank()) QuranData.surahs
        else {
            val query = searchQuery.trim()
            QuranData.surahs.filter {
                it.englishName.contains(query, ignoreCase = true) ||
                        it.banglaTranslation.contains(query, ignoreCase = true) ||
                        it.arabicName.contains(query, ignoreCase = true) ||
                        it.number.toString() == query
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Library Search Bar (Filters Surahs, Bookmarks, and Notes)
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            "সূরা বা আয়াত খুঁজুন (নাম বা নম্বর)...",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = IslamicEmeraldPrimary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                searchQuery = ""
                                haptics.tap()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IslamicEmeraldPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("library_search_input")
                )
            }
        }

        // 2. Tab Row: [Last Read, Favorites, Pins, Notes, All Surahs]
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            ScrollableTabRow(
                selectedTabIndex = activeSubTab.coerceIn(0, 4),
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IslamicEmeraldPrimary,
                edgePadding = 12.dp,
                divider = { HorizontalDivider(color = LightDivider) }
            ) {
                val tabs = listOf(
                    "Last Read" to Icons.Default.History,
                    "Favorites (পছন্দ)" to Icons.Default.Star,
                    "Pins / Bookmarks" to Icons.Default.Bookmark,
                    "My Notes" to Icons.Default.Note,
                    "Surah Index" to Icons.Default.List
                )

                tabs.forEachIndexed { index, (label, icon) ->
                    val isSelected = activeSubTab == index
                    Tab(
                        selected = isSelected,
                        onClick = {
                            haptics.tap()
                            onSubTabChange(index)
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = if (isSelected) {
                                    if (index == 1) QuranGold else IslamicEmeraldPrimary
                                } else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        text = {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 12.sp
                            )
                        },
                        selectedContentColor = IslamicEmeraldPrimary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.testTag("library_tab_$index")
                    )
                }
            }
        }

        // 3. Tab Content
        when (activeSubTab) {
            0 -> {
                // Last Read
                val filtered = filterLibraryItems(lastReadList)
                if (filtered.isEmpty()) {
                    EmptyState(
                        message = if (searchQuery.isNotEmpty()) "কোনো মিল পাওয়া যায়নি।" else "No reading history yet."
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filtered, key = { it.id }) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = {
                                    haptics.verseSwitch()
                                    onNavigateToAyah(item.surahNumber, item.ayahNumber)
                                }
                            )
                        }
                    }
                }
            }
            1 -> {
                // Favorites
                val filtered = filterLibraryItems(favoriteList)
                if (filtered.isEmpty()) {
                    EmptyState(
                        message = if (searchQuery.isNotEmpty()) "পছন্দের তালিকায় কোনো মিল পাওয়া যায়নি।"
                        else "No favorite verses saved yet.\nTap the Star/Bookmark icon on any verse while reading!"
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filtered, key = { it.id }) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = {
                                    haptics.verseSwitch()
                                    onNavigateToAyah(item.surahNumber, item.ayahNumber)
                                },
                                onDelete = {
                                    haptics.tap()
                                    onDeleteFavorite(item)
                                }
                            )
                        }
                    }
                }
            }
            2 -> {
                // Pins / Bookmarks
                val filtered = filterLibraryItems(pinnedList)
                if (filtered.isEmpty()) {
                    EmptyState(
                        message = if (searchQuery.isNotEmpty()) "বুকমার্কে কোনো মিল পাওয়া যায়নি।"
                        else "No pinned verses. Add bookmarks while reading!"
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filtered, key = { it.id }) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = {
                                    haptics.verseSwitch()
                                    onNavigateToAyah(item.surahNumber, item.ayahNumber)
                                },
                                onDelete = {
                                    haptics.tap()
                                    onDeletePin(item)
                                }
                            )
                        }
                    }
                }
            }
            3 -> {
                // Notes
                val filtered = filterLibraryItems(notesList)
                if (filtered.isEmpty()) {
                    EmptyState(
                        message = if (searchQuery.isNotEmpty()) "নোটের মধ্যে কোনো মিল পাওয়া যায়নি।"
                        else "No notes recorded yet. Reflect on verses and add notes!"
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filtered, key = { it.id }) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = {
                                    haptics.verseSwitch()
                                    onNavigateToAyah(item.surahNumber, item.ayahNumber)
                                },
                                onDelete = {
                                    haptics.tap()
                                    onDeleteNote(item)
                                }
                            )
                        }
                    }
                }
            }
            4 -> {
                // Surah Directory Filter List
                if (filteredSurahList.isEmpty()) {
                    EmptyState(message = "কোনো সূরা পাওয়া যায়নি: \"$searchQuery\"")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredSurahList, key = { it.number }) { surah ->
                            Card(
                                onClick = {
                                    haptics.verseSwitch()
                                    onNavigateToAyah(surah.number, 1)
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(10.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(IslamicEmeraldContainer),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${surah.number}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = IslamicEmeraldPrimary
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "${surah.englishName} (${surah.banglaTranslation})",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = "${surah.revelationType} • ${surah.totalAyahs} আয়াত",
                                                fontSize = 12.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Text(
                                        text = surah.arabicName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = IslamicEmeraldPrimary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryItemCard(
    item: LibraryItem,
    onClick: () -> Unit,
    onDelete: (() -> Unit)? = null
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (item.type == LibraryType.FAVORITE) QuranGold.copy(alpha = 0.2f)
                                else IslamicEmeraldContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (item.type) {
                                LibraryType.FAVORITE -> Icons.Default.Star
                                LibraryType.PIN -> Icons.Default.Bookmark
                                LibraryType.NOTE -> Icons.Default.Note
                                else -> Icons.Default.History
                            },
                            contentDescription = null,
                            tint = if (item.type == LibraryType.FAVORITE) QuranGold else IslamicEmeraldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "${item.surahName} • ${item.surahNumber}:${item.ayahNumber}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = IslamicEmeraldPrimary
                    )
                }

                if (onDelete != null) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteOutline,
                            contentDescription = "Delete",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.arabicSnippet,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.translationSnippet,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (!item.noteText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = IslamicEmeraldContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "📝 ${item.noteText}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = IslamicEmeraldPrimary,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
