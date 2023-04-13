package com.thewind.viewer.code

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.thewind.hyper.R
import com.thewind.hyper.databinding.FragmentCodeViewerBinding
import com.thewind.util.postfix
import io.github.kbiakov.codeview.adapters.Options
import io.github.kbiakov.codeview.highlight.ColorTheme
import java.io.File

class CodeViewerFragment private constructor(private val path: String): Fragment() {

    private lateinit var binding: FragmentCodeViewerBinding
    private lateinit var vm: CodeViewerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[CodeViewerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCodeViewerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.title = File(path).name
        vm.codeLiveData.observe(viewLifecycleOwner) {
            binding.cv.setOptions(Options.get(view.context).withCode(it).withLanguage(path.postfix()).disableHighlightAnimation().withTheme(ColorTheme.SOLARIZED_LIGHT).withoutShadows())
        }
        vm.loadCode(path)
    }

    companion object {

        @JvmStatic
        fun newInstance(path: String) = CodeViewerFragment(path)
    }
}