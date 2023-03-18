package com.thewind.torrent.search.model

class TorrentInfo {
    var detailUrl: String = ""
    var magnetUrl: String = ""
    var title: String = ""
    var date: String = ""
    var size: String = ""
    var src: Int = 0
    var torrentItems = ArrayList<TorrentItem>()
}

class TorrentItem {
    var fileName: String ?= null
    var fileSize: String ?= null
}