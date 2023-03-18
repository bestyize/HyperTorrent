package com.thewind.util

/**
 * @author: read
 * @date: 2023/3/18 上午2:08
 * @description:
 */

fun Long.toTime(): String {
    val seconds = this / 1000
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return "%02d:%02d:%02d".format(hour, minute, second)
}

