package com.thewind.user.login

import com.tencent.mmkv.MMKV
import com.thewind.user.bean.User
import com.thewind.util.fromJson
import com.thewind.util.toJson
import com.thewind.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

/**
 * @author: read
 * @date: 2023/3/28 上午1:08
 * @description:
 */
object AccountHelper {

    private const val USER_INFO = "user_info"

    private val changeListeners: MutableList<WeakReference<LoginStateChangeListener?>> = mutableListOf()

    fun registerChangeListener(listener: LoginStateChangeListener) {
        changeListeners.add(WeakReference(listener))
    }


    fun updateUserInfo() {
        val user = loadUserInfo()
        if (user.isValid) {
            MainScope().launch {
                withContext(Dispatchers.IO) {
                    LoginService.login(user.userName?:"", user.token?:"")
                }.let {
                    if (it.isValid) {
                        saveUserInfo(it)
                    }
                    if (!it.isValid) {
                        toast("登录状态失效，请重新登录")
                    }
                }
            }
        }
    }

    fun saveUserInfo(user: User) {
        MMKV.defaultMMKV().encode(USER_INFO, user.toJson())
        changeListeners.forEach {
            it.get()?.onLoginStateChange(user)
        }
    }

    fun loadUserInfo(): User {
        try {
            MMKV.defaultMMKV().decodeString(USER_INFO)?.let {
                return it.fromJson()
            }
        } catch (_: java.lang.Exception) {

        }
        return User()
    }

    fun isLogin(): Boolean {
        return loadUserInfo().isValid
    }

    fun logout() {
        MMKV.defaultMMKV().remove(USER_INFO)
        changeListeners.forEach { it.get()?.onLoginStateChange(User()) }
    }

}


interface LoginStateChangeListener {
    fun onLoginStateChange(user: User)
}