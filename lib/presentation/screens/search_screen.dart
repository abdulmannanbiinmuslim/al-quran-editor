import 'package:flutter/material.dart';

class SearchScreen extends StatefulWidget {
  const SearchScreen({Key? key}) : super(key: key);

  @override
  State<SearchScreen> createState() => _SearchScreenState();
}

class _SearchScreenState extends State<SearchScreen> {
  final TextEditingController _searchController = TextEditingController();
  List<Map<String, String>> _searchResults = [];

  void _performSearch(String query) {
    // Implement search logic
    setState(() {
      _searchResults = [
        {
          'surah': '2',
          'ayah': '255',
          'text': 'Allah - there is no deity except Him...',
          'name': 'Al-Baqarah (The Cow)'
        },
        {
          'surah': '36',
          'ayah': '1',
          'text': 'Ya, Sin. By the Qur\'an full of wisdom...',
          'name': 'Ya-Sin'
        },
      ];
    });
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: TextField(
          controller: _searchController,
          decoration: InputDecoration(
            hintText: 'Search Quran...',
            border: InputBorder.none,
            hintStyle: const TextStyle(color: Colors.white60),
          ),
          style: const TextStyle(color: Colors.white),
          onChanged: _performSearch,
        ),
        centerTitle: true,
      ),
      body: _searchController.text.isEmpty
          ? Center(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    Icons.search,
                    size: 64,
                    color: Theme.of(context).colorScheme.primary,
                  ),
                  const SizedBox(height: 16),
                  const Text('Search for Quranic verses'),
                ],
              ),
            )
          : _searchResults.isEmpty
              ? const Center(
                  child: Text('No results found'),
                )
              : ListView.builder(
                  padding: const EdgeInsets.all(8),
                  itemCount: _searchResults.length,
                  itemBuilder: (context, index) {
                    final result = _searchResults[index];
                    return Card(
                      margin: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 4,
                      ),
                      child: ListTile(
                        title: Text(result['name'] ?? ''),
                        subtitle: Text(
                          '${result['surah']}:${result['ayah']} - ${result['text']}',
                          maxLines: 2,
                          overflow: TextOverflow.ellipsis,
                        ),
                        trailing: const Icon(Icons.arrow_forward_ios),
                        onTap: () {
                          // Navigate to the verse
                        },
                      ),
                    );
                  },
                ),
    );
  }
}
