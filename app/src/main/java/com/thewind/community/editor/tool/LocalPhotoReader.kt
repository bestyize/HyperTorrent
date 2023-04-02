package com.thewind.community.editor.tool

import android.provider.MediaStore
import com.thewind.community.editor.model.ImagePickerItem
import com.thewind.hypertorrent.main.globalApplication

/**
 * @author: read
 * @date: 2023/4/2 下午1:40
 * @description:
 */
object LocalPhotoReader {

    fun loadLocalPhotoList() : MutableList<ImagePickerItem> {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATA
        )

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"
        val cursor = globalApplication.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )

        val photoList = mutableListOf<ImagePickerItem>()

        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val date = it.getLong(dateColumn)
                val data = it.getString(dataColumn)

                photoList.add(ImagePickerItem().apply { path = data })
            }
        }
        return photoList

    }

}