package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.data.repository.RecitersData
import com.example.data.timing.TimingGenerator
import com.example.ui.theme.IslamicEmeraldPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyahOptionsBottomSheet(
    ayah: AyahItem?,
    surahNumber: Int = 1,
    currentReciter: ReciterItem = RecitersData.recitersList[2],
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onAddToPlanner: () -> Unit,
    onTafsirNoteView: () -> Unit,
    onPlayAyah: () -> Unit,
    onOpenTimingSync: () -> Unit = {},
    onAddBookmark: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (ayah == null) return
    val context = LocalContext.current
    val downloadUrl = TimingGenerator.generateSingleAyahDownloadLink(currentReciter, surahNumber, ayah.ayahNumberInSurah)

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
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Options • Ayah ${ayah.ayahNumberInSurah}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = IslamicEmeraldPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OptionRow(
                title = "Copy",
                icon = Icons.Outlined.ContentCopy,
                onClick = {
                    onCopy()
                    onDismiss()
                }
            )

            OptionRow(
                title = "Share",
                icon = Icons.Outlined.Share,
                onClick = {
                    onShare()
                }
            )

            OptionRow(
                title = "Copy Audio Download Link",
                icon = Icons.Outlined.Link,
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Ayah Audio Download Link", downloadUrl)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "ডাউনলোড লিংক কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                    onDismiss()
                }
            )

            OptionRow(
                title = "Download Ayah Audio (MP3)",
                icon = Icons.Outlined.Download,
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "URL: $downloadUrl", Toast.LENGTH_LONG).show()
                    }
                    onDismiss()
                }
            )

            OptionRow(
                title = "Planner",
                icon = Icons.Outlined.EventNote,
                onClick = {
                    onAddToPlanner()
                    onDismiss()
                }
            )

            OptionRow(
                title = "Tafsir / Note View",
                icon = Icons.Outlined.MenuBook,
                onClick = {
                    onTafsirNoteView()
                }
            )

            OptionRow(
                title = "Timing File Generator (.lrc / .srt)",
                icon = Icons.Outlined.Timer,
                onClick = {
                    onDismiss()
                    onOpenTimingSync()
                }
            )

            OptionRow(
                title = "Play this Ayah",
                icon = Icons.Outlined.PlayCircleOutline,
                onClick = {
                    onPlayAyah()
                    onDismiss()
                }
            )

            OptionRow(
                title = "Add Bookmark",
                icon = Icons.Outlined.BookmarkBorder,
                onClick = {
                    onAddBookmark()
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
private fun OptionRow(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp)
            .testTag("option_${title.lowercase().replace(" ", "_")}")
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = IslamicEmeraldPrimary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
