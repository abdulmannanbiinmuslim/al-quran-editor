package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyVersesMetric
import com.example.data.model.WeeklyReadingSummary
import com.example.ui.theme.*

enum class ChartStyle {
    CURVE_AREA,
    BAR_CHART
}

/**
 * Canvas-based visualization of Quran reading progress metrics showing verses read per day over the last week.
 */
@Composable
fun ReadingProgressChartCard(
    weeklySummary: WeeklyReadingSummary,
    onViewFullStats: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedChartStyle by remember { mutableStateOf(ChartStyle.CURVE_AREA) }
    var selectedIndex by remember { mutableStateOf<Int?>(5) } // Default selected: Friday (highest)

    // Entry animation progress
    var isAnimated by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        isAnimated = true
    }
    val animProgress by animateFloatAsState(
        targetValue = if (isAnimated) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "chart_anim"
    )

    val metrics = weeklySummary.metrics
    val totalVerses = metrics.sumOf { it.versesCount }
    val maxVerses = (metrics.maxOfOrNull { it.versesCount } ?: 50).coerceAtLeast(40)
    val avgVerses = if (metrics.isNotEmpty()) totalVerses / metrics.size else 0
    val targetVerses = weeklySummary.goalVersesDaily

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("reading_progress_chart_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Header: Title, Subtitle, and Chart Mode Switcher
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(IslamicEmeraldPrimary, IslamicEmeraldDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoGraph,
                            contentDescription = null,
                            tint = QuranGoldLight,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "সাপ্তাহিক পাঠ অগ্রগতি (Reading Progress)",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Verses read per day (Last 7 Days)",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Toggle between Curve Area and Bar Chart
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Row(
                        modifier = Modifier.padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        IconButton(
                            onClick = { selectedChartStyle = ChartStyle.CURVE_AREA },
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedChartStyle == ChartStyle.CURVE_AREA) IslamicEmeraldPrimary
                                    else Color.Transparent
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShowChart,
                                contentDescription = "Area Chart",
                                tint = if (selectedChartStyle == ChartStyle.CURVE_AREA) Color.White
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = { selectedChartStyle = ChartStyle.BAR_CHART },
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (selectedChartStyle == ChartStyle.BAR_CHART) IslamicEmeraldPrimary
                                    else Color.Transparent
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Bar Chart",
                                tint = if (selectedChartStyle == ChartStyle.BAR_CHART) Color.White
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. High-level metric summary chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricSummaryPill(
                    label = "Total Read",
                    value = "$totalVerses Ayahs",
                    icon = Icons.Default.MenuBook,
                    accentColor = IslamicEmeraldPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricSummaryPill(
                    label = "Daily Avg",
                    value = "$avgVerses/day",
                    icon = Icons.Default.Speed,
                    accentColor = QuranGoldDark,
                    modifier = Modifier.weight(1f)
                )
                MetricSummaryPill(
                    label = "Active Streak",
                    value = "${weeklySummary.streakDays} Days 🔥",
                    icon = Icons.Default.LocalFireDepartment,
                    accentColor = Color(0xFFE65100),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Selected Day Tooltip Banner (Interactive Scrubbing Result)
            val activeItem = selectedIndex?.let { metrics.getOrNull(it) }
            if (activeItem != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = IslamicEmeraldContainer.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, IslamicEmeraldPrimary.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = IslamicEmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${activeItem.dayOfWeek} (${activeItem.dateLabel})",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${activeItem.versesCount} Ayahs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = IslamicEmeraldPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (activeItem.isCompleted) IslamicEmeraldPrimary.copy(alpha = 0.15f)
                                else MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = if (activeItem.isCompleted) "✓ Target Met" else "Under Goal",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (activeItem.isCompleted) IslamicEmeraldPrimary else MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // 4. Canvas-based Custom Visualization Chart
            val textMeasurer = rememberTextMeasurer()
            val emeraldPrimary = IslamicEmeraldPrimary
            val emeraldDark = IslamicEmeraldDark
            val goldColor = QuranGold
            val goldDark = QuranGoldDark
            val outlineColor = MaterialTheme.colorScheme.outlineVariant
            val textSecondaryColor = MaterialTheme.colorScheme.onSurfaceVariant
            val textPrimaryColor = MaterialTheme.colorScheme.onSurface

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                    .pointerInput(metrics) {
                        detectTapGestures { offset ->
                            val slotWidth = size.width / metrics.size.toFloat()
                            val clickedIndex = (offset.x / slotWidth)
                                .toInt()
                                .coerceIn(0, metrics.size - 1)
                            selectedIndex = clickedIndex
                        }
                    }
                    .pointerInput(metrics) {
                        detectDragGestures { change, _ ->
                            val slotWidth = size.width / metrics.size.toFloat()
                            val draggedIndex = (change.position.x / slotWidth)
                                .toInt()
                                .coerceIn(0, metrics.size - 1)
                            selectedIndex = draggedIndex
                        }
                    }
                    .padding(top = 16.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val bottomLabelHeight = 28.dp.toPx()
                    val topPadding = 16.dp.toPx()
                    val graphHeight = canvasHeight - bottomLabelHeight - topPadding
                    val count = metrics.size
                    val slotWidth = canvasWidth / count.toFloat()
                    val upperLimit = (maxVerses * 1.15f).coerceAtLeast(1f)

                    // Draw Horizontal Gridlines (0, Target, Max)
                    val targetY = topPadding + graphHeight * (1f - (targetVerses / upperLimit).coerceIn(0f, 1f))
                    val zeroY = topPadding + graphHeight

                    // Target Dotted Line
                    drawLine(
                        color = goldDark.copy(alpha = 0.55f),
                        start = Offset(0f, targetY),
                        end = Offset(canvasWidth, targetY),
                        strokeWidth = 1.5.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    )

                    // Target Text Label on Right
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "Goal: $targetVerses",
                        topLeft = Offset(canvasWidth - 55.dp.toPx(), targetY - 14.dp.toPx()),
                        style = TextStyle(
                            color = goldDark,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    // Zero Baseline
                    drawLine(
                        color = outlineColor.copy(alpha = 0.6f),
                        start = Offset(0f, zeroY),
                        end = Offset(canvasWidth, zeroY),
                        strokeWidth = 1.dp.toPx()
                    )

                    if (selectedChartStyle == ChartStyle.BAR_CHART) {
                        // RENDER BAR CHART
                        val barWidth = (slotWidth * 0.48f).coerceAtMost(32.dp.toPx())

                        metrics.forEachIndexed { index, item ->
                            val centerX = slotWidth * index + slotWidth / 2f
                            val barHeight = (graphHeight * (item.versesCount / upperLimit) * animProgress)
                                .coerceAtLeast(4.dp.toPx())
                            val barTop = zeroY - barHeight
                            val isSelected = selectedIndex == index

                            val barBrush = if (isSelected) {
                                Brush.verticalGradient(
                                    listOf(QuranGold, IslamicEmeraldPrimary),
                                    startY = barTop,
                                    endY = zeroY
                                )
                            } else {
                                Brush.verticalGradient(
                                    listOf(
                                        if (item.isCompleted) IslamicEmeraldPrimary else IslamicEmeraldPrimary.copy(alpha = 0.7f),
                                        if (item.isCompleted) IslamicEmeraldDark else IslamicEmeraldDark.copy(alpha = 0.7f)
                                    ),
                                    startY = barTop,
                                    endY = zeroY
                                )
                            }

                            // Draw rounded bar
                            drawRoundRect(
                                brush = barBrush,
                                topLeft = Offset(centerX - barWidth / 2f, barTop),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                            )

                            // Top value label on bar
                            val valText = item.versesCount.toString()
                            val valLayout = textMeasurer.measure(
                                text = valText,
                                style = TextStyle(
                                    color = if (isSelected) emeraldDark else textSecondaryColor,
                                    fontSize = if (isSelected) 11.sp else 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                            drawText(
                                textLayoutResult = valLayout,
                                topLeft = Offset(centerX - valLayout.size.width / 2f, barTop - valLayout.size.height - 2.dp.toPx())
                            )

                            // Selection highlight ring at base
                            if (isSelected) {
                                drawCircle(
                                    color = QuranGold,
                                    radius = 3.dp.toPx(),
                                    center = Offset(centerX, zeroY + 4.dp.toPx())
                                )
                            }

                            // Bottom Day of Week Label
                            val dayText = item.dayOfWeek
                            val dayLayout = textMeasurer.measure(
                                text = dayText,
                                style = TextStyle(
                                    color = if (isSelected) emeraldPrimary else textSecondaryColor,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
                                )
                            )
                            drawText(
                                textLayoutResult = dayLayout,
                                topLeft = Offset(centerX - dayLayout.size.width / 2f, zeroY + 6.dp.toPx())
                            )
                        }
                    } else {
                        // RENDER CURVED AREA CHART (BEZIER)
                        val points = metrics.mapIndexed { index, item ->
                            val x = slotWidth * index + slotWidth / 2f
                            val y = zeroY - (graphHeight * (item.versesCount / upperLimit) * animProgress)
                            Offset(x, y)
                        }

                        if (points.isNotEmpty()) {
                            // Build Smooth Cubic Bezier Path
                            val linePath = Path().apply {
                                moveTo(points[0].x, points[0].y)
                                for (i in 0 until points.size - 1) {
                                    val p0 = points[i]
                                    val p1 = points[i + 1]
                                    val controlX1 = (p0.x + p1.x) / 2f
                                    val controlY1 = p0.y
                                    val controlX2 = (p0.x + p1.x) / 2f
                                    val controlY2 = p1.y
                                    cubicTo(controlX1, controlY1, controlX2, controlY2, p1.x, p1.y)
                                }
                            }

                            // Gradient Area Path
                            val areaPath = Path().apply {
                                addPath(linePath)
                                lineTo(points.last().x, zeroY)
                                lineTo(points.first().x, zeroY)
                                close()
                            }

                            // Draw Area Gradient Fill
                            drawPath(
                                path = areaPath,
                                brush = Brush.verticalGradient(
                                    listOf(
                                        IslamicEmeraldPrimary.copy(alpha = 0.40f * animProgress),
                                        IslamicEmeraldPrimary.copy(alpha = 0.04f * animProgress),
                                        Color.Transparent
                                    ),
                                    startY = topPadding,
                                    endY = zeroY
                                )
                            )

                            // Draw Smooth Line Stroke
                            drawPath(
                                path = linePath,
                                brush = Brush.horizontalGradient(
                                    listOf(IslamicEmeraldPrimary, QuranGoldDark, IslamicEmeraldPrimary)
                                ),
                                style = Stroke(
                                    width = 3.dp.toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )

                            // Draw Points & Selection Highlights
                            points.forEachIndexed { index, point ->
                                val item = metrics[index]
                                val isSelected = selectedIndex == index

                                // Vertical dashed marker if selected
                                if (isSelected) {
                                    drawLine(
                                        color = emeraldPrimary.copy(alpha = 0.4f),
                                        start = Offset(point.x, topPadding),
                                        end = Offset(point.x, zeroY),
                                        strokeWidth = 1.2.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                                    )
                                }

                                // Outer Circle / Halo
                                drawCircle(
                                    color = if (isSelected) QuranGold else emeraldPrimary,
                                    radius = if (isSelected) 6.5.dp.toPx() else 4.dp.toPx(),
                                    center = point
                                )
                                // Inner White Dot
                                drawCircle(
                                    color = Color.White,
                                    radius = if (isSelected) 3.5.dp.toPx() else 2.dp.toPx(),
                                    center = point
                                )

                                // Day of Week Label
                                val dayText = item.dayOfWeek
                                val dayLayout = textMeasurer.measure(
                                    text = dayText,
                                    style = TextStyle(
                                        color = if (isSelected) emeraldPrimary else textSecondaryColor,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal
                                    )
                                )
                                drawText(
                                    textLayoutResult = dayLayout,
                                    topLeft = Offset(point.x - dayLayout.size.width / 2f, zeroY + 6.dp.toPx())
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Footer: View Full Stats & Analytics navigation link
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = textSecondaryColor,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tap or drag to inspect day metrics",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TextButton(
                    onClick = onViewFullStats,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                    modifier = Modifier.testTag("home_view_all_stats_button")
                ) {
                    Text(
                        text = "সব পরিসংখ্যান (Full Stats) →",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricSummaryPill(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
