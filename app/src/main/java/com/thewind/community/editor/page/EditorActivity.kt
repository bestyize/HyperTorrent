package com.thewind.community.editor.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ActivityEditorBinding
import com.thewind.util.ViewUtils

class EditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditorBinding.inflate(layoutInflater)
        ViewUtils.enterFullScreenMode(this, true)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, PosterEditorFragment.newInstance()).commitNowAllowingStateLoss()
    }
}