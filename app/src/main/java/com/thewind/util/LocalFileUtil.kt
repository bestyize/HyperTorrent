package com.thewind.util

import android.content.Intent
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.thewind.player.detail.DetailPlayerActivity
import com.thewind.viewer.FileViewerActivity
import java.io.File

/**
 * @author: read
 * @date: 2023/3/25 下午4:07
 * @description:
 */
object LocalFileUtil {
    fun openFile(activity: FragmentActivity?, file: File) {
        activity ?: return
        when {
            file.isVideo() -> {
                val intent = Intent(activity, DetailPlayerActivity::class.java)
                intent.putExtra("play_url", file.absolutePath)
                activity.startActivity(intent)
            }
            file.isJson() -> {
                val intent = Intent(activity, FileViewerActivity::class.java)
                intent.putExtra("type", "json")
                intent.putExtra("path", file.absolutePath)
                activity.startActivity(intent)
            }
            file.isPdf() -> {
                val intent = Intent(activity, FileViewerActivity::class.java)
                intent.putExtra("type", "pdf")
                intent.putExtra("path", file.absolutePath)
                activity.startActivity(intent)
            }

            file.isTextFile() -> {
                val intent = Intent(activity, FileViewerActivity::class.java)
                intent.putExtra("type", "code")
                intent.putExtra("path", file.absolutePath)
                activity.startActivity(intent)
            }
            file.isPicture() -> {
                val intent = Intent(activity, FileViewerActivity::class.java)
                intent.putExtra("type", "image")
                intent.putExtra("path", file.absolutePath)
                activity.startActivity(intent)
            }
            else -> {
                Intent(
                    Intent.ACTION_VIEW,
                    FileProvider.getUriForFile(
                        activity,
                        "com.thewind.hyper.provider",
                        file
                    )
                ).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    activity.startActivity(this)
                }
            }
        }
    }
}