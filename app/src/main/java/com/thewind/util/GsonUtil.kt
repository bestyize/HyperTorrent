package com.thewind.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder


fun Any.toJson(): String {
    return gson.toJson(this)
}
val gson: Gson = GsonBuilder().disableHtmlEscaping().create()
inline fun <reified T> String.fromJson(): T {
    return gson.fromJson(this, T::class.java)
}

fun String.print() {
    println(this)
}