package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReadingViewMode
import com.example.data.repository.QuranData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JumpToAyahBottomSheet(
    onDismiss: () -> Unit,
    onNavigateToSurah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(ReadingViewMode.SURAH) }
    var inputSurah by remember { mutableStateOf("1") }
    var inputAyah by remember { mutableStateOf("1") }
    var inputPage by remember { mutableStateOf("1") }

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
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            // Title & Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Jump to Ayah / Destination",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub Navigation Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 0.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                ReadingViewMode.values().forEach { mode ->
                    Tab(
                        selected = selectedTab == mode,
                        onClick = { selectedTab = mode },
                        text = {
                            Text(
                                text = mode.name.lowercase().replaceFirstChar { it.uppercase() },
                                fontWeight = if (selectedTab == mode) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                ReadingViewMode.SURAH -> {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = inputSurah,
                                onValueChange = { inputSurah = it },
                                label = { Text("Surah (1-114)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("jump_surah_input")
                            )

                            OutlinedTextField(
                                value = inputAyah,
                                onValueChange = { inputAyah = it },
                                label = { Text("Ayah") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("jump_ayah_input")
                            )

                            OutlinedTextField(
                                value = inputPage,
                                onValueChange = { inputPage = it },
                                label = { Text("Page (1-604)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("jump_page_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val s = inputSurah.toIntOrNull()?.coerceIn(1, 114) ?: 1
                                    val a = inputAyah.toIntOrNull()?.coerceAtLeast(1) ?: 1
                                    onNavigateToSurah(s, a)
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("go_to_surah_button")
                            ) {
                                Text("Go to Surah")
                            }

                            Button(
                                onClick = {
                                    val p = inputPage.toIntOrNull()?.coerceIn(1, 604) ?: 1
                                    val pageInfo = QuranData.pageList.find { it.pageNumber == p } ?: QuranData.pageList[0]
                                    onNavigateToSurah(pageInfo.startSurah, pageInfo.startAyah)
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = QuranGold),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("go_to_page_button")
                            ) {
                                Text("Go to Page", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Quick Surah List", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(6.dp))

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                        ) {
                            items(QuranData.surahs) { surah ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onNavigateToSurah(surah.number, 1)
                                            onDismiss()
                                        }
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = "${surah.number}.",
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary,
                                            modifier = Modifier.width(32.dp)
                                        )
                                        Column {
                                            Text(text = surah.englishName, fontWeight = FontWeight.Medium)
                                            Text(text = surah.banglaTranslation, fontSize = 11.sp, color = Color.Gray)
                                        }
                                    }

                                    Text(
                                        text = surah.arabicName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = IslamicEmeraldPrimary
                                    )
                                }
                                Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                            }
                        }
                    }
                }

                ReadingViewMode.JUZ -> {
                    LazyColumn(modifier = Modifier.height(280.dp)) {
                        items(QuranData.juzList) { juz ->
                            Card(
                                onClick = {
                                    onNavigateToSurah(juz.startSurah, juz.startAyah)
                                    onDismiss()
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Juz ${juz.number}", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                                        Text(juz.startSurahName, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(juz.arabicName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                ReadingViewMode.HIZB -> {
                    LazyColumn(modifier = Modifier.height(280.dp)) {
                        items(QuranData.hizbList) { hizb ->
                            Card(
                                onClick = {
                                    onNavigateToSurah(hizb.startSurah, hizb.startAyah)
                                    onDismiss()
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("${hizb.quarter} Hizb ${hizb.number}", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                                        Text(hizb.startSurahName, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(hizb.arabicName, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                ReadingViewMode.RUKU -> {
                    LazyColumn(modifier = Modifier.height(280.dp)) {
                        items(QuranData.rukuList.take(50)) { ruku ->
                            Card(
                                onClick = {
                                    onNavigateToSurah(ruku.startSurah, ruku.startAyah)
                                    onDismiss()
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Ruku ${ruku.rukuNumber}", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                                        Text(ruku.startSurahName, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(ruku.arabicSnippet, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }

                ReadingViewMode.PAGE -> {
                    LazyColumn(modifier = Modifier.height(280.dp)) {
                        items(QuranData.pageList.take(50)) { page ->
                            Card(
                                onClick = {
                                    onNavigateToSurah(page.startSurah, page.startAyah)
                                    onDismiss()
                                },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text("Page ${page.pageNumber}", fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                                        Text(page.startSurahName, fontSize = 12.sp, color = Color.Gray)
                                    }
                                    Text(page.arabicSnippet, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
