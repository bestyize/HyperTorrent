package com.thewind.widget.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ComponentActivity
import androidx.fragment.app.Fragment
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityFullScreenContainerBinding
import com.thewind.util.ViewUtils
import java.util.*
import kotlin.collections.HashMap

class FullScreenContainerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenContainerBinding
    private var uniqueId: String? = null
    private var fragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewUtils.enterFullScreenMode(this, true)
        uniqueId = intent.getStringExtra("frag_tag")
        fragment = fragmentPool[uniqueId]
        fragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, it).commitNowAllowingStateLoss()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        uniqueId?.let {
            fragmentPool.remove(it)
        }
    }

    companion object {

        private val fragmentPool: HashMap<String?, Fragment?> = HashMap()

        fun startWithFragment(activity: ComponentActivity?, fragment: Fragment) {
            activity ?: return
            val uniqueId = UUID.randomUUID().toString()
            fragmentPool[uniqueId] = fragment
            val intent = Intent(activity, FullScreenContainerActivity::class.java)
            intent.putExtra("frag_tag", uniqueId)
            activity.startActivity(intent)
            activity.overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_bottom_out)
        }
    }
}