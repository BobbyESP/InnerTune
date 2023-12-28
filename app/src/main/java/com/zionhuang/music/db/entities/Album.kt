package com.zionhuang.music.db.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.zionhuang.music.db.entities.album.AlbumArtistMap
import com.zionhuang.music.db.entities.album.AlbumEntity
import com.zionhuang.music.db.entities.artist.ArtistEntity

@Immutable
data class Album(
    @Embedded
    val album: AlbumEntity,
    @Relation(
        entity = ArtistEntity::class,
        entityColumn = "id",
        parentColumn = "id",
        associateBy = Junction(
            value = AlbumArtistMap::class,
            parentColumn = "albumId",
            entityColumn = "artistId"
        )
    )
    val artists: List<ArtistEntity>,
) : LocalItem() {
    override val id: String
        get() = album.id
}
