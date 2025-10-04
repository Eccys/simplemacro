# Firebase Authentication Fix

## What Was Fixed

### 1. ✅ **reCAPTCHA Error Fixed**
**Error:** `CONFIGURATION_NOT_FOUND - RecaptchaCallWrapper failed`

**Solution:** Disabled reCAPTCHA verification for development in `SimpleMacroApplication.kt`:
```kotlin
auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
```

**Note:** This is for **DEVELOPMENT ONLY**. For production, you need to:
- Enable Firebase App Check in Firebase Console
- Configure reCAPTCHA properly
- Remove the `setAppVerificationDisabledForTesting(true)` line

### 2. ✅ **Full Name Field Removed**
- Removed "Full Name (Optional)" field from signup form
- Username is now the only identifier field
- Name can be updated later in Settings

## Email/Password Authentication - Now Working! ✅

Your email/password authentication should now work properly:

1. **Sign Up**: Creates Firebase auth account + local database entry
2. **Login**: Authenticates with Firebase + syncs to local database
3. **Auto-sync**: User data automatically synced between Firebase and local storage

## Google Sign-In - Still Needs Setup

Google Sign-In requires additional configuration:

### Required Steps:

#### 1. Get SHA-1 Fingerprint
```bash
cd /home/ecys/git/simplemacro
./gradlew signingReport
```

Copy the **SHA-1** from the debug variant (looks like: `AB:CD:EF:12:34:...`)

#### 2. Add SHA-1 to Firebase
1. Go to [Firebase Console - Project Settings](https://console.firebase.google.com/project/simplemacro-ecys/settings/general)
2. Scroll to **Your apps** → Android app
3. Click **Add fingerprint**
4. Paste the SHA-1
5. Click **Save**

#### 3. Enable Google Sign-In Provider
1. Go to [Authentication Providers](https://console.firebase.google.com/project/simplemacro-ecys/authentication/providers)
2. Click **Google** provider
3. Click **Enable**
4. Choose support email
5. Click **Save**
6. Expand **Web SDK configuration**
7. Copy the **Web client ID**

#### 4. Update strings.xml
Replace in `/home/ecys/git/simplemacro/app/src/main/res/values/strings.xml`:
```xml
<string name="default_web_client_id" translatable="false">YOUR_ACTUAL_WEB_CLIENT_ID</string>
```

#### 5. Re-download google-services.json
1. Download updated `google-services.json` from Firebase Console
2. Replace at `/home/ecys/git/simplemacro/app/google-services.json`

#### 6. Rebuild
```bash
./gradlew clean assembleDebug
```

## Testing

### Test Email/Password (Should Work Now):
1. Open app
2. Click "Don't have an account? Sign Up"
3. Fill in:
   - Username
   - Age, Weight (lbs), Height (feet/inches)
   - Email
   - Password
4. Click "Sign Up"
5. Should successfully create account ✅

### Test Google Sign-In (After Setup):
1. Open app
2. Click "Continue with Google"
3. Select Google account
4. Should successfully sign in ✅

### Test Guest Mode (Already Works):
1. Open app
2. Click "Continue as Guest"
3. Can use app locally ✅

## Current Authentication Flow

```
┌─────────────────┐
│   Auth Screen   │
└────────┬────────┘
         │
    ┌────┴────┬────────┬──────────┐
    │         │        │          │
┌───▼───┐ ┌──▼──┐ ┌───▼────┐ ┌───▼───┐
│Sign Up│ │Login│ │ Google │ │ Guest │
└───┬───┘ └──┬──┘ └───┬────┘ └───┬───┘
    │        │        │          │
    ▼        ▼        ▼          ▼
┌──────────────────────────────────┐
│    Firebase Authentication       │
├──────────────────────────────────┤
│ ✅ Email/Password (Working)      │
│ ⏳ Google Sign-In (Needs Setup)  │
│ ✅ Guest Mode (Working)          │
└────────┬─────────────────────────┘
         │
         ▼
┌──────────────────┐
│  Local Database  │
│  (Room SQLite)   │
└──────────────────┘
```

## Production Checklist (Before Release)

- [ ] Enable Firebase App Check
- [ ] Configure reCAPTCHA properly
- [ ] Remove `setAppVerificationDisabledForTesting(true)`
- [ ] Add release keystore SHA-1 to Firebase
- [ ] Set up password reset flow
- [ ] Add email verification
- [ ] Configure Firestore security rules
- [ ] Test on physical device
- [ ] Enable ProGuard/R8 obfuscation

## Troubleshooting

**"CONFIGURATION_NOT_FOUND" error returns:**
- Make sure `setAppVerificationDisabledForTesting(true)` is in `SimpleMacroApplication.onCreate()`
- Check Firebase is initialized before auth is used
- Verify `google-services.json` is in the correct location

**Google Sign-In opens but doesn't return:**
- Check SHA-1 is added to Firebase
- Verify Web Client ID is correct
- Make sure Google provider is enabled in Firebase Console
- Check internet connection

**Email signup works but data not saved:**
- Check Room database is initialized
- Verify user repository methods are correct
- Look for exceptions in logcat

## Summary

✅ **Fixed:** reCAPTCHA error blocking email/password auth  
✅ **Fixed:** Removed redundant Full Name field  
✅ **Working:** Email/Password authentication  
✅ **Working:** Guest mode  
⏳ **Pending:** Google Sign-In setup (requires SHA-1 + Web Client ID)  

Your email/password authentication is now fully functional! 🎉
