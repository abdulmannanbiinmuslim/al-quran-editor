import 'package:freezed_annotation/freezed_annotation.dart';

part 'topic_model.freezed.dart';
part 'topic_model.g.dart';

@freezed
class Topic with _$Topic {
  const factory Topic({
    required String id,
    required String titleEnglish,
    required String titleBangla,
    @Default('') String description,
    @Default([]) List<TopicReference> references,
  }) = _Topic;

  factory Topic.fromJson(Map<String, dynamic> json) =>
      _$TopicFromJson(json);
}

@freezed
class TopicReference with _$TopicReference {
  const factory TopicReference({
    required int surahNumber,
    required int ayahNumber,
    @Default('') String context,
  }) = _TopicReference;

  factory TopicReference.fromJson(Map<String, dynamic> json) =>
      _$TopicReferenceFromJson(json);
}
