import 'package:freezed_annotation/freezed_annotation.dart';

part 'user_model.freezed.dart';
part 'user_model.g.dart';

@freezed
class AppUser with _$AppUser {
  const factory AppUser({
    required String uid,
    required String email,
    required String displayName,
    @Default('') String profileImageUrl,
    @Default('') String phoneNumber,
    @Default(0) int totalReadingMinutes,
    @Default(0) int currentStreak,
    @Default(0) int longestStreak,
    required DateTime createdAt,
    required DateTime lastSignIn,
  }) = _AppUser;

  factory AppUser.fromJson(Map<String, dynamic> json) =>
      _$AppUserFromJson(json);
}

@freezed
class ReadingSettings with _$ReadingSettings {
  const factory ReadingSettings({
    @Default(16.0) double arabicFontSize,
    @Default(18.0) double translationFontSize,
    @Default('UthmanicHafs') String selectedFont,
    @Default(1.5) double lineHeightMultiplier,
    @Default(0.5) double letterSpacing,
    @Default('regular') String fontWeight,
    @Default('light') String appColorTheme,
    @Default('auto') String nightModeOption,
    @Default(false) bool highContrastNightText,
    @Default('Bangla') String translationLanguage,
    @Default('ayah') String readingLayoutMode,
  }) = _ReadingSettings;

  factory ReadingSettings.fromJson(Map<String, dynamic> json) =>
      _$ReadingSettingsFromJson(json);
}

@freezed
class DailyReminderSettings with _$DailyReminderSettings {
  const factory DailyReminderSettings({
    @Default(false) bool isEnabled,
    @Default('08:00') String reminderTime,
    @Default('daily') String frequency,
    @Default(10) int readingTargetMinutes,
  }) = _DailyReminderSettings;

  factory DailyReminderSettings.fromJson(Map<String, dynamic> json) =>
      _$DailyReminderSettingsFromJson(json);
}
