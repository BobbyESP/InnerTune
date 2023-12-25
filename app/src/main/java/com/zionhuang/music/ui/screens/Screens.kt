package com.zionhuang.music.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import com.zionhuang.music.R

@Immutable
sealed class Screens(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int,
    val route: String,
) {
    data object Home : Screens(R.string.home, R.drawable.home, "home")
    data object Songs : Screens(R.string.songs, R.drawable.music_note, "songs")
    data object Artists : Screens(R.string.artists, R.drawable.artist, "artists")
    data object Albums : Screens(R.string.albums, R.drawable.album, "albums")
    data object Playlists : Screens(R.string.playlists, R.drawable.queue_music, "playlists")
}
