package com.thewind.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.main.globalApplication

/**
 * @author: read
 * @date: 2023/3/18 下午9:11
 * @description:
 */
object ClipboardUtil {
    private val appContext = globalApplication

    private val clipboardManager =
        appContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    /**
     * 底层是一个集合
     */
    fun addPrimaryClipChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener?) {
        clipboardManager.addPrimaryClipChangedListener(listener)
    }

    /**
     * 底层是一个集合
     */
    fun removePrimaryClipChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener?) {
        clipboardManager.removePrimaryClipChangedListener(listener)
    }

    /**
     * 剪切板是否含有内容
     */
    fun hasPrimaryClip(): Boolean {
        return clipboardManager.hasPrimaryClip()
    }

    /**
     * 复制文字到剪切板
     */
    fun copyClipboardText(content: String) {
        val mClipData = ClipData.newPlainText(appContext.getString(R.string.app_name), content)
        clipboardManager.setPrimaryClip(mClipData)
    }

    fun copyClipboardHtml(content: String, htmlText: String) {
        val mClipData =
            ClipData.newHtmlText(appContext.getString(R.string.app_name), content, htmlText)
        clipboardManager.setPrimaryClip(mClipData)
    }

    fun copyClipboardUri(uri: Uri) {
        val mClipData =
            ClipData.newUri(
                appContext.contentResolver,
                appContext.getString(R.string.app_name),
                uri
            )
        clipboardManager.setPrimaryClip(mClipData)
    }

    fun copyClipboardRawUri(uri: Uri) {
        val mClipData = ClipData.newRawUri(appContext.getString(R.string.app_name), uri)
        clipboardManager.setPrimaryClip(mClipData)
    }

    fun copyClipboardIntent(intent: Intent) {
        val mClipData = ClipData.newIntent(appContext.getString(R.string.app_name), intent)
        clipboardManager.setPrimaryClip(mClipData)
    }

    /**
     * 从剪切板读取文字
     */
    fun readClipboardText(): String {
        if (hasPrimaryClip()) {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).text
                return text.toString()
            }
        }
        return ""
    }

    fun readClipboardHtmlText(): String {
        if (hasPrimaryClip()) {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                return clipData.getItemAt(0).htmlText
            }
        }
        return ""
    }

    fun readClipboardUri(): Uri? {
        if (hasPrimaryClip()) {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                return clipData.getItemAt(0).uri
            }
        }
        return null
    }

    fun readClipboardIntent(): String {
        if (hasPrimaryClip()) {
            val clipData = clipboardManager.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).intent
                return text.toString()
            }
        }
        return ""
    }
}