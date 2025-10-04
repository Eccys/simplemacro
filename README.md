# SimpleMacro - Android Macro Tracking App

SimpleMacro is a modern Android application built with Kotlin and Jetpack Compose for tracking daily macronutrients (carbohydrates, protein, and fats) and calorie intake.

## Features

### Authentication
- **Email/Password Login & Sign Up**: Secure user authentication with email and password
- **Guest Mode**: Continue without creating an account for quick access
- **Google Sign-In**: (Coming Soon) Firebase-based Google authentication

### Home Screen
- **Calorie Tracking**: Large radial progress indicator showing daily calorie consumption vs goal
- **Macro Breakdown**: Three circular progress indicators for:
  - Carbohydrates
  - Protein
  - Fat
- **Calendar View**: Access historical data for the last month with daily calorie and macro breakdowns
- **Add Entries**: Floating action button to quickly add macro entries

### Settings
- **User Profile**: Update username
- **Macro Goals**: Customize daily goals for:
  - Total calories
  - Carbohydrates (grams)
  - Protein (grams)
  - Fat (grams)
- **Account Management**: Logout functionality

## Technical Stack

### Architecture
- **MVVM Pattern**: Clean separation of concerns with ViewModels
- **Repository Pattern**: Abstraction layer for data operations
- **Room Database**: Local SQLite database for offline-first functionality

### Technologies
- **Kotlin**: Primary programming language
- **Jetpack Compose**: Modern declarative UI framework
- **Material Design 3**: Latest Material Design guidelines
- **Room**: Database persistence
- **Coroutines & Flow**: Asynchronous programming and reactive data streams
- **DataStore**: Key-value storage for user preferences
- **Navigation Compose**: Type-safe navigation
- **Firebase**: (Placeholder) Future integration for authentication

### Project Structure
```
app/
├── data/
│   ├── dao/              # Data Access Objects
│   ├── database/         # Room database setup
│   ├── model/            # Data models and entities
│   ├── preferences/      # DataStore preferences
│   └── repository/       # Repository layer
├── ui/
│   ├── components/       # Reusable UI components
│   ├── navigation/       # Navigation configuration
│   ├── screens/          # Screen composables
│   ├── theme/            # App theming
│   └── viewmodel/        # ViewModels
└── MainActivity.kt       # Entry point
```

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 24+

### Installation
1. Clone this repository
2. Open the project in Android Studio
3. Sync Gradle files
4. Run the app on an emulator or physical device

### Firebase Setup (Optional - For Google Sign-In)
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add your Android app to the Firebase project
3. Download `google-services.json` and replace the placeholder file in `app/`
4. Enable Google Sign-In in Firebase Authentication settings

## Database Schema

### User Table
- `id`: Primary key
- `email`: User email
- `username`: Display name
- `isGuest`: Guest account flag
- `calorieGoal`: Daily calorie target
- `carbGoal`: Daily carbohydrate target (g)
- `proteinGoal`: Daily protein target (g)
- `fatGoal`: Daily fat target (g)

### MacroEntry Table
- `id`: Primary key
- `userId`: Foreign key to User
- `date`: Entry date (ISO format)
- `calories`: Calorie amount
- `carbohydrates`: Carb amount (g)
- `protein`: Protein amount (g)
- `fat`: Fat amount (g)
- `timestamp`: Creation timestamp

## Building the APK

```bash
./gradlew assembleDebug
```

The APK will be generated in `app/build/outputs/apk/debug/`

## Future Enhancements
- [ ] Firebase Google Sign-In integration
- [ ] Food database integration
- [ ] Barcode scanning for quick entry
- [ ] Meal planning features
- [ ] Weekly/monthly statistics and charts
- [ ] Export data functionality
- [ ] Widget support
- [ ] Dark mode improvements
- [ ] Offline sync with cloud backup

## License
This project is open source and available under the MIT License.

## Contributing
Contributions are welcome! Please feel free to submit a Pull Request.
