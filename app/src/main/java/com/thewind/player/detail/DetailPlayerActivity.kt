package com.thewind.player.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ActivityDetailPlayerBinding
import com.thewind.util.ViewUtils

class DetailPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.enterImmersiveFullScreenMode(this)
        val playUrl = intent.getStringExtra("play_url") ?: ""
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DetailPlayerFragment.newInstance(playUrl))
            .commitNowAllowingStateLoss()
    }
}