class AppConstants {
  // App Info
  static const String appName = 'Al Quran Editor';
  static const String appVersion = '1.0.0';
  static const String appPackageName = 'com.alquran.editor';

  // API
  static const String baseUrl = 'https://api.alquran.cloud/v1';
  static const String audioBaseUrl = 'https://everyayah.com/data';

  // Firebase
  static const String firebaseProjectId = 'al-quran-editor';

  // Quran Data
  static const int totalSurahs = 114;
  static const int totalAyahs = 6236;
  static const int totalReciters = 61;

  // Local Storage Keys
  static const String lastReadSurahKey = 'last_read_surah';
  static const String lastReadAyahKey = 'last_read_ayah';
  static const String userPreferencesKey = 'user_preferences';
  static const String bookmarksKey = 'bookmarks';
  static const String favoritesKey = 'favorites';
  static const String notesKey = 'notes';
  static const String readingSettingsKey = 'reading_settings';
  static const String dailyReminderKey = 'daily_reminder';
  static const String streakKey = 'reading_streak';
  static const String plannerKey = 'planner_data';
}
