package com.thewind.user.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.thewind.hyper.databinding.ActivityLoginBinding
import com.thewind.util.ViewUtils
import com.thewind.util.toast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var vm: LoginViewModel
    private var isPageInLoginMode = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        vm = ViewModelProvider(this)[LoginViewModel::class.java]
        ViewUtils.enterFullScreenMode(this, true)
        initView()
    }

    private fun initView() {
        binding.registerArea.visibility = if (isPageInLoginMode) View.GONE else View.VISIBLE

        binding.tvSwitchRegister.setOnClickListener {
            isPageInLoginMode = !isPageInLoginMode
            if (isPageInLoginMode) {
                binding.registerArea.visibility = View.GONE
                binding.tvLogin.text = "登录"
                binding.btnLogin.text = "登录"
                binding.tvSwitchRegister.text = "注册账号"
            } else {
                binding.registerArea.visibility = View.VISIBLE
                binding.tvLogin.text = "注册"
                binding.btnLogin.text = "注册"
                binding.tvSwitchRegister.text = "登录"
            }
        }

        binding.btnLogin.setOnClickListener {
            val userName = binding.etUserName.text.toString()
            val token = binding.etToken.text.toString()
            if (isPageInLoginMode) {
                vm.login(userName, token)
                return@setOnClickListener
            }
            val verifyCode = binding.etVerifyCode.text.toString()
            vm.register(userName, token, verifyCode)
        }

        binding.ivClose.setOnClickListener {
            finish()
        }

        vm.userLiveData.observe(this) {
            if (it.isValid) {
                toast("登录成功")
                AccountHelper.saveUserInfo(it)
                finish()
            }
        }

        vm.registerStatus.observe(this) {
            val userName = binding.etUserName.text.toString()
            val token = binding.etToken.text.toString()
            if (it) vm.login(userName, token)
        }

    }
}