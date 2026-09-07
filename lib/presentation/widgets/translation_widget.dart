import 'package:flutter/material.dart';

class TranslationWidget extends StatelessWidget {
  final String text;
  final String language;
  final double fontSize;

  const TranslationWidget({
    Key? key,
    required this.text,
    this.language = 'Bangla',
    this.fontSize = 16,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          language,
          style: Theme.of(context).textTheme.labelSmall,
        ),
        const SizedBox(height: 4),
        Text(
          text,
          style: TextStyle(
            fontSize: fontSize,
            height: 1.6,
          ),
        ),
      ],
    );
  }
}
