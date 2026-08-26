package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReciterItem
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.QuranGold
import com.example.ui.theme.QuranGoldLight

/**
 * High-quality Material 3 Islamic Reciter Avatar with distinct styling,
 * gradient backgrounds, and calligraphic monogram for each reciter.
 */
@Composable
fun ReciterAvatarBadge(
    reciter: ReciterItem,
    size: Dp = 56.dp,
    showBorder: Boolean = true,
    showPlayingBadge: Boolean = false,
    modifier: Modifier = Modifier
) {
    // Generate harmonious gradient based on reciter name hash
    val colorPalettes = listOf(
        listOf(Color(0xFF1E563F), Color(0xFF257A56), Color(0xFFD4AF37)),
        listOf(Color(0xFF0D47A1), Color(0xFF1976D2), Color(0xFF64B5F6)),
        listOf(Color(0xFF4A148C), Color(0xFF7B1FA2), Color(0xFFBA68C8)),
        listOf(Color(0xFF004D40), Color(0xFF00796B), Color(0xFF4DB6AC)),
        listOf(Color(0xFFBF360C), Color(0xFFE64A19), Color(0xFFFF8A65)),
        listOf(Color(0xFF1A237E), Color(0xFF283593), Color(0xFF5C6BC0)),
        listOf(Color(0xFF33691E), Color(0xFF558B2F), Color(0xFF9CCC65)),
        listOf(Color(0xFF880E4F), Color(0xFFAD1457), Color(0xFFF06292)),
        listOf(Color(0xFF37474F), Color(0xFF455A64), Color(0xFF90A4AE)),
        listOf(Color(0xFFE65100), Color(0xFFF57C00), Color(0xFFFFB74D))
    )
    val paletteIdx = (reciter.id.hashCode() and 0x7FFFFFFF) % colorPalettes.size
    val gradientColors = colorPalettes[paletteIdx]

    // Create 2-letter monogram
    val words = reciter.displayName.split(" ").filter { it.isNotBlank() }
    val monogram = if (words.size >= 2) {
        "${words[0].first()}${words[1].first()}".uppercase()
    } else if (words.isNotEmpty()) {
        words[0].take(2).uppercase()
    } else {
        "QR"
    }

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .shadow(4.dp, CircleShape)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(gradientColors[0], gradientColors[1])
                    )
                )
                .then(
                    if (showBorder) {
                        Modifier.border(2.dp, Brush.sweepGradient(listOf(QuranGold, QuranGoldLight, QuranGold)), CircleShape)
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            // Subtle Islamic inner geometric circle
            Box(
                modifier = Modifier
                    .size(size * 0.75f)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = monogram,
                    fontSize = (size.value * 0.36f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Micro badge indicator at bottom right
        if (showPlayingBadge) {
            Box(
                modifier = Modifier
                    .size(size * 0.36f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(QuranGold)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Playing",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.22f)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(size * 0.32f)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(IslamicEmeraldPrimary)
                    .border(1.5.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Reciter",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.18f)
                )
            }
        }
    }
}

/**
 * Horizontal Carousel Card for Reciter shown on Home Screen
 * Styled with soft glowing shadow and floating appearance.
 */
@Composable
fun HomeReciterCarouselCard(
    reciter: ReciterItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
        tonalElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = IslamicEmeraldPrimary.copy(alpha = 0.15f)
        ),
        modifier = modifier
            .width(92.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = IslamicEmeraldPrimary.copy(alpha = 0.2f),
                spotColor = IslamicEmeraldPrimary.copy(alpha = 0.35f)
            )
            .clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 6.dp)
        ) {
            // Reciter Avatar with Gold border and Play badge
            ReciterAvatarBadge(
                reciter = reciter,
                size = 56.dp,
                showBorder = true,
                showPlayingBadge = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reciter.displayName.split(" ").take(2).joinToString(" "),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                lineHeight = 13.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = reciter.style,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = IslamicEmeraldPrimary,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
    }
}
