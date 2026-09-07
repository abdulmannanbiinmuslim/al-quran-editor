import 'package:flutter/material.dart';

class QuranTextWidget extends StatelessWidget {
  final String text;
  final double fontSize;
  final TextAlign textAlign;

  const QuranTextWidget({
    Key? key,
    required this.text,
    this.fontSize = 24,
    this.textAlign = TextAlign.right,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Text(
      text,
      textAlign: textAlign,
      style: TextStyle(
        fontSize: fontSize,
        fontFamily: 'UthmanicHafs',
        fontWeight: FontWeight.w500,
        height: 1.8,
      ),
    );
  }
}
