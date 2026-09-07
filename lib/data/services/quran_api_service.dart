import 'package:dio/dio.dart';
import 'package:retrofit/retrofit.dart';
import '../models/surah_model.dart';
import '../models/reciter_model.dart';

part 'quran_api_service.g.dart';

@RestApi(baseUrl: 'https://api.alquran.cloud/v1')
abstract class QuranApiService {
  factory QuranApiService(Dio dio, {String? baseUrl}) = _QuranApiService;

  // Get all Surahs
  @GET('/surah')
  Future<dynamic> getAllSurahs();

  // Get specific Surah
  @GET('/surah/{number}')
  Future<dynamic> getSurah(@Path('number') int number);

  // Get Surah with translations
  @GET('/surah/{number}/{edition}')
  Future<dynamic> getSurahWithEdition(
    @Path('number') int number,
    @Path('edition') String edition,
  );

  // Get Surah with Bangla translation
  @GET('/surah/{number}/bn.bengali')
  Future<dynamic> getSurahBengali(@Path('number') int number);

  // Get Surah with English translation
  @GET('/surah/{number}/en.asad')
  Future<dynamic> getSurahEnglish(@Path('number') int number);

  // Get specific Ayah
  @GET('/ayah/{number}')
  Future<dynamic> getAyah(@Path('number') int number);

  // Search Ayahs
  @GET('/search')
  Future<dynamic> searchAyahs(
    @Query('q') String query,
    @Query('language') String language,
  );
}
