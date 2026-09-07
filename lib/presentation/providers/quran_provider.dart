import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../../data/models/surah_model.dart';
import '../../data/models/reciter_model.dart';
import '../../data/models/user_model.dart';
import '../../data/models/audio_state_model.dart';
import '../../data/repositories/quran_repository.dart';
import '../../data/services/audio_service.dart';

class QuranProvider extends ChangeNotifier {
  final QuranRepository _quranRepository;
  final AudioService _audioService;

  // State
  List<Surah> _allSurahs = [];
  Surah? _currentSurah;
  List<Ayah> _currentAyahs = [];
  AudioState _audioState = const AudioState();
  ReadingSettings _readingSettings = const ReadingSettings();
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  List<Surah> get allSurahs => _allSurahs;
  Surah? get currentSurah => _currentSurah;
  List<Ayah> get currentAyahs => _currentAyahs;
  AudioState get audioState => _audioState;
  ReadingSettings get readingSettings => _readingSettings;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  QuranProvider({
    required QuranRepository quranRepository,
    required AudioService audioService,
  })
      : _quranRepository = quranRepository,
        _audioService = audioService;

  // Load all Surahs
  Future<void> loadAllSurahs() async {
    try {
      _isLoading = true;
      _errorMessage = null;
      notifyListeners();

      _allSurahs = await _quranRepository.getAllSurahs();
      _errorMessage = null;
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Load specific Surah
  Future<void> loadSurah(int surahNumber) async {
    try {
      _isLoading = true;
      _errorMessage = null;
      notifyListeners();

      final surah = await _quranRepository.getSurahWithBangla(surahNumber);
      _currentSurah = surah;
      _currentAyahs = surah.ayahs;
      _errorMessage = null;
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Play Ayah
  Future<void> playAyah(int surahNumber, Ayah ayah, Reciter reciter) async {
    try {
      _audioState = _audioState.copyWith(isBuffering: true);
      notifyListeners();

      await _audioService.playAyah(surahNumber, ayah.ayahNumberInSurah, reciter);

      _audioState = _audioState.copyWith(
        isPlaying: true,
        isBuffering: false,
        currentAyah: ayah,
        currentReciter: reciter,
      );
    } catch (e) {
      _audioState = _audioState.copyWith(
        isPlaying: false,
        isBuffering: false,
        errorMessage: e.toString(),
      );
    }
    notifyListeners();
  }

  // Toggle Play/Pause
  Future<void> togglePlayPause() async {
    try {
      if (_audioState.isPlaying) {
        await _audioService.pause();
        _audioState = _audioState.copyWith(isPlaying: false);
      } else {
        await _audioService.resume();
        _audioState = _audioState.copyWith(isPlaying: true);
      }
    } catch (e) {
      _audioState = _audioState.copyWith(errorMessage: e.toString());
    }
    notifyListeners();
  }

  // Stop Audio
  Future<void> stopAudio() async {
    try {
      await _audioService.stop();
      _audioState = const AudioState();
    } catch (e) {
      _audioState = _audioState.copyWith(errorMessage: e.toString());
    }
    notifyListeners();
  }

  // Update Reading Settings
  void updateReadingSettings(ReadingSettings settings) {
    _readingSettings = settings;
    notifyListeners();
  }

  // Search Ayahs
  Future<List<Ayah>> searchAyahs(String query) async {
    try {
      return await _quranRepository.searchAyahs(query);
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
      return [];
    }
  }

  @override
  void dispose() {
    _audioService.dispose();
    super.dispose();
  }
}
