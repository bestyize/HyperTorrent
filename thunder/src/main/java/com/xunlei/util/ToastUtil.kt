package com.xunlei.util

import android.widget.Toast
import com.xunlei.downloadlib.XLDownloadManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * @author: read
 * @date: 2023/2/19 上午5:09
 * @description:
 */


fun toast(msg: String) {
    MainScope().launch {
        Toast.makeText(XLDownloadManager.getInstance().context, msg, Toast.LENGTH_SHORT).show()
    }

}