package xyz.ecys.simplemacro.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import xyz.ecys.simplemacro.data.dao.MacroEntryDao
import xyz.ecys.simplemacro.data.dao.UserDao
import xyz.ecys.simplemacro.data.model.MacroEntry
import xyz.ecys.simplemacro.data.model.User

@Database(
    entities = [User::class, MacroEntry::class],
    version = 3,
    exportSchema = false
)
abstract class SimpleMacroDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun macroEntryDao(): MacroEntryDao

    companion object {
        @Volatile
        private var INSTANCE: SimpleMacroDatabase? = null

        fun getDatabase(context: Context): SimpleMacroDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SimpleMacroDatabase::class.java,
                    "simple_macro_database"
                )
                    .fallbackToDestructiveMigration() // For development - recreate DB on schema change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
