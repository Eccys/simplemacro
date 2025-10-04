package xyz.ecys.simplemacro.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ecys.simplemacro.data.model.MacroEntry
import xyz.ecys.simplemacro.ui.components.CalorieCircularProgress
import xyz.ecys.simplemacro.ui.components.MacroCircularProgress
import xyz.ecys.simplemacro.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenNew(
    viewModel: HomeViewModel,
    userId: Long,
    onSettingsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val recentEntries = remember { mutableStateListOf<MacroEntry>() }
    var showCalendar by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var showLoadMore by remember { mutableStateOf(false) }
    var editingEntry by remember { mutableStateOf<MacroEntry?>(null) }

    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
    }

    LaunchedEffect(userId, showLoadMore) {
        viewModel.getRecentEntries(userId, if (showLoadMore) 10 else 3)
            .collectLatest { entries ->
                recentEntries.clear()
                recentEntries.addAll(entries)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "SimpleMacro",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddDialog = true },
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Add Food") },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Greeting
            val displayName = uiState.user?.name?.takeIf { it.isNotBlank() } 
                ?: uiState.user?.username ?: "there"
            
            Text(
                text = "Hey, $displayName!",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Calories Card with modern elevated style
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Calories",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CalorieCircularProgress(
                            current = uiState.dailyMacros?.totalCalories ?: 0,
                            goal = uiState.user?.calorieGoal ?: 2000,
                            size = 160.dp
                        )
                    }

                    FilledIconButton(
                        onClick = { showCalendar = true },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = "View Calendar",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Macros Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Macronutrients",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            MacroCircularProgress(
                                current = uiState.dailyMacros?.totalCarbs ?: 0,
                                goal = uiState.user?.carbGoal ?: 250,
                                label = "Carbs",
                                color = Color(0xFF10B981),
                                size = 100.dp,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Carbs",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            MacroCircularProgress(
                                current = uiState.dailyMacros?.totalProtein ?: 0,
                                goal = uiState.user?.proteinGoal ?: 150,
                                label = "Protein",
                                color = Color(0xFF3B82F6),
                                size = 100.dp,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Protein",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(8.dp)
                        ) {
                            MacroCircularProgress(
                                current = uiState.dailyMacros?.totalFat ?: 0,
                                goal = uiState.user?.fatGoal ?: 65,
                                label = "Fat",
                                color = Color(0xFFF59E0B),
                                size = 100.dp,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Fat",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Recent Entries Section
            if (recentEntries.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Entries",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                recentEntries.forEach { entry ->
                    EntryCard(
                        entry = entry,
                        onEdit = { editingEntry = entry },
                        onDelete = { viewModel.deleteMacroEntry(entry) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (recentEntries.size >= 3 && !showLoadMore) {
                    TextButton(
                        onClick = { showLoadMore = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Load More")
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
        }

        if (showCalendar) {
            CalendarDialog(
                viewModel = viewModel,
                userId = userId,
                onDismiss = { showCalendar = false }
            )
        }

        if (showAddDialog) {
            AddMacroDialogNew(
                viewModel = viewModel,
                onDismiss = { showAddDialog = false }
            )
        }

        editingEntry?.let { entry ->
            EditMacroDialog(
                entry = entry,
                viewModel = viewModel,
                onDismiss = { editingEntry = null }
            )
        }
    }
}

@Composable
fun EntryCard(
    entry: MacroEntry,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.name.takeIf { it.isNotBlank() } ?: "Food Entry",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${entry.calories} cal • C:${entry.carbohydrates}g P:${entry.protein}g F:${entry.fat}g",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun AddMacroDialogNew(
    viewModel: HomeViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }

    val calculatedCalories = remember(carbs, protein, fat) {
        MacroEntry.calculateCalories(
            carbs.toIntOrNull() ?: 0,
            protein.toIntOrNull() ?: 0,
            fat.toIntOrNull() ?: 0
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Add Food Entry",
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Food Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbs (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Calculated Calories: $calculatedCalories kcal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    viewModel.addMacroEntry(
                        name,
                        carbs.toIntOrNull() ?: 0,
                        protein.toIntOrNull() ?: 0,
                        fat.toIntOrNull() ?: 0
                    )
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditMacroDialog(
    entry: MacroEntry,
    viewModel: HomeViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(entry.name) }
    var carbs by remember { mutableStateOf(entry.carbohydrates.toString()) }
    var protein by remember { mutableStateOf(entry.protein.toString()) }
    var fat by remember { mutableStateOf(entry.fat.toString()) }

    val calculatedCalories = remember(carbs, protein, fat) {
        MacroEntry.calculateCalories(
            carbs.toIntOrNull() ?: 0,
            protein.toIntOrNull() ?: 0,
            fat.toIntOrNull() ?: 0
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Edit Entry",
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Food Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = carbs,
                    onValueChange = { carbs = it },
                    label = { Text("Carbs (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (g)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Calculated Calories: $calculatedCalories kcal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    viewModel.updateMacroEntry(
                        entry,
                        name,
                        carbs.toIntOrNull() ?: 0,
                        protein.toIntOrNull() ?: 0,
                        fat.toIntOrNull() ?: 0
                    )
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
