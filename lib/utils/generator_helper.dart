import 'package:uuid/uuid.dart';

const uuid = Uuid();

class GeneratorHelper {
  static String generateId() {
    return uuid.v4();
  }

  static String generateBookmarkId(int surah, int ayah) {
    return 'bookmark_${surah}_$ayah';
  }

  static String generateNoteId(int surah, int ayah) {
    return 'note_${surah}_${ayah}_${DateTime.now().millisecondsSinceEpoch}';
  }

  static String generatePlannerId() {
    return 'planner_${DateTime.now().millisecondsSinceEpoch}';
  }
}
