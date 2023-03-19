package com.thewind.download.page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentDownloadBinding

class DownloadFragment : Fragment() {

    private lateinit var binding: FragmentDownloadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DownloadFragment()
    }
}