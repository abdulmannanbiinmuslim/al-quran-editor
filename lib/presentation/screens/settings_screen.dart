import 'package:flutter/material.dart';

class SettingsScreen extends StatefulWidget {
  const SettingsScreen({Key? key}) : super(key: key);

  @override
  State<SettingsScreen> createState() => _SettingsScreenState();
}

class _SettingsScreenState extends State<SettingsScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Settings'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        child: Column(
          children: [
            // Reading Settings
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Reading Settings',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 12),
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        children: [
                          ListTile(
                            title: const Text('Font Size'),
                            subtitle: const Text('Adjust Arabic text size'),
                            trailing: const Text('16 sp'),
                            onTap: () {},
                          ),
                          const Divider(),
                          ListTile(
                            title: const Text('Line Height'),
                            subtitle: const Text('Space between lines'),
                            trailing: const Text('1.5x'),
                            onTap: () {},
                          ),
                          const Divider(),
                          ListTile(
                            title: const Text('Letter Spacing'),
                            subtitle: const Text('Space between letters'),
                            trailing: const Text('0.5'),
                            onTap: () {},
                          ),
                          const Divider(),
                          ListTile(
                            title: const Text('Font Family'),
                            subtitle: const Text('Select Arabic font'),
                            trailing: const Text('Uthmanic'),
                            onTap: () {},
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            // Theme & Display
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Theme & Display',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 12),
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        children: [
                          ListTile(
                            title: const Text('Night Mode'),
                            subtitle: const Text('Enable dark theme'),
                            trailing: Switch(
                              value: false,
                              onChanged: (value) {},
                            ),
                            onTap: () {},
                          ),
                          const Divider(),
                          ListTile(
                            title: const Text('Color Theme'),
                            subtitle: const Text('Change primary color'),
                            trailing: const Text('Islamic Green'),
                            onTap: () {},
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            // Audio Settings
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Audio Settings',
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 12),
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        children: [
                          ListTile(
                            title: const Text('Default Reciter'),
                            subtitle: const Text('Select default reciter'),
                            trailing: const Text('Sudais'),
                            onTap: () {},
                          ),
                          const Divider(),
                          ListTile(
                            title: const Text('Playback Speed'),
                            subtitle: const Text('Default playback speed'),
                            trailing: const Text('1.0x'),
                            onTap: () {},
                          ),
                          const Divider(),
                          ListTile(
                            title: const Text('Repeat Count'),
                            subtitle: const Text('Repeat each ayah'),
                            trailing: const Text('1'),
                            onTap: () {},
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
            // About
            Padding(
              padding: const EdgeInsets.all(16),
              child: Card(
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    children: [
                      ListTile(
                        title: const Text('App Version'),
                        trailing: const Text('1.0.0'),
                        onTap: () {},
                      ),
                      const Divider(),
                      ListTile(
                        title: const Text('Privacy Policy'),
                        trailing: const Icon(Icons.open_in_new),
                        onTap: () {},
                      ),
                      const Divider(),
                      ListTile(
                        title: const Text('Terms of Service'),
                        trailing: const Icon(Icons.open_in_new),
                        onTap: () {},
                      ),
                      const Divider(),
                      ListTile(
                        title: const Text('About'),
                        trailing: const Icon(Icons.info),
                        onTap: () {},
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
