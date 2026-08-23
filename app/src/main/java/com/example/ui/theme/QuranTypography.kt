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

enum class QuranFontCategory(val displayName: String, val banglaName: String) {
    ALL("All Fonts", "সকল ফন্ট"),
    UTHMANIC_MADANI("Madani / Uthmani", "মাদানী / উসমানী"),
    INDOPAK_NASTALEEQ("IndoPak / Asian", "ইন্দোপাক / নাস্তালিক"),
    CALLIGRAPHIC("Classical & Naskh", "ক্লাসিক্যাল ও নসখ")
}

data class QuranFontDetail(
    val fontFamilyEnum: QuranFontFamily,
    val displayName: String,
    val banglaName: String,
    val scriptType: String,
    val category: QuranFontCategory,
    val assetFileName: String,
    val recommendedSizeSp: Float = 28f,
    val defaultLineHeightMultiplier: Float = 1.7f,
    val description: String,
    val sampleAyahSnippet: String
)

/**
 * Dynamic Typography helper for Quranic Arabic text rendering
 * Supports multiple distinct styles (Uthmani Hafs, Amiri, Scheherazade, IndoPak Nastaleeq, etc.)
 * Loaded directly from high-quality TTF fonts in assets/fonts/ & res/font/
 */
object QuranTypography {

    private val UthmanicHafsFontFamily = try {
        FontFamily(Font(R.font.uthmanic_hafs))
    } catch (e: Throwable) {
        FontFamily.Serif
    }

    private val AmiriFontFamily = try {
        FontFamily(Font(R.font.amiri_quran))
    } catch (e: Throwable) {
        FontFamily.Serif
    }

    private val ScheherazadeFontFamily = try {
        FontFamily(Font(R.font.scheherazade_new))
    } catch (e: Throwable) {
        FontFamily.Serif
    }

    private val DigitalkhatFontFamily = try {
        FontFamily(Font(R.font.digitalkhat))
    } catch (e: Throwable) {
        FontFamily.Default
    }

    private val MeQuranFontFamily = try {
        FontFamily(Font(R.font.me_quran))
    } catch (e: Throwable) {
        FontFamily.Cursive
    }

    private val IndoPakNastaleeqFontFamily = try {
        FontFamily(Font(R.font.indopak_nastaleeq))
    } catch (e: Throwable) {
        FontFamily.Cursive
    }

    private val NoorehudaFontFamily = try {
        FontFamily(Font(R.font.noorehuda))
    } catch (e: Throwable) {
        FontFamily.Serif
    }

    private val NoorehidayatFontFamily = try {
        FontFamily(Font(R.font.noorehidayat))
    } catch (e: Throwable) {
        FontFamily.SansSerif
    }

    private val PdmsSaleemFontFamily = try {
        FontFamily(Font(R.font.pdms_saleem))
    } catch (e: Throwable) {
        FontFamily.Serif
    }

