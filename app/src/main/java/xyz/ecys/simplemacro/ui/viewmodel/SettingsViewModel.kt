package xyz.ecys.simplemacro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xyz.ecys.simplemacro.data.model.User
import xyz.ecys.simplemacro.data.preferences.UserPreferences
import xyz.ecys.simplemacro.data.repository.MacroRepository
import xyz.ecys.simplemacro.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SettingsUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

class SettingsViewModel(
    private val userRepository: UserRepository,
    private val macroRepository: MacroRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun loadUser(userId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            userRepository.getUserById(userId).collect { user ->
                _uiState.value = _uiState.value.copy(
                    user = user,
                    isLoading = false
                )
            }
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            val user = _uiState.value.user ?: return@launch
            
            try {
                val updatedUser = user.copy(username = newUsername)
                userRepository.updateUser(updatedUser)
                _uiState.value = _uiState.value.copy(
                    user = updatedUser,
                    isSaved = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateMacroGoals(calorieGoal: Int, carbGoal: Int, proteinGoal: Int, fatGoal: Int) {
        viewModelScope.launch {
            val user = _uiState.value.user ?: return@launch
            
            try {
                val updatedUser = user.copy(
                    calorieGoal = calorieGoal,
                    carbGoal = carbGoal,
                    proteinGoal = proteinGoal,
                    fatGoal = fatGoal
                )
                userRepository.updateUser(updatedUser)
                _uiState.value = _uiState.value.copy(
                    user = updatedUser,
                    isSaved = true
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferences.clearCurrentUserId()
        }
    }

    fun resetSavedState() {
        _uiState.value = _uiState.value.copy(isSaved = false)
    }
    
    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            val user = _uiState.value.user ?: return@launch
            try {
                val updatedUser = user.copy(isDarkMode = enabled)
                userRepository.updateUser(updatedUser)
                _uiState.value = _uiState.value.copy(user = updatedUser, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
    
    fun updateProfile(name: String, age: Int?, weight: Float?, height: Float?, gender: String? = null) {
        viewModelScope.launch {
            val user = _uiState.value.user ?: return@launch
            try {
                val updatedUser = user.copy(
                    name = name,
                    age = age,
                    weight = weight,
                    height = height,
                    gender = gender ?: user.gender
                )
                userRepository.updateUser(updatedUser)
                _uiState.value = _uiState.value.copy(user = updatedUser, isSaved = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }
}
