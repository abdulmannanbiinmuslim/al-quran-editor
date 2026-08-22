package com.example.ui.screens.topics

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
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
import com.example.data.model.TopicItem
import com.example.data.model.TopicVerseRef
import com.example.data.repository.TopicsData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.LightDivider
import com.example.ui.theme.QuranGold

@Composable
fun TopicsScreen(
    onNavigateToAyah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTopic by remember { mutableStateOf<TopicItem?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (selectedTopic == null) {
            // Main Topics Grid / List
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Quranic Topics & Themes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicEmeraldPrimary
                        ),
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }

                items(TopicsData.topicsList) { topic ->
                    TopicCard(
                        topic = topic,
                        onClick = { selectedTopic = topic }
                    )
                }
            }
        } else {
            // Selected Topic Verses View
            val topic = selectedTopic!!
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = IslamicEmeraldPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                    ) {
                        IconButton(onClick = { selectedTopic = null }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = topic.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Text(
                                text = "${topic.versesCount} Selected Verses",
                                fontSize = 12.sp,
                                color = QuranGold
                            )
                        }
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(topic.verses) { verseRef ->
                        TopicVerseCard(
                            verseRef = verseRef,
                            onClick = { onNavigateToAyah(verseRef.surahNumber, verseRef.ayahNumber) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicCard(
    topic: TopicItem,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("topic_card_${topic.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(IslamicEmeraldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (topic.id) {
                            "allah" -> Icons.Outlined.Brightness7
                            "aqidah" -> Icons.Outlined.AutoStories
                            "ibadah" -> Icons.Outlined.FavoriteBorder
                            "akhirah" -> Icons.Outlined.HourglassEmpty
                            "etiquette" -> Icons.Outlined.Handshake
                            "history" -> Icons.Outlined.AccountBalance
                            "muamalat" -> Icons.Outlined.Paid
                            "family" -> Icons.Outlined.Groups
                            "politics" -> Icons.Outlined.Gavel
                            else -> Icons.Outlined.MenuBook
                        },
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = topic.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = topic.subTitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(IslamicEmeraldContainer.copy(alpha = 0.5f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${topic.versesCount} Verses",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.Gray)
            }
        }
    }
}

@Composable
private fun TopicVerseCard(
    verseRef: TopicVerseRef,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = verseRef.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = IslamicEmeraldPrimary
                )
                Text(
                    text = "Surah ${verseRef.surahNumber}:${verseRef.ayahNumber}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuranGold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = verseRef.arabicSnippet,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Right,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = verseRef.translationSnippet,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "পড়ুন ও শুনুন (Read & Listen) →",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = IslamicEmeraldPrimary
                )
            }
        }
    }
}
