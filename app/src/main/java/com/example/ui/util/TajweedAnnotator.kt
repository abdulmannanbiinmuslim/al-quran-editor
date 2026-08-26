package com.example.ui.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString

object TajweedAnnotator {

    // Standard Authentic Mushaf Tajweed Colors (Light Theme)
    val MaddColorLight = Color(0xFFD32F2F)      // Crimson Red (মাদ - দীর্ঘস্বর)
    val MaddColorDark = Color(0xFFFF6B6B)       // Bright Soft Red in Night mode

    val GhunnahColorLight = Color(0xFFE65100)   // Deep Orange (ওয়াজিব গুন্নাহ)
    val GhunnahColorDark = Color(0xFFFFB74D)    // Warm Amber in Night mode

    val IkhfaColorLight = Color(0xFF2E7D32)     // Forest Green (ইখফা - গোপন নাসিক্য)
    val IkhfaColorDark = Color(0xFF81C784)      // Crisp Mint Green in Night mode

    val IdghamColorLight = Color(0xFF7B1FA2)    // Royal Purple (ইদগাম - মিলিয়ে পড়া)
    val IdghamColorDark = Color(0xFFCE93D8)     // Lilac Purple in Night mode

    val QalqalahColorLight = Color(0xFF0288D1)  // Ocean Blue (কলকলাহ - প্রতিধ্বনি)
    val QalqalahColorDark = Color(0xFF4FC3F7)   // Sky Blue in Night mode

    val IqlabColorLight = Color(0xFFC2185B)     // Rose Pink (ইকলাব - মীমে রূপান্তর)
    val IqlabColorDark = Color(0xFFF48FB1)      // Soft Rose in Night mode

    val AllahNameColorLight = Color(0xFF1B5E20) // Deep Islamic Green for Allah's Name
    val AllahNameColorDark = Color(0xFFA5D6A7)  // Radiant Green in Night mode

    val DefaultTextLight = Color(0xFF1A241E)
    val DefaultTextDark = Color(0xFFF2FAF5)

    private fun isCombiningMark(c: Char): Boolean {
        val code = c.code
        return (code in 0x0610..0x061A) ||
               (code in 0x064B..0x065F) ||
               (code == 0x0670) ||
               (code in 0x06D6..0x06ED) ||
               (c == '\u0653') || (c == '\u0651') || (c == '\u0652') || (c == '\u06E1') || (c == '\u06E2')
    }

    private fun getClusterEnd(text: String, startIndex: Int): Int {
        var end = startIndex + 1
        while (end < text.length && isCombiningMark(text[end])) {
            end++
        }
        return end
    }

    /**
     * Parses Arabic text and applies rich Tajweed color styling based on classic rules.
     * CRITICAL: Uses ONLY SpanStyle(color = ...) without changing fontWeight, fontSize,
     * fontFamily, or letterSpacing, ensuring Android HarfBuzz cursive shaping remains
     * 100% intact and letters NEVER detach or separate from each other.
     */
    fun buildTajweedText(
        text: String,
        baseTextColor: Color = DefaultTextLight,
        isDark: Boolean = false,
        enabled: Boolean = true
    ): AnnotatedString {
        if (text.isEmpty()) return AnnotatedString("")

        if (!enabled) {
            return buildAnnotatedString {
                append(text)
                addStyle(SpanStyle(color = baseTextColor), 0, text.length)
            }
        }

        val maddColor = if (isDark) MaddColorDark else MaddColorLight
        val ghunnahColor = if (isDark) GhunnahColorDark else GhunnahColorLight
        val ikhfaColor = if (isDark) IkhfaColorDark else IkhfaColorLight
        val qalqalahColor = if (isDark) QalqalahColorDark else QalqalahColorLight
        val iqlabColor = if (isDark) IqlabColorDark else IqlabColorLight
        val allahColor = if (isDark) AllahNameColorDark else AllahNameColorLight

        return buildAnnotatedString {
            append(text)
            // Base uniform color for the entire ayah
            addStyle(SpanStyle(color = baseTextColor), 0, text.length)

            val length = text.length
            var i = 0

            while (i < length) {
                val c = text[i]

                // Skip whitespace, ayah separators (۝), digits
                if (c.isWhitespace() || c == '۝' || (c in '0'..'9') || (c in '\u0660'..'\u0669')) {
                    i++
                    continue
                }

                // 1. Check for Lafz Allah (الله / ٱللَّهِ / ٱللَّهُ / ٱللَّهَ / لِلَّهِ)
                if (c == 'ٱ' || c == 'ا' || c == 'ل') {
                    val remaining = text.substring(i)
                    val allahPrefixes = listOf(
                        "ٱللَّهِ", "ٱللَّهُ", "ٱللَّهَ", "ٱللَّه",
                        "اللَّهِ", "اللَّهُ", "اللَّهَ", "اللَّه",
                        "لِلَّهِ", "لِلَّه", "الله"
                    )
                    val matched = allahPrefixes.firstOrNull { remaining.startsWith(it) }
                    if (matched != null) {
                        val wordEnd = i + matched.length
                        addStyle(SpanStyle(color = allahColor), i, wordEnd)
                        i = wordEnd
                        continue
                    }
                }

                val clusterEnd = getClusterEnd(text, i)
                val clusterString = text.substring(i, clusterEnd)

                // 2. Check for Madd: Contains Maddah sign (ٓ / \u0653 / \u06E4 / ~)
                if (clusterString.contains('\u0653') || clusterString.contains('\u06E4') || clusterString.contains('ٓ') || clusterString.contains('~')) {
                    addStyle(SpanStyle(color = maddColor), i, clusterEnd)
                    i = clusterEnd
                    continue
                }

                // 3. Check for Ghunnah: Noon (ن) or Meem (م) with Shaddah (ّ / \u0651)
                if ((c == 'ن' || c == 'م') && (clusterString.contains('\u0651') || clusterString.contains('ّ'))) {
                    addStyle(SpanStyle(color = ghunnahColor), i, clusterEnd)
                    i = clusterEnd
                    continue
                }

                // 4. Check for Iqlab: Small Meem (ۢ / \u06E2 / ۭ / \u06ED)
                if (clusterString.contains('\u06E2') || clusterString.contains('ۢ') || clusterString.contains('ۭ') || clusterString.contains('\u06ED')) {
                    addStyle(SpanStyle(color = iqlabColor), i, clusterEnd)
                    i = clusterEnd
                    continue
                }

                // 5. Check for Qalqalah letters (ق, ط, ب, ج, د) with Sukun (ْ / \u0652 / \u06E1 / ۡ)
                if (c in listOf('ق', 'ط', 'ب', 'ج', 'د') && (clusterString.contains('\u0652') || clusterString.contains('\u06E1') || clusterString.contains('ْ') || clusterString.contains('ۡ'))) {
                    addStyle(SpanStyle(color = qalqalahColor), i, clusterEnd)
                    i = clusterEnd
                    continue
                }

                // 6. Check for Tanween (ً, ٌ, ٍ) indicating Ikhfa / Idgham
                if (clusterString.contains('\u064B') || clusterString.contains('\u064C') || clusterString.contains('\u064D')) {
                    addStyle(SpanStyle(color = ikhfaColor), i, clusterEnd)
                    i = clusterEnd
                    continue
                }

                i = clusterEnd
            }
        }
    }
}

