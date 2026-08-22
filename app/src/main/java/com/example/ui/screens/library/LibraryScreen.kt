package com.example.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.LightDivider
import com.example.ui.theme.QuranGold

@Composable
fun LibraryScreen(
    activeSubTab: Int,
    onSubTabChange: (Int) -> Unit,
    lastReadList: List<LibraryItem>,
    pinnedList: List<LibraryItem>,
    notesList: List<LibraryItem>,
    onNavigateToAyah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    onDeletePin: (LibraryItem) -> Unit,
    onDeleteNote: (LibraryItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Row (Collections/Last Read, Pins, Notes)
        Surface(
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            TabRow(
                selectedTabIndex = activeSubTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IslamicEmeraldPrimary
            ) {
                Tab(
                    selected = activeSubTab == 0,
                    onClick = { onSubTabChange(0) },
                    text = { Text("Last Read", fontWeight = if (activeSubTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = activeSubTab == 1,
                    onClick = { onSubTabChange(1) },
                    text = { Text("Pins / Bookmarks", fontWeight = if (activeSubTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = activeSubTab == 2,
                    onClick = { onSubTabChange(2) },
                    text = { Text("My Notes", fontWeight = if (activeSubTab == 2) FontWeight.Bold else FontWeight.Normal) }
                )
            }
        }
        Divider(color = LightDivider)

        when (activeSubTab) {
            0 -> {
                // Last Read
                if (lastReadList.isEmpty()) {
                    EmptyState(message = "No reading history yet.")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(lastReadList) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = { onNavigateToAyah(item.surahNumber, item.ayahNumber) }
                            )
                        }
                    }
                }
            }
            1 -> {
                // Pins
                if (pinnedList.isEmpty()) {
                    EmptyState(message = "No pinned verses. Add bookmarks while reading!")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(pinnedList) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = { onNavigateToAyah(item.surahNumber, item.ayahNumber) },
                                onDelete = { onDeletePin(item) }
                            )
                        }
                    }
                }
            }
            2 -> {
                // Notes
                if (notesList.isEmpty()) {
                    EmptyState(message = "No notes recorded yet. Reflect on verses and add notes!")
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(notesList) { item ->
                            LibraryItemCard(
                                item = item,
                                onClick = { onNavigateToAyah(item.surahNumber, item.ayahNumber) },
                                onDelete = { onDeleteNote(item) }
                            )
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
                            .background(IslamicEmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (item.type) {
                                com.example.data.model.LibraryType.PIN -> Icons.Default.Bookmark
                                com.example.data.model.LibraryType.NOTE -> Icons.Default.Note
                                else -> Icons.Default.History
                            },
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
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
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(message, color = Color.Gray, fontSize = 14.sp)
    }
}
