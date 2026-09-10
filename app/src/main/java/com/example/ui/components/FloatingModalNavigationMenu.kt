package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.example.R

data class DrawerMenuItem(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val section: String = "",
    val hasBadge: Boolean = false,
    val badgeCount: Int = 0,
    val iconTint: Color? = null
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
        // Semi-transparent backdrop scrim spanning the entire screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.50f))
                .clickable { onDismiss() },
            contentAlignment = Alignment.CenterStart
        ) {
            // Left Navigation Drawer Card touching absolute top and bottom with rounded right corners (compact width)
            Surface(
                modifier = modifier
                    .fillMaxHeight()
                    .width(260.dp)
                    .clickable(enabled = false) {} // Prevent dismiss when tapping inside card
                    .shadow(
                        elevation = 24.dp,
                        shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 24.dp, bottomEnd = 24.dp),
                        spotColor = Color.Black.copy(alpha = 0.45f),
                        ambientColor = Color.Black.copy(alpha = 0.30f)
                    ),
                shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 24.dp, bottomEnd = 24.dp),
                color = Color.White,
                tonalElevation = 2.dp
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
    // Complete and rich items matching all app features and categories
    val menuItems = remember {
        listOf(
            // Section 1: Main Tools & Navigation
            DrawerMenuItem("jump_to_ayah", "Jump to Ayah", "দ্রুত আয়াত নেভিগেশন", Icons.AutoMirrored.Outlined.OpenInNew, section = "প্রধান বৈশিষ্ট্যসমূহ"),
            DrawerMenuItem("notifications", "Notifications", "দৈনিক তিলাওয়াত রিমাইন্ডার", Icons.Outlined.Notifications, section = "প্রধান বৈশিষ্ট্যসমূহ", hasBadge = true, badgeCount = 1),
            DrawerMenuItem("salat_times", "Salat Times", "নামাজের সঠিক সময়সূচি", Icons.Outlined.Schedule, section = "প্রধান বৈশিষ্ট্যসমূহ"),
            DrawerMenuItem("dictionary", "Dictionary", "শব্দার্থ ও তাফসীর কোষ", Icons.AutoMirrored.Outlined.MenuBook, section = "প্রধান বৈশিষ্ট্যসমূহ"),
            DrawerMenuItem("planner", "Khatam Planner", "কুরআন পাঠের খতম প্ল্যান", Icons.Outlined.EventNote, section = "প্রধান বৈশিষ্ট্যসমূহ"),
            DrawerMenuItem("bookmarks", "Bookmarks & Folders", "সংরক্ষিত আয়াতসমূহ", Icons.Outlined.BookmarkBorder, section = "প্রধান বৈশিষ্ট্যসমূহ"),

            // Section 2: Studio & Customization
            DrawerMenuItem("recitation_modes", "Recitation Modes", "হরফ, শব্দ, আয়াত ও সূরা তিলাওয়াত মোড", Icons.Outlined.RecordVoiceOver, section = "কাস্টমাইজেশন ও অডিও", iconTint = Color(0xFF1E563F)),
            DrawerMenuItem("download_manager", "Surah Download Page", "সূরা ও আয়াত ভিত্তিক অফলাইন ডাউনলোড", Icons.Default.Download, section = "কাস্টমাইজেশন ও অডিও", iconTint = Color(0xFF1E563F)),
            DrawerMenuItem("audio_manager", "Audio Manager", "ক্বারী, অফলাইন ডাউনলোড ও স্পিড", Icons.Outlined.Headphones, section = "কাস্টমাইজেশন ও অডিও", iconTint = Color(0xFF1E563F)),
            DrawerMenuItem("main_settings", "Main Setting Page", "কুরআন ও অ্যাপের পূর্ণাঙ্গ সেটিংস", Icons.Outlined.Settings, section = "কাস্টমাইজেশন ও অডিও", iconTint = Color(0xFF1E563F)),
            DrawerMenuItem("quick_settings", "Reading Quick Settings", "দ্রুত ড্রয়ার ও রিডিং প্রেফারেন্স", Icons.Outlined.Settings, section = "কাস্টমাইজেশন ও অডিও"),
            DrawerMenuItem("theme_night_mode", "Theme & Night Mode", "ডার্ক/লাইট ও গোল্ডেন থিম", Icons.Outlined.Palette, section = "কাস্টমাইজেশন ও অডিও"),
            DrawerMenuItem("font_studio", "Arabic Font & Tajweed", "ফন্ট সাইজ ও তাজবীদ কালার", Icons.Outlined.TextFields, section = "কাস্টমাইজেশন ও অডিও"),
            DrawerMenuItem("reciter_player", "Reciter Audio Station", "বিশ্ববিখ্যাত ক্বারীদের তিলাওয়াত", Icons.Outlined.Headphones, section = "কাস্টমাইজেশন ও অডিও"),
            DrawerMenuItem("timing_sync", "Audio Timing & Sync", "টাইমিং ফাইল জেনারেটর (.lrc/.srt/.vtt)", Icons.Outlined.Sync, section = "কাস্টমাইজেশন ও অডিও"),
            DrawerMenuItem("view_tutorials", "Tajweed Rules Guide", "তাজবীদের সহজ নিয়মাবলী", Icons.Outlined.School, section = "কাস্টমাইজেশন ও অডিও"),
            DrawerMenuItem("cloud_sync", "Cloud Backup & Sync", "ফায়ারবেস ক্লাউড ব্যাকআপ", Icons.Outlined.CloudUpload, section = "কাস্টমাইজেশন ও অডিও"),

            // Section 3: Settings & Community
            DrawerMenuItem("settings", "All Settings", "সাধারণ রিডিং সেটিংস", Icons.Outlined.Settings, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("other_apps", "Our Other Apps", "হাদিস ও দোয়ার অন্যান্য অ্যাপস", Icons.Outlined.PhoneAndroid, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("rate_app", "Rate App!", "৫ স্টার রিভিউ দিন", Icons.Outlined.StarOutline, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("talk_with_us", "Talk with Us", "আমাদের সাথে সরাসরি যোগাযোগ", Icons.Outlined.ChatBubbleOutline, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("help_translate", "Help Us Translate", "অনুবাদ ও পরিমার্জন সহায়তা", Icons.Outlined.Translate, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("share_app", "Share App", "বন্ধুদের সাথে শেয়ার করুন", Icons.Outlined.Share, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("help_support", "Help & Support", "সাহায্য ও সমাধান", Icons.AutoMirrored.Outlined.HelpOutline, section = "অন্যান্য ও সেটিংস"),
            DrawerMenuItem("give_feedback", "Give feedback", "মতামত ও প্রতিক্রিয়া", Icons.Outlined.RateReview, section = "অন্যান্য ও সেটিংস")
        )
    }

    // Group items by section
    val groupedItems = remember(menuItems) {
        menuItems.groupBy { it.section }
    }

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // =========================================================================
        // 2. MIDDLE SECTION: Scrollable Item Selection Layout (under floating layers)
        // =========================================================================
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    top = topInset + 148.dp,
                    bottom = bottomInset + 64.dp
                )
        ) {
            groupedItems.forEach { (sectionName, items) ->
                if (sectionName.isNotEmpty()) {
                    Text(
                        text = sectionName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32),
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 4.dp)
                    )
                }

                items.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onItemClick(item.id) }
                            .padding(horizontal = 20.dp, vertical = 8.5.dp)
                            .testTag("drawer_item_${item.id}"),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFFF3F6F4), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = item.iconTint ?: Color(0xFF2E7D32),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF1F2937),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.5.sp
                                )
                            )
                            if (item.subtitle != null) {
                                Text(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF8A99A8),
                                        fontSize = 10.5.sp
                                    )
                                )
                            }
                        }

                        if (item.hasBadge) {
                            Box(
                                modifier = Modifier
                                    .size(19.dp)
                                    .background(Color(0xFFE53935), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.badgeCount.toString(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(
                    color = Color(0xFFF3F5F7),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }

        // =========================================================================
        // 1. TOP SECTION: Branding Header (flush at top of screen) with Drop Shadow
        // =========================================================================
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .zIndex(5f)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 0.dp, topEnd = 24.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = topInset + 12.dp,
                            bottom = 12.dp,
                            start = 16.dp,
                            end = 16.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        shadowElevation = 4.dp,
                        color = Color.White,
                        border = BorderStroke(1.5.dp, Color(0xFF2E7D32).copy(alpha = 0.35f)),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.quran_calligraphy_icon_1787770939493),
                            contentDescription = "Al Quran Al Kareem Icon",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .testTag("drawer_brand_medallion")
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = "القرآن الكريم",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20),
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "Digital Quran & Tafsir",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF718096),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Visible Floating Drop-Shadow Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.18f),
                                Color.Black.copy(alpha = 0.06f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        // =========================================================================
        // 3. BOTTOM SECTION: Social Media Layout (flush at bottom of screen) with Drop Shadow
        // =========================================================================
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .zIndex(5f)
        ) {
            // Visible Floating Upward Drop-Shadow Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.06f),
                                Color.Black.copy(alpha = 0.18f)
                            )
                        )
                    )
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 24.dp),
                color = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 18.dp,
                            end = 18.dp,
                            top = 10.dp,
                            bottom = bottomInset + 10.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SocialCircleButton(
                        iconRes = R.drawable.ic_social_web,
                        label = "Website",
                        onClick = { onSocialClick("web") }
                    )
                    SocialCircleButton(
                        iconRes = R.drawable.ic_social_fb,
                        label = "Facebook",
                        onClick = { onSocialClick("fb") }
                    )
                    SocialCircleButton(
                        iconRes = R.drawable.ic_social_insta,
                        label = "Instagram",
                        onClick = { onSocialClick("instagram") }
                    )
                    SocialCircleButton(
                        iconRes = R.drawable.ic_social_x,
                        label = "Twitter / X",
                        onClick = { onSocialClick("x") }
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialCircleButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color(0xFFF3F6F4),
        border = BorderStroke(1.dp, Color(0xFFE1E7E3)),
        modifier = Modifier
            .size(36.dp)
            .testTag("social_btn_$label")
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                tint = Color(0xFF1B5E20),
                modifier = Modifier.size(17.dp)
            )
        }
    }
}
