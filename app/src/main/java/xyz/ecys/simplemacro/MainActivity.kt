package xyz.ecys.simplemacro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import xyz.ecys.simplemacro.ui.navigation.Screen
import xyz.ecys.simplemacro.ui.screens.AuthScreen
import xyz.ecys.simplemacro.ui.screens.HomeScreenNew
import xyz.ecys.simplemacro.ui.screens.OnboardingScreen
import xyz.ecys.simplemacro.ui.screens.SettingsScreenFixed
import xyz.ecys.simplemacro.ui.theme.SimpleMacroTheme
import xyz.ecys.simplemacro.ui.viewmodel.AuthViewModel
import xyz.ecys.simplemacro.ui.viewmodel.HomeViewModel
import xyz.ecys.simplemacro.ui.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    
    private val viewModelFactory by lazy {
        (application as SimpleMacroApplication).viewModelFactory
    }
    
    private val authViewModel: AuthViewModel by viewModels { viewModelFactory }
    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }
    private val settingsViewModel: SettingsViewModel by viewModels { viewModelFactory }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            SimpleMacroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleMacroApp(
                        authViewModel = authViewModel,
                        homeViewModel = homeViewModel,
                        settingsViewModel = settingsViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleMacroApp(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navController = rememberNavController()
    var currentUserId by remember { mutableStateOf<Long?>(null) }
    
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                viewModel = authViewModel,
                onAuthSuccess = {
                    val authState = authViewModel.authState.value
                    if (authState is xyz.ecys.simplemacro.ui.viewmodel.AuthState.Success) {
                        currentUserId = authState.userId
                        if (authState.needsOnboarding) {
                            // New user from Google Sign-In needs onboarding
                            navController.navigate(Screen.Onboarding.createRoute(authState.userId)) {
                                popUpTo(Screen.Auth.route) { inclusive = true }
                            }
                        } else {
                            // Existing user or email signup (already has profile)
                            navController.navigate(Screen.Home.createRoute(authState.userId)) {
                                popUpTo(Screen.Auth.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
        
        composable(
            route = Screen.Onboarding.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: return@composable
            
            OnboardingScreen(
                onComplete = { name, age, weightLbs, heightFeet, heightInches, gender, calorieGoal, carbGoal, proteinGoal, fatGoal ->
                    // Convert imperial to metric
                    val weightKg = weightLbs?.let { it / 2.20462f }
                    val heightCm = if (heightFeet != null && heightInches != null) {
                        ((heightFeet * 12 + heightInches) * 2.54).toFloat()
                    } else null
                    
                    // Update user profile
                    settingsViewModel.loadUser(userId)
                    settingsViewModel.updateProfile(
                        name = name ?: "",
                        age = age,
                        weight = weightKg,
                        height = heightCm,
                        gender = gender
                    )
                    settingsViewModel.updateMacroGoals(calorieGoal, carbGoal, proteinGoal, fatGoal)
                    
                    // Navigate to home
                    navController.navigate(Screen.Home.createRoute(userId)) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onSkip = {
                    // Skip onboarding, go straight to home
                    navController.navigate(Screen.Home.createRoute(userId)) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(
            route = Screen.Home.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: return@composable
            
            HomeScreenNew(
                viewModel = homeViewModel,
                userId = userId,
                onSettingsClick = {
                    navController.navigate(Screen.Settings.createRoute(userId))
                }
            )
        }
        
        composable(
            route = Screen.Settings.route,
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: return@composable
            
            SettingsScreenFixed(
                viewModel = settingsViewModel,
                authViewModel = authViewModel,
                userId = userId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onAuthSuccess = { newUserId ->
                    // User successfully signed up/in - navigate to home with new user ID
                    navController.navigate(Screen.Home.createRoute(newUserId)) {
                        popUpTo(Screen.Settings.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
