package com.thewind.user.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.thewind.user.bean.User
import com.thewind.util.EncryptUtil
import com.thewind.util.fromJson
import com.thewind.util.post
import com.thewind.util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

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
    private const val loginUrl = "https://thewind.xyz/user/api/login"
    private const val registerUrl = "https://thewind.xyz/user/api/register"
    fun login(userName: String, token: String): User {
        try {
            val resp = post(loginUrl, EncryptUtil.encrypt("userName=$userName&token=$token")).fromJson<CommonResponse>()
            if (resp.code != 0) {
                toast(resp.message ?: "发生未知错误,若您忘记密码，请去微信公众号回复：找回账号")
            } else {
                return resp.message?.fromJson<User>() ?: User()
            }
        } catch (_:java.lang.Exception){}

        return User()
    }

    fun register(userName: String, token: String, verifyCode: String): Boolean {
        try {
            if (!isValidUserName(userName) || !isValidToken(token)) {
                toast("用户名或密码格式不符合要求")
                return false
            }
            val resp = post(registerUrl, EncryptUtil.encrypt("userName=$userName&token=$token&verifyCode=$verifyCode")).fromJson<CommonResponse>()
            if (resp.code != 0) {
                toast(resp.message ?: "发生未知错误")
            } else {
                return true
            }
        } catch (_:java.lang.Exception){}
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

interface ApiService {
    @POST("your/endpoint")
    suspend fun postData(@Body data: RequestBody): Response<ResponseBody>
}

class CommonResponse {
    var code: Int = 0
    var message: String? = null

}