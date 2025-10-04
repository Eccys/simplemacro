package xyz.ecys.simplemacro.data.repository

import xyz.ecys.simplemacro.data.dao.MacroEntryDao
import xyz.ecys.simplemacro.data.model.DailyMacros
import xyz.ecys.simplemacro.data.model.MacroEntry
import kotlinx.coroutines.flow.Flow

class MacroRepository(private val macroEntryDao: MacroEntryDao) {
    
    fun getEntriesForDate(userId: Long, date: String): Flow<List<MacroEntry>> =
        macroEntryDao.getEntriesForDate(userId, date)
    
    fun getDailyMacros(userId: Long, date: String): Flow<DailyMacros?> =
        macroEntryDao.getDailyMacros(userId, date)
    
    fun getMacrosForDateRange(userId: Long, startDate: String, endDate: String): Flow<List<DailyMacros>> =
        macroEntryDao.getMacrosForDateRange(userId, startDate, endDate)
    
    suspend fun insertEntry(entry: MacroEntry): Long =
        macroEntryDao.insertEntry(entry)
    
    suspend fun updateEntry(entry: MacroEntry) =
        macroEntryDao.updateEntry(entry)
    
    suspend fun deleteEntry(entry: MacroEntry) =
        macroEntryDao.deleteEntry(entry)
    
    suspend fun deleteAllEntriesForUser(userId: Long) =
        macroEntryDao.deleteAllEntriesForUser(userId)
    
    fun getRecentEntries(userId: Long, limit: Int = 3): Flow<List<MacroEntry>> =
        macroEntryDao.getRecentEntries(userId, limit)
}
