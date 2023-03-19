package com.thewind.viewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.ActivityFileViewerBinding
import com.thewind.viewer.json.JsonViewerFragment
import com.thewind.viewer.pdf.PdfViewerFragment

class FileViewerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dispatcher()
    }

    private fun dispatcher() {
        val path = intent.getStringExtra("path") ?: ""
        when(intent.getStringExtra("type")) {
            "json" -> supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, JsonViewerFragment.newInstance(path)).commitNow()
            "pdf" -> supportFragmentManager.beginTransaction().add(R.id.fragment_container, PdfViewerFragment.newInstance(path))
        }
    }
}