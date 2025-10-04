package xyz.ecys.simplemacro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import xyz.ecys.simplemacro.data.preferences.UserPreferences
import xyz.ecys.simplemacro.data.repository.MacroRepository
import xyz.ecys.simplemacro.data.repository.UserRepository

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val macroRepository: MacroRepository,
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository, userPreferences) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, macroRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userRepository, macroRepository, userPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
