package com.thewind.hyper.config.router

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.facade.service.SerializationService
import com.thewind.util.gson
import com.xunlei.util.toJson
import java.lang.reflect.Type

/**
 * @author: read
 * @date: 2023/4/14 上午3:09
 * @description:
 */
@Route(path = "/yourservicegroupname/json")
open class JsonServiceImpl : SerializationService {
    override fun <T> json2Object(input: String?, clazz: Class<T>): T {
        return gson.fromJson(input, clazz)
    }

    override fun object2Json(instance: Any?): String {
        return instance.toJson()
    }

    override fun <T> parseObject(input: String?, clazz: Type): T {
        return gson.fromJson(input, clazz)
    }

    override fun init(context: Context?) {}
}