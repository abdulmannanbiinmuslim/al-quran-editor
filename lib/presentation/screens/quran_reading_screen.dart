import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../providers/quran_provider.dart';
import '../widgets/quran_text_widget.dart';
import '../widgets/translation_widget.dart';

class QuranReadingScreen extends StatefulWidget {
  final int surahNumber;
  final int initialAyah;

  const QuranReadingScreen({
    Key? key,
    required this.surahNumber,
    this.initialAyah = 1,
  }) : super(key: key);

  @override
  State<QuranReadingScreen> createState() => _QuranReadingScreenState();
}

class _QuranReadingScreenState extends State<QuranReadingScreen> {
  late ScrollController _scrollController;

  @override
  void initState() {
    super.initState();
    _scrollController = ScrollController();
    Future.microtask(() {
      context.read<QuranProvider>().loadSurah(widget.surahNumber);
    });
  }

  @override
  void dispose() {
    _scrollController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Consumer<QuranProvider>(
          builder: (context, provider, _) {
            return Text(
              provider.currentSurah?.englishName ?? 'Quran',
              style: const TextStyle(fontSize: 18),
            );
          },
        ),
        centerTitle: true,
        actions: [
          IconButton(
            icon: const Icon(Icons.settings),
            onPressed: () {
              // Open settings
            },
          ),
        ],
      ),
      body: Consumer<QuranProvider>(
        builder: (context, provider, _) {
          if (provider.isLoading) {
            return const Center(child: CircularProgressIndicator());
          }

          if (provider.errorMessage != null) {
            return Center(
              child: Text('Error: ${provider.errorMessage}'),
            );
          }

          if (provider.currentAyahs.isEmpty) {
            return const Center(child: Text('No Ayahs found'));
          }

          return ListView.builder(
            controller: _scrollController,
            padding: const EdgeInsets.all(16),
            itemCount: provider.currentAyahs.length,
            itemBuilder: (context, index) {
              final ayah = provider.currentAyahs[index];
              return Card(
                margin: const EdgeInsets.only(bottom: 16),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.end,
                    children: [
                      // Ayah number
                      Container(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 12,
                          vertical: 6,
                        ),
                        decoration: BoxDecoration(
                          color: Theme.of(context).colorScheme.primary,
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Text(
                          '${widget.surahNumber}:${ayah.ayahNumberInSurah}',
                          style: const TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      const SizedBox(height: 12),
                      // Arabic text
                      Text(
                        ayah.textUthmani,
                        textAlign: TextAlign.right,
                        style: Theme.of(context).textTheme.headlineSmall,
                      ),
                      const SizedBox(height: 12),
                      // Bangla translation
                      Text(
                        ayah.banglaTranslation,
                        style: Theme.of(context).textTheme.bodyMedium,
                      ),
                      const SizedBox(height: 8),
                      // English translation
                      Text(
                        ayah.englishTranslation,
                        style: Theme.of(context).textTheme.bodySmall?.copyWith(
                              color: Colors.grey,
                            ),
                      ),
                      const SizedBox(height: 12),
                      // Action buttons
                      Row(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        children: [
                          IconButton(
                            icon: const Icon(Icons.bookmark_border),
                            onPressed: () {
                              // Add bookmark
                            },
                          ),
                          IconButton(
                            icon: const Icon(Icons.favorite_border),
                            onPressed: () {
                              // Add favorite
                            },
                          ),
                          IconButton(
                            icon: const Icon(Icons.note_add),
                            onPressed: () {
                              // Add note
                            },
                          ),
                          IconButton(
                            icon: const Icon(Icons.share),
                            onPressed: () {
                              // Share ayah
                            },
                          ),
                          IconButton(
                            icon: const Icon(Icons.volume_up),
                            onPressed: () {
                              // Play audio
                            },
                          ),
                        ],
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
    );
  }
}
