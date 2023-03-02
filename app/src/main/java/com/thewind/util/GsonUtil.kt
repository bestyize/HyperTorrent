package com.thewind.util

import com.google.gson.Gson


fun Any.toJson() :String {
    return Gson().toJson(this)
}

fun String.print() {
    println(this)
}