package xyz.ecys.simplemacro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xyz.ecys.simplemacro.data.model.DailyMacros
import xyz.ecys.simplemacro.data.model.MacroEntry
import xyz.ecys.simplemacro.data.model.User
import xyz.ecys.simplemacro.data.repository.MacroRepository
import xyz.ecys.simplemacro.data.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HomeUiState(
    val user: User? = null,
    val currentDate: LocalDate = LocalDate.now(),
    val dailyMacros: DailyMacros? = null,
    val recentEntries: List<MacroEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(
    private val userRepository: UserRepository,
    private val macroRepository: MacroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            userRepository.getUserById(userId)
                .combine(
                    macroRepository.getDailyMacros(
                        userId,
                        _selectedDate.value.format(DateTimeFormatter.ISO_DATE)
                    )
                ) { user, dailyMacros ->
                    HomeUiState(
                        user = user,
                        currentDate = _selectedDate.value,
                        dailyMacros = dailyMacros,
                        isLoading = false
                    )
                }
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun addMacroEntry(name: String, carbs: Int, protein: Int, fat: Int) {
        viewModelScope.launch {
            val user = _uiState.value.user ?: return@launch
            
            // Auto-calculate calories from macros
            val calories = MacroEntry.calculateCalories(carbs, protein, fat)
            
            val entry = MacroEntry(
                userId = user.id,
                date = _selectedDate.value.format(DateTimeFormatter.ISO_DATE),
                name = name,
                calories = calories,
                carbohydrates = carbs,
                protein = protein,
                fat = fat
            )
            
            macroRepository.insertEntry(entry)
        }
    }
    
    fun updateMacroEntry(entry: MacroEntry, name: String, carbs: Int, protein: Int, fat: Int) {
        viewModelScope.launch {
            val calories = MacroEntry.calculateCalories(carbs, protein, fat)
            val updatedEntry = entry.copy(
                name = name,
                calories = calories,
                carbohydrates = carbs,
                protein = protein,
                fat = fat
            )
            macroRepository.updateEntry(updatedEntry)
        }
    }
    
    fun deleteMacroEntry(entry: MacroEntry) {
        viewModelScope.launch {
            macroRepository.deleteEntry(entry)
        }
    }

    fun setSelectedDate(date: LocalDate) {
        _selectedDate.value = date
        val user = _uiState.value.user
        if (user != null) {
            loadUserData(user.id)
        }
    }

    fun getRecentEntries(userId: Long, limit: Int = 3): Flow<List<MacroEntry>> {
        return macroRepository.getRecentEntries(userId, limit)
    }
    
    fun getMacrosForDateRange(userId: Long, startDate: LocalDate, endDate: LocalDate): Flow<List<DailyMacros>> {
        return macroRepository.getMacrosForDateRange(
            userId,
            startDate.format(DateTimeFormatter.ISO_DATE),
            endDate.format(DateTimeFormatter.ISO_DATE)
        )
    }
}
