package xyz.ecys.simplemacro.data.dao

import androidx.room.*
import xyz.ecys.simplemacro.data.model.DailyMacros
import xyz.ecys.simplemacro.data.model.MacroEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface MacroEntryDao {
    @Query("SELECT * FROM macro_entries WHERE userId = :userId AND date = :date")
    fun getEntriesForDate(userId: Long, date: String): Flow<List<MacroEntry>>

    @Query("""
        SELECT 
            date,
            SUM(calories) as totalCalories,
            SUM(carbohydrates) as totalCarbs,
            SUM(protein) as totalProtein,
            SUM(fat) as totalFat
        FROM macro_entries 
        WHERE userId = :userId AND date = :date
        GROUP BY date
    """)
    fun getDailyMacros(userId: Long, date: String): Flow<DailyMacros?>

    @Query("""
        SELECT 
            date,
            SUM(calories) as totalCalories,
            SUM(carbohydrates) as totalCarbs,
            SUM(protein) as totalProtein,
            SUM(fat) as totalFat
        FROM macro_entries 
        WHERE userId = :userId 
        AND date BETWEEN :startDate AND :endDate
        GROUP BY date
        ORDER BY date DESC
    """)
    fun getMacrosForDateRange(userId: Long, startDate: String, endDate: String): Flow<List<DailyMacros>>

    @Insert
    suspend fun insertEntry(entry: MacroEntry): Long

    @Update
    suspend fun updateEntry(entry: MacroEntry)

    @Delete
    suspend fun deleteEntry(entry: MacroEntry)

    @Query("DELETE FROM macro_entries WHERE userId = :userId")
    suspend fun deleteAllEntriesForUser(userId: Long)
    
    @Query("""
        SELECT * FROM macro_entries 
        WHERE userId = :userId 
        ORDER BY timestamp DESC 
        LIMIT :limit
    """)
    fun getRecentEntries(userId: Long, limit: Int = 3): Flow<List<MacroEntry>>
}
