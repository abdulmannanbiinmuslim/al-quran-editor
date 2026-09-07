import 'package:flutter/material.dart';

class AudioManagerScreen extends StatefulWidget {
  const AudioManagerScreen({Key? key}) : super(key: key);

  @override
  State<AudioManagerScreen> createState() => _AudioManagerScreenState();
}

class _AudioManagerScreenState extends State<AudioManagerScreen> {
  double _playbackSpeed = 1.0;
  int _repeatCount = 1;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Audio Manager'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Playback Speed
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Playback Speed',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 12),
                    Slider(
                      value: _playbackSpeed,
                      min: 0.5,
                      max: 2.0,
                      divisions: 6,
                      label: '${_playbackSpeed.toStringAsFixed(1)}x',
                      onChanged: (value) {
                        setState(() => _playbackSpeed = value);
                      },
                    ),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          '0.5x',
                          style: Theme.of(context).textTheme.bodySmall,
                        ),
                        Text(
                          '${_playbackSpeed.toStringAsFixed(1)}x',
                          style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                                fontWeight: FontWeight.bold,
                              ),
                        ),
                        Text(
                          '2.0x',
                          style: Theme.of(context).textTheme.bodySmall,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            // Repeat Count
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Repeat Each Ayah',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 12),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.remove),
                          onPressed: _repeatCount > 1
                              ? () => setState(() => _repeatCount--)
                              : null,
                        ),
                        Container(
                          padding: const EdgeInsets.symmetric(
                            horizontal: 24,
                            vertical: 12,
                          ),
                          decoration: BoxDecoration(
                            border: Border.all(),
                            borderRadius: BorderRadius.circular(8),
                          ),
                          child: Text(
                            _repeatCount.toString(),
                            style: Theme.of(context).textTheme.headlineSmall,
                          ),
                        ),
                        IconButton(
                          icon: const Icon(Icons.add),
                          onPressed: _repeatCount < 10
                              ? () => setState(() => _repeatCount++)
                              : null,
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 16),
            // Audio Format
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      'Audio Quality',
                      style: Theme.of(context).textTheme.titleMedium,
                    ),
                    const SizedBox(height: 12),
                    RadioListTile<String>(
                      title: const Text('High Quality (128 kbps)'),
                      value: 'high',
                      groupValue: 'high',
                      onChanged: (value) {},
                    ),
                    RadioListTile<String>(
                      title: const Text('Normal Quality (64 kbps)'),
                      value: 'normal',
                      groupValue: 'high',
                      onChanged: (value) {},
                    ),
                    RadioListTile<String>(
                      title: const Text('Low Quality (32 kbps)'),
                      value: 'low',
                      groupValue: 'high',
                      onChanged: (value) {},
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
