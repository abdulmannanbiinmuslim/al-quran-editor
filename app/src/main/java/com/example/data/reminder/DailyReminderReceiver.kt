package com.example.data.reminder

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity

class DailyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val reminderText = intent?.getStringExtra("reminder_text")
            ?: "সময় হয়েছে আজকের কুরআন তিলাওয়াতের! আপনার রুটিন বজায় রাখুন ✨"

        DailyReminderManager.initNotificationChannel(context)

        val activityIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, DailyReminderManager.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("দৈনিক কুরআন পাঠ রিমাইন্ডার 📖")
            .setContentText(reminderText)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "$reminderText\n\nপ্রতিদিনের তিলাওয়াত আপনার ঈমানকে সতেজ ও অন্তরে প্রশান্তি দান করে।"
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1001, builder.build())
        } catch (e: SecurityException) {
            // Notification permission missing
        }
    }
}
