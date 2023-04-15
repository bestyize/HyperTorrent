package com.thewind.util

import android.os.Build
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * @author: read
 * @date: 2023/4/15 下午8:39
 * @description:
 */


fun String?.urlEncode(): String {
    this ?: return ""
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        URLEncoder.encode(this, StandardCharsets.UTF_8)
    } else URLEncoder.encode(this)
}