package com.zionhuang.music.db.entities.event

import androidx.compose.runtime.Immutable
import androidx.room.Embedded
import androidx.room.Relation
import com.zionhuang.music.db.entities.Event
import com.zionhuang.music.db.entities.Song
import com.zionhuang.music.db.entities.song.SongEntity

@Immutable
data class EventWithSong(
    @Embedded
    val event: Event,
    @Relation(
        entity = SongEntity::class,
        parentColumn = "songId",
        entityColumn = "id"
    )
    val song: Song,
)
