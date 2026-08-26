package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.model.AyahItem
import com.example.data.model.BookmarkFolder
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight
import com.example.ui.util.rememberAppHaptics

/**
 * Custom Bookmark and Pin Collection Management Bottom Sheet
 * Styled according to design shown in the UI screenshot.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkBottomSheet(
    ayah: AyahItem?,
    folders: List<BookmarkFolder>,
    selectedFolderIds: Set<String>,
    onCreateFolder: (name: String, colorHex: String, iconType: String) -> Unit,
    onSaveSelection: (selectedIds: Set<String>) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (ayah == null) return

    val haptics = rememberAppHaptics()
    var currentSelection by remember(selectedFolderIds) { mutableStateOf(selectedFolderIds) }
    var showCreateDialog by remember { mutableStateOf(false) }

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
            // Header Title
            Text(
                text = "Bookmark",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            // + Create New Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        haptics.tap()
                        showCreateDialog = true
                    }
                    .padding(vertical = 10.dp)
                    .testTag("bookmark_create_new_button")
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircleOutline,
                    contentDescription = "Create New",
                    tint = IslamicEmeraldPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Create New",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = IslamicEmeraldPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Subheader: Pins
            Text(
                text = "Pins",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Create a new pin folder to pin an ayah",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // Subheader: Collections
            Text(
                text = "Collections",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // List of Bookmark Folders / Collections
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .heightIn(max = 320.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(folders, key = { it.id }) { folder ->
                    val isChecked = currentSelection.contains(folder.id)
                    val folderColor = try {
                        Color(android.graphics.Color.parseColor(folder.colorHex))
                    } catch (e: Exception) {
                        IslamicEmeraldPrimary
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                haptics.tap()
                                currentSelection = if (isChecked) {
                                    currentSelection - folder.id
                                } else {
                                    currentSelection + folder.id
                                }
                            }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                haptics.tap()
                                currentSelection = if (checked) {
                                    currentSelection + folder.id
                                } else {
                                    currentSelection - folder.id
                                }
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = IslamicEmeraldPrimary,
                                uncheckedColor = MaterialTheme.colorScheme.outlineVariant
                            )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Folder icon with custom color
                        val iconVector = when (folder.iconType) {
                            "clock" -> Icons.Default.AccessTime
                            "star" -> Icons.Default.Star
                            "heart" -> Icons.Default.Favorite
                            "pin" -> Icons.Default.PushPin
                            else -> Icons.Default.Bookmark
                        }

                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = folderColor,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = folder.name,
                            fontSize = 14.sp,
                            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom Buttons (Cancel & Done)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        haptics.tap()
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text("Cancel", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = {
                        haptics.celebration()
                        onSaveSelection(currentSelection)
                        onDismiss()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("bookmark_done_button"),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IslamicEmeraldPrimary,
                        contentColor = Color.White
                    )
                ) {
                    Text("Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    // Dialog for creating a new custom bookmark collection
    if (showCreateDialog) {
        var folderNameInput by remember { mutableStateOf("") }
        var selectedColorHex by remember { mutableStateOf("#1E563F") }
        var selectedIconType by remember { mutableStateOf("bookmark") }

        val colorOptions = listOf(
            "#1E563F" to "Green",
            "#1976D2" to "Blue",
            "#D4AF37" to "Gold",
            "#7B1FA2" to "Purple",
            "#D84315" to "Amber",
            "#00897B" to "Teal"
        )

        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = {
                Text(
                    text = "নতুন বুকমার্ক ফোল্ডার তৈরি করুন",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = IslamicEmeraldPrimary
                )
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "ফোল্ডারের নাম দিন যাতে পছন্দের আয়াতগুলো সাজিয়ে রাখতে পারেন:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = folderNameInput,
                        onValueChange = { folderNameInput = it },
                        placeholder = { Text("যেমন: দো'আ ও মোনাজাত, তাহাজ্জুদ...") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("new_folder_name_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "কালার নির্বাচন করুন:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        colorOptions.forEach { (hex, _) ->
                            val color = Color(android.graphics.Color.parseColor(hex))
                            val isSelected = selectedColorHex == hex
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable { selectedColorHex = hex }
                                    .then(
                                        if (isSelected) Modifier.border(3.dp, QuranGold, CircleShape)
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (folderNameInput.isNotBlank()) {
                            haptics.tap()
                            onCreateFolder(folderNameInput.trim(), selectedColorHex, selectedIconType)
                            showCreateDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                    shape = RoundedCornerShape(8.dp),
                    enabled = folderNameInput.isNotBlank()
                ) {
                    Text("তৈরি করুন (Create)")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) {
                    Text("বাতিল (Cancel)")
                }
            }
        )
    }
}
