import 'package:freezed_annotation/freezed_annotation.dart';
import 'surah_model.dart';
import 'reciter_model.dart';

part 'audio_state_model.freezed.dart';
part 'audio_state_model.g.dart';

@freezed
class AudioState with _$AudioState {
  const factory AudioState({
    @Default(false) bool isPlaying,
    @Default(false) bool isBuffering,
    @Default(0) int currentDuration,
    @Default(0) int totalDuration,
    Ayah? currentAyah,
    Reciter? currentReciter,
    @Default(1.0) double playbackSpeed,
    @Default(1) int repeatCount,
    @Default('') String errorMessage,
  }) = _AudioState;

  factory AudioState.fromJson(Map<String, dynamic> json) =>
      _$AudioStateFromJson(json);
}
