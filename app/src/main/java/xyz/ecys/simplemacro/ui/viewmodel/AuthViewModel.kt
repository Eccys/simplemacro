package xyz.ecys.simplemacro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import xyz.ecys.simplemacro.data.model.User
import xyz.ecys.simplemacro.data.preferences.UserPreferences
import xyz.ecys.simplemacro.data.repository.UserRepository
import xyz.ecys.simplemacro.data.auth.FirebaseAuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: Long, val needsOnboarding: Boolean = false) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences,
    private val firebaseAuth: FirebaseAuthService = FirebaseAuthService()
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun loginWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                if (email.isBlank() || password.isBlank()) {
                    _authState.value = AuthState.Error("Email and password cannot be empty")
                    return@launch
                }

                // Firebase Authentication
                val result = firebaseAuth.signInWithEmail(email, password)
                
                result.onSuccess { firebaseUser ->
                    // Check if user exists in local database
                    var user = userRepository.getUserByEmail(email)
                    
                    if (user == null) {
                        // Create user in local database
                        val newUser = User(
                            email = email,
                            username = firebaseUser.displayName ?: email.substringBefore("@"),
                            name = firebaseUser.displayName ?: "",
                            isGuest = false,
                            isDarkMode = true
                        )
                        val userId = userRepository.insertUser(newUser)
                        user = userRepository.getUserByEmail(email)
                    }
                    
                    user?.let {
                        userPreferences.setCurrentUserId(it.id)
                        _authState.value = AuthState.Success(it.id)
                    } ?: run {
                        _authState.value = AuthState.Error("Failed to create user")
                    }
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Login failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Login failed")
            }
        }
    }

    fun signUpWithEmail(
        email: String, 
        password: String, 
        username: String,
        name: String = "",
        age: Int? = null,
        weight: Float? = null,
        height: Float? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                if (email.isBlank() || password.isBlank() || username.isBlank()) {
                    _authState.value = AuthState.Error("Email, password and username are required")
                    return@launch
                }

                // Check if user already exists locally
                val existingUser = userRepository.getUserByEmail(email)
                if (existingUser != null) {
                    _authState.value = AuthState.Error("User already exists. Please login.")
                    return@launch
                }

                // Firebase Authentication
                val result = firebaseAuth.signUpWithEmail(email, password)
                
                result.onSuccess { firebaseUser ->
                    // Create user in local database
                    val newUser = User(
                        email = email,
                        username = username,
                        name = name,
                        age = age,
                        weight = weight,
                        height = height,
                        gender = gender,
                        isGuest = false,
                        isDarkMode = true
                    )

                    val userId = userRepository.insertUser(newUser)
                    userPreferences.setCurrentUserId(userId)
                    _authState.value = AuthState.Success(userId)
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Sign up failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun continueAsGuest() {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val guestUser = User(
                    email = "guest_${System.currentTimeMillis()}@simplemacro.app",
                    username = "Guest",
                    isGuest = true
                )

                val userId = userRepository.insertUser(guestUser)
                userPreferences.setCurrentUserId(userId)
                _authState.value = AuthState.Success(userId)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to continue as guest")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = firebaseAuth.signInWithGoogle(idToken)
                
                result.onSuccess { firebaseUser ->
                    val email = firebaseUser.email ?: ""
                    
                    // Check if user exists in local database
                    var user = userRepository.getUserByEmail(email)
                    var isNewUser = false
                    
                    if (user == null) {
                        // Create user in local database
                        val newUser = User(
                            email = email,
                            username = firebaseUser.displayName ?: email.substringBefore("@"),
                            name = firebaseUser.displayName ?: "",
                            isGuest = false,
                            isDarkMode = true
                        )
                        val userId = userRepository.insertUser(newUser)
                        user = userRepository.getUserByEmail(email)
                        isNewUser = true
                    }
                    
                    user?.let {
                        userPreferences.setCurrentUserId(it.id)
                        // New Google Sign-In users need onboarding
                        _authState.value = AuthState.Success(it.id, needsOnboarding = isNewUser)
                    } ?: run {
                        _authState.value = AuthState.Error("Failed to create user")
                    }
                }.onFailure { exception ->
                    _authState.value = AuthState.Error(exception.message ?: "Google sign-in failed")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Google sign-in failed")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }
}
