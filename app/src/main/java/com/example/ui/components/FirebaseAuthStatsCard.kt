package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CloudSyncStatus
import com.example.data.model.UserProfile
import com.example.data.model.UserQuranCloudData
import com.example.ui.theme.*

/**
 * Top Firebase Authentication & Cloud Firestore Persistence Card for Stats Screen.
 */
@Composable
fun FirebaseAuthStatsCard(
    user: UserProfile?,
    syncStatus: CloudSyncStatus,
    cloudData: UserQuranCloudData?,
    onSignInWithGoogle: () -> Unit,
    onQuickSignIn: () -> Unit,
    onSyncNow: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showDataDialog by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "sync_spin")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (user != null) IslamicEmeraldContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.2.dp,
            if (user != null) IslamicEmeraldPrimary.copy(alpha = 0.5f) else QuranGold.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("firebase_auth_stats_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (user != null) {
                // SIGNED IN VIEW
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(IslamicEmeraldPrimary, IslamicEmeraldDark)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (user.displayName ?: "U").take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.displayName ?: "Quran Reader",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified Google User",
                                    tint = IslamicEmeraldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = user.email ?: "Firebase Google Authenticated",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Firestore sync badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = IslamicEmeraldPrimary.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(IslamicEmeraldPrimary)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (syncStatus.isSyncing) "Syncing..." else "Firestore Live",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = IslamicEmeraldPrimary.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(10.dp))

                // Sync status information
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Cloud Persistence: Active",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = IslamicEmeraldDark
                        )
                        Text(
                            text = if (syncStatus.lastSyncedAt > 0) "Last saved to Firestore just now" else "Connected to Firebase",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    TextButton(
                        onClick = { showDataDialog = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Storage,
                            contentDescription = null,
                            tint = IslamicEmeraldPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "View Cloud DB",
                            fontSize = 11.sp,
                            color = IslamicEmeraldPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Action buttons: Sync Now & Sign Out
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onSyncNow,
                        enabled = !syncStatus.isSyncing,
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("stats_sync_now_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .then(if (syncStatus.isSyncing) Modifier.rotate(rotationAngle) else Modifier)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (syncStatus.isSyncing) "Syncing..." else "Sync Stats Now",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = onSignOut,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                        modifier = Modifier.testTag("stats_sign_out_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Sign Out",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Sign Out", fontSize = 12.sp)
                    }
                }
            } else {
                // SIGNED OUT VIEW - PROMINENT CONNECT BANNER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(QuranGold.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudSync,
                                contentDescription = null,
                                tint = QuranGoldDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Firebase Cloud Sync & Database",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = IslamicEmeraldPrimary
                            )
                            Text(
                                text = "Google Sign-In & Firestore Persistence",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = QuranGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "FIREBASE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuranGoldDark,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Sign in to securely backup your Quran reading streak, daily listening time, bookmarks, and font settings with Cloud Firestore across all your devices.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Feature Highlights
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FeaturePill(text = "🔥 Streak Sync", modifier = Modifier.weight(1f))
                    FeaturePill(text = "☁️ Firestore DB", modifier = Modifier.weight(1f))
                    FeaturePill(text = "🔒 Google Auth", modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Sign In Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onSignInWithGoogle,
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("google_sign_in_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Sign in with Google",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = onQuickSignIn,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("quick_connect_button")
                    ) {
                        Text(text = "Quick Connect", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    // Cloud Data Inspection Dialog
    if (showDataDialog && cloudData != null) {
        AlertDialog(
            onDismissRequest = { showDataDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CloudDone,
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cloud Firestore Document",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Path: users/${cloudData.uid}",
                            fontSize = 11.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            color = IslamicEmeraldDark,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    CloudDataRow("User Email", cloudData.email)
                    CloudDataRow("Streak Days", "${cloudData.streakDays} Days")
                    CloudDataRow("Today's Reading", "${cloudData.readTodayMinutes} / ${cloudData.readTargetMinutes} min")
                    CloudDataRow("Total Verses", "${cloudData.totalVersesRead} Verses")
                    CloudDataRow("Surahs Completed", "${cloudData.surahsCompleted} Surahs")
                    CloudDataRow("Listening Time", "${cloudData.totalListeningMinutes} Minutes")
                    CloudDataRow("Selected Font", cloudData.fontName)
                    CloudDataRow("Font Size", "${cloudData.fontSizeSp} sp")
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDataDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
                ) {
                    Text("Done")
                }
            }
        )
    }
}

@Composable
private fun FeaturePill(text: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        modifier = modifier
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
            maxLines = 1
        )
    }
}

@Composable
private fun CloudDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