    val availableFonts: List<QuranFontDetail> = listOf(
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.UTHMANIC_HAFS,
            displayName = "Uthmanic Hafs V20 (Madani)",
            banglaName = "উসমানী হাফস ভি২০ (মাদানী)",
            scriptType = "Madani Hafs Naskh",
            category = QuranFontCategory.UTHMANIC_MADANI,
            assetFileName = "fonts/uthmanic_hafs.ttf",
            recommendedSizeSp = 28f,
            defaultLineHeightMultiplier = 1.65f,
            description = "কিং ফাহদ কমপ্লেক্সের স্ট্যান্ডার্ড মদিনা মুসহাফের অফিশিয়াল লিপি।",
            sampleAyahSnippet = "ٱلْحَمْدُ لِلَّهِ رَبِّ ٱلْعَٰلَمِينَ"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.UTHMANIC_AMIRI,
            displayName = "Amiri Quran (Naskh Calligraphy)",
            banglaName = "আমিরি কুরআন (ক্লাসিক্যাল নসখ)",
            scriptType = "Amiri Classical Naskh",
            category = QuranFontCategory.CALLIGRAPHIC,
            assetFileName = "fonts/amiri_quran.ttf",
            recommendedSizeSp = 30f,
            defaultLineHeightMultiplier = 1.75f,
            description = "কায়রো সংস্করণের ঐতিহাসিক মার্জিত ও চমৎকার ক্যালিগ্রাফিক নসখ ফন্ট।",
            sampleAyahSnippet = "ٱلرَّحْمَٰنِ ٱلرَّحِيمِ ۝٣"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.UTHMANIC_SCHEHERAZADE,
            displayName = "Scheherazade Quran (SIL)",
            banglaName = "শেহেরজাদেহ নতুন (ঐতিহ্যবাহী)",
            scriptType = "Traditional Arabic",
            category = QuranFontCategory.CALLIGRAPHIC,
            assetFileName = "fonts/scheherazade_new.ttf",
            recommendedSizeSp = 30f,
            defaultLineHeightMultiplier = 1.75f,
            description = "আন্তর্জাতিক এসআইএল কর্তৃক ডেভেলপকৃত অত্যন্ত স্পষ্ট ও চোখের জন্য আরামদায়ক।",
            sampleAyahSnippet = "مَٰلِكِ يَوْمِ ٱلدِّينِ ۝٤"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.UTHMANIC_DIGITALKHAT,
            displayName = "Digitalkhat Standard",
            banglaName = "ডিজিটাল খত স্ট্যান্ডার্ড",
            scriptType = "Modern Clean Naskh",
            category = QuranFontCategory.UTHMANIC_MADANI,
            assetFileName = "fonts/digitalkhat.ttf",
            recommendedSizeSp = 27f,
            defaultLineHeightMultiplier = 1.6f,
            description = "মোবাইল স্ক্রিনে দ্রুত পড়ার জন্য আধুনিক ও পরিষ্কার ডিজিটাল খত।",
            sampleAyahSnippet = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝٥"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.ME_QURAN,
            displayName = "MeQuran Classical Naskh",
            banglaName = "মি-কুরআন ক্লাসিক্যাল",
            scriptType = "Classical Islamic Naskh",
            category = QuranFontCategory.CALLIGRAPHIC,
            assetFileName = "fonts/me_quran.ttf",
            recommendedSizeSp = 28f,
            defaultLineHeightMultiplier = 1.8f,
            description = "কুরআন পাঠ ও মুখস্থ করার জন্য অত্যন্ত জনপ্রিয় শাস্ত্রীয় শৈলী।",
            sampleAyahSnippet = "ٱهْدِنَا ٱلصِّرَٰطَ ٱلْمُسْتَقِيمَ ۝٦"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.INDOPAK_NASTALEEQ,
            displayName = "IndoPak Nastaleeq (Urdu/Persian)",
            banglaName = "ইন্দোপাক নাস্তালিক ক্যালিগ্রাফি",
            scriptType = "Nastaleeq Hanging Script",
            category = QuranFontCategory.INDOPAK_NASTALEEQ,
            assetFileName = "fonts/indopak_nastaleeq.ttf",
            recommendedSizeSp = 32f,
            defaultLineHeightMultiplier = 1.95f,
            description = "উপমহাদেশের ঐতিহ্যবাহী ঝুলন্ত নাস্তালিক লিপিশৈলী।",
            sampleAyahSnippet = "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ۝١"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.INDOPAK_NOOREHIDAYAT,
            displayName = "Noorehidayat (High Clarity)",
            banglaName = "নূরে হেদায়াত (উচ্চ স্পষ্টতা)",
            scriptType = "Subcontinental Naskh",
            category = QuranFontCategory.INDOPAK_NASTALEEQ,
            assetFileName = "fonts/noorehidayat.ttf",
            recommendedSizeSp = 30f,
            defaultLineHeightMultiplier = 1.8f,
            description = "উপমহাদেশের নূরে হেদায়াত প্রিন্ট, হরকত ও নুকতা অত্যন্ত স্পষ্ট।",
            sampleAyahSnippet = "الرَّحْمٰنِ الرَّحِيمِ ۝٢"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.INDOPAK_NOOREHUDA,
            displayName = "Noorehuda (Asian Bold)",
            banglaName = "নূরে হুদা (বোল্ড এশিয়ান)",
            scriptType = "Subcontinental Bold",
            category = QuranFontCategory.INDOPAK_NASTALEEQ,
            assetFileName = "fonts/noorehuda.ttf",
            recommendedSizeSp = 29f,
            defaultLineHeightMultiplier = 1.8f,
            description = "গাঢ় অক্ষরের ছাপচিত্র যা বয়োজ্যেষ্ঠ ও শিক্ষার্থীদের জন্য উপযোগী।",
            sampleAyahSnippet = "مٰلِكِ يَوْمِ الدِّينِ ۝٣"
        ),
        QuranFontDetail(
            fontFamilyEnum = QuranFontFamily.INDOPAK_PDMS_SALEEM,
            displayName = "PDMS Saleem Quran Print",
            banglaName = "পিডিএমএস সালীম কুরআন প্রিন্ট",
            scriptType = "Subcontinental Standard",
            category = QuranFontCategory.INDOPAK_NASTALEEQ,
            assetFileName = "fonts/pdms_saleem.ttf",
            recommendedSizeSp = 29f,
            defaultLineHeightMultiplier = 1.75f,
            description = "জনপ্রিয় পাকিস্তানি ও ভারতীয় ছাপাখানার অফিশিয়াল কুরআন ফন্ট।",
            sampleAyahSnippet = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ۝٤"
        )
    )

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

    fun parseFontWeight(weightName: String): FontWeight {
        return when (weightName.lowercase()) {
            "normal" -> FontWeight.Normal
            "medium" -> FontWeight.Medium
            "semibold" -> FontWeight.SemiBold
            "bold" -> FontWeight.Bold
            "extrabold" -> FontWeight.ExtraBold
            else -> FontWeight.Bold
        }
    }

    fun getArabicTextStyle(
        font: QuranFontFamily,
        fontSizeSp: Float = 28f,
        fontWeight: FontWeight = FontWeight.Bold,
        letterSpacingSp: Float = 0f,
        lineHeightMultiplier: Float = 0f
    ): TextStyle {
        val family = getFontFamily(font)
        val defaultMultiplier = when (font) {
            QuranFontFamily.INDOPAK_NASTALEEQ -> 1.95f
            QuranFontFamily.INDOPAK_NOOREHUDA -> 1.8f
            QuranFontFamily.INDOPAK_NOOREHIDAYAT -> 1.8f
            QuranFontFamily.UTHMANIC_AMIRI -> 1.75f
            QuranFontFamily.UTHMANIC_SCHEHERAZADE -> 1.75f
            QuranFontFamily.ME_QURAN -> 1.8f
            else -> 1.65f
        }

        val multiplier = if (lineHeightMultiplier > 0f) lineHeightMultiplier else defaultMultiplier
        val lineHeightSp = fontSizeSp * multiplier

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

    fun getFontDetail(font: QuranFontFamily): QuranFontDetail {
        return availableFonts.firstOrNull { it.fontFamilyEnum == font } ?: availableFonts[0]
    }

    fun getFontDisplayName(font: QuranFontFamily): String {
        return getFontDetail(font).displayName
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
