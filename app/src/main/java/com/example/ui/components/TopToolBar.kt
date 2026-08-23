package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalExtendedTheme
import com.example.ui.theme.QuranGold

@Composable
fun TopToolBar(
    title: String = "Al Quran",
    isSearchActive: Boolean,
    searchQuery: String,
    isNightMode: Boolean = false,
    onToggleNightMode: () -> Unit = {},
    onOpenThemeSelector: () -> Unit = {},
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onTitleClick: () -> Unit,
    onMenuClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedTheme = LocalExtendedTheme.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(extendedTheme.primaryHeaderGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Menu Drawer Button
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("menu_drawer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Open Navigation Drawer",
                        tint = Color.White
                    )
                }

                // Center Clickable Title
                Surface(
                    onClick = onTitleClick,
                    color = Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("toolbar_title_clickable")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 19.sp
                            )
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Jump to Ayah",
                            tint = QuranGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Right Action Buttons: Streak, Night Mode Toggle, Theme Palette, Search
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Global Theme Palette Button
                    IconButton(
                        onClick = onOpenThemeSelector,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("toolbar_theme_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = "Change Theme",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Quick Night Mode Toggle Button (Moon / Sun)
                    IconButton(
                        onClick = onToggleNightMode,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("toolbar_night_mode_button")
                    ) {
                        Icon(
                            imageVector = if (isNightMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = if (isNightMode) "Switch to Light Mode" else "Switch to Night Mode",
                            tint = if (isNightMode) QuranGold else Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Search Icon with click animation
                    IconButton(
                        onClick = onSearchToggle,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("toolbar_search_button")
                    ) {
                        Icon(
                            imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Animated Expandable Search Bar
            AnimatedVisibility(
                visible = isSearchActive,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = {
                            Text(
                                "সূরা, আয়াত, পৃষ্ঠা বা অর্থ খুঁজুন...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchQueryChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("main_search_input")
                    )
                }
            }
        }
    }
}
