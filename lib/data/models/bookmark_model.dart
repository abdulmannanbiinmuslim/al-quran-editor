import 'package:freezed_annotation/freezed_annotation.dart';

part 'bookmark_model.freezed.dart';
part 'bookmark_model.g.dart';

@freezed
class BookmarkFolder with _$BookmarkFolder {
  const factory BookmarkFolder({
    required String id,
    required String name,
    @Default('#1F7A5F') String colorHex,
    @Default([]) List<String> ayahIds,
    required DateTime createdAt,
    required DateTime updatedAt,
  }) = _BookmarkFolder;

  factory BookmarkFolder.fromJson(Map<String, dynamic> json) =>
      _$BookmarkFolderFromJson(json);
}

@freezed
class Bookmark with _$Bookmark {
  const factory Bookmark({
    required String id,
    required int surahNumber,
    required int ayahNumber,
    @Default([]) List<String> folderIds,
    required DateTime createdAt,
  }) = _Bookmark;

  factory Bookmark.fromJson(Map<String, dynamic> json) =>
      _$BookmarkFromJson(json);
}

@freezed
class UserNote with _$UserNote {
  const factory UserNote({
    required String id,
    required int surahNumber,
    required int ayahNumber,
    required String content,
    required DateTime createdAt,
    required DateTime updatedAt,
  }) = _UserNote;

  factory UserNote.fromJson(Map<String, dynamic> json) =>
      _$UserNoteFromJson(json);
}
