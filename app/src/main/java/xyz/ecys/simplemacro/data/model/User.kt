package xyz.ecys.simplemacro.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val username: String,
    val name: String = "",
    val age: Int? = null,
    val weight: Float? = null, // in kg
    val height: Float? = null, // in cm
    val gender: String? = null, // "Male" or "Female"
    val isGuest: Boolean = false,
    val isDarkMode: Boolean = true,
    val calorieGoal: Int = 2000,
    val carbGoal: Int = 250,
    val proteinGoal: Int = 150,
    val fatGoal: Int = 65
)
