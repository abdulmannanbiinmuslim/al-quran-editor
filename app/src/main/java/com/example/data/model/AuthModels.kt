package com.example.data.model

data class UserProfile(
    val uid: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val isGoogleLinked: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

data class CloudSyncStatus(
    val lastSyncedAt: Long = 0L,
    val isSyncing: Boolean = false,
    val syncMessage: String? = null,
    val totalItemsSynced: Int = 0,
    val isOnline: Boolean = true
)

data class UserQuranCloudData(
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val streakDays: Int = 0,
    val readTodayMinutes: Int = 0,
    val readTargetMinutes: Int = 15,
    val totalVersesRead: Int = 427,
    val surahsCompleted: Int = 4,
    val totalListeningMinutes: Int = 185,
    val pinnedAyahsCount: Int = 2,
    val notesCount: Int = 1,
    val lastReadSurah: Int = 1,
    val lastReadAyah: Int = 1,
    val weeklyStatsMap: Map<String, Int> = emptyMap(),
    val fontName: String = "UTHMANIC_HAFS",
    val fontSizeSp: Float = 28f,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
