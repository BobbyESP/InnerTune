package com.zionhuang.music.utils

import android.net.Uri
import androidx.navigation.NavController
import com.zionhuang.innertube.YouTube
import com.zionhuang.music.MainActivity.Companion.sharedSong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

fun processUri(uri: Uri, navController: NavController, coroutineScope: CoroutineScope) {
    val uriPathSegments = uri.pathSegments
    Timber.i("uriPathSegments: $uriPathSegments")
    when (uriPathSegments.firstOrNull()) {
        "playlist" -> handlePlaylistUri(uri, navController, coroutineScope)

        "channel", "c" -> handleChannelUri(uri, navController)

        else -> handleDefaultUri(uri, coroutineScope)
    }
}

private fun handlePlaylistUri(
    uri: Uri,
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    val playlistId = uri.extractPlaylistId()
    if (playlistId != null) {
        if (playlistId.startsWith("OLAK5uy_")) {
            handleSpecialPlaylist(playlistId, navController, coroutineScope)
        } else {
            navController.navigate("online_playlist/$playlistId")
        }
    }
}

private fun handleChannelUri(uri: Uri, navController: NavController) {
    uri.lastPathSegment?.let { artistId ->
        navController.navigate("artist/$artistId")
    }
}

private fun handleDefaultUri(uri: Uri, coroutineScope: CoroutineScope) {
    val path = uri.pathSegments.firstOrNull()
    when {
        path == "watch" -> uri.getQueryParameter("v")
        uri.host == "youtu.be" -> path
        else -> null
    }?.let { videoId ->
        coroutineScope.launch {
            withContext(Dispatchers.IO) {
                YouTube.queue(listOf(videoId))
            }.onSuccess {
                sharedSong.value = it.firstOrNull()
            }.onFailure {
                reportException(it)
            }
        }
    }
}

private fun Uri.extractPlaylistId(): String? {
    return if (pathSegments.firstOrNull() == "playlist") {
        getQueryParameter("list")
    } else {
        null
    }
}

private fun handleSpecialPlaylist(
    playlistId: String,
    navController: NavController,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        YouTube.albumSongs(playlistId).onSuccess { songs ->
            songs.firstOrNull()?.album?.id?.let { browseId ->
                navController.navigate("album/$browseId")
            }
        }.onFailure {
            reportException(it)
        }
    }
}