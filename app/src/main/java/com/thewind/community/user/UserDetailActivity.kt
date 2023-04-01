package com.thewind.community.user

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityUserDetailBinding
import com.thewind.user.center.UserCenterFragment

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val uid = intent.getLongExtra("uid", -1)
        supportFragmentManager.beginTransaction().add(
            R.id.fragment_container,
            UserCenterFragment.newInstance(uid)
        ).commitNowAllowingStateLoss()
    }
}