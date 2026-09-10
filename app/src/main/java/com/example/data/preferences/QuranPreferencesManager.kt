package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AppColorTheme
import com.example.data.model.NightModeOption
import com.example.data.model.QuranFontFamily
import com.example.data.model.QuranScriptType
import com.example.data.model.RecitationMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "quran_settings_preferences")

data class PersistedTypographySettings(
    val selectedFont: QuranFontFamily = QuranFontFamily.UTHMANIC_HAFS,
    val selectedScript: QuranScriptType = QuranScriptType.MADANI_UTHMANI,
    val arabicFontSizeSp: Float = 28f,
    val arabicLineHeightMultiplier: Float = 1.7f,
    val arabicLetterSpacingSp: Float = 0f,
    val arabicFontWeight: String = "Bold",
    val appColorTheme: AppColorTheme = AppColorTheme.EMERALD,
    val nightModeOption: NightModeOption = NightModeOption.LIGHT,
    val recitationMode: RecitationMode = RecitationMode.AYAH_BY_AYAH,
    val wordPauseDurationMs: Long = 500L,
    val letterPauseDurationMs: Long = 350L
)

class QuranPreferencesManager(private val context: Context) {

    private object PreferencesKeys {
        val SELECTED_FONT = stringPreferencesKey("selected_font_family")
        val SELECTED_SCRIPT = stringPreferencesKey("selected_script_type")
        val ARABIC_FONT_SIZE = floatPreferencesKey("arabic_font_size_sp")
        val ARABIC_LINE_HEIGHT = floatPreferencesKey("arabic_line_height_multiplier")
        val ARABIC_LETTER_SPACING = floatPreferencesKey("arabic_letter_spacing_sp")
        val ARABIC_FONT_WEIGHT = stringPreferencesKey("arabic_font_weight")
        val APP_COLOR_THEME = stringPreferencesKey("app_color_theme")
        val NIGHT_MODE_OPTION = stringPreferencesKey("night_mode_option")
        val RECITATION_MODE = stringPreferencesKey("recitation_mode")
        val WORD_PAUSE_DURATION = longPreferencesKey("word_pause_duration_ms")
        val LETTER_PAUSE_DURATION = longPreferencesKey("letter_pause_duration_ms")
    }

    val typographySettingsFlow: Flow<PersistedTypographySettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val fontName = preferences[PreferencesKeys.SELECTED_FONT]
            val selectedFont = fontName?.let {
                try {
                    QuranFontFamily.valueOf(it)
                } catch (e: Exception) {
                    QuranFontFamily.UTHMANIC_HAFS
                }
            } ?: QuranFontFamily.UTHMANIC_HAFS

            val scriptName = preferences[PreferencesKeys.SELECTED_SCRIPT]
            val selectedScript = scriptName?.let {
                try {
                    QuranScriptType.valueOf(it)
                } catch (e: Exception) {
                    QuranScriptType.MADANI_UTHMANI
                }
            } ?: QuranScriptType.MADANI_UTHMANI

            val colorThemeName = preferences[PreferencesKeys.APP_COLOR_THEME]
            val appColorTheme = colorThemeName?.let {
                try {
                    AppColorTheme.valueOf(it)
                } catch (e: Exception) {
                    AppColorTheme.EMERALD
                }
            } ?: AppColorTheme.EMERALD

            val nightModeName = preferences[PreferencesKeys.NIGHT_MODE_OPTION]
            val nightModeOption = nightModeName?.let {
                try {
                    NightModeOption.valueOf(it)
                } catch (e: Exception) {
                    NightModeOption.LIGHT
                }
            } ?: NightModeOption.LIGHT

            val recitationModeName = preferences[PreferencesKeys.RECITATION_MODE]
            val recitationMode = recitationModeName?.let {
                try {
                    RecitationMode.valueOf(it)
                } catch (e: Exception) {
                    RecitationMode.AYAH_BY_AYAH
                }
            } ?: RecitationMode.AYAH_BY_AYAH

            PersistedTypographySettings(
                selectedFont = selectedFont,
                selectedScript = selectedScript,
                arabicFontSizeSp = preferences[PreferencesKeys.ARABIC_FONT_SIZE] ?: 28f,
                arabicLineHeightMultiplier = preferences[PreferencesKeys.ARABIC_LINE_HEIGHT] ?: 1.7f,
                arabicLetterSpacingSp = preferences[PreferencesKeys.ARABIC_LETTER_SPACING] ?: 0f,
                arabicFontWeight = preferences[PreferencesKeys.ARABIC_FONT_WEIGHT] ?: "Bold",
                appColorTheme = appColorTheme,
                nightModeOption = nightModeOption,
                recitationMode = recitationMode,
                wordPauseDurationMs = preferences[PreferencesKeys.WORD_PAUSE_DURATION] ?: 500L,
                letterPauseDurationMs = preferences[PreferencesKeys.LETTER_PAUSE_DURATION] ?: 350L
            )
        }

    suspend fun saveRecitationMode(mode: RecitationMode) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.RECITATION_MODE] = mode.name
        }
    }

    suspend fun saveWordPauseDuration(durationMs: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WORD_PAUSE_DURATION] = durationMs
        }
    }

    suspend fun saveLetterPauseDuration(durationMs: Long) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.LETTER_PAUSE_DURATION] = durationMs
        }
    }

    suspend fun saveFontFamily(font: QuranFontFamily) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_FONT] = font.name
        }
    }

    suspend fun saveScriptType(script: QuranScriptType, defaultFont: QuranFontFamily) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_SCRIPT] = script.name
            preferences[PreferencesKeys.SELECTED_FONT] = defaultFont.name
        }
    }

    suspend fun saveFontSize(sizeSp: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ARABIC_FONT_SIZE] = sizeSp
        }
    }

    suspend fun saveLineHeight(multiplier: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ARABIC_LINE_HEIGHT] = multiplier
        }
    }

    suspend fun saveLetterSpacing(spacingSp: Float) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ARABIC_LETTER_SPACING] = spacingSp
        }
    }

    suspend fun saveFontWeight(weight: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ARABIC_FONT_WEIGHT] = weight
        }
    }

    suspend fun saveFullTypography(
        font: QuranFontFamily,
        fontSizeSp: Float,
        lineHeightMultiplier: Float,
        letterSpacingSp: Float,
        fontWeight: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.SELECTED_FONT] = font.name
            preferences[PreferencesKeys.ARABIC_FONT_SIZE] = fontSizeSp
            preferences[PreferencesKeys.ARABIC_LINE_HEIGHT] = lineHeightMultiplier
            preferences[PreferencesKeys.ARABIC_LETTER_SPACING] = letterSpacingSp
            preferences[PreferencesKeys.ARABIC_FONT_WEIGHT] = fontWeight
        }
    }

    suspend fun saveColorTheme(theme: AppColorTheme) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_COLOR_THEME] = theme.name
        }
    }

    suspend fun saveNightMode(mode: NightModeOption) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.NIGHT_MODE_OPTION] = mode.name
        }
    }
}
