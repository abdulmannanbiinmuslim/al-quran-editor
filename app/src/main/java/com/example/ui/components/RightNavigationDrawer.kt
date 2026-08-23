package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

data class DrawerMenuItem(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val actionType: String = ""
)

@Composable
fun RightNavigationDrawerContent(
    onItemClick: (String) -> Unit,
    onSocialClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedTheme = LocalExtendedTheme.current

    val menuItems = listOf(
        DrawerMenuItem("theme_night_mode", "Themes & Night Mode (থিম ও নাইট মোড)", Icons.Outlined.Palette),
        DrawerMenuItem("jump_to_ayah", "Jump to Ayah", Icons.Outlined.Navigation),
        DrawerMenuItem("font_studio", "Arabic Fonts & Typography", Icons.Default.Edit),
        DrawerMenuItem("timing_sync", "Audio Timing Sync (.lrc / .srt)", Icons.Outlined.Timer),
        DrawerMenuItem("notifications", "Notifications", Icons.Outlined.Notifications),
        DrawerMenuItem("salat_times", "Salat Times", Icons.Outlined.Schedule),
        DrawerMenuItem("dictionary", "Dictionary", Icons.Outlined.MenuBook),
        DrawerMenuItem("other_apps", "Our Other Apps", Icons.Outlined.Apps),
        DrawerMenuItem("settings", "Settings", Icons.Outlined.Settings),
        DrawerMenuItem("rate_app", "Rate App", Icons.Outlined.StarRate),
        DrawerMenuItem("talk_with_us", "Talk with Us", Icons.Outlined.Chat),
        DrawerMenuItem("view_tutorials", "View Tutorials", Icons.Outlined.PlayCircleOutline),
        DrawerMenuItem("help_translate", "Help Us Translate", Icons.Outlined.Translate),
        DrawerMenuItem("share_app", "Share App", Icons.Outlined.Share),
        DrawerMenuItem("help_support", "Help & Support", Icons.Outlined.HelpOutline),
        DrawerMenuItem("give_feedback", "Give feedback", Icons.Outlined.Feedback)
    )

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier
            .fillMaxHeight()
            .width(320.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // 1. Icon + Branding Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(extendedTheme.primaryHeaderGradient)
                    .padding(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(QuranGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Quran Logo",
                            tint = QuranGold,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Al Quran",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Text(
                        text = "The Holy Quran & Audio Station",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), thickness = 1.dp)

            // 2. Scrollable Item Selection Layout
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                menuItems.forEach { item ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item.id) }
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                            .testTag("drawer_item_${item.id}")
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 15.sp
                            )
                        )
                    }
                }
            }

            Divider(color = LightDivider, thickness = 1.dp)

            // 3. Social Media Layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SocialIcon(
                    label = "Web",
                    icon = Icons.Outlined.Language,
                    onClick = { onSocialClick("web") }
                )
                SocialIcon(
                    label = "FB",
                    icon = Icons.Outlined.ThumbUp,
                    onClick = { onSocialClick("fb") }
                )
                SocialIcon(
                    label = "X",
                    icon = Icons.Outlined.Tag,
                    onClick = { onSocialClick("x") }
                )
                SocialIcon(
                    label = "Share",
                    icon = Icons.Outlined.Share,
                    onClick = { onSocialClick("share") }
                )
            }
        }
    }
}

@Composable
private fun SocialIcon(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = IslamicEmeraldContainer,
        modifier = Modifier.size(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = IslamicEmeraldPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
