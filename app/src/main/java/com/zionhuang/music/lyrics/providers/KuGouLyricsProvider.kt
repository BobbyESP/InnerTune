package com.zionhuang.music.lyrics.providers

import android.content.Context
import com.zionhuang.kugou.KuGou
import com.zionhuang.music.constants.EnableKugouKey
import com.zionhuang.music.lyrics.LyricsProvider
import com.zionhuang.music.utils.dataStore
import com.zionhuang.music.utils.get
import timber.log.Timber

object KuGouLyricsProvider : LyricsProvider {
    override val name = "Kugou"

    override fun isEnabled(context: Context): Boolean {
        val dataStoreValue = context.dataStore[EnableKugouKey]
        Timber.i("KuGouLyricsProvider isEnabled: $dataStoreValue")

        return dataStoreValue ?: false
    }

    override suspend fun getLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int
    ): Result<String> =
        KuGou.getLyrics(title, artist, duration)

    override suspend fun getAllLyrics(
        id: String,
        title: String,
        artist: String,
        duration: Int,
        callback: (String) -> Unit
    ) {
        KuGou.getAllPossibleLyricsOptions(title, artist, duration, callback)
    }
}
