package com.zionhuang.music.db.entities

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.zionhuang.music.db.entities.album.AlbumEntity
import com.zionhuang.music.db.entities.artist.ArtistEntity
import com.zionhuang.music.db.entities.song.SongAlbumMap
import com.zionhuang.music.db.entities.song.SongEntity
import com.zionhuang.music.db.entities.song.SortedSongArtistMap

@Immutable
data class Song @JvmOverloads constructor(
    @Embedded val song: SongEntity,
    @Relation(
        entity = ArtistEntity::class,
        entityColumn = "id",
        parentColumn = "id",
        associateBy = Junction(
            value = SortedSongArtistMap::class,
            parentColumn = "songId",
            entityColumn = "artistId"
        )
    )
    val artists: List<ArtistEntity>,
    @Relation(
        entity = AlbumEntity::class,
        entityColumn = "id",
        parentColumn = "id",
        associateBy = Junction(
            value = SongAlbumMap::class,
            parentColumn = "songId",
            entityColumn = "albumId"
        )
    )
    val album: AlbumEntity? = null,
) : LocalItem() {
    override val id: String
        get() = song.id
}
