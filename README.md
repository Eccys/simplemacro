# SimpleMacro

Track what you eat.

## What It Does

SimpleMacro helps you track your daily food intake. You enter what you eat and the app shows your progress visually with graphs.

### Track Your Food
- Add meals and snacks
- Log calories, carbs, protein, and fat
- See your daily totals
- View your BMI and body fat using the Jackson-Pollock method

### Set Your Goals
- Pick your daily calorie target and macro goals (carbs, protein, fat)
- Track your weight and height
- Input your measurements for a body fat percentage calculation

### Stay On Track
- See progress bars for each goal
- View past days in calendar
- Get a simple overview of your stats
- Edit or delete entries anytime

## Getting Started

You can start three ways:
1. Sign up with your email
2. Log in with Google
3. Use guest mode

Guest mode lets you try the app without an account. Your data stays on your phone only.

## How It Works

The app saves your data on your phone. You can use it offline. If you make an account, your data syncs with the cloud.

### Home Screen
You see a big circle that shows your daily calories. Below that are three smaller circles for carbs, protein, and fat. A plus button lets you add food.

### Settings Screen
Here you can:
- Update your profile (name, age, weight, height)
- Change your daily goals
- Use the body fat calculator
- Log out or delete your account

## Building The App

You need:
- Android Studio
- Java 17 or newer

Steps:
1. Open the project in Android Studio
2. Wait for Gradle to sync
3. Click Run

To make an APK file:
```bash
./gradlew assembleDebug
```

The APK will be in `app/build/outputs/apk/debug/`.

## Firebase Setup

The app uses Firebase for:
- Email login
- Google Sign-In
- Cloud sync

You need a `google-services.json` file. Get it from the Firebase Console. Put it in the `app/` folder.

**Note:** The `.gitignore` file blocks this file. It will not get pushed to GitHub.

## Tech Used

- **Kotlin** for all the code
- **Jetpack Compose** for the UI
- **Room Database** to save data
- **Firebase** for accounts and sync
- **Material Design 3** for the look

## Project Layout

```
app/
├── data/          Data models and database
├── ui/            Screens and UI parts
│   ├── screens/   Each screen
│   ├── theme/     Colors and styles
│   └── viewmodel/ App logic
└── MainActivity   App start point
```

## What's Stored

### User Data
- Email and name
- Daily goals
- Weight and height
- Gender and age

### Food Entries
- Date and time
- Calories
- Carbs, protein, and fat
- Which user it belongs to

## License

MIT License. You can use this code freely.

## Help Out

Want to add a feature? Found a bug? Pull requests are welcome.
