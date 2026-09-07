import 'package:shared_preferences/shared_preferences.dart';

class PreferencesHelper {
  static late SharedPreferences _preferences;

  static Future<void> initialize() async {
    _preferences = await SharedPreferences.getInstance();
  }

  // String operations
  static Future<void> setString(String key, String value) async {
    await _preferences.setString(key, value);
  }

  static String? getString(String key) {
    return _preferences.getString(key);
  }

  // Int operations
  static Future<void> setInt(String key, int value) async {
    await _preferences.setInt(key, value);
  }

  static int? getInt(String key) {
    return _preferences.getInt(key);
  }

  // Bool operations
  static Future<void> setBool(String key, bool value) async {
    await _preferences.setBool(key, value);
  }

  static bool? getBool(String key) {
    return _preferences.getBool(key);
  }

  // List operations
  static Future<void> setStringList(String key, List<String> value) async {
    await _preferences.setStringList(key, value);
  }

  static List<String>? getStringList(String key) {
    return _preferences.getStringList(key);
  }

  // Remove and Clear
  static Future<void> remove(String key) async {
    await _preferences.remove(key);
  }

  static Future<void> clear() async {
    await _preferences.clear();
  }
}
