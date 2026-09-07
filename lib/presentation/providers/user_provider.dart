import 'package:flutter/material.dart';
import '../../data/models/user_model.dart';
import '../../data/models/bookmark_model.dart';
import '../../data/models/planner_model.dart';
import '../../data/repositories/user_repository.dart';
import '../../data/local/quran_local_database.dart';

class UserProvider extends ChangeNotifier {
  final UserRepository _userRepository;
  final QuranLocalDatabase _localDatabase;

  // State
  AppUser? _currentUser;
  List<Bookmark> _bookmarks = [];
  List<UserNote> _notes = [];
  List<PlannerItem> _planners = [];
  ReadingStatistics? _statistics;
  bool _isLoading = false;
  String? _errorMessage;

  // Getters
  AppUser? get currentUser => _currentUser;
  List<Bookmark> get bookmarks => _bookmarks;
  List<UserNote> get notes => _notes;
  List<PlannerItem> get planners => _planners;
  ReadingStatistics? get statistics => _statistics;
  bool get isLoading => _isLoading;
  String? get errorMessage => _errorMessage;

  UserProvider({
    required UserRepository userRepository,
    required QuranLocalDatabase localDatabase,
  })
      : _userRepository = userRepository,
        _localDatabase = localDatabase;

  // Initialize
  Future<void> initialize() async {
    await loadCurrentUser();
    await loadBookmarks();
    await loadNotes();
    await loadPlanners();
    await loadStatistics();
  }

  // Load current user
  Future<void> loadCurrentUser() async {
    try {
      _isLoading = true;
      _errorMessage = null;
      notifyListeners();

      _currentUser = await _userRepository.getCurrentUser();
      _errorMessage = null;
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Sign in with Google
  Future<void> signInWithGoogle() async {
    try {
      _isLoading = true;
      _errorMessage = null;
      notifyListeners();

      _currentUser = await _userRepository.signInWithGoogle();
      _errorMessage = null;
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Sign out
  Future<void> signOut() async {
    try {
      _isLoading = true;
      _errorMessage = null;
      notifyListeners();

      await _userRepository.signOut();
      _currentUser = null;
      await _localDatabase.clearAll();
      _bookmarks.clear();
      _notes.clear();
      _planners.clear();
      _statistics = null;
      _errorMessage = null;
    } catch (e) {
      _errorMessage = e.toString();
    } finally {
      _isLoading = false;
      notifyListeners();
    }
  }

  // Add Bookmark
  Future<void> addBookmark(Bookmark bookmark) async {
    try {
      await _localDatabase.addBookmark(bookmark);
      _bookmarks.add(bookmark);
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Remove Bookmark
  Future<void> removeBookmark(String bookmarkId) async {
    try {
      await _localDatabase.removeBookmark(bookmarkId);
      _bookmarks.removeWhere((b) => b.id == bookmarkId);
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Load Bookmarks
  Future<void> loadBookmarks() async {
    try {
      _bookmarks = await _localDatabase.getAllBookmarks();
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Add Note
  Future<void> addNote(UserNote note) async {
    try {
      await _localDatabase.saveNote(note);
      _notes.add(note);
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Delete Note
  Future<void> deleteNote(String noteId) async {
    try {
      await _localDatabase.deleteNote(noteId);
      _notes.removeWhere((n) => n.id == noteId);
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Load Notes
  Future<void> loadNotes() async {
    try {
      _notes = await _localDatabase.getAllNotes();
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Add Planner Item
  Future<void> addPlanner(PlannerItem item) async {
    try {
      await _localDatabase.savePlannerItem(item);
      _planners.add(item);
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Load Planners
  Future<void> loadPlanners() async {
    try {
      _planners = await _localDatabase.getPlannerItems();
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }

  // Load Statistics
  Future<void> loadStatistics() async {
    try {
      _statistics = await _localDatabase.getStatistics() ??
          const ReadingStatistics();
      notifyListeners();
    } catch (e) {
      _errorMessage = e.toString();
      notifyListeners();
    }
  }
}
