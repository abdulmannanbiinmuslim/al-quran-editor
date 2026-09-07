import 'package:firebase_core/firebase_core.dart';

class DefaultFirebaseOptions {
  static FirebaseOptions get currentPlatform {
    return web;
  }

  static const FirebaseOptions web = FirebaseOptions(
    apiKey: 'AIzaSyDQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ',
    authDomain: 'al-quran-editor.firebaseapp.com',
    projectId: 'al-quran-editor',
    storageBucket: 'al-quran-editor.appspot.com',
    messagingSenderId: '123456789',
    appId: '1:123456789:web:abcdef1234567890',
  );

  static const FirebaseOptions android = FirebaseOptions(
    apiKey: 'AIzaSyDQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ',
    appId: '1:123456789:android:1234567890abcdef',
    messagingSenderId: '123456789',
    projectId: 'al-quran-editor',
    storageBucket: 'al-quran-editor.appspot.com',
  );

  static const FirebaseOptions ios = FirebaseOptions(
    apiKey: 'AIzaSyDQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ',
    appId: '1:123456789:ios:1234567890abcdef',
    messagingSenderId: '123456789',
    projectId: 'al-quran-editor',
    storageBucket: 'al-quran-editor.appspot.com',
    iosBundleId: 'com.alquran.editor',
  );
}
