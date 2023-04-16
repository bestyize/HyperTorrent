package com.thewind.picture.search.page

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.thewind.hyper.R
import com.thewind.hyper.databinding.ImageSearchFragmentBinding
import com.thewind.picture.feed.page.PictureFeedFragment
import com.thewind.picture.main.model.ImageRecommendTab
import com.thewind.util.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import xyz.thewind.community.image.model.ImageSrc


/**
 * @author: read
 * @date: 2023/4/16 下午4:16
 * @description:
 */
class ImageSearchFragment : Fragment() {

    private lateinit var binding: ImageSearchFragmentBinding

    private var keyword: String? = null
    private var imageSrc: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageSrc = arguments?.getInt("src")?:ImageSrc.PEXELS.src
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ImageSearchFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivSearch.setOnClickListener {
            keyword = binding.etSearchText.text.toString()
            if (keyword.isNullOrBlank()) {
                toast("关键词不可为空")
                return@setOnClickListener
            }
            childFragmentManager.beginTransaction().replace(R.id.fragment_container, PictureFeedFragment.newInstance(
                ImageRecommendTab().apply {
                    this.src = imageSrc
                    this.title = keyword
                    this.query = keyword
                    this.page = 1
                    this.num = 20
                }
            )).commitAllowingStateLoss()

            lifecycleScope.launch {

                delay(1000)
                val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.showSoftInput(binding.etSearchText, InputMethodManager.SHOW_FORCED)
            }
        }



    }


    companion object {
        fun newInstance(src: Int): ImageSearchFragment = ImageSearchFragment().apply {
            arguments = Bundle().apply {
                putInt("src", src)
            }
        }
    }
}