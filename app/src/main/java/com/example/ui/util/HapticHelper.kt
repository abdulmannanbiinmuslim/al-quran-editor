package com.example.ui.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

class AppHapticHelper(
    private val composeHaptic: androidx.compose.ui.hapticfeedback.HapticFeedback?,
    private val vibrator: Vibrator?
) {
    /**
     * Subtle click feedback when toggling buttons or changing font styles
     */
    fun tap() {
        try {
            composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        } catch (ignored: Throwable) {
            fallbackVibrate(15L)
        }
    }

    /**
     * Crisp feedback when switching verses / scrolling to ayah
     */
    fun verseSwitch() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && vibrator != null) {
            try {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
                return
            } catch (ignored: Throwable) {}
        }
        fallbackVibrate(20L)
    }

    /**
     * Medium feedback when expanding/collapsing Tafsir or toggling audio
     */
    fun toggleTafsir() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && vibrator != null) {
            try {
                vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
                return
            } catch (ignored: Throwable) {}
        }
        fallbackVibrate(35L)
    }

    /**
     * Light tick on incremental font size changes
     */
    fun fontSizeTick() {
        try {
            composeHaptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        } catch (ignored: Throwable) {
            fallbackVibrate(10L)
        }
    }

    /**
     * Joyful celebratory double-pulse when favoriting verse or completing surah
     */
    fun celebration() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && vibrator != null) {
            try {
                val timings = longArrayOf(0, 40, 60, 70)
                val amplitudes = intArrayOf(0, 180, 0, 255)
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
                return
            } catch (ignored: Throwable) {}
        }
        fallbackVibrate(80L)
    }

    private fun fallbackVibrate(durationMs: Long) {
        try {
            vibrator?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    it.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    it.vibrate(durationMs)
                }
            }
        } catch (ignored: Throwable) {}
    }
}

@Composable
fun rememberAppHaptics(): AppHapticHelper {
    val composeHaptic = LocalHapticFeedback.current
    val context = LocalContext.current
    return remember(context, composeHaptic) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        AppHapticHelper(composeHaptic, vibrator)
    }
}
