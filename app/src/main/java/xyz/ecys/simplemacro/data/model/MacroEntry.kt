package xyz.ecys.simplemacro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "macro_entries")
data class MacroEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val date: String, // Store as ISO date string (YYYY-MM-DD)
    val name: String = "", // Food/meal name
    val calories: Int,
    val carbohydrates: Int,
    val protein: Int,
    val fat: Int,
    val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        // Auto-calculate calories: 4 cal/g carb, 4 cal/g protein, 9 cal/g fat
        fun calculateCalories(carbs: Int, protein: Int, fat: Int): Int {
            return (carbs * 4) + (protein * 4) + (fat * 9)
        }
    }
}

data class DailyMacros(
    val date: String,
    val totalCalories: Int,
    val totalCarbs: Int,
    val totalProtein: Int,
    val totalFat: Int
)
