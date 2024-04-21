package com.zionhuang.music.ui.screens

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.QueueMusic
import androidx.compose.material.icons.rounded.Album
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.QueueMusic
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.zionhuang.music.R

@Immutable
sealed class Screens(
    @StringRes val titleId: Int,
    val icon: ImageVector,
    val route: String,
) {
    data object Home : Screens(R.string.home, Icons.Rounded.Home, "home")
    data object Songs : Screens(R.string.songs, Icons.Rounded.MusicNote, "songs")
    data object Artists : Screens(R.string.artists, Icons.Rounded.Person, "artists")
    data object Albums : Screens(R.string.albums, Icons.Rounded.Album, "albums")
    data object Playlists : Screens(R.string.playlists, Icons.AutoMirrored.Rounded.QueueMusic, "playlists")

    companion object {
        val MainScreens = listOf(Home, Songs, Artists, Albums, Playlists)
    }
}
