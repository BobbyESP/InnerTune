package com.zionhuang.music.db.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import com.zionhuang.music.db.entities.artist.ArtistEntity

@Immutable
data class Artist(
    @Embedded
    val artist: ArtistEntity,
    val songCount: Int,
) : LocalItem() {
    override val id: String
        get() = artist.id
}
