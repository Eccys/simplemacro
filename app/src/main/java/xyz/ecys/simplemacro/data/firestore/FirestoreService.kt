package xyz.ecys.simplemacro.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import xyz.ecys.simplemacro.data.model.MacroEntry
import xyz.ecys.simplemacro.data.model.User
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    // User operations
    suspend fun saveUser(userId: String, user: User): Result<Unit> {
        return try {
            val userMap = hashMapOf(
                "email" to user.email,
                "username" to user.username,
                "name" to user.name,
                "age" to user.age,
                "weight" to user.weight,
                "height" to user.height,
                "gender" to user.gender,
                "calorieGoal" to user.calorieGoal,
                "carbGoal" to user.carbGoal,
                "proteinGoal" to user.proteinGoal,
                "fatGoal" to user.fatGoal,
                "isDarkMode" to user.isDarkMode,
                "isGuest" to user.isGuest
            )
            db.collection("users").document(userId).set(userMap).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<User?> {
        return try {
            val doc = db.collection("users").document(userId).get().await()
            if (doc.exists()) {
                val user = User(
                    id = 0, // Local ID will be set when inserted to Room
                    email = doc.getString("email") ?: "",
                    username = doc.getString("username") ?: "",
                    name = doc.getString("name") ?: "",
                    age = doc.getLong("age")?.toInt(),
                    weight = doc.getDouble("weight")?.toFloat(),
                    height = doc.getDouble("height")?.toFloat(),
                    gender = doc.getString("gender"),
                    calorieGoal = doc.getLong("calorieGoal")?.toInt() ?: 2000,
                    carbGoal = doc.getLong("carbGoal")?.toInt() ?: 250,
                    proteinGoal = doc.getLong("proteinGoal")?.toInt() ?: 150,
                    fatGoal = doc.getLong("fatGoal")?.toInt() ?: 65,
                    isDarkMode = doc.getBoolean("isDarkMode") ?: true,
                    isGuest = doc.getBoolean("isGuest") ?: false
                )
                Result.success(user)
            } else {
                Result.success(null)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Macro entry operations
    suspend fun saveMacroEntry(userId: String, entry: MacroEntry): Result<Unit> {
        return try {
            val entryMap = hashMapOf(
                "name" to entry.name,
                "date" to entry.date,
                "calories" to entry.calories,
                "carbohydrates" to entry.carbohydrates,
                "protein" to entry.protein,
                "fat" to entry.fat,
                "timestamp" to entry.timestamp
            )
            db.collection("users")
                .document(userId)
                .collection("entries")
                .add(entryMap)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMacroEntries(userId: String): Result<List<MacroEntry>> {
        return try {
            val snapshot = db.collection("users")
                .document(userId)
                .collection("entries")
                .get()
                .await()
            
            val entries = snapshot.documents.mapNotNull { doc ->
                MacroEntry(
                    id = 0, // Local ID
                    userId = 0, // Local user ID
                    date = doc.getString("date") ?: LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                    name = doc.getString("name") ?: "",
                    calories = doc.getLong("calories")?.toInt() ?: 0,
                    carbohydrates = doc.getLong("carbohydrates")?.toInt() ?: 0,
                    protein = doc.getLong("protein")?.toInt() ?: 0,
                    fat = doc.getLong("fat")?.toInt() ?: 0,
                    timestamp = doc.getLong("timestamp") ?: System.currentTimeMillis()
                )
            }
            Result.success(entries)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
