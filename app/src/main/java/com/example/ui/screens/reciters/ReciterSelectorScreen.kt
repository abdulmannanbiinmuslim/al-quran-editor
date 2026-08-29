package com.example.ui.screens.reciters

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ReciterItem
import com.example.data.repository.RecitersData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.LightDivider
import com.example.ui.theme.QuranGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReciterSelectorScreen(
    currentReciter: ReciterItem,
    onSelectReciter: (ReciterItem) -> Unit,
    onOpenDownloadManager: (ReciterItem) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredReciters = remember(searchQuery) {
        if (searchQuery.isBlank()) RecitersData.recitersList
        else RecitersData.recitersList.filter {
            it.displayName.contains(searchQuery, ignoreCase = true) ||
                    it.style.contains(searchQuery, ignoreCase = true) ||
                    it.country.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = IslamicEmeraldPrimary,
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.statusBarsPadding()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Choose Reciter (${filteredReciters.size})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    // Search Reciters TextField
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search by name, country, or style...") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null, tint = IslamicEmeraldPrimary)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = null, tint = Color.Gray)
                                    }
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            items(filteredReciters) { reciter ->
                val isSelected = reciter.name == currentReciter.name

                Card(
                    onClick = { onSelectReciter(reciter) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) IslamicEmeraldContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(10.dp),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, IslamicEmeraldPrimary) else null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            com.example.ui.components.ReciterAvatarBadge(
                                reciter = reciter,
                                size = 46.dp,
                                showBorder = isSelected,
                                showPlayingBadge = isSelected
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = reciter.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${reciter.style} • ${reciter.country} • ${reciter.bitRate}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onOpenDownloadManager(reciter) }) {
                                Icon(
                                    imageVector = Icons.Outlined.CloudDownload,
                                    contentDescription = "Download Package",
                                    tint = IslamicEmeraldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = IslamicEmeraldPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
