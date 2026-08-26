package com.example.ui.screens.stats

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CloudSyncStatus
import com.example.data.model.DailyReminderSettings
import com.example.data.model.DailyVersesMetric
import com.example.data.model.UserProfile
import com.example.data.model.UserQuranCloudData
import com.example.data.model.WeeklyReadingSummary
import com.example.ui.components.FirebaseAuthStatsCard
import com.example.ui.components.ReadingProgressChartCard
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.util.StatsExporter
import com.example.ui.util.rememberAppHaptics

@Composable
fun StatsScreen(
    currentStreakDays: Int,
    readTodayMinutes: Int,
    readTargetMinutes: Int,
    weeklyStats: List<Pair<String, Int>>,
    weeklyReadingSummary: WeeklyReadingSummary? = null,
    dailyReminderSettings: DailyReminderSettings = DailyReminderSettings(),
    onUpdateDailyReminder: (DailyReminderSettings) -> Unit = {},
    onSendTestReminder: () -> Unit = {},
    currentUser: UserProfile? = null,
    cloudSyncStatus: CloudSyncStatus = CloudSyncStatus(),
    cloudUserData: UserQuranCloudData? = null,
    onSignInWithGoogle: () -> Unit = {},
    onQuickSignIn: () -> Unit = {},
    onSyncNow: () -> Unit = {},
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val haptics = rememberAppHaptics()
    var showExportDialog by remember { mutableStateOf(false) }

    val defaultWeeklySummary = weeklyReadingSummary ?: WeeklyReadingSummary(
        metrics = listOf(
            DailyVersesMetric(dayOfWeek = "Sun", dateLabel = "17 Aug", versesCount = 28, minutesSpent = 15, goalVerses = 30),
            DailyVersesMetric(dayOfWeek = "Mon", dateLabel = "18 Aug", versesCount = 45, minutesSpent = 28, goalVerses = 30),
            DailyVersesMetric(dayOfWeek = "Tue", dateLabel = "19 Aug", versesCount = 18, minutesSpent = 10, goalVerses = 30),
            DailyVersesMetric(dayOfWeek = "Wed", dateLabel = "20 Aug", versesCount = 56, minutesSpent = 35, goalVerses = 30),
            DailyVersesMetric(dayOfWeek = "Thu", dateLabel = "21 Aug", versesCount = 38, minutesSpent = 22, goalVerses = 30),
            DailyVersesMetric(dayOfWeek = "Fri", dateLabel = "22 Aug", versesCount = 85, minutesSpent = 50, goalVerses = 30),
            DailyVersesMetric(dayOfWeek = "Sat", dateLabel = "23 Aug", versesCount = 42, minutesSpent = 26, goalVerses = 30)
        ),
        streakDays = currentStreakDays.coerceAtLeast(1),
        totalVersesThisWeek = 312,
        averageVersesPerDay = 44,
        totalMinutesThisWeek = 186,
        goalVersesDaily = 30
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // TOP: Firebase Authentication & Cloud Firestore Persistence
        FirebaseAuthStatsCard(
            user = currentUser,
            syncStatus = cloudSyncStatus,
            cloudData = cloudUserData,
            onSignInWithGoogle = onSignInWithGoogle,
            onQuickSignIn = onQuickSignIn,
            onSyncNow = onSyncNow,
            onSignOut = onSignOut
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Canvas-based Reading Progress Metrics Visualization
        ReadingProgressChartCard(
            weeklySummary = defaultWeeklySummary,
            onViewFullStats = { }
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Export Reading Progress Metrics Button Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(IslamicEmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.FileDownload,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Export Progress Metrics",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "PDF / Text report for personal tracking",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Button(
                    onClick = {
                        haptics.tap()
                        showExportDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("export_stats_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Daily Reminder Notification Settings Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(QuranGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = QuranGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Daily Reading Reminder",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            val timeStr = String.format("%02d:%02d", dailyReminderSettings.hour, dailyReminderSettings.minute)
                            Text(
                                text = if (dailyReminderSettings.isEnabled) "সক্রিয় • প্রতিদিন $timeStr টায়" else "নিষ্ক্রিয়",
                                fontSize = 11.sp,
                                color = if (dailyReminderSettings.isEnabled) IslamicEmeraldPrimary else Color.Gray
                            )
                        }
                    }

                    Switch(
                        checked = dailyReminderSettings.isEnabled,
                        onCheckedChange = { isChecked ->
                            haptics.tap()
                            onUpdateDailyReminder(dailyReminderSettings.copy(isEnabled = isChecked))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = IslamicEmeraldPrimary
                        ),
                        modifier = Modifier.testTag("daily_reminder_switch")
                    )
                }

                if (dailyReminderSettings.isEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = {
                                haptics.celebration()
                                onSendTestReminder()
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("test_reminder_button")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Test Notification", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Quran Reading Insights",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = IslamicEmeraldPrimary
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Top Streak & Daily Target Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Streak Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(QuranGold.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = QuranGold,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$currentStreakDays Days",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )

                    Text(
                        text = "Current Streak",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            // Daily Target Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.weight(1f)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(IslamicEmeraldContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Timer,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "$readTodayMinutes / $readTargetMinutes Min",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )

                    Text(
                        text = "Today's Target",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Weekly Activity Bar Chart
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Weekly Activity (Minutes)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val maxMinutes = weeklyStats.maxOfOrNull { it.second }?.coerceAtLeast(1) ?: 30

                    weeklyStats.forEach { (day, minutes) ->
                        val barHeightFactor = (minutes.toFloat() / maxMinutes.toFloat()).coerceIn(0.08f, 1f)

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${minutes}m",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (minutes > 0) IslamicEmeraldPrimary else Color.Gray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Box(
                                modifier = Modifier
                                    .width(18.dp)
                                    .fillMaxHeight(barHeightFactor)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(if (minutes > 0) IslamicEmeraldPrimary else Color.LightGray.copy(alpha = 0.5f))
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = day,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lifetime Stats
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Overall Quran Progress",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Verses Read", fontSize = 13.sp, color = Color.Gray)
                    Text("427 Verses", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Surahs Completed", fontSize = 13.sp, color = Color.Gray)
                    Text("4 Surahs", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Listening Time", fontSize = 13.sp, color = Color.Gray)
                    Text("185 Minutes", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = QuranGold)
                }
            }
        }
    }

    // Export Options Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text("অগ্রগতি রিপোর্ট এক্সপোর্ট", fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "আপনার সাপ্তাহিক ও সামগ্রিক কুরআন পাঠের মেট্রিক্স টেক্সট ফাইল বা শেয়ারেবল ফরম্যাটে এক্সপোর্ট করুন:",
                        fontSize = 13.sp
                    )

                    OutlinedButton(
                        onClick = {
                            showExportDialog = false
                            StatsExporter.exportAsTextFile(
                                context = context,
                                weeklySummary = defaultWeeklySummary,
                                currentStreakDays = currentStreakDays
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, tint = IslamicEmeraldPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export as Text File (.txt)")
                    }

                    Button(
                        onClick = {
                            showExportDialog = false
                            StatsExporter.exportAsFormattedReport(
                                context = context,
                                weeklySummary = defaultWeeklySummary,
                                currentStreakDays = currentStreakDays
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share Summary Report")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}
