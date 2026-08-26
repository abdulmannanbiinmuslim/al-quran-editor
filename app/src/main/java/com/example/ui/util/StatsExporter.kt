package com.example.ui.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.WeeklyReadingSummary
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

object StatsExporter {

    fun generateTextReport(
        weeklySummary: WeeklyReadingSummary,
        currentStreakDays: Int,
        totalVersesRead: Int = 427,
        surahsCompleted: Int = 4,
        totalListeningMinutes: Int = 185
    ): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val builder = StringBuilder()
        builder.append("═══════════════════════════════════════\n")
        builder.append("       📖 আল-কুরআন পাঠক অগ্রগতি রিপোর্ট       \n")
        builder.append("      Quran Reading Progress Summary   \n")
        builder.append("═══════════════════════════════════════\n")
        builder.append("তারিখ (Date): $currentDate\n\n")

        builder.append("📊 সামগ্রিক পরিসংখ্যান (Lifetime Summary):\n")
        builder.append("• মোট তিলাওয়াতকৃত আয়াত: $totalVersesRead টি\n")
        builder.append("• সম্পন্নকৃত সূরা: $surahsCompleted টি\n")
        builder.append("• তিলাওয়াত ধারাবাহিকতা (Streak): $currentStreakDays দিন 🔥\n")
        builder.append("• মোট অডিও শোনার সময়: $totalListeningMinutes মিনিট 🎧\n\n")

        builder.append("📅 বিগত ৭ দিনের সাপ্তাহিক বিবরণী (Weekly Log):\n")
        builder.append("───────────────────────────────────────\n")
        builder.append(String.format("%-6s | %-8s | %-12s | %-8s\n", "দিন", "তারিখ", "আয়াত সংখ্যা", "সময়"))
        builder.append("───────────────────────────────────────\n")

        weeklySummary.metrics.forEach { metric ->
            val status = if (metric.isCompleted) "✅" else "⏳"
            builder.append(
                String.format(
                    "%-4s %s | %-8s | %-4d/%-4d আয়াত | %2d মিনিট\n",
                    metric.dayOfWeek,
                    status,
                    metric.dateLabel,
                    metric.versesCount,
                    metric.goalVerses,
                    metric.minutesSpent
                )
            )
        }

        builder.append("───────────────────────────────────────\n")
        builder.append("• এই সপ্তাহে মোট আয়াত: ${weeklySummary.totalVersesThisWeek} টি\n")
        builder.append("• দৈনিক গড় আয়াত: ${weeklySummary.averageVersesPerDay} টি\n")
        builder.append("• মোট পঠিত সময়: ${weeklySummary.totalMinutesThisWeek} মিনিট\n")
        builder.append("═══════════════════════════════════════\n")
        builder.append("\"তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সেই, যে নিজে কুরআন শিখে এবং অন্যকে শেখায়।\" (সহীহ বুখারী)")

        return builder.toString()
    }

    fun exportAsTextFile(
        context: Context,
        weeklySummary: WeeklyReadingSummary,
        currentStreakDays: Int,
        totalVersesRead: Int = 427,
        surahsCompleted: Int = 4,
        totalListeningMinutes: Int = 185
    ) {
        val report = generateTextReport(
            weeklySummary,
            currentStreakDays,
            totalVersesRead,
            surahsCompleted,
            totalListeningMinutes
        )

        try {
            val fileName = "Quran_Reading_Progress_${System.currentTimeMillis()}.txt"
            val file = File(context.cacheDir, fileName)
            val writer = FileWriter(file)
            writer.write(report)
            writer.flush()
            writer.close()

            val uri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "আমার কুরআন তিলাওয়াত অগ্রগতি রিপোর্ট")
                putExtra(Intent.EXTRA_TEXT, report)
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(Intent.createChooser(shareIntent, "অগ্রগতি রিপোর্ট এক্সপোর্ট করুন (Export Report)"))
        } catch (e: Exception) {
            // Fallback to plain text share
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "কুরআন তিলাওয়াত অগ্রগতি রিপোর্ট")
                putExtra(Intent.EXTRA_TEXT, report)
            }
            context.startActivity(Intent.createChooser(shareIntent, "অগ্রগতি রিপোর্ট শেয়ার করুন"))
        }
    }

    fun exportAsFormattedReport(
        context: Context,
        weeklySummary: WeeklyReadingSummary,
        currentStreakDays: Int,
        totalVersesRead: Int = 427,
        surahsCompleted: Int = 4,
        totalListeningMinutes: Int = 185
    ) {
        val report = generateTextReport(
            weeklySummary,
            currentStreakDays,
            totalVersesRead,
            surahsCompleted,
            totalListeningMinutes
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Quran Reading Progress Report - ${SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())}")
            putExtra(Intent.EXTRA_TEXT, report)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Export Reading Progress (PDF / Text)"))
        Toast.makeText(context, "রিপোর্ট সফলভাবে প্রস্তুত করা হয়েছে!", Toast.LENGTH_SHORT).show()
    }
}
