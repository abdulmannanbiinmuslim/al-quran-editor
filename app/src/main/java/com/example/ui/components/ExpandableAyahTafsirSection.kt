package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyahItem
import com.example.data.repository.QuranTafsirRepository
import com.example.data.repository.TafsirEntry
import com.example.data.repository.TafsirSource
import com.example.ui.theme.*
import kotlinx.coroutines.launch

/**
 * Expandable Tafsir explanation section for an individual Ayah.
 * Fetches and displays authentic Tafsir (Ibn Kathir, Ahsanul Bayaan, Jalalayn, Brief Context)
 * with Shaan-e-Nuzul, key takeaways, and interactive actions.
 */
@Composable
fun ExpandableAyahTafsirSection(
    ayah: AyahItem,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    var selectedSource by remember { mutableStateOf(TafsirSource.IBN_KATHIR) }
    var currentTafsir by remember { mutableStateOf<TafsirEntry?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Fetch Tafsir when expanded or when switching source
    LaunchedEffect(isExpanded, selectedSource, ayah) {
        if (isExpanded) {
            isLoading = true
            currentTafsir = QuranTafsirRepository.getTafsirForAyah(
                surahNumber = ayah.surahNumber,
                ayahNumber = ayah.ayahNumberInSurah,
                source = selectedSource,
                ayah = ayah
            )
            isLoading = false
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Expand / Collapse Trigger Pill
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = if (isExpanded) IslamicEmeraldPrimary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(
                width = 1.dp,
                color = if (isExpanded) IslamicEmeraldPrimary.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .clickable { onToggleExpand() }
                .testTag("ayah_${ayah.ayahNumberInSurah}_tafsir_toggle")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (isExpanded) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = if (isExpanded) Color.White else IslamicEmeraldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (isExpanded) "তাফসীর ও ব্যাখ্যা (Tafsir & Exegesis)" else "তাফসীর দেখুন (View Tafsir)",
                        fontSize = 12.sp,
                        fontWeight = if (isExpanded) FontWeight.Bold else FontWeight.Medium,
                        color = if (isExpanded) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurface
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!isExpanded && ayah.banglaTafsir.isNotBlank()) {
                        Text(
                            text = "সংক্ষিপ্ত ব্যাখ্যা লভ্য",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = if (isExpanded) IslamicEmeraldPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Expanded Content Container
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(animationSpec = tween(280)) + fadeIn(animationSpec = tween(280)),
            exit = shrinkVertically(animationSpec = tween(220)) + fadeOut(animationSpec = tween(220))
        ) {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f)
                ),
                border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .testTag("ayah_${ayah.ayahNumberInSurah}_tafsir_expanded_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    // 1. Tafsir Source Selection Chips
                    Text(
                        text = "তাফসীর গ্রন্থ নির্বাচন (Select Source):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        TafsirSource.values().forEach { src ->
                            val isSelected = selectedSource == src
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.surface,
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = if (isSelected) IslamicEmeraldPrimary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedSource = src }
                            ) {
                                Box(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = src.shortName,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = IslamicEmeraldPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        val tafsir = currentTafsir
                        if (tafsir != null) {
                            // 2. Brief Summary Card
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = IslamicEmeraldContainer.copy(alpha = 0.45f),
                                border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = QuranGoldDark,
                                        modifier = Modifier
                                            .size(16.dp)
                                            .padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "সংক্ষিপ্ত সার ও মূলবার্তা (Core Essence):",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldDark
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = tafsir.briefSummary,
                                            fontSize = 12.sp,
                                            lineHeight = 17.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // 3. Detailed Exegesis Body
                            Text(
                                text = "বিস্তারিত ব্যাখ্যা (${tafsir.sourceName}):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tafsir.detailedExplanation,
                                fontSize = 12.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            // 4. Shaan-e-Nuzul (Context of Revelation) if available
                            if (tafsir.revelationContext.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(0.8.dp, QuranGold.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.HistoryEdu,
                                                contentDescription = null,
                                                tint = QuranGoldDark,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "শানে নুযূল (Context of Revelation)",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = QuranGoldDark
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = tafsir.revelationContext,
                                            fontSize = 11.sp,
                                            lineHeight = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // 5. Key Lessons / শিক্ষণীয় বিষয়সমূহ
                            if (tafsir.keyLessons.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "বাস্তব জীবনের শিক্ষা (Takeaways):",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicEmeraldPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                tafsir.keyLessons.forEach { lesson ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp),
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Text(
                                            text = "• ",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicEmeraldPrimary
                                        )
                                        Text(
                                            text = lesson,
                                            fontSize = 11.sp,
                                            lineHeight = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // 6. Authentic Hadith Reference
                            if (tafsir.authenticHadithRef.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = IslamicEmeraldContainer.copy(alpha = 0.2f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Verified,
                                            contentDescription = null,
                                            tint = IslamicEmeraldPrimary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "হাদীস রেফারেন্স: ${tafsir.authenticHadithRef}",
                                            fontSize = 10.sp,
                                            color = IslamicEmeraldDark,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 7. Interactive Action Bar (Copy, Add Note, Collapse)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Copy Tafsir Button
                                    OutlinedButton(
                                        onClick = {
                                            val copyText = "তাফসীর: সূরা ${ayah.surahNumber}, আয়াত ${ayah.ayahNumberInSurah}\n\n" +
                                                    "মূলবার্তা: ${tafsir.briefSummary}\n\n" +
                                                    "ব্যাখ্যা (${tafsir.sourceName}):\n${tafsir.detailedExplanation}"
                                            clipboardManager.setText(AnnotatedString(copyText))
                                            Toast.makeText(context, "তাফসীর কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "কপি", fontSize = 11.sp)
                                    }

                                    // Add Note Button
                                    OutlinedButton(
                                        onClick = onAddNoteClick,
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.EditNote,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "নোট", fontSize = 11.sp)
                                    }
                                }

                                TextButton(
                                    onClick = onToggleExpand,
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Text(
                                        text = "সংক্ষেপ করুন ▴",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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
