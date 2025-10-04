package xyz.ecys.simplemacro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.ecys.simplemacro.ui.viewmodel.HomeViewModel
import java.time.LocalDate

@Composable
fun CalendarDialog(
    viewModel: HomeViewModel,
    userId: Long,
    onDismiss: () -> Unit
) {
    val endDate = LocalDate.now()
    val startDate = endDate.minusMonths(1)
    val macrosForMonth by viewModel.getMacrosForDateRange(userId, startDate, endDate)
        .collectAsState(initial = emptyList())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { 
            Text(
                "Last Month's Logs",
                fontWeight = FontWeight.Bold
            ) 
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                if (macrosForMonth.isEmpty()) {
                    Text(
                        text = "No entries for the last month",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    macrosForMonth.forEach { dailyMacro ->
                        ElevatedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {
                                Text(
                                    text = dailyMacro.date,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Calories: ${dailyMacro.totalCalories}",
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Carbs: ${dailyMacro.totalCarbs}g | Protein: ${dailyMacro.totalProtein}g | Fat: ${dailyMacro.totalFat}g",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
