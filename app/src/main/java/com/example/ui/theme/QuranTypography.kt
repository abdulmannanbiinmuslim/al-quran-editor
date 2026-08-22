package com.example.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranFontFamily

/**
 * Dynamic Typography helper for Quranic Arabic text rendering
 * Supports multiple distinct styles (Uthmani Hafs, Amiri, Scheherazade, IndoPak Nastaleeq, etc.)
 */
object QuranTypography {

    fun getFontFamily(font: QuranFontFamily): FontFamily {
        return when (font) {
            QuranFontFamily.UTHMANIC_HAFS -> FontFamily.Serif
            QuranFontFamily.UTHMANIC_AMIRI -> FontFamily.Serif
            QuranFontFamily.UTHMANIC_SCHEHERAZADE -> FontFamily.Serif
            QuranFontFamily.UTHMANIC_DIGITALKHAT -> FontFamily.Default
            QuranFontFamily.ME_QURAN -> FontFamily.Cursive
            QuranFontFamily.INDOPAK_NASTALEEQ -> FontFamily.Cursive
            QuranFontFamily.INDOPAK_NOOREHUDA -> FontFamily.Serif
            QuranFontFamily.INDOPAK_NOOREHIDAYAT -> FontFamily.SansSerif
            QuranFontFamily.INDOPAK_PDMS_SALEEM -> FontFamily.Serif
        }
    }

    fun getArabicTextStyle(
        font: QuranFontFamily,
        fontSizeSp: Float = 28f,
        fontWeight: FontWeight = FontWeight.Bold,
        letterSpacingSp: Float = 0f
    ): TextStyle {
        val family = getFontFamily(font)
        val lineHeightSp = when (font) {
            QuranFontFamily.INDOPAK_NASTALEEQ -> fontSizeSp * 1.9f
            QuranFontFamily.INDOPAK_NOOREHUDA -> fontSizeSp * 1.8f
            QuranFontFamily.UTHMANIC_AMIRI -> fontSizeSp * 1.7f
            QuranFontFamily.UTHMANIC_SCHEHERAZADE -> fontSizeSp * 1.75f
            QuranFontFamily.ME_QURAN -> fontSizeSp * 1.8f
            else -> fontSizeSp * 1.6f
        }

        val fontStyle = when (font) {
            QuranFontFamily.INDOPAK_NASTALEEQ -> FontStyle.Normal
            QuranFontFamily.ME_QURAN -> FontStyle.Italic
            else -> FontStyle.Normal
        }

        return TextStyle(
            fontFamily = family,
            fontSize = fontSizeSp.sp,
            fontWeight = fontWeight,
            fontStyle = fontStyle,
            lineHeight = lineHeightSp.sp,
            letterSpacing = letterSpacingSp.sp
        )
    }

    fun getFontDisplayName(font: QuranFontFamily): String {
        return when (font) {
            QuranFontFamily.UTHMANIC_HAFS -> "Uthmanic Hafs V20 (Madani)"
            QuranFontFamily.UTHMANIC_AMIRI -> "Amiri Quran (Naskh Style)"
            QuranFontFamily.UTHMANIC_SCHEHERAZADE -> "Scheherazade Quran Calligraphy"
            QuranFontFamily.UTHMANIC_DIGITALKHAT -> "Digitalkhat Standard"
            QuranFontFamily.ME_QURAN -> "MeQuran Classical Naskh"
            QuranFontFamily.INDOPAK_NASTALEEQ -> "IndoPak Nastaleeq (Urdu Style)"
            QuranFontFamily.INDOPAK_NOOREHUDA -> "Noorehuda (Asian Bold)"
            QuranFontFamily.INDOPAK_NOOREHIDAYAT -> "Noorehidayat (High Clarity)"
            QuranFontFamily.INDOPAK_PDMS_SALEEM -> "PDMS Saleem Quran Print"
        }
    }

    fun getFontSampleText(font: QuranFontFamily): String {
        return when (font) {
            QuranFontFamily.INDOPAK_NASTALEEQ,
            QuranFontFamily.INDOPAK_NOOREHUDA,
            QuranFontFamily.INDOPAK_NOOREHIDAYAT,
            QuranFontFamily.INDOPAK_PDMS_SALEEM -> "بِسْمِ اللَّهِ الرَّحْمٰنِ الرَّحِيمِ ۝١"
            else -> "بِسْمِ ٱللَّهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ ۝١"
        }
    }
}
