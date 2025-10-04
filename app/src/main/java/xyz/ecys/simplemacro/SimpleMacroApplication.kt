package xyz.ecys.simplemacro

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import xyz.ecys.simplemacro.data.database.SimpleMacroDatabase
import xyz.ecys.simplemacro.data.preferences.UserPreferences
import xyz.ecys.simplemacro.data.repository.MacroRepository
import xyz.ecys.simplemacro.data.repository.UserRepository
import xyz.ecys.simplemacro.ui.viewmodel.ViewModelFactory

class SimpleMacroApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Disable reCAPTCHA (requires billing account)
        // Re-enable this for production once billing is set up
        val auth = FirebaseAuth.getInstance()
        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
    }
    
    private val database by lazy { SimpleMacroDatabase.getDatabase(this) }
    
    val userRepository by lazy { UserRepository(database.userDao()) }
    val macroRepository by lazy { MacroRepository(database.macroEntryDao()) }
    val userPreferences by lazy { UserPreferences(this) }
    
    val viewModelFactory by lazy {
        ViewModelFactory(userRepository, macroRepository, userPreferences)
    }
}
