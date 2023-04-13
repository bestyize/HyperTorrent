package com.thewind.viewer.pdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thewind.hyper.databinding.FragmentPdfViewerBinding
import com.thewind.util.postfix
import java.io.File

class PdfViewerFragment private constructor(private val path: String): Fragment() {

    private lateinit var binding: FragmentPdfViewerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPdfViewerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = File(path).name
        binding.pdfView.fromFile(path).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(path: String) =
            PdfViewerFragment(path)
    }
}