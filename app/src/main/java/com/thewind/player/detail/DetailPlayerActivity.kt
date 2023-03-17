package com.thewind.player.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityDetailPlayerBinding
import com.thewind.util.ViewUtils

class DetailPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.enterFullScreenMode(this, false)
        val playUrl = intent.getStringExtra("play_url") ?: ""
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailPlayerFragment.newInstance(playUrl))
            .commitNowAllowingStateLoss()
    }
}