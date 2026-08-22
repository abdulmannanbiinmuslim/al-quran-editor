package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.*

@Composable
fun TajweedRulesDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tajweed Rules (তাজবীদের নিয়ম)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicEmeraldPrimary
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                TajweedCard(
                    title = "Ghunna (Nasalisation)",
                    banglaTitle = "ওয়াজিব গুন্নাহ",
                    badgeColor = TajweedGhunna,
                    arabicExample = "إِنَّهَا عَلَيْهِم مُّؤْصَدَةٌ",
                    description = "নুন বা মীমের ওপর তাশদীদ থাকলে স্পষ্ট নাসিক্য ধ্বনি বা গুন্নাহ করে পড়তে হয়।"
                )

                Spacer(modifier = Modifier.height(10.dp))

                TajweedCard(
                    title = "Ikhfa'a (Hiding / Lenition)",
                    banglaTitle = "ইখফা (গোপন করে পড়া)",
                    badgeColor = TajweedIkhfa,
                    arabicExample = "تَرْمِيهِم بِحِجَارَةٍ مِّن سِجِّيلٍ",
                    description = "নুন সাকিন বা তানভীনের পর ইখফার ১৫টি হরফের কোনো একটি আসলে গুন্নাহসহ অস্পষ্ট করে পড়তে হয়।"
                )

                Spacer(modifier = Modifier.height(10.dp))

                TajweedCard(
                    title = "Idgham (Merging / Elision)",
                    banglaTitle = "ইদগাম (মিলিয়ে পড়া)",
                    badgeColor = TajweedIdgham,
                    arabicExample = "مَن يَقُولُ ۝ مِّن وَّالٍ",
                    description = "নুন সাকিন বা তানভীনের পর ইদগামের হরফ (ירמלون) আসলে পরবর্তী হরফের সাথে মিলিয়ে গুন্নাহ সহ/ছাড়া পড়তে হয়।"
                )

                Spacer(modifier = Modifier.height(10.dp))

                TajweedCard(
                    title = "Qalqalah (Echoing / Bouncing)",
                    banglaTitle = "কলকলাহ (প্রতিধ্বনি)",
                    badgeColor = TajweedQalqalah,
                    arabicExample = "قُلْ هُوَ ٱللَّهُ أَحَدٌ ۝ ٱلْفَلَقِ",
                    description = "কলকলার ৫টি হরফ (ق ط ب ج د) সাকিন অবস্থায় থাকলে প্রতিধ্বনি সৃষ্টি করে সজোরে উচ্চারণ করতে হয়।"
                )

                Spacer(modifier = Modifier.height(10.dp))

                TajweedCard(
                    title = "Iqlab (Conversion)",
                    banglaTitle = "ইকলাব (বদলানো)",
                    badgeColor = TajweedIqlab,
                    arabicExample = "مِنۢ بَعْدِ ۝ أَنۢبِئْهُم",
                    description = "নুন সাকিন বা তানভীনের পর 'বা' (ب) হরফ আসলে নুনকে মীমে রূপান্তরিত করে গুন্নাহ সহ পড়তে হয়।"
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("বুঝেছি (Close)")
                }
            }
        }
    }
}

@Composable
private fun TajweedCard(
    title: String,
    banglaTitle: String,
    badgeColor: Color,
    arabicExample: String,
    description: String
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(badgeColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$title ($banglaTitle)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = arabicExample,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = badgeColor,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
