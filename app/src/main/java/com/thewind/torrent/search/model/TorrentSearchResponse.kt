package com.thewind.torrent.search.model

class TorrentSearchResponse {
    var code: Int = -1
    var list: MutableList<TorrentInfo> = mutableListOf()
}