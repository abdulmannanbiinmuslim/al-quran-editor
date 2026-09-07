import 'package:freezed_annotation/freezed_annotation.dart';

part 'planner_model.freezed.dart';
part 'planner_model.g.dart';

@freezed
class PlannerItem with _$PlannerItem {
  const factory PlannerItem({
    required String id,
    required String title,
    required String description,
    @Default(7) int durationDays,
    @Default(5) int targetMinutesPerDay,
    @Default('active') String status, // active, paused, completed
    required DateTime createdAt,
    required DateTime startDate,
    DateTime? completedDate,
    @Default([]) List<String> completedDays,
    @Default(0) int totalMinutesRead,
  }) = _PlannerItem;

  factory PlannerItem.fromJson(Map<String, dynamic> json) =>
      _$PlannerItemFromJson(json);
}

@freezed
class ReadingStatistics with _$ReadingStatistics {
  const factory ReadingStatistics({
    @Default(0) int totalMinutesRead,
    @Default(0) int totalAyahsRead,
    @Default(0) int totalSurahsCompleted,
    @Default(0) int currentStreakDays,
    @Default(0) int longestStreakDays,
    @Default({}) Map<String, int> dailyReadMinutes,
    @Default({}) Map<String, int> surahReadCount,
  }) = _ReadingStatistics;

  factory ReadingStatistics.fromJson(Map<String, dynamic> json) =>
      _$ReadingStatisticsFromJson(json);
}
