package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LibraryItem
import com.example.ui.theme.IslamicEmeraldDark
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

data class FeaturedAyahSlide(
    val badge: String,
    val arabic: String,
    val bangla: String,
    val reference: String,
    val surahNumber: Int,
    val ayahNumber: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatsHeroBanner(
    lastRead: LibraryItem?,
    onResumeRead: (LibraryItem) -> Unit,
    onAyahClick: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // 10 Important Quranic Verses + 1 Last Read = 11 interactive slides
    val featuredAyahs = remember {
        listOf(
            FeaturedAyahSlide(
                badge = "সর্বশ্রেষ্ঠ আয়াত • আয়াতুল কুরসী",
                arabic = "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ",
                bangla = "আল্লাহ! তিনি ছাড়া কোনো সত্য উপাস্য নেই, তিনি চিরঞ্জীব, সর্বসত্তার ধারক।",
                reference = "সূরা আল-বাক্বারা [২:২৫৫]",
                surahNumber = 2,
                ayahNumber = 255
            ),
            FeaturedAyahSlide(
                badge = "অন্তরের প্রশান্তি ও যিকির",
                arabic = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
                bangla = "জেনে রেখো, আল্লাহর স্মরণেই কেবল অন্তরসমূহ পরম প্রশান্তি লাভ করে।",
                reference = "সূরা আর-রাদ [১৩:২৮]",
                surahNumber = 13,
                ayahNumber = 28
            ),
            FeaturedAyahSlide(
                badge = "রহমত ও ক্ষমার নিশ্চয়তা",
                arabic = "لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا",
                bangla = "তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না; নিশ্চয় আল্লাহ সমস্ত গুনাহ ক্ষমা করেন।",
                reference = "সূরা আয-যুমার [৩৯:৫৩]",
                surahNumber = 39,
                ayahNumber = 53
            ),
            FeaturedAyahSlide(
                badge = "উভয় জাহানের কল্যাণের দোয়া",
                arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الآخِرَةِ حَسَنَةً",
                bangla = "হে আমাদের প্রতিপালক! আমাদের ইহকালে কল্যাণ দিন ও পরকালে কল্যাণ দিন।",
                reference = "সূরা আল-বাক্বারা [২:২০১]",
                surahNumber = 2,
                ayahNumber = 201
            ),
            FeaturedAyahSlide(
                badge = "কষ্টের পর স্বস্তির সুসংবাদ",
                arabic = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا",
                bangla = "নিশ্চয় কষ্টের সাথেই রয়েছে স্বস্তি, নিশ্চয় কষ্টের সাথেই স্বস্তি বিদ্যমান।",
                reference = "সূরা আল-ইনশিরাহ [৯৪:৫-৬]",
                surahNumber = 94,
                ayahNumber = 5
            ),
            FeaturedAyahSlide(
                badge = "আল্লাহর নৈকট্য ও দুয়ার সাড়া",
                arabic = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ",
                bangla = "আমার বান্দারা যখন আমার সম্পর্কে জিজ্ঞেস করে, আমি তো কাছেই; আহ্বানকারীর ডাকে সাড়া দিই।",
                reference = "সূরা আল-বাক্বারা [২:১৮৬]",
                surahNumber = 2,
                ayahNumber = 186
            ),
            FeaturedAyahSlide(
                badge = "তাওয়াক্কুল ও অপ্রত্যাশিত রিযিক",
                arabic = "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ ۚ إِنَّ اللَّهَ بَالِغُ أَمْرِهِ",
                bangla = "যে ব্যক্তি আল্লাহর ওপর ভরসা করে, তার জন্য তিনিই যথেষ্ট; নিশ্চয় আল্লাহ স্বীয় কাজ সম্পন্ন করবেন।",
                reference = "সূরা আত-ত্বালাক্ব [৬৫:৩]",
                surahNumber = 65,
                ayahNumber = 3
            ),
            FeaturedAyahSlide(
                badge = "পিতা-মাতার জন্য রহমতের দোয়া",
                arabic = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                bangla = "হে আমার প্রতিপালক! তাঁদের উভয়ের প্রতি রহম করুন, যেমন তাঁরা আমাকে শৈশবে লালন করেছেন।",
                reference = "সূরা আল-ইসরা [১৭:২৪]",
                surahNumber = 17,
                ayahNumber = 24
            ),
            FeaturedAyahSlide(
                badge = "সরল-সঠিক পথের হেদায়েত",
                arabic = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                bangla = "আমাদেরকে সরল-সঠিক ও অবিচল পথ প্রদর্শন করুন।",
                reference = "সূরা আল-ফাতিহা [১:৬]",
                surahNumber = 1,
                ayahNumber = 6
            ),
            FeaturedAyahSlide(
                badge = "ক্ষমা ও পরম দয়া প্রার্থনা",
                arabic = "رَّبِّ اغْفِرْ وَارْحَمْ وَأَنتَ خَيْرُ الرَّاحِمِينَ",
                bangla = "হে আমার রব! ক্ষমা করুন ও দয়া করুন, আর আপনিই তো শ্রেষ্ঠ দয়ালু।",
                reference = "সূরা আল-মুমিনূন [২৩:১১৮]",
                surahNumber = 23,
                ayahNumber = 118
            )
        )
    }

    val totalSlides = 1 + featuredAyahs.size // 11 slides
    val pagerState = rememberPagerState(pageCount = { totalSlides })
    val coroutineScope = rememberCoroutineScope()
    var isAutoPlayPaused by remember { mutableStateOf(false) }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    // Smooth Auto-sliding effect every 4.5 seconds when not interacting or paused
    LaunchedEffect(pagerState.currentPage, isDragged, isAutoPlayPaused) {
        if (!isDragged && !isAutoPlayPaused) {
            delay(4500)
            yield()
            val nextPage = (pagerState.currentPage + 1) % totalSlides
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(
                    durationMillis = 550,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    // Refined compact gradients per slide
    val slideGradients = listOf(
        listOf(IslamicEmeraldDark, IslamicEmeraldPrimary, Color(0xFF136846)), // Last Read
        listOf(Color(0xFF0F2B48), Color(0xFF184A78), Color(0xFF20639B)),      // 1. Ayatul Kursi
        listOf(Color(0xFF203A43), Color(0xFF0F2027), Color(0xFF2C5364)),      // 2. Peace of Heart
        listOf(Color(0xFF2C1338), Color(0xFF4A1E5C), Color(0xFF6B2D82)),      // 3. Mercy & Forgiveness
        listOf(Color(0xFF134E5E), Color(0xFF71B280), Color(0xFF1B5E20)),      // 4. Dua for Goodness
        listOf(Color(0xFF4A1224), Color(0xFF6E1B36), Color(0xFF8C2446)),      // 5. Ease with Hardship
        listOf(Color(0xFF1A2A6C), Color(0xFF27588C), Color(0xFF0B3C5D)),      // 6. Allah is Near
        listOf(Color(0xFF3E2723), Color(0xFF5D4037), Color(0xFF795548)),      // 7. Tawakkul
        listOf(Color(0xFF004D40), Color(0xFF00695C), Color(0xFF00796B)),      // 8. Parents Dua
        listOf(Color(0xFF1E3C72), Color(0xFF2A5298), Color(0xFF1B3B6F)),      // 9. Guidance
        listOf(Color(0xFF37474F), Color(0xFF455A64), Color(0xFF546E7A))       // 10. Forgiveness
    )

    val currentGradient = slideGradients[pagerState.currentPage % slideGradients.size]

    val banglaDigits = listOf("১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "১০", "১১")

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = currentGradient[1].copy(alpha = 0.35f),
                spotColor = currentGradient[1].copy(alpha = 0.45f)
            )
            .testTag("stats_hero_banner")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(currentGradient))
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp)) {
                // Header Row: Slide Counter & Compact Nav Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Slide Counter Badge (মার্জিত ও পরিপাটি)
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.25f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = QuranGoldLight,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val pageNumStr = if (pagerState.currentPage in banglaDigits.indices) {
                                banglaDigits[pagerState.currentPage]
                            } else "${pagerState.currentPage + 1}"
                            Text(
                                text = "$pageNumStr / ১১",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Mini Navigation Controls: Prev, Pause/Play, Next
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        // Prev button
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    val prev = (pagerState.currentPage - 1 + totalSlides) % totalSlides
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(prev)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পূর্ববর্তী",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        // Play/Pause button
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(if (isAutoPlayPaused) QuranGold.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.15f))
                                .clickable { isAutoPlayPaused = !isAutoPlayPaused },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAutoPlayPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isAutoPlayPaused) "প্লে" else "পজ",
                                tint = if (isAutoPlayPaused) QuranGoldLight else Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }

                        // Next button
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    val next = (pagerState.currentPage + 1) % totalSlides
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(next)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "পরবর্তী",
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                // Compact Pager Content (Height reduced & typography refined)
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    if (page == 0) {
                        // Slide 0: Last Read Quick Resume (সর্বশেষ পঠিত আয়াত)
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(QuranGold.copy(alpha = 0.25f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bookmark,
                                            contentDescription = null,
                                            tint = QuranGoldLight,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "সর্বশেষ পঠিত আয়াত",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranGoldLight
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = if (lastRead != null) "${lastRead.surahName} [${lastRead.surahNumber}:${lastRead.ayahNumber}]"
                                        else "সূরা আল-ফাতিহা [১:১]",
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Arabic text preview
                            Text(
                                text = if (lastRead != null && lastRead.arabicSnippet.isNotEmpty()) lastRead.arabicSnippet
                                else "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranGoldLight,
                                textAlign = TextAlign.End,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (lastRead != null && lastRead.translationSnippet.isNotEmpty()) lastRead.translationSnippet
                                    else "পরম করুণাময় আল্লাহর নামে পাঠ শুরু করুন",
                                    fontSize = 11.5.sp,
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                )

                                FilledTonalButton(
                                    onClick = {
                                        if (lastRead != null) onResumeRead(lastRead)
                                        else onResumeRead(
                                            LibraryItem(
                                                id = "1",
                                                surahNumber = 1,
                                                ayahNumber = 1,
                                                surahName = "Al-Fatihah",
                                                arabicSnippet = "بِسْمِ ٱللَّهِ",
                                                translationSnippet = "পরম করুণাময় আল্লাহর নামে"
                                            )
                                        )
                                    },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = QuranGold,
                                        contentColor = Color(0xFF2C2200)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("পড়ুন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    } else {
                        // Slide 1..10: Important Quranic Ayahs
                        val item = featuredAyahs[page - 1]
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Top Row: Badge & Reference
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = QuranGold.copy(alpha = 0.22f),
                                    border = BorderStroke(0.6.dp, QuranGoldLight.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = item.badge,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranGoldLight,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Text(
                                    text = item.reference,
                                    fontSize = 10.5.sp,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            // Arabic Verse
                            Text(
                                text = item.arabic,
                                fontSize = 16.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranGoldLight,
                                textAlign = TextAlign.End,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(3.dp))

                            // Bangla Meaning & Action Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item.bangla,
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.92f),
                                    fontWeight = FontWeight.Normal,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 6.dp)
                                )

                                FilledTonalButton(
                                    onClick = { onAyahClick(item.surahNumber, item.ayahNumber) },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = QuranGold,
                                        contentColor = Color(0xFF2C2200)
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 9.dp, vertical = 2.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoStories,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text("পড়ুন", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(7.dp))

                // Compact Interactive Slide Indicators (Clickable animated pills)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(totalSlides) { idx ->
                        val isSelected = pagerState.currentPage == idx
                        val targetWidth = if (isSelected) 16.dp else 5.dp
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 2.dp)
                                .height(4.dp)
                                .width(targetWidth)
                                .clip(RoundedCornerShape(2.dp))
                                .background(if (isSelected) QuranGold else Color.White.copy(alpha = 0.3f))
                                .clickable {
                                    coroutineScope.launch {
                                        pagerState.animateScrollToPage(idx)
                                    }
                                }
                        )
                    }
                }
            }
        }
    }
}
