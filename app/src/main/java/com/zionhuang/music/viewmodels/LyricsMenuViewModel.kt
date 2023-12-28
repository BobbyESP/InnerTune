package com.zionhuang.music.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zionhuang.music.db.MusicDatabase
import com.zionhuang.music.db.entities.LyricsEntity
import com.zionhuang.music.lyrics.LyricsHelper
import com.zionhuang.music.lyrics.LyricsResult
import com.zionhuang.music.models.MediaMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LyricsMenuViewModel @Inject constructor(
    private val lyricsHelper: LyricsHelper,
    val database: MusicDatabase,
) : ViewModel() {
    private var job: Job? = null
    val results = MutableStateFlow(emptyList<LyricsResult>())
    val isLoading = MutableStateFlow(false)

    fun search(mediaId: String, title: String, artist: String, duration: Int) {
        isLoading.value = true
        results.value = emptyList()
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            lyricsHelper.getLyricsFromAllProviders(mediaId, title, artist, duration) { result ->
                Timber.i("LyricsMenuViewModel search: $result")
                results.update {
                    it + result
                }
            }
            isLoading.value = false
        }
    }

    fun cancelSearch() {
        job?.cancel()
        job = null
    }

    fun chooseLyrics(mediaMetadata: MediaMetadata, lyricsResult: LyricsResult) {
        viewModelScope.launch(Dispatchers.IO) {
            cancelSearch()
            database.query {
                upsert(LyricsEntity(mediaMetadata.id, lyricsResult.lyrics))
            }
        }
    }

    suspend fun refetchLyrics(mediaMetadata: MediaMetadata, lyricsEntity: LyricsEntity?) {
        val lyricsDeferred = withContext(Dispatchers.IO) {
            async { lyricsHelper.getLyrics(mediaMetadata) }
        }

        val lyrics = runCatching {
            lyricsDeferred.await()
        }.getOrNull()

        database.query {
            lyricsEntity?.let(::delete)

            lyrics?.let {
                upsert(LyricsEntity(mediaMetadata.id, it))
            }
        }

    }

}
