package com.xunlei.tool.editor


import com.dampcake.bencode.Bencode
import com.dampcake.bencode.BencodeInputStream
import com.dampcake.bencode.BencodeOutputStream
import com.dampcake.bencode.Type
import com.xunlei.download.provider.TorrentTaskHelper
import org.apache.commons.io.FileUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets


//https://cloud.tencent.com/developer/article/1751200
object TorrentEditor {

    fun parseTorrentFileWithThunder(
        path: String,
        selectedIndexList: List<Int>? = null
    ): TorrentSimpleInfo {
        val simpleInfo = parseTorrent(path)
        simpleInfo.filesList = mutableListOf()
        val torrentInfo = TorrentTaskHelper.instance.getTorrentInfo(path)
        simpleInfo.hash = torrentInfo.mInfoHash
        torrentInfo.mSubFileInfo?.forEach {
            simpleInfo.filesList.add(TorrentFileSimpleInfo().apply {
                this.name = it.mFileName
                this.index = it.mFileIndex
                this.size = it.mFileSize
                this.subPath = it.mSubPath
                this.isChecked =
                    selectedIndexList == null || selectedIndexList.contains(it.mFileIndex)

            })
        }
        return simpleInfo
    }

    fun parseTorrent(torrentPath: String): TorrentSimpleInfo {
        val torrentInfo = TorrentSimpleInfo()
        try {
            val stream = ByteArrayInputStream(FileUtils.readFileToByteArray(File(torrentPath)))
            val bencode = BencodeInputStream(stream)
            val dict: Map<String, Any> = bencode.readDictionary()
            if (dict.containsKey("creation time")) {
                torrentInfo.createDate = (dict["creation time"] ?: "0").toString().toLong()
            }
            if (dict.containsKey("magnet")) {
                torrentInfo.torrentUrl = (dict["magnet"] ?: "").toString()
            }
            if (!dict.containsKey("info")) {
                return torrentInfo
            }
            val obj = dict["info"]
            if (obj is Map<*, *>) {
                val info = obj as Map<String, Any>
                torrentInfo.torrentTitle = (info["name"] ?: "").toString()
                torrentInfo.size = (info["length"] ?: "0").toString().toLong()
                if (info.containsKey("files")) {
                    var fileIndex = 0
                    val files = info["files"] as List<Map<String, Any>>?
                    for (item in files!!) {
                        val file = TorrentFileSimpleInfo()
                        file.size = (item["length"] ?: "0").toString().toLong()
                        val paths = item["path"] as List<String>?
                        if (!paths!!.isEmpty()) {
                            file.index = fileIndex++
                            file.name = paths[paths.size - 1]
                        }
                        torrentInfo.filesList.add(file)
                    }
                } else {
                    val torrentFile = TorrentFileSimpleInfo()
                    torrentFile.name = torrentInfo.torrentTitle
                    torrentFile.size = torrentInfo.size
                    torrentInfo.filesList.add(torrentFile)
                }
            }
        } catch (ignored: IOException) {
        }
        return torrentInfo
    }

    private fun reverseByteBuf(bf: ByteBuffer?): ByteBuffer? {
        if (bf == null) return null
        val bArr = bf.array()
        val str = String(bArr)
        val lastDotPos = str.lastIndexOf(".")
        return if (lastDotPos <= 0 || lastDotPos == str.length - 1) {
            val sb = StringBuilder(str)
            ByteBuffer.wrap(sb.reverse().toString().toByteArray(StandardCharsets.UTF_8))
        } else {
            val nameField = str.substring(0, lastDotPos)
            val postFix = str.substring(lastDotPos)
            ByteBuffer.wrap(
                (StringBuilder(nameField).reverse().toString() + postFix).toByteArray(
                    StandardCharsets.UTF_8
                )
            )
        }
    }

    fun washTorrentFile(torrentPath: String): Boolean {
        return try {
            val bencode = Bencode(true)
            val dict = bencode.decode<Map<String?, Any?>>(
                FileUtils.readFileToByteArray(File(torrentPath)), Type.DICTIONARY
            )
            val obj = dict["info"] as? Map<*, *> ?: return false
            val info = obj as Map<String, Any>
            val name = info["name"] as ByteBuffer?
            // info.put("name", reverseByteBuf(name));
            if (info.containsKey("files")) {
                val files = info["files"] as List<Map<String, Any>>?
                for (item in files!!) {
                    val paths = item["path"] as MutableList<ByteBuffer?>?
                    if (!paths!!.isEmpty()) {
                        val lastPos = paths.size - 1
                        val fileName = paths[lastPos]
                        paths.removeAt(lastPos)
                        paths.add(reverseByteBuf(fileName))
                    }
                }
            }
            saveToFile(dict, torrentPath)
        } catch (ignored: IOException) {
            false
        }
    }

    private fun saveToFile(data: Map<String?, Any?>?, filePath: String): Boolean {
        try {
            val out = ByteArrayOutputStream()
            val encoder = BencodeOutputStream(out)
            encoder.writeDictionary(data)
            FileUtils.writeByteArrayToFile(File("$filePath.bak.torrent"), out.toByteArray())
        } catch (e: IOException) {
            return false
        }
        return true
    }


}

class TorrentSimpleInfo {
    var hash: String = ""
    var torrentUrl: String? = null
    var torrentTitle: String = ""
    var createDate: Long = 0L
    var size: Long = 0L
    var filesList: MutableList<TorrentFileSimpleInfo> = mutableListOf()
}

class TorrentFileSimpleInfo {
    var name: String? = ""
    var size: Long = 0L
    var index: Int = 0
    var subPath: String = ""
    var isChecked = true
}