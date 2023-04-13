package com.thewind.user.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.hyper.BuildConfig
import com.thewind.user.bean.User
import com.thewind.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder

/**
 * @author: read
 * @date: 2023/3/27 下午11:58
 * @description:
 */
class LoginViewModel : ViewModel() {

    val userLiveData: MutableLiveData<User> = MutableLiveData()

    val registerStatus: MutableLiveData<Boolean> = MutableLiveData()

    fun login(userName: String, token: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val r = LoginService.login(userName, token)
                r
            }.let {
                userLiveData.value = it
            }
        }
    }

    fun register(userName: String, token: String, verifyCode: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                LoginService.register(userName, token, verifyCode)
            }.let {
                registerStatus.value = it
            }
        }
    }

}


object LoginService {
    private var TAG = "LoginService"
    private var loginUrl = "${baseUrl()}/user/api/login"
    private var registerUrl = "${baseUrl()}/user/api/register"
    fun login(userName: String, token: String): User {
        try {
            val resp = post(
                loginUrl,
                EncryptUtil.encrypt("userName=$userName&token=$token")
            ).fromJson<CommonResponse>()
            if (resp.code != 0) {
                toast(resp.message ?: "发生未知错误,若您忘记密码，请去微信公众号回复：找回账号")
            } else {
                return resp.message?.fromJson<User>() ?: User()
            }
        } catch (e: java.lang.Exception) {
            Log.i(TAG, "login failed, e = ${e.message}")
        }

        return User()
    }

    fun register(userName: String, token: String, verifyCode: String): Boolean {
        try {
            if (!isValidUserName(userName) || !isValidToken(token)) {
                toast("用户名或密码格式不符合要求")
                return false
            }
            val resp = get(
                "$registerUrl?userName=${URLEncoder.encode(userName)}&token=$token&verifyCode=$verifyCode",
                hashMapOf("Content-Type" to "x-www-form-urlencoded")
            ).fromJson<CommonResponse>()
            if (resp.code != 0) {
                toast(resp.message ?: "发生未知错误")
                return false
            } else {
                return true
            }
        } catch (e: java.lang.Exception) {
            if (BuildConfig.DEBUG) {
                toast("register error, e = $e")
            }
        }
        toast("注册失败！")
        return false
    }

    private fun isValidUserName(userName: String): Boolean {
        val regex = "^[\\w\\u4e00-\\u9fa5]{4,20}$".toRegex()
        return userName.matches(regex)
    }

    private fun isValidToken(token: String): Boolean {
        val regex = "^(?=.*[A-Za-z\\d@_])[A-Za-z\\d@_]{6,50}$".toRegex()
        return token.matches(regex)

    }


}

class CommonResponse {
    var code: Int = 0
    var message: String? = null

}