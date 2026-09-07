import 'package:just_audio/just_audio.dart';
import '../models/surah_model.dart';
import '../models/reciter_model.dart';

class AudioService {
  late AudioPlayer _audioPlayer;
  final String audioBaseUrl = 'https://everyayah.com/data';

  AudioService() {
    _audioPlayer = AudioPlayer();
  }

  AudioPlayer get player => _audioPlayer;

  Future<void> playAyah(
    int surahNumber,
    int ayahNumber,
    Reciter reciter,
  ) async {
    try {
      final surahPadded = surahNumber.toString().padLeft(3, '0');
      final ayahPadded = ayahNumber.toString().padLeft(3, '0');
      final url =
          '$audioBaseUrl/${reciter.serverFolder}/$surahPadded$ayahPadded.mp3';

      await _audioPlayer.setUrl(url);
      await _audioPlayer.play();
    } catch (e) {
      throw Exception('Failed to play audio: $e');
    }
  }

  Future<void> playSurah(
    int surahNumber,
    List<Ayah> ayahs,
    Reciter reciter,
  ) async {
    try {
      final playlist = ConcatenatingAudioSource(
        children: [
          for (final ayah in ayahs)
            AudioSource.uri(
              Uri.parse(
                _getAudioUrl(surahNumber, ayah.ayahNumberInSurah, reciter),
              ),
            ),
        ],
      );

      await _audioPlayer.setAudioSource(playlist);
      await _audioPlayer.play();
    } catch (e) {
      throw Exception('Failed to play surah: $e');
    }
  }

  String _getAudioUrl(int surah, int ayah, Reciter reciter) {
    final surahPadded = surah.toString().padLeft(3, '0');
    final ayahPadded = ayah.toString().padLeft(3, '0');
    return '$audioBaseUrl/${reciter.serverFolder}/$surahPadded$ayahPadded.mp3';
  }

  Future<void> pause() async {
    await _audioPlayer.pause();
  }

  Future<void> resume() async {
    await _audioPlayer.play();
  }

  Future<void> stop() async {
    await _audioPlayer.stop();
  }

  Future<void> seek(Duration position) async {
    await _audioPlayer.seek(position);
  }

  Future<void> setPlaybackSpeed(double speed) async {
    await _audioPlayer.setSpeed(speed);
  }

  Future<void> dispose() async {
    await _audioPlayer.dispose();
  }
}
