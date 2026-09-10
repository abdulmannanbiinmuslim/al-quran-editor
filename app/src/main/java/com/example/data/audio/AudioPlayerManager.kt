package com.example.data.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import com.example.data.model.AyahItem
import com.example.data.model.ReciterItem
import com.example.data.model.RecitationMode
import com.example.data.repository.QuranData
import com.example.data.repository.RecitersData
import com.example.data.timing.SurahTimingData
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
    val errorMessage: String? = null,
    val recitationMode: RecitationMode = RecitationMode.AYAH_BY_AYAH,
    val activeWordIndex: Int = -1,
    val activeLetterIndex: Int = -1
)

class AudioPlayerManager(private val context: Context) {
    val downloadManager = QuranDownloadManager(context)
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var progressJob: Job? = null
    @Volatile
    private var isPrepared: Boolean = false

    private val _playerState = MutableStateFlow(AudioPlayerState())
    val playerState = _playerState.asStateFlow()

    private var playlistAyahs: List<AyahItem> = emptyList()
    private var currentPlaylistIndex: Int = 0
    private var currentSurahTiming: SurahTimingData? = null
    private var isPlayingSurahFile: Boolean = false

    init {
        setupMediaPlayer()
    }

    private fun setupMediaPlayer() {
        stopProgressTicker()
        isPrepared = false
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.w("AudioPlayerManager", "Error releasing previous MediaPlayer: ${e.message}")
        }
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnCompletionListener {
                stopProgressTicker()
                isPrepared = false
                handleAyahCompletion()
            }
            setOnErrorListener { mp, what, extra ->
                Log.e("AudioPlayerManager", "MediaPlayer error: what=$what, extra=$extra")
                isPrepared = false
                stopProgressTicker()
                _playerState.value = _playerState.value.copy(
                    isPlaying = false,
                    isBuffering = false,
                    errorMessage = "Error playing audio"
                )
                try {
                    mp.reset()
                } catch (e: Exception) {
                    Log.w("AudioPlayerManager", "Error resetting on error: ${e.message}")
                }
                true
            }
            setOnPreparedListener { mp ->
                try {
                    isPrepared = true
                    val duration = try {
                        mp.duration.toLong().coerceAtLeast(0L)
                    } catch (e: Exception) {
                        0L
                    }
                    _playerState.value = _playerState.value.copy(
                        isBuffering = false,
                        isPlaying = true,
                        durationMs = duration,
                        currentPositionMs = 0L
                    )
                    applyPlaybackSpeed()
                    mp.start()
                    startProgressTicker()
                } catch (e: Exception) {
                    Log.e("AudioPlayerManager", "Error starting playback after prepared: ${e.message}")
                }
            }
        }
    }

    private fun startProgressTicker() {
        stopProgressTicker()
        progressJob = scope.launch {
            while (isActive) {
                if (isPrepared) {
                    mediaPlayer?.let { mp ->
                        try {
                            if (mp.isPlaying) {
                                val pos = mp.currentPosition.toLong().coerceAtLeast(0L)
                                val dur = mp.duration.toLong().coerceAtLeast(1L)
                                val curAyah = _playerState.value.currentAyah
                                val timing = currentSurahTiming
                                val wordIdx: Int
                                val letterIdx: Int

                                if (timing != null) {
                                    val (actAyahNum, wIdx, lIdx) = downloadManager.timingRepository.findActiveElements(
                                        surahTiming = timing,
                                        positionMs = pos,
                                        currentAyahNumber = curAyah?.ayahNumberInSurah,
                                        isSurahFullAudio = isPlayingSurahFile
                                    )
                                    wordIdx = wIdx
                                    letterIdx = lIdx

                                    // In continuous surah playback, dynamically synchronize active ayah
                                    if (isPlayingSurahFile && curAyah != null && actAyahNum != curAyah.ayahNumberInSurah) {
                                        val newAyah = playlistAyahs.find { it.ayahNumberInSurah == actAyahNum }
                                        if (newAyah != null) {
                                            _playerState.value = _playerState.value.copy(
                                                currentAyahNumber = actAyahNum,
                                                currentAyah = newAyah
                                            )
                                        }
                                    }
                                } else {
                                    wordIdx = if (curAyah != null && curAyah.words.isNotEmpty() && dur > 0) {
                                        ((pos.toFloat() / dur) * curAyah.words.size).toInt().coerceIn(0, curAyah.words.size - 1)
                                    } else -1
                                    letterIdx = if (curAyah != null && curAyah.textUthmani.isNotEmpty() && dur > 0) {
                                        ((pos.toFloat() / dur) * curAyah.textUthmani.length).toInt().coerceIn(0, curAyah.textUthmani.length - 1)
                                    } else -1
                                }

                                _playerState.value = _playerState.value.copy(
                                    currentPositionMs = pos,
                                    durationMs = dur,
                                    activeWordIndex = wordIdx,
                                    activeLetterIndex = letterIdx
                                )
                            }
                        } catch (e: Exception) {
                            // Suppress transient state query exceptions during transitions
                        }
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

    fun setRecitationMode(mode: RecitationMode) {
        _playerState.value = _playerState.value.copy(recitationMode = mode)
    }

    private fun applyPlaybackSpeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isPrepared) {
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

    fun setSpeed(speed: Float) {
        setPlaybackSpeed(speed)
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

    /**
     * Play full continuous surah stream with live synchronized word, letter, and ayah tracking.
     */
    fun playFullSurah(
        surahNumber: Int,
        reciter: ReciterItem,
        startAyah: Int = 1
    ) {
        val allAyahs = QuranData.getAyahsForSurah(surahNumber)
        if (allAyahs.isEmpty()) return

        playlistAyahs = allAyahs
        currentPlaylistIndex = (startAyah - 1).coerceIn(0, allAyahs.size - 1)
        isPlayingSurahFile = true

        val initialAyah = allAyahs[currentPlaylistIndex]

        stopProgressTicker()
        isPrepared = false

        _playerState.value = _playerState.value.copy(
            currentSurahNumber = surahNumber,
            currentReciter = reciter,
            currentAyahNumber = initialAyah.ayahNumberInSurah,
            currentAyah = initialAyah,
            startAyahRange = 1,
            endAyahRange = allAyahs.size,
            repeatAyahTimes = 0,
            repeatAyahRemaining = 0,
            hasPrevious = false,
            hasNext = false,
            isBuffering = true,
            isPlaying = false,
            recitationMode = RecitationMode.SURAH_BY_SURAH,
            errorMessage = null
        )

        scope.launch {
            try {
                currentSurahTiming = downloadManager.timingRepository.getSurahTiming(reciter, surahNumber)
            } catch (e: Exception) {
                Log.w("AudioPlayerManager", "Failed to load timing: ${e.message}")
            }
        }

        val localSurahFile = downloadManager.getLocalAudioFile(reciter, surahNumber)
        val audioSource = if (localSurahFile != null && localSurahFile.exists() && localSurahFile.length() > 2048) {
            localSurahFile.absolutePath
        } else {
            val jsonSurahUrl = runBlocking {
                try {
                    downloadManager.timingRepository.getSurahAudioUrl(reciter, surahNumber)
                } catch (e: Exception) {
                    null
                }
            }
            jsonSurahUrl ?: RecitersData.getSurahFullAudioUrl(reciter, surahNumber)
        }

        try {
            if (mediaPlayer == null) {
                setupMediaPlayer()
            }
            mediaPlayer?.let { mp ->
                mp.reset()
                mp.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                mp.setDataSource(audioSource)
                mp.prepareAsync()
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Failed to load full surah audio: ${e.message}")
            isPrepared = false
            _playerState.value = _playerState.value.copy(
                isBuffering = false,
                isPlaying = false,
                errorMessage = "Surah audio stream error"
            )
        }
    }

    private fun playCurrentAyahFromPlaylist() {
        if (currentPlaylistIndex !in playlistAyahs.indices) {
            stopAudio()
            return
        }

        stopProgressTicker()
        isPrepared = false
        isPlayingSurahFile = false

        val ayah = playlistAyahs[currentPlaylistIndex]
        val reciter = _playerState.value.currentReciter

        scope.launch {
            try {
                currentSurahTiming = downloadManager.timingRepository.getSurahTiming(reciter, ayah.surahNumber)
            } catch (e: Exception) {
                Log.w("AudioPlayerManager", "Failed to load timing: ${e.message}")
            }
        }

        val localFile = downloadManager.getLocalAudioFile(reciter, ayah.surahNumber, ayah.ayahNumberInSurah)
        val audioSource = if (localFile != null && localFile.exists() && localFile.length() > 512) {
            localFile.absolutePath
        } else {
            val jsonUrl = runBlocking {
                try {
                    downloadManager.timingRepository.getAyahAudioUrl(reciter, ayah.surahNumber, ayah.ayahNumberInSurah)
                } catch (e: Exception) {
                    null
                }
            }
            jsonUrl ?: RecitersData.getAudioUrl(reciter, ayah.surahNumber, ayah.ayahNumberInSurah)
        }

        _playerState.value = _playerState.value.copy(
            currentAyahNumber = ayah.ayahNumberInSurah,
            currentAyah = ayah,
            currentPositionMs = 0L,
            durationMs = 0L,
            isBuffering = true,
            isPlaying = false,
            hasPrevious = currentPlaylistIndex > 0,
            hasNext = currentPlaylistIndex < playlistAyahs.size - 1,
            errorMessage = null
        )

        try {
            if (mediaPlayer == null) {
                setupMediaPlayer()
            }
            mediaPlayer?.let { mp ->
                try {
                    mp.reset()
                    mp.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    mp.setDataSource(audioSource)
                    mp.prepareAsync()
                } catch (e: Exception) {
                    Log.e("AudioPlayerManager", "Failed to setup audio source: ${e.message}")
                    setupMediaPlayer()
                    mediaPlayer?.setDataSource(audioSource)
                    mediaPlayer?.prepareAsync()
                }
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Failed to load audio: ${e.message}")
            isPrepared = false
            _playerState.value = _playerState.value.copy(
                isBuffering = false,
                isPlaying = false,
                errorMessage = "Network or audio stream error"
            )
        }
    }

    private fun handleAyahCompletion() {
        val state = _playerState.value
        if (state.repeatAyahRemaining > 0) {
            _playerState.value = state.copy(repeatAyahRemaining = state.repeatAyahRemaining - 1)
            playCurrentAyahFromPlaylist()
            return
        }

        when (state.recitationMode) {
            RecitationMode.SURAH_BY_SURAH -> {
                // Continuous, uninterrupted playback through whole surah
                if (currentPlaylistIndex < playlistAyahs.size - 1) {
                    currentPlaylistIndex++
                    _playerState.value = _playerState.value.copy(repeatAyahRemaining = 0)
                    playCurrentAyahFromPlaylist()
                } else {
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        isBuffering = false,
                        currentPositionMs = _playerState.value.durationMs,
                        activeWordIndex = -1,
                        activeLetterIndex = -1
                    )
                    stopProgressTicker()
                }
            }
            else -> {
                if (currentPlaylistIndex < playlistAyahs.size - 1) {
                    currentPlaylistIndex++
                    _playerState.value = _playerState.value.copy(repeatAyahRemaining = state.repeatAyahTimes)
                    playCurrentAyahFromPlaylist()
                } else {
                    // Reached end of range
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        isBuffering = false,
                        currentPositionMs = _playerState.value.durationMs,
                        activeWordIndex = -1,
                        activeLetterIndex = -1
                    )
                    stopProgressTicker()
                }
            }
        }
    }

    fun seekTo(positionMs: Long) {
        if (!isPrepared) return
        mediaPlayer?.let { mp ->
            try {
                val duration = _playerState.value.durationMs.coerceAtLeast(0L)
                if (duration > 0) {
                    val clamped = positionMs.coerceIn(0L, duration).toInt()
                    mp.seekTo(clamped)
                    _playerState.value = _playerState.value.copy(currentPositionMs = clamped.toLong())
                }
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
        try {
            if (_playerState.value.isPlaying) {
                if (isPrepared) {
                    try {
                        mediaPlayer?.let { mp ->
                            if (mp.isPlaying) {
                                mp.pause()
                            }
                        }
                    } catch (e: Exception) {
                        Log.w("AudioPlayerManager", "Error pausing: ${e.message}")
                    }
                }
                _playerState.value = _playerState.value.copy(isPlaying = false)
                stopProgressTicker()
            } else {
                if (isPrepared && playlistAyahs.isNotEmpty()) {
                    try {
                        mediaPlayer?.start()
                        _playerState.value = _playerState.value.copy(isPlaying = true)
                        startProgressTicker()
                    } catch (e: Exception) {
                        Log.w("AudioPlayerManager", "Error resuming, reloading current ayah: ${e.message}")
                        playCurrentAyahFromPlaylist()
                    }
                } else if (playlistAyahs.isNotEmpty()) {
                    playCurrentAyahFromPlaylist()
                } else if (_playerState.value.currentAyah != null) {
                    playSingleAyah(
                        _playerState.value.currentSurahNumber,
                        _playerState.value.currentAyah!!,
                        _playerState.value.currentReciter
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("AudioPlayerManager", "Error in togglePlayPause: ${e.message}")
        }
    }

    fun stopAudio() {
        stopProgressTicker()
        isPrepared = false
        try {
            mediaPlayer?.let { mp ->
                try {
                    if (mp.isPlaying) {
                        mp.pause()
                    }
                } catch (e: Exception) {
                    Log.w("AudioPlayerManager", "Error pausing in stopAudio: ${e.message}")
                }
                mp.reset()
            }
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
        isPrepared = false
        try {
            mediaPlayer?.release()
        } catch (e: Exception) {
            Log.w("AudioPlayerManager", "Error releasing in release(): ${e.message}")
        }
        mediaPlayer = null
    }
}

