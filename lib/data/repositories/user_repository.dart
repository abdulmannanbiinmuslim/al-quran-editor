import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:google_sign_in/google_sign_in.dart';
import '../models/user_model.dart';

class UserRepository {
  final FirebaseAuth _firebaseAuth;
  final FirebaseFirestore _firebaseFirestore;
  final GoogleSignIn _googleSignIn;

  UserRepository({
    required FirebaseAuth firebaseAuth,
    required FirebaseFirestore firebaseFirestore,
    required GoogleSignIn googleSignIn,
  })
      : _firebaseAuth = firebaseAuth,
        _firebaseFirestore = firebaseFirestore,
        _googleSignIn = googleSignIn;

  // Get current user
  Future<AppUser?> getCurrentUser() async {
    final user = _firebaseAuth.currentUser;
    if (user == null) return null;

    try {
      final doc = await _firebaseFirestore.collection('users').doc(user.uid).get();
      if (doc.exists) {
        return AppUser.fromJson(doc.data()!);
      }
    } catch (e) {
      throw Exception('Failed to fetch user: $e');
    }
    return null;
  }

  // Sign in with Google
  Future<AppUser> signInWithGoogle() async {
    try {
      final googleUser = await _googleSignIn.signIn();
      if (googleUser == null) throw Exception('Google sign in aborted');

      final googleAuth = await googleUser.authentication;
      final credential = GoogleAuthProvider.credential(
        accessToken: googleAuth.accessToken,
        idToken: googleAuth.idToken,
      );

      final userCredential = await _firebaseAuth.signInWithCredential(credential);
      final user = userCredential.user!;

      // Create or update user document
      final appUser = AppUser(
        uid: user.uid,
        email: user.email ?? '',
        displayName: user.displayName ?? '',
        profileImageUrl: user.photoURL ?? '',
        phoneNumber: user.phoneNumber ?? '',
        createdAt: DateTime.now(),
        lastSignIn: DateTime.now(),
      );

      await _firebaseFirestore.collection('users').doc(user.uid).set(
            appUser.toJson(),
            SetOptions(merge: true),
          );

      return appUser;
    } catch (e) {
      throw Exception('Google sign in failed: $e');
    }
  }

  // Sign out
  Future<void> signOut() async {
    try {
      await _googleSignIn.signOut();
      await _firebaseAuth.signOut();
    } catch (e) {
      throw Exception('Sign out failed: $e');
    }
  }

  // Update user reading statistics
  Future<void> updateReadingStatistics({
    required int minutesRead,
    required int ayahsRead,
  }) async {
    try {
      final user = _firebaseAuth.currentUser;
      if (user == null) throw Exception('User not signed in');

      await _firebaseFirestore.collection('users').doc(user.uid).update({
        'totalReadingMinutes': FieldValue.increment(minutesRead),
        'lastSignIn': DateTime.now(),
      });
    } catch (e) {
      throw Exception('Failed to update statistics: $e');
    }
  }

  // Stream user data
  Stream<AppUser?> userStream() {
    final user = _firebaseAuth.currentUser;
    if (user == null) {
      return Stream.value(null);
    }

    return _firebaseFirestore
        .collection('users')
        .doc(user.uid)
        .snapshots()
        .map((doc) => doc.exists ? AppUser.fromJson(doc.data()!) : null);
  }
}
