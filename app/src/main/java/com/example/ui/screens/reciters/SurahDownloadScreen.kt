package com.example.ui.screens.reciters

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import com.example.data.audio.DownloadMode
import com.example.data.audio.QuranDownloadManager
import com.example.data.audio.SurahDownloadStatus
import com.example.data.model.ReciterItem
import com.example.data.model.SurahItem
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDownloadScreen(
    reciter: ReciterItem,
    downloadManager: QuranDownloadManager,
    onSelectReciter: (ReciterItem) -> Unit,
    onDismiss: () -> Unit
) {
    val downloadMode by downloadManager.downloadMode.collectAsState()
    val surahStatusMap by downloadManager.surahStatusMap.collectAsState()
    val downloadedCount by downloadManager.downloadedCount.collectAsState()
    val usedStorageBytes by downloadManager.usedStorageBytes.collectAsState()
    val isBulkDownloading by downloadManager.isBulkDownloading.collectAsState()
    val bulkProgress by downloadManager.bulkProgress.collectAsState()
    val bulkCurrentSurahNumber by downloadManager.bulkCurrentSurahNumber.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showReciterDialog by remember { mutableStateOf(false) }
    var showDeleteAllConfirmDialog by remember { mutableStateOf(false) }
    var surahToDeleteConfirm by remember { mutableStateOf<SurahItem?>(null) }
    val scope = rememberCoroutineScope()
    var timingExportMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(reciter, downloadMode) {
        downloadManager.refreshStatuses(reciter)
    }

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranData.surahs
        } else {
            QuranData.surahs.filter {
                it.englishName.contains(searchQuery, ignoreCase = true) ||
                it.banglaTranslation.contains(searchQuery, ignoreCase = true) ||
                it.arabicName.contains(searchQuery, ignoreCase = true) ||
                it.number.toString() == searchQuery.trim()
            }
        }
    }

    // Full screen styled dialog / surface
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "Download Manager",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "সূরা ও আয়াত ভিত্তিক অফলাইন অডিও",
                                fontSize = 11.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        FilledTonalButton(
                            onClick = { showReciterDialog = true },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ক্বারী পরিবর্তন",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = IslamicEmeraldPrimary,
                        titleContentColor = Color.White
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 1. Reciter Name Header Banner
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = reciter.displayName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${reciter.style} • 114 Surahs (${if (downloadMode == DownloadMode.AYAH_BY_AYAH) "Ayah Mode" else "Surah Mode"})",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 2. Basic Info Card (as shown on Page 46-47)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Basic Info",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = IslamicEmeraldPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Left Column: Statistics
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = IslamicEmeraldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Downloaded Surah:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = "$downloadedCount out of 114",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(start = 22.dp)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Storage,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Device Space Used:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Text(
                                    text = downloadManager.formatBytes(usedStorageBytes),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary,
                                    modifier = Modifier.padding(start = 22.dp)
                                )
                            }

                            // Right Column: Download Mode Radio Buttons (Ayah by Ayah vs Surah by Surah)
                            Column(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                    .padding(8.dp)
                            ) {
                                // Option 1: Ayah by Ayah
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable {
                                            downloadManager.setDownloadMode(DownloadMode.AYAH_BY_AYAH, reciter)
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = downloadMode == DownloadMode.AYAH_BY_AYAH,
                                        onClick = {
                                            downloadManager.setDownloadMode(DownloadMode.AYAH_BY_AYAH, reciter)
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = IslamicEmeraldPrimary),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "Ayah by Ayah",
                                            fontSize = 13.sp,
                                            fontWeight = if (downloadMode == DownloadMode.AYAH_BY_AYAH) FontWeight.Bold else FontWeight.Normal,
                                            color = if (downloadMode == DownloadMode.AYAH_BY_AYAH) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "আয়াত ভিত্তিক",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // Option 2: Surah by Surah
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .clickable {
                                            downloadManager.setDownloadMode(DownloadMode.SURAH_BY_SURAH, reciter)
                                        }
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = downloadMode == DownloadMode.SURAH_BY_SURAH,
                                        onClick = {
                                            downloadManager.setDownloadMode(DownloadMode.SURAH_BY_SURAH, reciter)
                                        },
                                        colors = RadioButtonDefaults.colors(selectedColor = IslamicEmeraldPrimary),
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = "Surah by Surah",
                                            fontSize = 13.sp,
                                            fontWeight = if (downloadMode == DownloadMode.SURAH_BY_SURAH) FontWeight.Bold else FontWeight.Normal,
                                            color = if (downloadMode == DownloadMode.SURAH_BY_SURAH) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = "সূরা ভিত্তিক",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 3. Action Buttons: Download all & Remove all
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Download All Button
                            Button(
                                onClick = {
                                    downloadManager.downloadAllSurahs(reciter)
                                },
                                enabled = !isBulkDownloading && downloadedCount < 114,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IslamicEmeraldPrimary,
                                    disabledContainerColor = IslamicEmeraldPrimary.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Download,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Download all",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }

                            // Remove All Button
                            OutlinedButton(
                                onClick = {
                                    if (downloadedCount > 0) {
                                        showDeleteAllConfirmDialog = true
                                    }
                                },
                                enabled = downloadedCount > 0,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFD32F2F)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (downloadedCount > 0) Color(0xFFEF9A9A) else Color.LightGray
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = if (downloadedCount > 0) Color(0xFFD32F2F) else Color.Gray,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Remove all",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = if (downloadedCount > 0) Color(0xFFD32F2F) else Color.Gray
                                )
                            }
                        }

                        // 3.5 Reciter Timing Integration Card
                        if (downloadManager.isReciterTimingSupported(reciter)) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFBF8EE)),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2CB87)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = Color(0xFFB8860B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Abdul Basit Precision Timings Active",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = Color(0xFF5D4037)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "যেকোনো সূরা ডাউনলোডের সাথে 4টি মোড (Letter, Word, Ayah, Surah) সমন্বিত 'Abdul basit timing.json' ফাইল তৈরি হবে।",
                                        fontSize = 11.sp,
                                        color = Color(0xFF5D4037).copy(alpha = 0.85f),
                                        lineHeight = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "4-Mode Recitation & Surah Support",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = IslamicEmeraldPrimary
                                        )
                                        FilledTonalButton(
                                            onClick = {
                                                scope.launch {
                                                    val exportFile = downloadManager.generateAndExportTimingFile(reciter, emptyList())
                                                    timingExportMessage = if (exportFile != null) {
                                                        "টাইমিং ফাইল সংরক্ষিত: ${exportFile.name}"
                                                    } else {
                                                        "টাইমিং ফাইল তৈরি হয়েছে"
                                                    }
                                                }
                                            },
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Download,
                                                contentDescription = null,
                                                modifier = Modifier.size(12.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Export Timing", fontSize = 10.sp)
                                        }
                                    }
                                }
                            }
                        }

                        // Timing export feedback
                        timingExportMessage?.let { msg ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(containerColor = IslamicEmeraldContainer)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = msg, fontSize = 11.sp, color = IslamicEmeraldPrimary)
                                    IconButton(
                                        onClick = { timingExportMessage = null },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Bulk Download In-Progress Banner
                AnimatedVisibility(visible = isBulkDownloading) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = IslamicEmeraldContainer),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "সবগুলো সূরা ডাউনলোড হচ্ছে (সূরা $bulkCurrentSurahNumber)...",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = IslamicEmeraldPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                LinearProgressIndicator(
                                    progress = bulkProgress,
                                    color = IslamicEmeraldPrimary,
                                    trackColor = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp))
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { downloadManager.cancelBulkDownload(reciter) }
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Cancel",
                                    tint = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }
                }

                // 5. Search Bar & Surah List Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Surah list",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${filteredSurahs.size} সূরা",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("সূরা খুঁজুন (নাম বা নম্বর)...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 2.dp)
                        .height(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IslamicEmeraldPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 6. Surah List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredSurahs, key = { it.number }) { surah ->
                        val status = surahStatusMap[surah.number] ?: SurahDownloadStatus.NotDownloaded
                        SurahDownloadItemRow(
                            surah = surah,
                            status = status,
                            downloadMode = downloadMode,
                            formatBytes = { downloadManager.formatBytes(it) },
                            onDownloadClick = {
                                downloadManager.downloadSurah(reciter, surah.number)
                            },
                            onCancelClick = {
                                downloadManager.cancelSurahDownload(surah.number, reciter)
                            },
                            onDeleteClick = {
                                surahToDeleteConfirm = surah
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete All Confirmation Dialog
    if (showDeleteAllConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllConfirmDialog = false },
            title = { Text("সবগুলো সূরা ডিলিট করবেন?", fontWeight = FontWeight.Bold) },
            text = { Text("এই ক্বারীর (${reciter.displayName}) ডাউনলোড করা সকল অডিও ফাইল মুছে ফেলা হবে। আপনি কি নিশ্চিত?") },
            confirmButton = {
                Button(
                    onClick = {
                        downloadManager.deleteAllSurahs(reciter)
                        showDeleteAllConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("হ্যাঁ, ডিলিট করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAllConfirmDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // Delete Single Surah Confirmation Dialog
    if (surahToDeleteConfirm != null) {
        val surah = surahToDeleteConfirm!!
        AlertDialog(
            onDismissRequest = { surahToDeleteConfirm = null },
            title = { Text("সূরা ডিলিট করবেন?", fontWeight = FontWeight.Bold) },
            text = { Text("${surah.number}. ${surah.englishName} এর অফলাইন অডিও ফাইলটি ডিলিট করতে চান?") },
            confirmButton = {
                Button(
                    onClick = {
                        downloadManager.deleteSurahAudio(reciter, surah.number)
                        surahToDeleteConfirm = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("ডিলিট করুন")
                }
            },
            dismissButton = {
                TextButton(onClick = { surahToDeleteConfirm = null }) {
                    Text("বাতিল")
                }
            }
        )
    }

    // Reciter Selector Modal Dialog
    if (showReciterDialog) {
        ReciterSelectionDialog(
            currentReciter = reciter,
            onSelect = {
                onSelectReciter(it)
                downloadManager.refreshStatuses(it)
                showReciterDialog = false
            },
            onDismiss = { showReciterDialog = false }
        )
    }
}

@Composable
fun SurahDownloadItemRow(
    surah: SurahItem,
    status: SurahDownloadStatus,
    downloadMode: DownloadMode,
    formatBytes: (Long) -> String,
    onDownloadClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 0.8.dp,
                color = when (status) {
                    is SurahDownloadStatus.Downloaded -> IslamicEmeraldPrimary.copy(alpha = 0.4f)
                    is SurahDownloadStatus.Downloading -> IslamicEmeraldPrimary
                    else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                },
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Surah Number Circle
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (status is SurahDownloadStatus.Downloaded) IslamicEmeraldPrimary
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${surah.number}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = if (status is SurahDownloadStatus.Downloaded) Color.White
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Center: Surah Name, Ayahs and Size
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${surah.number}. ${surah.englishName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = surah.arabicName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Size & Ayah info
                val sizeText = when (status) {
                    is SurahDownloadStatus.Downloaded -> "(${formatBytes(status.fileSizeBytes)}) • ${surah.totalAyahs} Ayahs"
                    is SurahDownloadStatus.Downloading -> {
                        if (downloadMode == DownloadMode.AYAH_BY_AYAH) {
                            "${status.downloadedAyahs}/${status.totalAyahs} Ayahs • ${(status.progress * 100).toInt()}%"
                        } else {
                            "${(status.progress * 100).toInt()}% downloaded"
                        }
                    }
                    is SurahDownloadStatus.Error -> "ডাউনলোড ত্রুটি"
                    else -> "~${estimateSurahSize(surah.totalAyahs)} • ${surah.totalAyahs} Ayahs"
                }

                Text(
                    text = sizeText,
                    fontSize = 11.sp,
                    color = when (status) {
                        is SurahDownloadStatus.Downloaded -> IslamicEmeraldPrimary
                        is SurahDownloadStatus.Downloading -> IslamicEmeraldPrimary
                        is SurahDownloadStatus.Error -> Color(0xFFD32F2F)
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )

                // Linear progress bar if downloading
                if (status is SurahDownloadStatus.Downloading) {
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = status.progress,
                        color = IslamicEmeraldPrimary,
                        trackColor = IslamicEmeraldContainer,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp))
                    )
                }
            }

            // Right: Actions (Download / Delete / Progress)
            when (status) {
                is SurahDownloadStatus.Downloaded -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Downloaded check badge
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IslamicEmeraldContainer,
                            modifier = Modifier.padding(end = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Downloaded",
                                    tint = IslamicEmeraldPrimary,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = "Ready",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary
                                )
                            }
                        }

                        // Delete button
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Surah",
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                is SurahDownloadStatus.Downloading -> {
                    IconButton(
                        onClick = onCancelClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Cancel Download",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                else -> {
                    // Download Button
                    IconButton(
                        onClick = onDownloadClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "Download Surah",
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReciterSelectionDialog(
    currentReciter: ReciterItem,
    onSelect: (ReciterItem) -> Unit,
    onDismiss: () -> Unit
) {
    var searchReciter by remember { mutableStateOf("") }
    val filtered = remember(searchReciter) {
        if (searchReciter.isBlank()) RecitersData.recitersList
        else RecitersData.recitersList.filter {
            it.displayName.contains(searchReciter, ignoreCase = true) ||
            it.style.contains(searchReciter, ignoreCase = true)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text("ক্বারী নির্বাচন করুন (Select Reciter)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = searchReciter,
                    onValueChange = { searchReciter = it },
                    placeholder = { Text("ক্বারীর নাম খুঁজুন...", fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
            ) {
                items(filtered, key = { it.id }) { rec ->
                    val isSelected = rec.id == currentReciter.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) IslamicEmeraldContainer
                                else Color.Transparent
                            )
                            .clickable { onSelect(rec) }
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = rec.displayName,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${rec.style} (${rec.country})",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isSelected) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = null,
                                tint = IslamicEmeraldPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন")
            }
        }
    )
}

fun estimateSurahSize(ayahCount: Int): String {
    val estimatedKb = (ayahCount * 140L).coerceAtLeast(300L)
    return if (estimatedKb < 1024) {
        "$estimatedKb KB"
    } else {
        String.format("%.1f MB", estimatedKb.toDouble() / 1024)
    }
}
