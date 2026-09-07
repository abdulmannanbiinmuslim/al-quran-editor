import 'package:freezed_annotation/freezed_annotation.dart';

part 'surah_model.freezed.dart';
part 'surah_model.g.dart';

@freezed
class Surah with _$Surah {
  const factory Surah({
    @JsonKey(name: 'number') required int number,
    @JsonKey(name: 'name') required String name,
    @JsonKey(name: 'englishName') required String englishName,
    @JsonKey(name: 'englishNameTranslation') required String englishNameTranslation,
    @JsonKey(name: 'numberOfAyahs') required int totalAyahs,
    @JsonKey(name: 'revelationType') required String revelationType,
    @Default([]) List<Ayah> ayahs,
  }) = _Surah;

  factory Surah.fromJson(Map<String, dynamic> json) => _$SurahFromJson(json);
}

@freezed
class Ayah with _$Ayah {
  const factory Ayah({
    required int number,
    @JsonKey(name: 'numberInSurah') required int ayahNumberInSurah,
    @JsonKey(name: 'text') required String textUthmani,
    required String banglaTranslation,
    required String englishTranslation,
    required String tafsir,
    @Default(false) bool isFavorite,
    @Default(false) bool isPinned,
    @Default('') String userNote,
  }) = _Ayah;

  factory Ayah.fromJson(Map<String, dynamic> json) => _$AyahFromJson(json);
}

@freezed
class SurahListItem with _$SurahListItem {
  const factory SurahListItem({
    required int number,
    required String name,
    required String englishName,
    required int totalAyahs,
    @Default(false) bool isCompleted,
  }) = _SurahListItem;

  factory SurahListItem.fromJson(Map<String, dynamic> json) =>
      _$SurahListItemFromJson(json);
}
