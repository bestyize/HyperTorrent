package com.thewind.util

import com.google.gson.Gson


fun Any.toJson(): String {
    return Gson().toJson(this)
}
inline fun <reified T> String.fromJson(): T {
    val gson = Gson()
    return gson.fromJson(this, T::class.java)
}

fun String.print() {
    println(this)
}