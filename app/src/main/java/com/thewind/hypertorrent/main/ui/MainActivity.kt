package com.thewind.hypertorrent.main.ui

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
import com.thewind.download.page.DownloadFragment
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityMainBinding
import com.thewind.local.LocalFileFragment
import com.thewind.torrent.search.recommend.TorrentSearchRecommendFragment
import com.thewind.user.center.UserCenterFragment
import com.thewind.util.ViewUtils
import com.thewind.util.toast
import com.xunlei.download.config.TORRENT_DIR
import com.xunlei.download.config.TORRENT_FILE_DIR
import java.io.File


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val torrentSearchFragment by lazy { TorrentSearchRecommendFragment.newInstance() }
    private val localFileFragment by lazy { LocalFileFragment.newInstance() }
    private val downloadFragment by lazy { DownloadFragment.newInstance() }
    private val userCenterFragment by lazy { UserCenterFragment.newInstance(12345) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
        ViewUtils.enterFullScreenMode(this, true)
        supportActionBar?.hide()
        initView()
    }


    private fun initView() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, torrentSearchFragment).commitNowAllowingStateLoss()
        binding.mainItemMain.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, torrentSearchFragment)
        }
        binding.mainItemLocal.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, localFileFragment)
        }
        binding.mainItemMy.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, userCenterFragment)
        }
        binding.mainItemDown.setOnCheckedChangeListener { buttonView, isChecked ->
            handleMainTabChecked(buttonView, isChecked, downloadFragment)
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


    private fun requestPermission() {
        initDir()
        // 通过api判断手机当前版本号
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 安卓11，判断有没有“所有文件访问权限”权限
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:" + application.packageName)
                startActivityForResult(intent, 100)
            }
        } else {
            // 安卓6 判断有没有读写权限权限
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) === PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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