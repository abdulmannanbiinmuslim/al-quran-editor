import 'package:flutter/material.dart';

class AppProvider extends ChangeNotifier {
  ThemeMode _themeMode = ThemeMode.system;
  String _selectedLanguage = 'en';
  int _currentTab = 0;
  int _currentSurahNumber = 1;
  int _currentAyahNumber = 1;

  // Getters
  ThemeMode get themeMode => _themeMode;
  String get selectedLanguage => _selectedLanguage;
  int get currentTab => _currentTab;
  int get currentSurahNumber => _currentSurahNumber;
  int get currentAyahNumber => _currentAyahNumber;

  // Setters
  void setThemeMode(ThemeMode mode) {
    _themeMode = mode;
    notifyListeners();
  }

  void toggleTheme() {
    _themeMode = _themeMode == ThemeMode.dark ? ThemeMode.light : ThemeMode.dark;
    notifyListeners();
  }

  void setLanguage(String language) {
    _selectedLanguage = language;
    notifyListeners();
  }

  void setCurrentTab(int tab) {
    _currentTab = tab;
    notifyListeners();
  }

  void setSurah(int surahNumber, {int ayahNumber = 1}) {
    _currentSurahNumber = surahNumber;
    _currentAyahNumber = ayahNumber;
    notifyListeners();
  }

  void setAyah(int ayahNumber) {
    _currentAyahNumber = ayahNumber;
    notifyListeners();
  }
}
