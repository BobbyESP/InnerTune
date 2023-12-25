package com.zionhuang.music.extensions

import android.net.Uri

fun Uri.extractPlaylistId(): String? {
    return if (pathSegments.firstOrNull() == "playlist") {
        getQueryParameter("list")
    } else {
        null
    }
}
