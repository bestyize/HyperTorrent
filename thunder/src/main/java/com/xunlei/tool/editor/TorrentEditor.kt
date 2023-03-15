package com.xunlei.tool.editor

import com.xunlei.tool.bencode.Bencode
import com.xunlei.tool.bencode.Type.DICTIONARY
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

//https://cloud.tencent.com/developer/article/1751200
object TorrentEditor {

    private const val NOTICE = "[洗白by]thewind.xyz-"

    private val byteEditor by lazy {
        Bencode(StandardCharsets.UTF_8, true)
    }

    private val textEditor by lazy {
        Bencode()
    }

    fun parseTorrentFile(filePath: String): TorrentSimpleInfo {
        val map = decodeTorrentFileToMap(filePath, true)
        val torrentSimpleInfo = TorrentSimpleInfo()
        torrentSimpleInfo.createDate = (map[TorrentFileKeyDefine.CREATE_DATE.key] as? Long) ?: 0L
        map[TorrentFileKeyDefine.INFO.key]?.let {info ->
            val infoMap = (info as? HashMap<String, Any>) ?: java.util.HashMap<String, Any>()
            val nameStr = infoMap[TorrentFileKeyDefine.NAME.key] as? String ?: ""
            torrentSimpleInfo.torrentTitle = nameStr

            val files = infoMap[TorrentFileKeyDefine.FILES.key] as? ArrayList<HashMap<String, Any>>
                ?: ArrayList()
            files.forEachIndexed { index, torrentFileInfo ->
                val wsList = (torrentFileInfo[TorrentFileKeyDefine.PATH.key] as? ArrayList<String>) ?: ArrayList()
                val len = (torrentFileInfo[TorrentFileKeyDefine.LENGTH.key] as? Long) ?: 0L
                TorrentFileSimpleInfo().apply {
                    this.name = if (wsList.size > 0) wsList[0] else ""
                    this.size = len
                    this.index = index
                }.let {
                    if (!it.name.isNullOrBlank() && it.name?.contains("_padding_file") != true) {
                        torrentSimpleInfo.filesList.add(it)
                    }
                }



            }
        }
        return torrentSimpleInfo
    }


    fun decodeTorrentFileToMap(path: String, useTextEditor:Boolean = false): MutableMap<String, Any?> {
        val map = mutableMapOf<String, Any?>()
        val file = File(path)
        if (!file.exists()) {
            return map
        }
        val bArr = FileUtils.readFileToByteArray(file)
        return if (useTextEditor) textEditor.decode(bArr, DICTIONARY) else byteEditor.decode(bArr, DICTIONARY)
    }

    fun saveTorrentFile(map: MutableMap<String, Any?>, fullPath: String) {
        val file = File(fullPath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        FileUtils.writeByteArrayToFile(file, byteEditor.encode(map))
    }

    fun washTorrent(map: MutableMap<String, Any>): MutableMap<String, Any> {
        val createBArr = map[TorrentFileKeyDefine.CREATED_BY.key] as ByteArray?
        createBArr?.let {
            map[TorrentFileKeyDefine.CREATED_BY.key] = "Hyper磁力编辑工具 - By [thewind.xyz]"
        }

        val mm = map[TorrentFileKeyDefine.INFO.key]?.let { info ->
            val infoMap = (info as? HashMap<String, Any>) ?: java.util.HashMap<String, Any>()
            val nameBArr = infoMap[TorrentFileKeyDefine.NAME.key] as? ByteArray ?: ByteArray(0)
            val nameStr = NOTICE + String(nameBArr).reversed()
            infoMap[TorrentFileKeyDefine.NAME.key] = ByteBuffer.wrap(nameStr.encodeToByteArray())

            val files = infoMap[TorrentFileKeyDefine.FILES.key] as? ArrayList<HashMap<String, Any>>
                ?: ArrayList<HashMap<String, Any>>()
            files.forEach { torrentFileInfo ->
                val wsList = (torrentFileInfo[TorrentFileKeyDefine.PATH.key] as? ArrayList<ByteArray>)?.map {
                    washName(it)
                } ?: ArrayList<ByteBuffer>()
                torrentFileInfo[TorrentFileKeyDefine.PATH.key] = wsList

                val wpList = (torrentFileInfo[TorrentFileKeyDefine.PATH_UTF8.key] as? ArrayList<ByteArray>)?.map {
                    washName(it)
                } ?: ArrayList<ByteArray>()
                torrentFileInfo[TorrentFileKeyDefine.PATH_UTF8.key] = wpList

            }
            infoMap
        } ?: ""
        map[TorrentFileKeyDefine.INFO.key] = mm
        return map
    }

    private fun washName(byteArray: ByteArray): ByteArray {
        val originName = String(byteArray)
        if (!originName.contains(".")) {
            return (NOTICE + String(byteArray).reversed()).toByteArray()
        }
        val prefixRange = IntRange(0, originName.lastIndexOf(".") - 1)
        val postfixRange = IntRange(originName.lastIndexOf("."), originName.length - 1)
        val nameStr = NOTICE + originName.substring(prefixRange).reversed() + originName.substring(postfixRange)
        return nameStr.encodeToByteArray()
    }

}

class TorrentSimpleInfo {
    var torrentTitle: String?= ""
    var createDate: Long = 0L
    var filesList: MutableList<TorrentFileSimpleInfo> = mutableListOf()
}

class TorrentFileSimpleInfo {
    var name: String ?= ""
    var size: Long = 0L
    var index: Int = 0
    var isChecked = true
}

enum class TorrentFileKeyDefine(val key: String) {
    CREATE_DATE("creation date"),
    COMMENT("comment"),
    ANNOUNCE_LIST("announce-list"),
    CREATED_BY("created by"),
    ANNOUNCE("announce"),
    INFO("info"),
    PIECES("pieces"),
    NAME("name"),
    FILES("files"),
    PATH("path"),
    PATH_UTF8("path.utf-8"),
    LENGTH("length"),
    PIECE_LENGTH("piece length")
}