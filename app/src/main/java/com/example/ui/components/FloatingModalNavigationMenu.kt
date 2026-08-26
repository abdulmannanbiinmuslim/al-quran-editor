package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.*

data class NavCategory(
    val id: String,
    val title: String,
    val banglaTitle: String,
    val icon: ImageVector,
    val items: List<NavMenuItem>
)

data class NavMenuItem(
    val id: String,
    val title: String,
    val banglaSubtitle: String,
    val icon: ImageVector
)

@Composable
fun FloatingModalNavigationMenu(
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit,
    onSocialClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            // Floating Modal Card Container (Google Chrome / Floating Panel Style)
            Surface(
                modifier = modifier
                    .widthIn(max = 390.dp)
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.88f)
                    .clickable(enabled = false) {} // Prevent click-through
                    .shadow(
                        elevation = 18.dp,
                        shape = RoundedCornerShape(26.dp),
                        spotColor = IslamicEmeraldPrimary.copy(alpha = 0.5f),
                        ambientColor = Color.Black.copy(alpha = 0.3f)
                    ),
                shape = RoundedCornerShape(26.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                border = BorderStroke(1.5.dp, IslamicEmeraldPrimary.copy(alpha = 0.25f))
            ) {
                FloatingModalNavigationMenuContent(
                    onDismiss = onDismiss,
                    onItemClick = { id ->
                        onItemClick(id)
                        onDismiss()
                    },
                    onSocialClick = onSocialClick
                )
            }
        }
    }
}

