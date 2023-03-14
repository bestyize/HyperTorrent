package com.xunlei.util

import com.google.gson.GsonBuilder

/**
 * @author: read
 * @date: 2023/3/15 上午1:01
 * @description:
 */

private val gson = GsonBuilder().disableHtmlEscaping().create()

fun Any?.toJson(): String {
    this ?: return ""
    return gson.toJson(this)
}