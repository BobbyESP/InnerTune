package com.zionhuang.music.db.entities.playlist

import androidx.room.Embedded
import androidx.room.Relation
import com.zionhuang.music.db.entities.Song
import com.zionhuang.music.db.entities.song.SongEntity

data class PlaylistSong(
    @Embedded val map: PlaylistSongMap,
    @Relation(
        parentColumn = "songId",
        entityColumn = "id",
        entity = SongEntity::class
    )
    val song: Song,
)
