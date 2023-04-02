package com.thewind.user.bean

import com.google.gson.annotations.SerializedName

/**
 * @author: read
 * @date: 2023/3/28 上午12:04
 * @description:
 */
class User {
    var uid: Long = -1
    var userName: String? = null

    var token: String? = null

    val email: String? = null

    val nickName: String? = null

    val headerUrl: String? = null

    val level // 0: 未激活  1、普通用户 2、高级用户  50、管理员  100、拥有者 -1、禁止发言
            = 0

    val isVip = false

    val vipStartTime: Long = -1

    val vipEndTime: Long = -1

    val registerTime: Long? = null
    val isValid: Boolean
        get() = userName != null && token != null && userName!!.length >= 4 && token!!.length >= 6
}