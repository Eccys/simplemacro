# Google Sign-In Setup Guide

## Overview
Your app now supports **3 authentication methods**:
1. ✅ **Email/Password Sign-Up** (with Firebase)
2. ✅ **Google Sign-In** (one-tap authentication)
3. ✅ **Guest Mode** (local data only)

## Setup Steps

### 1. Enable Email/Password Authentication in Firebase
1. Go to [Firebase Console](https://console.firebase.google.com/project/simplemacro-ecys/authentication/providers)
2. Click **Authentication** → **Sign-in method**
3. Enable **Email/Password** provider
4. Click **Save**

### 2. Enable Google Sign-In
1. In the same **Sign-in method** tab
2. Click on **Google** provider
3. Click **Enable**
4. Select a **Project support email** (your email)
5. Click **Save**

### 3. Get Your Web Client ID
1. After enabling Google Sign-In, click on it again
2. Expand **Web SDK configuration**
3. Copy the **Web client ID** (looks like: `123456789-abcdefg.apps.googleusercontent.com`)

### 4. Add Web Client ID to Your App
1. Open `/home/ecys/git/simplemacro/app/src/main/res/values/strings.xml`
2. Replace `YOUR_WEB_CLIENT_ID_HERE` with your actual Web client ID:

```xml
<string name="default_web_client_id" translatable="false">123456789-abcdefg.apps.googleusercontent.com</string>
```

### 5. Add SHA-1 Certificate to Firebase
Google Sign-In requires SHA-1 fingerprints:

**Debug Certificate (for testing):**
```bash
cd /home/ecys/git/simplemacro
./gradlew signingReport
```

Copy the SHA-1 from the **debug** variant output.

**Add to Firebase:**
1. Go to [Project Settings](https://console.firebase.google.com/project/simplemacro-ecys/settings/general)
2. Scroll to **Your apps** → Select your Android app
3. Click **Add fingerprint**
4. Paste the SHA-1
5. Click **Save**

**For Production (Release Build):**
Generate a release keystore and add its SHA-1 as well.

### 6. Re-download google-services.json
1. After adding SHA-1, download the updated `google-services.json`
2. Replace the existing file at `/home/ecys/git/simplemacro/app/google-services.json`

### 7. Build and Test
```bash
cd /home/ecys/git/simplemacro
./gradlew clean assembleDebug
```

## How It Works

### Sign Up Flow
1. **Guest clicks "Sign Up" button** in Settings
2. **Navigates to Auth Screen** with signup form
3. **User can choose**:
   - Email/Password signup (fills form)
   - Google Sign-In (one tap)
   - Continue as Guest

### Email/Password Sign-Up
- Creates account in **Firebase Authentication**
- Stores user profile locally in **Room database**
- Syncs data across devices (when Firestore integration is added)

### Google Sign-In
- One-tap authentication
- Auto-creates account with Google profile info
- Email and name pre-filled from Google account
- Syncs with Firebase and local database

### Guest Mode
- Local storage only (Room database)
- No Firebase account created
- Can upgrade to full account anytime via Settings

## Features Implemented

✅ **Firebase Authentication** integrated  
✅ **Google Sign-In** button on auth screen  
✅ **Email/Password** signup with profile data  
✅ **"Sign Up" button** in Settings for guests  
✅ **Auto-sync** between Firebase and local database  
✅ **Imperial units** (lbs, feet, inches) in signup  
✅ **Auto-save** in settings (no save buttons)  

## Security Notes

- Passwords are handled by Firebase (never stored locally)
- Google tokens are validated server-side by Firebase
- Local database uses SQLCipher encryption (can be added if needed)
- All auth operations use coroutines for async safety

## Troubleshooting

**Google Sign-In doesn't work:**
1. Check SHA-1 is added to Firebase
2. Verify Web Client ID in strings.xml
3. Make sure Google provider is enabled in Firebase
4. Rebuild app after updating google-services.json

**Email signup fails:**
1. Check Firebase Email/Password is enabled
2. Verify internet connection
3. Check Firebase quotas (free tier limits)

**"Sign Up" button does nothing:**
1. Ensure you're in Guest mode
2. Check navigation is working (look at logcat)

## Next Steps

- Add **Firestore** for cloud data sync
- Implement **password reset** via email
- Add **email verification** for new accounts
- Support **Facebook/Apple Sign-In**
- Add **biometric authentication** for quick login
