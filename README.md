# Al Quran Editor - Flutter Edition

A modern, full-featured Flutter application for reading and listening to the Holy Quran with comprehensive features.

## Features

### Core Reading
- 114 Surahs with complete Quranic text
- Multiple Arabic font styles (Uthmanic, IndoPak, KFGQPC)
- Bangla and English translations
- Tajweed rules highlighting
- Page-based and Ayah-by-Ayah reading modes

### Audio Features
- 61 world-renowned Reciters
- High-quality audio streaming and download
- Synchronized audio playback
- Repeat controls and speed adjustment
- Playback history

### Timing & Synchronization
- LRC (Lyrics) file generator
- SRT subtitle file generator
- VTT video subtitle generator
- Automatic synchronization with audio

### Planning & Progress
- Quran Planner with custom reading goals
- Progress tracking and statistics
- Daily reading reminders
- Streak counter
- Weekly reading summaries

### Personal Library
- Bookmarks with custom folders
- Favorites management
- Personal notes and reflections
- Last read tracking
- Search functionality

### Topics & Dictionary
- Quranic topics and themes
- Subject-wise verse grouping
- Quick reference dictionary

### Cloud Sync
- Firebase authentication
- Cloud backup and restoration
- Cross-device synchronization
- User data persistence

### Customization
- Light and Dark themes
- High contrast mode
- Customizable font sizes
- Line spacing adjustment
- Letter spacing control
- Font weight selection
- Typography presets

### Statistics
- Reading time tracking
- Surah completion stats
- Daily/Weekly/Monthly reports
- Achievement badges

## Project Structure

```
lib/
├── main.dart                 # Entry point
├── config/                   # App configuration
│   ├── app_config.dart
│   ├── constants.dart
│   └── theme/
├── data/                     # Data layer
│   ├── models/              # Data models
│   ├── services/            # API services
│   ├── repositories/        # Repository pattern
│   ├── datasources/         # Local & remote data
│   └── local/               # Local storage
├── presentation/            # Presentation layer
│   ├── screens/            # Screen pages
│   ├── widgets/            # Reusable widgets
│   ├── providers/          # State management
│   └── theme/              # UI theme
└── utils/                  # Utilities
    ├── helpers/
    ├── extensions/
    └── constants/
```

## Getting Started

### Prerequisites
- Flutter 3.16.0 or higher
- Dart 3.0.0 or higher
- Firebase project setup

### Installation

1. Clone the repository
```bash
git clone https://github.com/abdulmannanbiinmuslim/al-quran-editor.git
cd al-quran-editor
```

2. Install dependencies
```bash
flutter pub get
```

3. Generate code (for JSON serialization, etc.)
```bash
flutter pub run build_runner build
```

4. Run the app
```bash
flutter run
```

## Configuration

### Firebase Setup
1. Create a Firebase project
2. Add Android and iOS apps
3. Download `google-services.json` (Android) and `GoogleService-Info.plist` (iOS)
4. Place them in appropriate directories

### Environment Variables
Create `.env` file:
```
GEMINI_API_KEY=your_api_key
FIREBASE_PROJECT_ID=your_project_id
```

## Building

### Android
```bash
flutter build apk --release
```

### iOS
```bash
flutter build ios --release
```

## Architecture

This project follows the Clean Architecture pattern with:
- **Data Layer**: Models, APIs, Local storage
- **Domain Layer**: Use cases, Repositories
- **Presentation Layer**: UI, State Management, Providers

## Technologies Used

- **State Management**: Provider / GetX
- **Local Storage**: Hive, SQLite, SharedPreferences
- **Backend**: Firebase (Auth, Firestore)
- **Audio**: just_audio
- **HTTP**: Dio, Retrofit
- **Navigation**: GoRouter
- **Localization**: Intl

## Contributing

Contributions are welcome! Please follow the code style and submit pull requests.

## License

This project is open source and available under the MIT License.

## Support

For support, email support@alquran-app.com or open an issue on GitHub.
