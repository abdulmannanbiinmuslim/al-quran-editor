import 'package:hive_flutter/hive_flutter.dart';
import '../models/surah_model.dart';
import '../models/bookmark_model.dart';
import '../models/user_model.dart';
import '../models/planner_model.dart';

class QuranLocalDatabase {
  static const String surahBoxName = 'surahs';
  static const String bookmarkBoxName = 'bookmarks';
  static const String favoritesBoxName = 'favorites';
  static const String notesBoxName = 'notes';
  static const String settingsBoxName = 'settings';
  static const String plannerBoxName = 'planner';
  static const String statsBoxName = 'statistics';

  late Box<dynamic> surahBox;
  late Box<dynamic> bookmarkBox;
  late Box<dynamic> favoritesBox;
  late Box<dynamic> notesBox;
  late Box<dynamic> settingsBox;
  late Box<dynamic> plannerBox;
  late Box<dynamic> statsBox;

  Future<void> initializeBoxes() async {
    await Hive.initFlutter();
    surahBox = await Hive.openBox(surahBoxName);
    bookmarkBox = await Hive.openBox(bookmarkBoxName);
    favoritesBox = await Hive.openBox(favoritesBoxName);
    notesBox = await Hive.openBox(notesBoxName);
    settingsBox = await Hive.openBox(settingsBoxName);
    plannerBox = await Hive.openBox(plannerBoxName);
    statsBox = await Hive.openBox(statsBoxName);
  }

  // Surah Operations
  Future<void> saveSurah(Surah surah) async {
    await surahBox.put('surah_${surah.number}', surah.toJson());
  }

  Future<Surah?> getSurah(int surahNumber) async {
    final data = surahBox.get('surah_$surahNumber');
    if (data != null) {
      return Surah.fromJson(Map<String, dynamic>.from(data));
    }
    return null;
  }

  // Bookmark Operations
  Future<void> addBookmark(Bookmark bookmark) async {
    await bookmarkBox.put(bookmark.id, bookmark.toJson());
  }

  Future<void> removeBookmark(String bookmarkId) async {
    await bookmarkBox.delete(bookmarkId);
  }

  Future<List<Bookmark>> getAllBookmarks() async {
    final bookmarks = <Bookmark>[];
    for (final value in bookmarkBox.values) {
      bookmarks.add(Bookmark.fromJson(Map<String, dynamic>.from(value)));
    }
    return bookmarks;
  }

  // Favorites Operations
  Future<void> addFavorite(String ayahId) async {
    await favoritesBox.put(ayahId, true);
  }

  Future<void> removeFavorite(String ayahId) async {
    await favoritesBox.delete(ayahId);
  }

  Future<bool> isFavorite(String ayahId) async {
    return favoritesBox.containsKey(ayahId);
  }

  // Notes Operations
  Future<void> saveNote(UserNote note) async {
    await notesBox.put(note.id, note.toJson());
  }

  Future<void> deleteNote(String noteId) async {
    await notesBox.delete(noteId);
  }

  Future<List<UserNote>> getAllNotes() async {
    final notes = <UserNote>[];
    for (final value in notesBox.values) {
      notes.add(UserNote.fromJson(Map<String, dynamic>.from(value)));
    }
    return notes;
  }

  // Settings Operations
  Future<void> saveSettings(ReadingSettings settings) async {
    await settingsBox.put('reading_settings', settings.toJson());
  }

  Future<ReadingSettings?> getSettings() async {
    final data = settingsBox.get('reading_settings');
    if (data != null) {
      return ReadingSettings.fromJson(Map<String, dynamic>.from(data));
    }
    return null;
  }

  // Planner Operations
  Future<void> savePlannerItem(PlannerItem item) async {
    await plannerBox.put(item.id, item.toJson());
  }

  Future<List<PlannerItem>> getPlannerItems() async {
    final items = <PlannerItem>[];
    for (final value in plannerBox.values) {
      items.add(PlannerItem.fromJson(Map<String, dynamic>.from(value)));
    }
    return items;
  }

  // Statistics Operations
  Future<void> saveStatistics(ReadingStatistics stats) async {
    await statsBox.put('statistics', stats.toJson());
  }

  Future<ReadingStatistics?> getStatistics() async {
    final data = statsBox.get('statistics');
    if (data != null) {
      return ReadingStatistics.fromJson(Map<String, dynamic>.from(data));
    }
    return null;
  }

  Future<void> clearAll() async {
    await surahBox.clear();
    await bookmarkBox.clear();
    await favoritesBox.clear();
    await notesBox.clear();
    await settingsBox.clear();
    await plannerBox.clear();
    await statsBox.clear();
  }
}
