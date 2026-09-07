import 'package:freezed_annotation/freezed_annotation.dart';

part 'reciter_model.freezed.dart';
part 'reciter_model.g.dart';

@freezed
class Reciter with _$Reciter {
  const factory Reciter({
    required int id,
    required String identifier,
    required String displayName,
    required String englishName,
    @Default('') String language,
    @Default('') String countryOfOrigin,
    @Default('') String profileImageUrl,
    @Default('') String description,
    required String serverFolder,
    @Default(0) int downloadedSurahs,
    @Default(false) bool isDownloading,
    @Default(false) bool isFavorite,
  }) = _Reciter;

  factory Reciter.fromJson(Map<String, dynamic> json) =>
      _$ReciterFromJson(json);
}

@freezed
class AudioDownload with _$AudioDownload {
  const factory AudioDownload({
    required int surahNumber,
    required String reciterId,
    @Default('pending') String status, // pending, downloading, completed, failed
    @Default(0.0) double progress,
    @Default('') String localPath,
    @Default(0) int fileSize,
  }) = _AudioDownload;

  factory AudioDownload.fromJson(Map<String, dynamic> json) =>
      _$AudioDownloadFromJson(json);
}
