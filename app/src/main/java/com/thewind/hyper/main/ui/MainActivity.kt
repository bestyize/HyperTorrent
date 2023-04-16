package com.thewind.hyper.main.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.thewind.community.index.page.CommunityFragment
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ActivityMainBinding
import com.thewind.local.LocalFileFragment
import com.thewind.user.center.UserCenterFragment
import com.thewind.user.login.AccountHelper
import com.thewind.user.login.LoginActivity
import com.thewind.util.ViewUtils
import com.thewind.util.toast
import com.thewind.widget.actiondialog.ButtonActionType
import com.thewind.widget.actiondialog.ButtonData
import com.thewind.widget.actiondialog.NotifyData
import com.thewind.widget.actiondialog.NotifyDialogFragment
import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.download.config.TORRENT_FILE_DIR
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val localFileFragment by lazy { LocalFileFragment.newInstance() }
    private val userCenterFragment by lazy { UserCenterFragment.newInstance() }
    private val communityFragment by lazy { CommunityFragment.newInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.enterFullScreenMode(this, true)
        supportActionBar?.hide()
        initView()
        checkPermission()
    }


    private fun initView() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, communityFragment).commitNowAllowingStateLoss()
        binding.mainItemRecommend.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, communityFragment)
        }
        binding.mainItemLocal.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, localFileFragment)
        }
        binding.mainItemMy.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, userCenterFragment)
            if (!AccountHelper.isLogin()) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

        }

    }

    private fun handleMainTabChecked(button: CompoundButton, checked: Boolean, fragment: Fragment) {
        button.textSize = if (checked) 20f else 18f
        button.typeface =
            if (checked) Typeface.create(Typeface.DEFAULT, Typeface.BOLD) else Typeface.create(
                Typeface.DEFAULT,
                Typeface.NORMAL
            )

        if (checked) {
            if (!fragment.isAdded) supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment).commitNowAllowingStateLoss()
            supportFragmentManager.beginTransaction().show(fragment).commitNowAllowingStateLoss()
        } else {
            supportFragmentManager.beginTransaction().hide(fragment).commitNowAllowingStateLoss()
        }
    }

    private fun checkPermission() {
        lifecycleScope.launch {
            initDir()
            if (!hasWritePermission()) {
                delay(3000)
                val notifyData = NotifyData().apply {
                    this.title = "温馨提示"
                    this.content = "本软件运行需要读写文件权限，是否同意打开读写文件权限"
                    this.cancelable = true
                    this.leftButton = ButtonData().apply {
                        this.text = "不同意"
                        this.actionType = ButtonActionType.NONE
                    }
                    this.rightButton = ButtonData().apply {
                        this.text = "去打开"
                        this.actionType = ButtonActionType.NONE

                    }
                }
                NotifyDialogFragment.newInstance(notifyData) {
                    if (it.text?.contains("去打开") == true) {
                        requestPermission()
                    }
                }.showNow(supportFragmentManager, "req")
            }

        }
    }


    private fun requestPermission() {
        // 通过api判断手机当前版本号
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:" + application.packageName)
            startActivityForResult(intent, 100)
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                100
            )

        }
    }

    private fun hasWritePermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) Environment.isExternalStorageManager() else {
            return ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) === PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) === PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        initDir()
        if (requestCode == 100) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                toast("存储权限获取失败, 无法正常搜索下载")
            } else {
                initDir()
            }
        }
    }

    private fun initDir() {
        var file = File(TORRENT_FILE_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        file = File(TORRENT_DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
    }
}

private const val TAG = "[main]MainActivity"