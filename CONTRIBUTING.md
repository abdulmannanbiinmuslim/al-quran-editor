# Contributing to Al Quran Editor

## Code Style

### Dart Style Guide
- Follow [Dart style guide](https://dart.dev/guides/language/effective-dart)
- Use `dart format` to format code
- Use `dart analyze` to check code quality

### Naming Conventions
- Classes: `PascalCase` (e.g., `QuranProvider`)
- Methods/Variables: `camelCase` (e.g., `loadSurah`)
- Constants: `camelCase` (e.g., `totalSurahs`)
- Files: `snake_case` (e.g., `quran_provider.dart`)

## Architecture

We follow Clean Architecture with:
- **Data Layer**: Models, APIs, Local storage
- **Domain Layer**: Use cases, Repositories
- **Presentation Layer**: UI, State Management

## Commit Messages

```
[Type] Brief description

Optional detailed explanation

Types: feat, fix, docs, style, refactor, test, chore
```

Example:
```
[feat] Add search functionality for Quranic verses

Implemented full-text search across all surahs and ayahs
with support for Arabic text and translations.
```

## Pull Request Process

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m '[feat] Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Testing

```bash
# Run all tests
flutter test

# Run with coverage
flutter test --coverage

# Integration tests
flutter drive --target=test_driver/app.dart
```

## Resources

- [Flutter Documentation](https://flutter.dev/docs)
- [Dart Language Tour](https://dart.dev/guides/language/language-tour)
- [Firebase Flutter Plugins](https://firebase.flutter.dev)

## Questions?

Open an issue or contact support@alquran-app.com
