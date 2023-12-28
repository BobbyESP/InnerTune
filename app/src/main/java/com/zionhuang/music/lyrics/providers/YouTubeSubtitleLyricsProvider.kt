package com.zionhuang.music.lyrics.providers

import android.content.Context
import com.zionhuang.innertube.YouTube
import com.zionhuang.music.lyrics.LyricsProvider

object YouTubeSubtitleLyricsProvider : LyricsProvider {
    override val name = "YouTube Subtitle"
    override fun isEnabled(context: Context) = true
    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int
    ): Result<String> =
        YouTube.transcript(id)
}
