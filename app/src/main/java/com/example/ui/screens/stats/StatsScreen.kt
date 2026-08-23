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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CloudSyncStatus
import com.example.data.model.UserProfile
import com.example.data.model.UserQuranCloudData
import com.example.ui.components.FirebaseAuthStatsCard
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold

@Composable
fun StatsScreen(
    currentStreakDays: Int,
    readTodayMinutes: Int,
    readTargetMinutes: Int,
    weeklyStats: List<Pair<String, Int>>,
    currentUser: UserProfile? = null,
    cloudSyncStatus: CloudSyncStatus = CloudSyncStatus(),
    cloudUserData: UserQuranCloudData? = null,
    onSignInWithGoogle: () -> Unit = {},
    onQuickSignIn: () -> Unit = {},
    onSyncNow: () -> Unit = {},
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Surahs Completed", fontSize = 13.sp, color = Color.Gray)
                    Text("4 Surahs", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = IslamicEmeraldPrimary)
                }

                Divider(modifier = Modifier.padding(vertical = 8.dp))

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
}
