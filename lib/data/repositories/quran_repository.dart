import '../models/surah_model.dart';
import '../services/quran_api_service.dart';
import '../local/quran_local_database.dart';

class QuranRepository {
  final QuranApiService apiService;
  final QuranLocalDatabase localDatabase;

  QuranRepository({
    required this.apiService,
    required this.localDatabase,
  });

  // Get all Surahs
  Future<List<Surah>> getAllSurahs() async {
    try {
      final response = await apiService.getAllSurahs();
      final surahs = (response['data'] as List)
          .map((e) => Surah.fromJson(e))
          .toList();
      return surahs;
    } catch (e) {
      throw Exception('Failed to fetch Surahs: $e');
    }
  }

  // Get specific Surah
  Future<Surah> getSurah(int surahNumber) async {
    try {
      // Try to get from local database first
      final cached = await localDatabase.getSurah(surahNumber);
      if (cached != null) {
        return cached;
      }

      // Fetch from API
      final response = await apiService.getSurah(surahNumber);
      final surah = Surah.fromJson(response['data']);

      // Save to local database
      await localDatabase.saveSurah(surah);

      return surah;
    } catch (e) {
      throw Exception('Failed to fetch Surah $surahNumber: $e');
    }
  }

  // Get Surah with Bangla translation
  Future<Surah> getSurahWithBangla(int surahNumber) async {
    try {
      final response = await apiService.getSurahBengali(surahNumber);
      final surah = Surah.fromJson(response['data']);
      await localDatabase.saveSurah(surah);
      return surah;
    } catch (e) {
      throw Exception('Failed to fetch Surah with Bangla: $e');
    }
  }

  // Search Ayahs
  Future<List<Ayah>> searchAyahs(String query) async {
    try {
      final response = await apiService.searchAyahs(query, 'en');
      final ayahs = (response['matches'] as List)
          .map((e) => Ayah.fromJson(e))
          .toList();
      return ayahs;
    } catch (e) {
      throw Exception('Search failed: $e');
    }
  }
}
