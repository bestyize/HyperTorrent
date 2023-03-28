package com.thewind.user.bean

/**
 * @author: read
 * @date: 2023/3/29 上午2:24
 * @description:
 */
class UserInfo {
    var uid: Long = -1
    var userName: String? = null
    var selfDesc: String? = null
    var headerUrl: String? = null
    var followCount = 0L
    var fansCount = 0L
}