package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

data class HomeAyahSlide(
    val badge: String,
    val arabic: String,
    val bangla: String,
    val reference: String,
    val surahNumber: Int,
    val ayahNumber: Int
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeHeroBanner(
    lastRead: LibraryItem?,
    onResumeRead: (LibraryItem) -> Unit,
    onAyahClick: (surahNumber: Int, ayahNumber: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val featuredAyahs = remember {
        listOf(
            HomeAyahSlide(
                badge = "সর্বশ্রেষ্ঠ আয়াত • আয়াতুল কুরসী",
                arabic = "ٱللَّهُ لَآ إِلَٰهَ إِلَّا هُوَ ٱلْحَىُّ ٱلْقَيُّومُ ۚ لَا تَأْخُذُهُۥ سِنَةٌۭ وَلَا نَوْمٌۭ",
                bangla = "আল্লাহ! তিনি ছাড়া কোনো সত্য উপাস্য নেই, তিনি চিরঞ্জীব, সর্বসত্তার ধারক; তন্দ্রা বা নিদ্রা তাঁকে স্পর্শ করে না।",
                reference = "সূরা আল-বাক্বারা [২:২৫৫]",
                surahNumber = 2,
                ayahNumber = 255
            ),
            HomeAyahSlide(
                badge = "অন্তরের প্রশান্তি ও যিকির",
                arabic = "أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
                bangla = "জেনে রেখো, কেবল আল্লাহর স্মরণেই অন্তরসমূহ পরম প্রশান্তি ও তৃপ্তি লাভ করে।",
                reference = "সূরা আর-রাদ [১৩:২৮]",
                surahNumber = 13,
                ayahNumber = 28
            ),
            HomeAyahSlide(
                badge = "কষ্টের পর স্বস্তির সুসংবাদ",
                arabic = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا",
                bangla = "নিশ্চয় কষ্টের সাথেই রয়েছে স্বস্তি; নিশ্চয় প্রতিটি কষ্টের সাথেই রয়েছে স্বস্তি।",
                reference = "সূরা আল-ইনশিরাহ [৯৪:৫-৬]",
                surahNumber = 94,
                ayahNumber = 5
            ),
            HomeAyahSlide(
                badge = "রহমত ও ক্ষমার নিশ্চয়তা",
                arabic = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ",
                bangla = "বলুন, হে আমার বান্দাগণ যারা নিজেদের ওপর বাড়াবাড়ি করেছ! আল্লাহর রহমত থেকে কখনো নিরাশ হয়ো না।",
                reference = "সূরা আয-যুমার [৩৯:৫৩]",
                surahNumber = 39,
                ayahNumber = 53
            ),
            HomeAyahSlide(
                badge = "উভয় জাহানের কল্যাণের দোয়া",
                arabic = "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ",
                bangla = "হে আমাদের রব! আমাদের ইহকালে কল্যাণ দিন, পরকালেও কল্যাণ দিন এবং দোযখের আযাব থেকে রক্ষা করুন।",
                reference = "সূরা আল-বাক্বারা [২:২০১]",
                surahNumber = 2,
                ayahNumber = 201
            ),
            HomeAyahSlide(
                badge = "আল্লাহর নৈকট্য ও দুয়ার সাড়া",
                arabic = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ",
                bangla = "আর আমার বান্দারা যখন আপনাকে আমার ব্যাপারে জিজ্ঞেস করে, আমি তো তাদের অতি কাছেই আছি।",
                reference = "সূরা আল-বাক্বারা [২:১৮৬]",
                surahNumber = 2,
                ayahNumber = 186
            ),
            HomeAyahSlide(
                badge = "তাওয়াক্কুল ও আল্লাহর ওপর ভরসা",
                arabic = "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ ۚ إِنَّ اللَّهَ بَالِغُ أَمْرِهِ",
                bangla = "আর যে ব্যক্তি আল্লাহর ওপর তাওয়াক্কুল বা ভরসা করে, তার জন্য তিনিই যথেষ্ট।",
                reference = "সূরা আত-ত্বালাক্ব [৬৫:৩]",
                surahNumber = 65,
                ayahNumber = 3
            ),
            HomeAyahSlide(
                badge = "পিতা-মাতার জন্য রহমতের দোয়া",
                arabic = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                bangla = "হে আমার প্রতিপালক! আপনি আমার পিতা-মাতার প্রতি দয়া করুন, যেমন তাঁরা আমাকে শৈশবে স্নেহভরে লালন করেছেন।",
                reference = "সূরা আল-ইসরা [১৭:২৪]",
                surahNumber = 17,
                ayahNumber = 24
            ),
            HomeAyahSlide(
                badge = "সরল ও সঠিক পথের হেদায়েত",
                arabic = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                bangla = "আমাদেরকে সরল, সত্য ও অবিচল পথের সঠিক দিকনির্দেশনা দান করুন।",
                reference = "সূরা আল-ফাতিহা [১:৬]",
                surahNumber = 1,
                ayahNumber = 6
            )
        )
    }

    val totalSlides = 1 + featuredAyahs.size // 10 slides
    val pagerState = rememberPagerState(pageCount = { totalSlides })
    val coroutineScope = rememberCoroutineScope()
    var isAutoPlayPaused by remember { mutableStateOf(false) }
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    // Smooth auto-slide every 5 seconds
    LaunchedEffect(pagerState.currentPage, isDragged, isAutoPlayPaused) {
        if (!isDragged && !isAutoPlayPaused) {
            delay(5000)
            yield()
            val nextPage = (pagerState.currentPage + 1) % totalSlides
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    val slideGradients = listOf(
        listOf(Color(0xFF0F382A), Color(0xFF1B5E43), Color(0xFF0B2B1F)), // 0: Last Read (Emerald)
        listOf(Color(0xFF102A45), Color(0xFF1B4975), Color(0xFF0D2137)), // 1: Ayatul Kursi (Sapphire)
        listOf(Color(0xFF1E3231), Color(0xFF264D4B), Color(0xFF132221)), // 2: Peace (Deep Teal)
        listOf(Color(0xFF381B2B), Color(0xFF5A2C46), Color(0xFF27131E)), // 3: Hardship (Royal Ruby)
        listOf(Color(0xFF2A1C3D), Color(0xFF452B66), Color(0xFF1E132D)), // 4: Mercy (Deep Amethyst)
        listOf(Color(0xFF143834), Color(0xFF245B54), Color(0xFF0E2825)), // 5: Goodness (Oceanic)
        listOf(Color(0xFF1A334E), Color(0xFF29517D), Color(0xFF112336)), // 6: Nearness (Night Sky)
        listOf(Color(0xFF352B1E), Color(0xFF544430), Color(0xFF241D14)), // 7: Tawakkul (Bronze/Earth)
        listOf(Color(0xFF123B30), Color(0xFF1E5C4B), Color(0xFF0C2720)), // 8: Parents (Olive Emerald)
        listOf(Color(0xFF182D42), Color(0xFF244869), Color(0xFF101F2E))  // 9: Guidance (Slate Navy)
    )

    val currentGradient = slideGradients[pagerState.currentPage % slideGradients.size]
    val banglaDigits = listOf("১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯", "১০")

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.18f)),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = currentGradient[1].copy(alpha = 0.4f),
                spotColor = currentGradient[1].copy(alpha = 0.5f)
            )
            .testTag("home_hero_banner")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(currentGradient))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Row: Slide Badge & Mini Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Slide Counter Badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.25f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoStories,
                                contentDescription = null,
                                tint = QuranGoldLight,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            val pageNumStr = if (pagerState.currentPage in banglaDigits.indices) {
                                banglaDigits[pagerState.currentPage]
                            } else "${pagerState.currentPage + 1}"
                            Text(
                                text = "$pageNumStr / ১০",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Mini Controls (Prev, Pause/Play, Next)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Prev
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    val prev = (pagerState.currentPage - 1 + totalSlides) % totalSlides
                                    coroutineScope.launch { pagerState.animateScrollToPage(prev) }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পূর্ববর্তী",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Play/Pause
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(if (isAutoPlayPaused) QuranGold.copy(alpha = 0.35f) else Color.White.copy(alpha = 0.15f))
                                .clickable { isAutoPlayPaused = !isAutoPlayPaused },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isAutoPlayPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                                contentDescription = if (isAutoPlayPaused) "প্লে" else "পজ",
                                tint = if (isAutoPlayPaused) QuranGoldLight else Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        // Next
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                                .clickable {
                                    val next = (pagerState.currentPage + 1) % totalSlides
                                    coroutineScope.launch { pagerState.animateScrollToPage(next) }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "পরবর্তী",
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Pager Content: Unrestricted spacious layout ensuring NO lines break awkwardly
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page ->
                    if (page == 0) {
                        // Slide 0: Last Read
                        val targetItem = lastRead ?: LibraryItem(
                            id = "1",
                            surahNumber = 1,
                            ayahNumber = 1,
                            surahName = "Al-Fatihah",
                            arabicSnippet = "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                            translationSnippet = "শুরু করছি পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে।"
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onResumeRead(targetItem) }
                        ) {
                            // Sub-header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = QuranGold.copy(alpha = 0.22f),
                                    border = BorderStroke(0.8.dp, QuranGoldLight.copy(alpha = 0.45f))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Bookmark,
                                            contentDescription = null,
                                            tint = QuranGoldLight,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "সর্বশেষ পঠিত আয়াত",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = QuranGoldLight
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "${targetItem.surahName} [${targetItem.surahNumber}:${targetItem.ayahNumber}]",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Full Arabic Text (Full width, no truncation, no breaking)
                            Text(
                                text = targetItem.arabicSnippet.ifEmpty { "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ" },
                                fontSize = 18.sp,
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranGoldLight,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Full Bangla Translation (Full width, clear text, no broken lines)
                            Text(
                                text = targetItem.translationSnippet.ifEmpty { "শুরু করছি পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে।" },
                                fontSize = 12.5.sp,
                                lineHeight = 19.sp,
                                color = Color.White.copy(alpha = 0.95f),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Bottom Action Row: Dot Indicators & Resume Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dot indicators
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(totalSlides) { idx ->
                                        val isSelected = pagerState.currentPage == idx
                                        Box(
                                            modifier = Modifier
                                                .height(5.dp)
                                                .width(if (isSelected) 16.dp else 5.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(if (isSelected) QuranGold else Color.White.copy(alpha = 0.35f))
                                        )
                                    }
                                }

                                // Clean Play/Read Button
                                Surface(
                                    onClick = { onResumeRead(targetItem) },
                                    shape = RoundedCornerShape(10.dp),
                                    color = QuranGold,
                                    shadowElevation = 2.dp
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color(0xFF2C2200),
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "পড়া চালিয়ে যান",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2C2200)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        // Slide 1..9: Featured Quranic Verses
                        val item = featuredAyahs[page - 1]
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAyahClick(item.surahNumber, item.ayahNumber) }
                        ) {
                            // Sub-header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = QuranGold.copy(alpha = 0.22f),
                                    border = BorderStroke(0.8.dp, QuranGoldLight.copy(alpha = 0.45f))
                                ) {
                                    Text(
                                        text = item.badge,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuranGoldLight,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color.White.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = item.reference,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Full Arabic Text (Full width, no squashing or awkward wrapping)
                            Text(
                                text = item.arabic,
                                fontSize = 18.sp,
                                lineHeight = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuranGoldLight,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Full Bangla Translation (Full width, natural reading flow)
                            Text(
                                text = item.bangla,
                                fontSize = 12.5.sp,
                                lineHeight = 19.sp,
                                color = Color.White.copy(alpha = 0.95f),
                                fontWeight = FontWeight.Normal,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Bottom Action Row: Dot Indicators & Read Button
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Dot indicators
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(totalSlides) { idx ->
                                        val isSelected = pagerState.currentPage == idx
                                        Box(
                                            modifier = Modifier
                                                .height(5.dp)
                                                .width(if (isSelected) 16.dp else 5.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(if (isSelected) QuranGold else Color.White.copy(alpha = 0.35f))
                                        )
                                    }
                                }

                                // Clean Read & Listen Button
                                Surface(
                                    onClick = { onAyahClick(item.surahNumber, item.ayahNumber) },
                                    shape = RoundedCornerShape(10.dp),
                                    color = QuranGold,
                                    shadowElevation = 2.dp
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color(0xFF2C2200),
                                            modifier = Modifier.size(15.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "তিলাওয়াত শুনুন ও পড়ুন",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF2C2200)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
