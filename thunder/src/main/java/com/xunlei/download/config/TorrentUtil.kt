package com.xunlei.download.config

/**
 * @author: read
 * @date: 2023/3/3 上午12:46
 * @description:
 */
object TorrentUtil {

    fun getMagnetHash(magnetUrl: String?): String {
        magnetUrl ?: return ""
        if (magnetUrl.endsWith(".torrent") && magnetUrl.contains("/")) return magnetUrl.substring(IntRange(magnetUrl.lastIndexOf("/") + 1, magnetUrl.lastIndexOf(".") - 1))
        val magnetLink = if (magnetUrl.startsWith(TORRENT_PREFIX)) {
            magnetUrl
        } else {
            TORRENT_PREFIX + magnetUrl
        }
        val minLen = TORRENT_PREFIX.length + 32
        if (magnetLink.length < minLen) {
            return ""
        }
        if (!magnetLink.contains("&")){
            return magnetLink.substring(TORRENT_PREFIX.length, magnetLink.length)
        }
        return magnetLink.substring(TORRENT_PREFIX.length, magnetLink.indexOf("&"))

    }

    fun getLocalTorrentPath(hash: String): String {
        return "$TORRENT_DIR$hash.torrent"
    }


    fun isHashHex(str: String?): Boolean {
        str ?: return false
        if (str.length != 32) {
            return false
        }
        str.lowercase().forEach {
            if (!it.isDigit() && !"abcdef".contains(it)) {
                return false
            }
        }
        return true
    }
}