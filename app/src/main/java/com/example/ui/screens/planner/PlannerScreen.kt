package com.example.ui.screens.planner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.PlannerItem
import com.example.ui.theme.IslamicEmeraldContainer
import com.example.ui.theme.IslamicEmeraldPrimary
import com.example.ui.theme.LightDivider
import com.example.ui.theme.QuranGold

@Composable
fun PlannerScreen(
    activeSubTab: Int,
    onSubTabChange: (Int) -> Unit,
    activePlanners: List<PlannerItem>,
    findPlanners: List<PlannerItem>,
    completedPlanners: List<PlannerItem>,
    onCreatePlanner: (title: String, days: Int, versesPerDay: Int) -> Unit,
    onStartPlanner: (PlannerItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (activeSubTab == 0) {
                FloatingActionButton(
                    onClick = { showCreateDialog = true },
                    containerColor = IslamicEmeraldPrimary,
                    contentColor = Color.White,
                    modifier = Modifier.testTag("create_planner_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create Custom Planner")
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Sub Tab Row (My Planners, Find Planners, Completed)
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                TabRow(
                    selectedTabIndex = activeSubTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IslamicEmeraldPrimary
                ) {
                    Tab(
                        selected = activeSubTab == 0,
                        onClick = { onSubTabChange(0) },
                        text = { Text("My Planners", fontWeight = if (activeSubTab == 0) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = activeSubTab == 1,
                        onClick = { onSubTabChange(1) },
                        text = { Text("Find Planners", fontWeight = if (activeSubTab == 1) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = activeSubTab == 2,
                        onClick = { onSubTabChange(2) },
                        text = { Text("Completed", fontWeight = if (activeSubTab == 2) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
            Divider(color = LightDivider)

            // Content List
            when (activeSubTab) {
                0 -> {
                    // My Planners
                    if (activePlanners.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No active planners. Click '+' to create one!", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(activePlanners) { item ->
                                ActivePlannerCard(item = item)
                            }
                        }
                    }
                }
                1 -> {
                    // Find Planners
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(findPlanners) { item ->
                            FindPlannerCard(
                                item = item,
                                onStart = { onStartPlanner(item) }
                            )
                        }
                    }
                }
                2 -> {
                    // Completed
                    if (completedPlanners.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No completed plans yet. Keep reading!", color = Color.Gray)
                        }
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(completedPlanners) { item ->
                                ActivePlannerCard(item = item)
                            }
                        }
                    }
                }
            }
        }

        // Custom Planner Dialog
        if (showCreateDialog) {
            CreatePlannerDialog(
                onDismiss = { showCreateDialog = false },
                onCreate = { title, days, verses ->
                    onCreatePlanner(title, days, verses)
                    showCreateDialog = false
                }
            )
        }
    }
}

@Composable
private fun ActivePlannerCard(item: PlannerItem) {
    val progress = (item.completedDays.toFloat() / item.totalDays.toFloat()).coerceIn(0f, 1f)

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = IslamicEmeraldPrimary
                    )
                    Text(
                        text = "${item.targetVersesPerDay} verses / day • ${item.totalDays} Days Plan",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(IslamicEmeraldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = progress,
                color = IslamicEmeraldPrimary,
                trackColor = IslamicEmeraldContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Day ${item.completedDays} of ${item.totalDays}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${item.totalDays - item.completedDays} days remaining",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuranGold
                )
            }
        }
    }
}

@Composable
private fun FindPlannerCard(
    item: PlannerItem,
    onStart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(IslamicEmeraldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Flag,
                        contentDescription = null,
                        tint = IslamicEmeraldPrimary
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = item.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "${item.totalDays} Days • ${item.targetVersesPerDay} Verses/day",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
            ) {
                Text("Start", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun CreatePlannerDialog(
    onDismiss: () -> Unit,
    onCreate: (title: String, days: Int, versesPerDay: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("30") }
    var versesPerDay by remember { mutableStateOf("20") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Create Custom Planner",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = IslamicEmeraldPrimary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Planner Title") },
                    placeholder = { Text("e.g. Daily Quran Habit") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = days,
                    onValueChange = { days = it },
                    label = { Text("Total Days") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = versesPerDay,
                    onValueChange = { versesPerDay = it },
                    label = { Text("Verses Per Day") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isNotBlank()) {
                                onCreate(
                                    title,
                                    days.toIntOrNull() ?: 30,
                                    versesPerDay.toIntOrNull() ?: 20
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicEmeraldPrimary)
                    ) {
                        Text("Create")
                    }
                }
            }
        }
    }
}
