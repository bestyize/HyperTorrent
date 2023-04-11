package com.thewind.torrent.search.service

import com.thewind.torrent.search.model.TorrentConfigResponse
import com.thewind.torrent.search.model.TorrentSearchResponse
import com.thewind.torrent.search.model.TorrentUrlResponse
import com.thewind.util.RetrofitDefault

/**
 * @author: read
 * @date: 2023/3/18 下午6:56
 * @description:
 */


object TorrentServiceHelper {
    fun search(keyword: String, src: Int, page: Int): TorrentSearchResponse {
        return try {
            RetrofitDefault.create(TorrentService::class.java)
                .search(keyword = keyword, src = src, page = page).execute().body()
                ?: TorrentSearchResponse()
        } catch (_: java.lang.Exception) {
            TorrentSearchResponse()
        }
    }

    fun requestMagnetLink(src: Int, link: String): TorrentUrlResponse {
        return try {
            RetrofitDefault.create(TorrentService::class.java).requestMagnetLink(src, link)
                .execute().body() ?: TorrentUrlResponse()
        } catch (_: java.lang.Exception) {
            TorrentUrlResponse()
        }
    }

    fun requestTorrentConfig(): TorrentConfigResponse {
        return try {
            RetrofitDefault.create(TorrentService::class.java).requestTorrentConfig().execute()
                .body() ?: TorrentConfigResponse()
        } catch (_: java.lang.Exception) {
            TorrentConfigResponse()
        }
    }
}

