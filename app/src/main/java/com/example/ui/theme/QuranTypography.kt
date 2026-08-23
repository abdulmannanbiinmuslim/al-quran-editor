package com.example.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.QuranFontFamily

/**
 * Dynamic Typography helper for Quranic Arabic text rendering
 * Supports multiple distinct styles (Uthmani Hafs, Amiri, Scheherazade, IndoPak Nastaleeq, etc.)
 * Loaded directly from high-quality TTF fonts in assets / res/font
 */
object QuranTypography {

    private val UthmanicHafsFontFamily = FontFamily(Font(R.font.uthmanic_hafs))
    private val AmiriFontFamily = FontFamily(Font(R.font.amiri_quran))
    private val ScheherazadeFontFamily = FontFamily(Font(R.font.scheherazade_new))
    private val DigitalkhatFontFamily = FontFamily(Font(R.font.digitalkhat))
    private val MeQuranFontFamily = FontFamily(Font(R.font.me_quran))
    private val IndoPakNastaleeqFontFamily = FontFamily(Font(R.font.indopak_nastaleeq))
    private val NoorehudaFontFamily = FontFamily(Font(R.font.noorehuda))
    private val NoorehidayatFontFamily = FontFamily(Font(R.font.noorehidayat))
    private val PdmsSaleemFontFamily = FontFamily(Font(R.font.pdms_saleem))

    fun getFontFamily(font: QuranFontFamily): FontFamily {
        return when (font) {
            QuranFontFamily.UTHMANIC_HAFS -> UthmanicHafsFontFamily
            QuranFontFamily.UTHMANIC_AMIRI -> AmiriFontFamily
            QuranFontFamily.UTHMANIC_SCHEHERAZADE -> ScheherazadeFontFamily
            QuranFontFamily.UTHMANIC_DIGITALKHAT -> DigitalkhatFontFamily
            QuranFontFamily.ME_QURAN -> MeQuranFontFamily
            QuranFontFamily.INDOPAK_NASTALEEQ -> IndoPakNastaleeqFontFamily
            QuranFontFamily.INDOPAK_NOOREHUDA -> NoorehudaFontFamily
            QuranFontFamily.INDOPAK_NOOREHIDAYAT -> NoorehidayatFontFamily
            QuranFontFamily.INDOPAK_PDMS_SALEEM -> PdmsSaleemFontFamily
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
