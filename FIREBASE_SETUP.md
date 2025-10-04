# Firebase Setup Guide

## Package Name
**`xyz.ecys.simplemacro`**

## Setup Steps

### 1. Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select your existing project
3. Follow the setup wizard

### 2. Register Android App
1. In Firebase Console, click "Add app" → Android icon
2. Enter package name: **`xyz.ecys.simplemacro`**
3. Optional: Add a nickname (e.g., "SimpleMacro Android")
4. Optional: Add SHA-1 certificate (required for Google Sign-In)
   - Get SHA-1: `./gradlew signingReport`
   - Copy the SHA-1 from the debug variant
5. Click "Register app"

### 3. Download google-services.json
1. Download the `google-services.json` file
2. Place it in: `/home/ecys/git/simplemacro/app/google-services.json`
3. **Replace the placeholder file**

### 4. Enable Authentication
1. In Firebase Console, go to **Authentication** → **Sign-in method**
2. Enable **Email/Password**
3. Enable **Google** (if you want Google Sign-In)
   - Add your SHA-1 certificate
   - Add OAuth client ID

### 5. Enable Firestore Database
1. Go to **Firestore Database** → **Create database**
2. Choose **Start in test mode** (for development)
3. Select a location
4. Click "Enable"

### 6. Firestore Security Rules (For Production)
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only read/write their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Entries are user-specific
    match /entries/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

### 7. Uncomment Firebase Plugin
In `build.gradle.kts` files, uncomment:
```kotlin
// Top-level build.gradle.kts
id("com.google.gms.google-services") version "4.4.0" apply false

// app/build.gradle.kts
id("com.google.gms.google-services")
```

### 8. Sync and Build
1. Sync Gradle files in Android Studio
2. Build and run the app

## Firebase Structure

### Firestore Collections

**`/users/{userId}`**
```json
{
  "email": "user@example.com",
  "username": "johndoe",
  "name": "John Doe",
  "age": 25,
  "weight": 70.5,
  "height": 175.0,
  "isDarkMode": true,
  "calorieGoal": 2000,
  "carbGoal": 250,
  "proteinGoal": 150,
  "fatGoal": 65,
  "createdAt": "timestamp"
}
```

**`/entries/{userId}`**
```json
{
  "entries": [
    {
      "id": "entry_id",
      "date": "2025-10-03",
      "name": "Chicken Breast",
      "calories": 165,
      "carbohydrates": 0,
      "protein": 31,
      "fat": 4,
      "timestamp": 1696348800000
    }
  ],
  "lastUpdated": "timestamp"
}
```

## Future Enhancements
- Implement Firebase Authentication for Google Sign-In
- Sync local Room database with Firestore
- Add offline support with Firestore offline persistence
- Implement real-time updates across devices

## Notes
- Current app uses Room database for local storage
- Firebase integration is prepared but not yet implemented
- Dark mode is enabled by default
- Calories are auto-calculated from macros (C:4, P:4, F:9 cal/g)
