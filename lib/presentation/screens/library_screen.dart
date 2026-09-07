import 'package:flutter/material.dart';

class LibraryScreen extends StatefulWidget {
  const LibraryScreen({Key? key}) : super(key: key);

  @override
  State<LibraryScreen> createState() => _LibraryScreenState();
}

class _LibraryScreenState extends State<LibraryScreen>
    with SingleTickerProviderStateMixin {
  late TabController _tabController;

  @override
  void initState() {
    super.initState();
    _tabController = TabController(length: 4, vsync: this);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Library'),
        centerTitle: true,
        bottom: TabBar(
          controller: _tabController,
          tabs: const [
            Tab(text: 'Last Read'),
            Tab(text: 'Favorites'),
            Tab(text: 'Bookmarks'),
            Tab(text: 'Notes'),
          ],
        ),
      ),
      body: TabBarView(
        controller: _tabController,
        children: [
          // Last Read
          ListView(
            padding: const EdgeInsets.all(8),
            children: [
              Card(
                margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                child: ListTile(
                  title: const Text('Surah Al-Baqarah'),
                  subtitle: const Text('Ayah 1-10'),
                  trailing: const Text('Yesterday'),
                  onTap: () {},
                ),
              ),
            ],
          ),
          // Favorites
          ListView(
            padding: const EdgeInsets.all(8),
            children: [
              Card(
                margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                child: ListTile(
                  title: const Text('2:255 (Ayat Al-Kursi)'),
                  subtitle: const Text('The Throne Verse'),
                  trailing: IconButton(
                    icon: const Icon(Icons.favorite),
                    onPressed: () {},
                  ),
                  onTap: () {},
                ),
              ),
            ],
          ),
          // Bookmarks
          ListView(
            padding: const EdgeInsets.all(8),
            children: [
              Card(
                margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                child: ListTile(
                  title: const Text('Surah Al-Imran Ayah 103'),
                  subtitle: const Text('Personal Collection'),
                  trailing: IconButton(
                    icon: const Icon(Icons.bookmark),
                    onPressed: () {},
                  ),
                  onTap: () {},
                ),
              ),
            ],
          ),
          // Notes
          ListView(
            padding: const EdgeInsets.all(8),
            children: [
              Card(
                margin: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                child: ListTile(
                  title: const Text('Note on Surah Al-Fatihah'),
                  subtitle: const Text('This is a reminder about the opening chapter...'),
                  trailing: IconButton(
                    icon: const Icon(Icons.delete),
                    onPressed: () {},
                  ),
                  onTap: () {},
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
