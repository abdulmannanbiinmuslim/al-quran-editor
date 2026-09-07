import 'package:flutter/material.dart';

class TopicsScreen extends StatelessWidget {
  const TopicsScreen({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final topics = [
      'Faith & Beliefs',
      'Prayer & Worship',
      'Morality & Ethics',
      'Family & Relations',
      'Wealth & Finance',
      'Knowledge & Wisdom',
      'History & Stories',
      'Forgiveness & Mercy',
      'Justice & Rights',
      'Nature & Creation',
      'Hell & Paradise',
      'Angels & Jinn',
    ];

    return Scaffold(
      appBar: AppBar(
        title: const Text('Topics'),
        centerTitle: true,
      ),
      body: GridView.builder(
        padding: const EdgeInsets.all(8),
        gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
          crossAxisCount: 2,
          childAspectRatio: 1,
          crossAxisSpacing: 8,
          mainAxisSpacing: 8,
        ),
        itemCount: topics.length,
        itemBuilder: (context, index) {
          return Card(
            child: InkWell(
              onTap: () {
                // Navigate to topic verses
              },
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Icon(
                      Icons.topic,
                      size: 40,
                      color: Theme.of(context).colorScheme.primary,
                    ),
                    const SizedBox(height: 12),
                    Text(
                      topics[index],
                      textAlign: TextAlign.center,
                      style: Theme.of(context).textTheme.titleSmall,
                    ),
                  ],
                ),
              ),
            ),
          );
        },
      ),
    );
  }
}
