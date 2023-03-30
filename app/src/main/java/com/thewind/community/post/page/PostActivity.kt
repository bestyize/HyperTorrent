package com.thewind.community.post.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityPostBinding
import com.thewind.util.ViewUtils

class PostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewUtils.enterFullScreenMode(this, true)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}