package com.example.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AudioPlayerState(
    val isPlaying: Boolean = false,
    val isBuffering: Boolean = false,
    val currentAyahNumber: Int = 1,
    val currentSurahNumber: Int = 1,
    val currentReciter: ReciterItem = RecitersData.recitersList[37], // Mishary Alafasy default
    val currentAyah: AyahItem? = null,
    val currentPositionMs: Long = 0L,
    val durationMs: Long = 0L,
    val startAyahRange: Int = 1,
    val endAyahRange: Int = 7,
    val repeatAyahTimes: Int = 0,
    val repeatAyahRemaining: Int = 0,
    val repeatRangeTimes: Int = 0,
    val playbackSpeed: Float = 1.0f,
    val hasNext: Boolean = false,
    val hasPrevious: Boolean = false,
    val errorMessage: String? = null
)

class AudioPlayerManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState = _playerState.asStateFlow()

    private var playlistAyahs: List<AyahItem> = emptyList()
    private var currentPlaylistIndex: Int = 0

    init {
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnCompletionListener {
                handleAyahCompletion()
            }
            setOnErrorListener { _, what, extra ->
                Log.e("AudioPlayerManager", "MediaPlayer error: what=$what, extra=$extra")
                _playerState.value = _playerState.value.copy(
                    isPlaying = false,
                    isBuffering = false,
                    errorMessage = "Error playing audio"
                )
                stopProgressTicker()
                true
            }
            setOnPreparedListener { mp ->
                val duration = mp.duration.toLong().coerceAtLeast(0L)
                _playerState.value = _playerState.value.copy(
                    isBuffering = false,
                    isPlaying = true,
                    durationMs = duration,
                    currentPositionMs = 0L
                )
                applyPlaybackSpeed()
                mp.start()
                startProgressTicker()
            }
        }
    }

    private fun startProgressTicker() {
        stopProgressTicker()
        progressJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        val pos = mp.currentPosition.toLong().coerceAtLeast(0L)
                        val dur = mp.duration.toLong().coerceAtLeast(1L)
                        _playerState.value = _playerState.value.copy(
                            currentPositionMs = pos,
                            durationMs = dur
                        )
                    }
                }
                delay(200)
            }
        }
    }

    private fun stopProgressTicker() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun applyPlaybackSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer?.let { mp ->
                try {
                    val speed = _playerState.value.playbackSpeed
                    mp.playbackParams = mp.playbackParams.setSpeed(speed)
                } catch (e: Exception) {
                    Log.w("AudioPlayerManager", "Could not set playback speed: ${e.message}")
                }
            }
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playerState.value = _playerState.value.copy(playbackSpeed = speed)
        applyPlaybackSpeed()
    }

    fun setRepeatAyahTimes(count: Int) {
        _playerState.value = _playerState.value.copy(
            repeatAyahTimes = count,
            repeatAyahRemaining = count
        )
    }

    fun playAyahRange(
        surahNumber: Int,
        ayahs: List<AyahItem>,
        reciter: ReciterItem,
        startAyah: Int,
        endAyah: Int,
        repeatAyahCount: Int = 0
    ) {
        val filteredAyahs = ayahs.filter { it.ayahNumberInSurah in startAyah..endAyah }
        if (filteredAyahs.isEmpty()) return

        playlistAyahs = filteredAyahs
        currentPlaylistIndex = 0
        _playerState.value = _playerState.value.copy(
            currentSurahNumber = surahNumber,
            currentReciter = reciter,
            startAyahRange = startAyah,
            endAyahRange = endAyah,
            repeatAyahTimes = repeatAyahCount,
            repeatAyahRemaining = repeatAyahCount,
            hasPrevious = false,
            hasNext = filteredAyahs.size > 1
        )

        playCurrentAyahFromPlaylist()
    }

    fun playSingleAyah(
        surahNumber: Int,
        ayah: AyahItem,
        reciter: ReciterItem
    ) {
        val allAyahs = QuranData.getAyahsForSurah(surahNumber)
        val targetAyahs = if (allAyahs.isNotEmpty()) allAyahs else listOf(ayah)
        val index = targetAyahs.indexOfFirst { it.ayahNumberInSurah == ayah.ayahNumberInSurah }.coerceAtLeast(0)

        playlistAyahs = targetAyahs
        currentPlaylistIndex = index

        _playerState.value = _playerState.value.copy(
            currentSurahNumber = surahNumber,
            currentReciter = reciter,
            startAyahRange = ayah.ayahNumberInSurah,
            endAyahRange = targetAyahs.lastOrNull()?.ayahNumberInSurah ?: ayah.ayahNumberInSurah,
            repeatAyahTimes = 0,
            repeatAyahRemaining = 0,
            hasPrevious = currentPlaylistIndex > 0,
            hasNext = currentPlaylistIndex < playlistAyahs.size - 1
        )
        playCurrentAyahFromPlaylist()
    }

    private fun playCurrentAyahFromPlaylist() {
        if (currentPlaylistIndex !in playlistAyahs.indices) {
            stopAudio()
            return
        }

        val ayah = playlistAyahs[currentPlaylistIndex]
        val reciter = _playerState.value.currentReciter
        val audioUrl = RecitersData.getAudioUrl(reciter, ayah.surahNumber, ayah.ayahNumberInSurah)

        _playerState.value = _playerState.value.copy(
            currentAyahNumber = ayah.ayahNumberInSurah,
            currentAyah = ayah,
            currentPositionMs = 0L,
            durationMs = 0L,
            isBuffering = true,
            hasPrevious = currentPlaylistIndex > 0,
            hasNext = currentPlaylistIndex < playlistAyahs.size - 1,
            errorMessage = null
        )

        scope.launch(Dispatchers.IO) {
            try {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(audioUrl)
                mediaPlayer?.prepareAsync()
            } catch (e: Exception) {
                Log.e("AudioPlayerManager", "Failed to load audio: ${e.message}")
                _playerState.value = _playerState.value.copy(
                    isBuffering = false,
                    isPlaying = false,
                    errorMessage = "Network or audio stream error"
                )
            }
        }
    }

    private fun handleAyahCompletion() {
        val state = _playerState.value
        if (state.repeatAyahRemaining > 0) {
            _playerState.value = state.copy(repeatAyahRemaining = state.repeatAyahRemaining - 1)
            playCurrentAyahFromPlaylist()
            return
        }

        if (currentPlaylistIndex < playlistAyahs.size - 1) {
            currentPlaylistIndex++
            _playerState.value = _playerState.value.copy(repeatAyahRemaining = state.repeatAyahTimes)
            playCurrentAyahFromPlaylist()
        } else {
            // Reached end of range
            _playerState.value = _playerState.value.copy(
                isPlaying = false,
                isBuffering = false,
                currentPositionMs = _playerState.value.durationMs
            )
            stopProgressTicker()
        }
    }

    fun seekTo(positionMs: Long) {
        mediaPlayer?.let { mp ->
            try {
                val clamped = positionMs.coerceIn(0L, _playerState.value.durationMs.coerceAtLeast(0L)).toInt()
                mp.seekTo(clamped)
                _playerState.value = _playerState.value.copy(currentPositionMs = clamped.toLong())
            } catch (e: Exception) {
                Log.w("AudioPlayerManager", "Error seeking: ${e.message}")
            }
        }
    }

    fun seekForward(milliseconds: Long = 10000L) {
        val target = _playerState.value.currentPositionMs + milliseconds
        seekTo(target)
    }

    fun seekBackward(milliseconds: Long = 10000L) {
        val target = (_playerState.value.currentPositionMs - milliseconds).coerceAtLeast(0L)
        seekTo(target)
    }

    fun skipNext() {
        if (currentPlaylistIndex < playlistAyahs.size - 1) {
            currentPlaylistIndex++
            _playerState.value = _playerState.value.copy(
                repeatAyahRemaining = _playerState.value.repeatAyahTimes
            )
            playCurrentAyahFromPlaylist()
        }
    }

    fun skipPrevious() {
        if (_playerState.value.currentPositionMs > 3000L) {
            // If already played more than 3 seconds, restart current ayah
            seekTo(0L)
        } else if (currentPlaylistIndex > 0) {
            currentPlaylistIndex--
            _playerState.value = _playerState.value.copy(
                repeatAyahRemaining = _playerState.value.repeatAyahTimes
            )
            playCurrentAyahFromPlaylist()
        } else {
            seekTo(0L)
        }
    }

    fun changeReciter(reciter: ReciterItem) {
        _playerState.value = _playerState.value.copy(currentReciter = reciter)
        if (_playerState.value.isPlaying || _playerState.value.isBuffering) {
            playCurrentAyahFromPlaylist()
        }
    }

    fun togglePlayPause() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
                _playerState.value = _playerState.value.copy(isPlaying = false)
                stopProgressTicker()
            } else {
                if (playlistAyahs.isNotEmpty()) {
                    mp.start()
                    _playerState.value = _playerState.value.copy(isPlaying = true)
                    startProgressTicker()
                } else if (_playerState.value.currentAyah != null) {
                    playSingleAyah(
                        _playerState.value.currentSurahNumber,
                        _playerState.value.currentAyah!!,
                        _playerState.value.currentReciter
                    )
                }
            }
        }
    }

    fun stopAudio() {
        stopProgressTicker()
        try {
            mediaPlayer?.stop()
        } catch (e: Exception) {
            Log.w("AudioPlayerManager", "Error stopping: ${e.message}")
        }
        _playerState.value = _playerState.value.copy(
            isPlaying = false,
            isBuffering = false,
            currentPositionMs = 0L
        )
    }

    fun release() {
        stopProgressTicker()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

