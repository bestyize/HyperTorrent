package com.thewind.download.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thewind.download.page.detail.DownloadDetailFragment
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ActivityDownloadBinding
import com.thewind.util.ViewUtils

class DownloadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.enterFullScreenMode(this, false)
        intent.getStringExtra("stable_task_id")?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DownloadDetailFragment.newInstance(it)).commitNow()
        }


    }
}