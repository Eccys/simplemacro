package xyz.ecys.simplemacro.ui.navigation

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Onboarding : Screen("onboarding/{userId}") {
        fun createRoute(userId: Long) = "onboarding/$userId"
    }
    object Home : Screen("home/{userId}") {
        fun createRoute(userId: Long) = "home/$userId"
    }
    object Settings : Screen("settings/{userId}") {
        fun createRoute(userId: Long) = "settings/$userId"
    }
}