@Composable
fun FloatingModalNavigationMenuContent(
    onDismiss: () -> Unit,
    onItemClick: (String) -> Unit,
    onSocialClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val extendedTheme = LocalExtendedTheme.current

    // Categorized Subject-Wise Menu Structure (শাখাসমূহ)
    val categories = remember {
        listOf(
            NavCategory(
                id = "cat_quran",
                title = "Quran & Reading",
                banglaTitle = "কুরআন তিলাওয়াত ও পাঠ",
                icon = Icons.Outlined.MenuBook,
                items = listOf(
                    NavMenuItem("jump_to_ayah", "Jump to Ayah / Destination", "সূরা, আয়াত, পৃষ্ঠা ও পারায় যান", Icons.Outlined.Navigation),
                    NavMenuItem("theme_night_mode", "Themes & Night Mode", "থিম ও নাইট মোড পরিবর্তন", Icons.Outlined.Palette),
                    NavMenuItem("font_studio", "Arabic Fonts & Typography", "আরবি ফন্ট ও ফন্ট সাইজ কাস্টমাইজেশন", Icons.Default.Edit),
                    NavMenuItem("view_tutorials", "Tajweed Rules & Guide", "তাজবীদ কালার কোডিং নির্দেশিকা", Icons.Outlined.School),
                    NavMenuItem("dictionary", "Quranic Dictionary", "কুরআনিক শব্দকোষ ও অর্থ", Icons.Outlined.LibraryBooks)
                )
            ),
            NavCategory(
                id = "cat_audio",
                title = "Audio & Reciters",
                banglaTitle = "অডিও ও তিলাওয়াত স্টেশন",
                icon = Icons.Outlined.Headphones,
                items = listOf(
                    NavMenuItem("reciter_player", "Reciter Audio Player", "ক্বারী নির্বাচন ও পূর্ণ কুরআন অডিও", Icons.Outlined.PlayCircleFilled),
                    NavMenuItem("timing_sync", "Audio Timing Sync (.lrc / .srt)", "অডিও ওয়ার্ড বাই ওয়ার্ড টাইমিং", Icons.Outlined.Timer),
                    NavMenuItem("settings", "Audio & Auto-Scroll Speed", "অটো-স্ক্রোল ও গতি নিয়ন্ত্রণ", Icons.Outlined.Speed)
                )
            ),
            NavCategory(
                id = "cat_tools",
                title = "Islamic Daily Tools",
                banglaTitle = "ইসলামিক দৈনন্দিন ফিচার",
                icon = Icons.Outlined.Mosque,
                items = listOf(
                    NavMenuItem("salat_times", "Salat Times & Qibla", "নামাজের সময়সূচী", Icons.Outlined.Schedule),
                    NavMenuItem("notifications", "Daily Reminders & Notifications", "দৈনিক কুরআন আয়াত রিমাইন্ডার", Icons.Outlined.Notifications),
                    NavMenuItem("planner", "Khatam & Reading Planner", "খতম ও তিলাওয়াত প্ল্যানার", Icons.Outlined.EventNote),
                    NavMenuItem("bookmarks", "Bookmarks & Pinned Ayahs", "বুকমার্ক এবং সংরক্ষিত আয়াত", Icons.Outlined.Bookmarks)
                )
            ),
            NavCategory(
                id = "cat_support",
                title = "Settings & Support",
                banglaTitle = "অ্যাপ সেটিংস ও সমর্থন",
                icon = Icons.Outlined.Settings,
                items = listOf(
                    NavMenuItem("settings", "General Settings", "অ্যাপের সাধারণ সেটিংস", Icons.Outlined.Tune),
                    NavMenuItem("cloud_sync", "Cloud Backup & Sync", "গুগল সাইন-ইন ও ডাটা ব্যাকআপ", Icons.Outlined.CloudSync),
                    NavMenuItem("rate_app", "Rate App", "প্লে-স্টোরে রেটিং দিন", Icons.Outlined.StarRate),
                    NavMenuItem("talk_with_us", "Talk with Us", "আমাদের সাথে যোগাযোগ করুন", Icons.Outlined.Chat),
                    NavMenuItem("help_translate", "Help Us Translate", "অনুবাদে সহায়তা করুন", Icons.Outlined.Translate),
                    NavMenuItem("share_app", "Share App", "বন্ধুদের সাথে শেয়ার করুন", Icons.Outlined.Share),
                    NavMenuItem("help_support", "Help & Support", "সহায়তা ও প্রশ্নোত্তর", Icons.Outlined.HelpOutline),
                    NavMenuItem("give_feedback", "Give Feedback", "মতামত ও পরামর্শ পাঠান", Icons.Outlined.Feedback),
                    NavMenuItem("other_apps", "Our Other Apps", "আমাদের অন্যান্য ইসলামিক অ্যাপস", Icons.Outlined.Apps)
                )
            )
        )
    }

    // State to track expanded accordion categories
    var expandedCategories by remember {
        mutableStateOf(setOf("cat_quran", "cat_audio", "cat_tools", "cat_support"))
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // ----------------------------------------------------
        // 1. TOP BRANDING HEADER LAYER WITH PROMINENT DROP SHADOW
        // ----------------------------------------------------
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                    spotColor = IslamicEmeraldPrimary.copy(alpha = 0.35f)
                ),
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(extendedTheme.primaryHeaderGradient)
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Brand Icon Container with decorative frame
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .padding(3.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_quran_brand),
                                contentDescription = "Quran Brand Icon",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "আল-কুরআনুল কারীম",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Al Quran Al Kareem",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = QuranGoldLight,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "Audio Station & Study Platform",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    // Close Dialog Icon Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Menu",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        // ----------------------------------------------------
        // 2. MIDDLE SCROLLABLE SECTION (SUBJECT-WISE DROPDOWNS)
        // ----------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            categories.forEach { category ->
                val isExpanded = expandedCategories.contains(category.id)
                val rotationAngle by animateFloatAsState(
                    targetValue = if (isExpanded) 180f else 0f,
                    animationSpec = tween(durationMillis = 250),
                    label = "chevron_rotation"
                )

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExpanded)
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        else
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isExpanded) IslamicEmeraldPrimary.copy(alpha = 0.35f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Category Header (Dropdown toggle)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedCategories = if (isExpanded) {
                                        expandedCategories - category.id
                                    } else {
                                        expandedCategories + category.id
                                    }
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                                .testTag("menu_category_${category.id}"),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(IslamicEmeraldContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = category.icon,
                                        contentDescription = category.title,
                                        tint = IslamicEmeraldPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = category.banglaTitle,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontSize = 14.sp
                                        )
                                    )
                                    Text(
                                        text = category.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = IslamicEmeraldPrimary.copy(alpha = 0.12f),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "${category.items.size}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = IslamicEmeraldPrimary,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .rotate(rotationAngle)
                                )
                            }
                        }

                        // Collapsible Child Items
                        AnimatedVisibility(
                            visible = isExpanded,
                            enter = expandVertically() + fadeIn(),
                            exit = shrinkVertically() + fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp)
                            ) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                                    thickness = 0.8.dp,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )

                                category.items.forEachIndexed { index, item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onItemClick(item.id) }
                                            .padding(horizontal = 14.dp, vertical = 10.dp)
                                            .testTag("drawer_item_${item.id}"),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = item.title,
                                            tint = IslamicEmeraldPrimary,
                                            modifier = Modifier.size(19.dp)
                                        )

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    fontSize = 13.5.sp
                                                )
                                            )
                                            Text(
                                                text = item.banglaSubtitle,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontSize = 10.5.sp
                                                )
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.outlineVariant,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    if (index < category.items.size - 1) {
                                        HorizontalDivider(
                                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                                            modifier = Modifier.padding(horizontal = 14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // ----------------------------------------------------
        // 3. BOTTOM SOCIAL MEDIA LAYER WITH CAST UPWARD SHADOW
        // ----------------------------------------------------
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    spotColor = Color.Black.copy(alpha = 0.25f)
                ),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "যুক্ত থাকুন ও শেয়ার করুন (Stay Connected)",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FloatingSocialButton(
                        label = "Website",
                        icon = Icons.Outlined.Language,
                        onClick = { onSocialClick("web") }
                    )
                    FloatingSocialButton(
                        label = "Facebook",
                        icon = Icons.Outlined.ThumbUp,
                        onClick = { onSocialClick("fb") }
                    )
                    FloatingSocialButton(
                        label = "Twitter / X",
                        icon = Icons.Outlined.Tag,
                        onClick = { onSocialClick("x") }
                    )
                    FloatingSocialButton(
                        label = "Instagram",
                        icon = Icons.Outlined.CameraAlt,
                        onClick = { onSocialClick("instagram") }
                    )
                    FloatingSocialButton(
                        label = "Share",
                        icon = Icons.Outlined.Share,
                        onClick = { onSocialClick("share") }
                    )
                }
            }
        }
    }
}

@Composable
private fun FloatingSocialButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = IslamicEmeraldContainer,
        border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.3f)),
        modifier = Modifier
            .size(42.dp)
            .shadow(2.dp, CircleShape)
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
