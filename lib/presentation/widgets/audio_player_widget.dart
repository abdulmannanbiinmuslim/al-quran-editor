import 'package:flutter/material.dart';

class AudioPlayerWidget extends StatelessWidget {
  final bool isPlaying;
  final double progress;
  final Duration currentDuration;
  final Duration totalDuration;
  final VoidCallback onPlayPause;
  final VoidCallback onNext;
  final VoidCallback onPrevious;
  final Function(double) onSeek;

  const AudioPlayerWidget({
    Key? key,
    required this.isPlaying,
    required this.progress,
    required this.currentDuration,
    required this.totalDuration,
    required this.onPlayPause,
    required this.onNext,
    required this.onPrevious,
    required this.onSeek,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.all(16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            Slider(
              value: progress,
              onChanged: onSeek,
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  currentDuration.toString().split('.').first,
                  style: Theme.of(context).textTheme.bodySmall,
                ),
                Text(
                  totalDuration.toString().split('.').first,
                  style: Theme.of(context).textTheme.bodySmall,
                ),
              ],
            ),
            const SizedBox(height: 12),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                IconButton(
                  icon: const Icon(Icons.skip_previous),
                  onPressed: onPrevious,
                ),
                FloatingActionButton(
                  onPressed: onPlayPause,
                  child: Icon(
                    isPlaying ? Icons.pause : Icons.play_arrow,
                  ),
                ),
                IconButton(
                  icon: const Icon(Icons.skip_next),
                  onPressed: onNext,
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}
