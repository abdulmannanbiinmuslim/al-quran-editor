# Flutter Al Quran Editor - Migration Guide

## Converting from Kotlin/Android to Flutter/Dart

هذا ডকুমেন্ট Kotlin Android প্রজেক্ট থেকে Flutter Dart প্রজেক্টে মাইগ্রেশনের জন্য সম্পূর্ণ গাইড।

## প্রজেক্ট স্ট্রাকচার

### Kotlin (Android) → Flutter (Dart) ম্যাপিং

```
Android/Kotlin              Flutter/Dart
────────────────            ────────────
Activity                 → Screen (StatefulWidget)
Fragment                 → Screen/Widget
ViewModel                → Provider/GetX
Repository               → Repository class
Service                  → Service class
Model/POJO               → Freezed Model
RxJava/Coroutines        → Streams/Futures
Room Database            → Hive/SQLite
SharedPreferences        → SharedPreferences
Retrofit                 → Dio/Retrofit
Firebase Auth            → Firebase Auth
```

## কী বৈশিষ্ট্য কনভার্ট করা হয়েছে

### 1. Core Features (✅ সম্পন্ন)
- ✅ 114 Surahs ডেটা ম্যানেজমেন্ট
- ✅ Ayah display with translations (Bangla/English)
- ✅ Quran reading screens
- ✅ Surah list navigation

### 2. Audio System (✅ সম্পন্ন)
- ✅ just_audio integration
- ✅ 61 Reciters support
- ✅ Audio playback controls
- ✅ Playback speed & repeat

### 3. User Library (✅ সম্পন্ন)
- ✅ Bookmarks management
- ✅ Favorites tracking
- ✅ Personal notes
- ✅ Last read history

### 4. Planning & Statistics (✅ সম্পন্ন)
- ✅ Quran planner
- ✅ Reading statistics
- ✅ Daily reminders
- ✅ Streak tracking

### 5. Customization (✅ সম্পন্ন)
- ✅ Theme system (Light/Dark)
- ✅ Font customization
- ✅ Text size adjustment
- ✅ Reading mode options

### 6. Cloud Sync (✅ সম্পন্ন)
- ✅ Firebase authentication
- ✅ Google Sign-in
- ✅ Cloud data persistence

### 7. Timing Generator (✅ সম্পন্ন)
- ✅ LRC file generation
- ✅ SRT file generation
- ✅ VTT file generation

## ডেপেন্ডেন্সি ম্যাপিং

### Android/Kotlin Dependencies
```kotlin
// Jetpack
androidx.compose.ui
androidx.lifecycle
androidx.room

// Firebase
firebase.auth
cloud.firestore

// Network
retrofit
okhttp
moshi

// Storage
shared_preferences
```

### Flutter/Dart Equivalents
```yaml
# State Management
provider: ^6.4.0
get: ^4.6.6

# Storage
hive: ^2.2.3
shared_preferences: ^2.2.2
sqflite: ^2.3.3+1

# Firebase
firebase_core: ^2.24.2
firebase_auth: ^4.15.3
cloud_firestore: ^4.14.0

# Network
dio: ^5.3.1
retrofit: ^4.1.0

# Audio
just_audio: ^0.9.34

# Utilities
freeezed_annotation: ^2.4.1
json_annotation: ^4.8.1
```

## সেটআপ নির্দেশাবলী

### 1. ক্লোন করুন
```bash
git clone https://github.com/abdulmannanbiinmuslim/al-quran-editor.git
cd al-quran-editor
```

### 2. ব্রাঞ্চ চেকআউট করুন
```bash
git checkout flutter-conversion
```

### 3. ডেপেন্ডেন্সি ইনস্টল করুন
```bash
flutter pub get
```

### 4. কোড জেনারেশন
```bash
flutter pub run build_runner build --delete-conflicting-outputs
```

### 5. চালান
```bash
flutter run
```

## Firebase সেটআপ

### Android
1. Firebase Console এ যান
2. Android অ্যাপ যোগ করুন
3. `google-services.json` ডাউনলোড করুন
4. `android/app/` এ রাখুন

### iOS
1. Firebase Console এ যান
2. iOS অ্যাপ যোগ করুন
3. `GoogleService-Info.plist` ডাউনলোড করুন
4. Xcode এর মাধ্যমে `ios/Runner` এ যোগ করুন

## বিল্ড করুন

### APK (Android)
```bash
flutter build apk --release
```

### iOS
```bash
flutter build ios --release
```

## ফাইল স্ট্রাকচার

```
lib/
├── main.dart                    # এন্ট্রি পয়েন্ট
├── config/
│   ├── app_config.dart
│   ├── constants.dart
│   └── theme/
│       └── app_theme.dart
├── data/
│   ├── models/                  # ডেটা মডেল
│   ├── services/                # API সার্ভিস
│   ├── repositories/            # রিপোজিটরি প্যাটার্ন
│   ├── datasources/
│   └── local/                   # লোকাল স্টোরেজ
├── presentation/
│   ├── screens/                 # UI স্ক্রিন
│   ├── widgets/                 # রিইউজেবল উইজেট
│   ├── providers/               # স্টেট ম্যানেজমেন্ট
│   └── theme/                   # থিম
└── utils/                       # ইউটিলিটি ফাংশন
```

## পরবর্তী ধাপসমূহ

1. **JSON মডেল জেনারেশন**
   ```bash
   flutter pub run build_runner build
   ```

2. **Firebase কনফিগারেশন**
   - Firebase credentials আপডেট করুন
   - Google Sign-in সেটআপ করুন

3. **API এন্ডপয়েন্ট**
   - Quran API endpoints কনফিগার করুন
   - Audio streaming URL সেটআপ করুন

4. **টেস্টিং**
   ```bash
   flutter test
   flutter drive --target=test_driver/app.dart
   ```

## ট্রাবলশুটিং

### Common Issues

1. **Build Runner Issues**
   ```bash
   flutter clean
   flutter pub get
   flutter pub run build_runner build --delete-conflicting-outputs
   ```

2. **Firebase Issues**
   - Ensure credentials are properly placed
   - Check Firebase console for app registration

3. **Pub Get Issues**
   ```bash
   flutter pub cache clean
   flutter pub get
   ```

## অবদান

অবদান স্বাগত! অনুগ্রহ করে একটি Pull Request জমা দিন।

## লাইসেন্স

MIT লাইসেন্সের অধীনে।

## সাপোর্ট

সমর্থনের জন্য: support@alquran-app.com
