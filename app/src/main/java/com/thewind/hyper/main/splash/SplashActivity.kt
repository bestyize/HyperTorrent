package com.thewind.hyper.main.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thewind.hyper.databinding.ActivitySplashBinding
import com.thewind.hyper.main.ui.MainActivity
import com.thewind.util.ViewUtils
import com.thewind.util.baseUrl
import com.thewind.viewer.h5.H5PageFragment
import com.thewind.widget.actiondialog.ButtonActionType
import com.thewind.widget.actiondialog.ButtonData
import com.thewind.widget.actiondialog.NotifyDialogFragment
import com.thewind.widget.actiondialog.NotifyType
import com.thewind.widget.activity.FullScreenContainerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var startCheckVm: SplashViewModel
    private var notifyDialogFragment: NotifyDialogFragment? = null
    private var needBlockAutoClose = false


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startCheckVm = ViewModelProvider(this)[SplashViewModel::class.java]

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewUtils.enterFullScreenMode(this, true)
        binding.countDown.setOnClickListener {
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        startCheckVm.startupCheckLiveData.observe(this) { resp ->
            needBlockAutoClose = true
            if (resp.code != 0 || resp.data == null) {
                start()
                return@observe
            }
            if (resp.data?.leftButton != null || resp.data?.rightButton != null) {
                notifyDialogFragment = NotifyDialogFragment.newInstance(resp.data!!, actionLeft = { data ->
                    handleNotifyButtonClick(data)
                    if (resp.data?.type == NotifyType.APP_UPDATE) {
                        exitProcess(0)
                    } else {
                        start()
                    }
                }, actionRight = { data ->
                    handleNotifyButtonClick(data)
                    if (resp.data?.type == NotifyType.APP_UPDATE) {
                        exitProcess(0)
                    } else {
                        lifecycleScope.launch {
                            delay(1000)
                            start()
                        }

                    }
                })
                notifyDialogFragment?.showNow(supportFragmentManager, "start_check")
            }
        }
        startCheckVm.checkStartUp()
        lifecycleScope.launch {
            repeat(3) {
                binding.countDown.text = "跳过 ${2 - it}"
                delay(1000)
            }
            if (!needBlockAutoClose) {
                start()
            }
        }


    }

    private fun start() {
        val intent = Intent(this@SplashActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun handleNotifyButtonClick(buttonData: ButtonData) {
        when(buttonData.actionType) {
            ButtonActionType.INNER_H5 -> FullScreenContainerActivity.startWithFragment(this, H5PageFragment.newInstance(buttonData.actionUrl?: baseUrl()))
            ButtonActionType.OUTER_JUMP -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(buttonData.actionUrl)
                startActivity(intent)
            }
        }
        notifyDialogFragment?.dismissAllowingStateLoss()
    }

}