import 'package:just_audio/just_audio.dart';
import 'dart:async';

class TimingFileGenerator {
  // Generate LRC format timing file
  static String generateLRC({
    required List<Map<String, dynamic>> ayahs,
    required String surahName,
  }) {
    final buffer = StringBuffer();
    buffer.writeln('[ar:Quran]');
    buffer.writeln('[ti:$surahName]');
    buffer.writeln('[al:Holy Quran]');
    buffer.writeln('[length: 00:00:00]');
    buffer.writeln('');

    for (final ayah in ayahs) {
      final timeMs = ayah['timeMs'] as int? ?? 0;
      final minutes = timeMs ~/ 60000;
      final seconds = (timeMs % 60000) ~/ 1000;
      final millis = timeMs % 1000;
      final timeStr =
          '${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}.${millis.toString().padLeft(2, '0')}';
      buffer.writeln('[$timeStr]${ayah['text']}');
    }

    return buffer.toString();
  }

  // Generate SRT format timing file
  static String generateSRT({
    required List<Map<String, dynamic>> ayahs,
    required String surahName,
  }) {
    final buffer = StringBuffer();
    buffer.writeln('1');
    buffer.writeln('00:00:00,000 --> 00:00:05,000');
    buffer.writeln(surahName);
    buffer.writeln('');

    for (int i = 0; i < ayahs.length; i++) {
      final ayah = ayahs[i];
      final startTimeMs = ayah['timeMs'] as int? ?? 0;
      final endTimeMs = i + 1 < ayahs.length
          ? ayahs[i + 1]['timeMs'] as int? ?? 0
          : startTimeMs + 3000;

      final startStr = _formatSRTTime(startTimeMs);
      final endStr = _formatSRTTime(endTimeMs);

      buffer.writeln('${i + 2}');
      buffer.writeln('$startStr --> $endStr');
      buffer.writeln('${ayah['text']}');
      buffer.writeln('');
    }

    return buffer.toString();
  }

  // Generate VTT format timing file
  static String generateVTT({
    required List<Map<String, dynamic>> ayahs,
    required String surahName,
  }) {
    final buffer = StringBuffer();
    buffer.writeln('WEBVTT');
    buffer.writeln('');
    buffer.writeln('00:00:00.000 --> 00:00:05.000');
    buffer.writeln(surahName);
    buffer.writeln('');

    for (int i = 0; i < ayahs.length; i++) {
      final ayah = ayahs[i];
      final startTimeMs = ayah['timeMs'] as int? ?? 0;
      final endTimeMs = i + 1 < ayahs.length
          ? ayahs[i + 1]['timeMs'] as int? ?? 0
          : startTimeMs + 3000;

      final startStr = _formatVTTTime(startTimeMs);
      final endStr = _formatVTTTime(endTimeMs);

      buffer.writeln('$startStr --> $endStr');
      buffer.writeln('${ayah['text']}');
      buffer.writeln('');
    }

    return buffer.toString();
  }

  static String _formatSRTTime(int milliseconds) {
    final hours = milliseconds ~/ 3600000;
    final minutes = (milliseconds % 3600000) ~/ 60000;
    final seconds = (milliseconds % 60000) ~/ 1000;
    final millis = milliseconds % 1000;

    return '${hours.toString().padLeft(2, '0')}:${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')},${millis.toString().padLeft(3, '0')}';
  }

  static String _formatVTTTime(int milliseconds) {
    final hours = milliseconds ~/ 3600000;
    final minutes = (milliseconds % 3600000) ~/ 60000;
    final seconds = (milliseconds % 60000) ~/ 1000;
    final millis = milliseconds % 1000;

    return '${hours.toString().padLeft(2, '0')}:${minutes.toString().padLeft(2, '0')}:${seconds.toString().padLeft(2, '0')}.${millis.toString().padLeft(3, '0')}';
  }
}
