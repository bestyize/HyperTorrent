package com.thewind.viewer.h5

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment

import com.thewind.hypertorrent.databinding.H5PageFragmentBinding
import com.thewind.user.login.AccountHelper

/**
 * @author: read
 * @date: 2023/4/10 上午1:01
 * @description:
 */
class H5PageFragment : Fragment() {

    private var url: String? = null
    private lateinit var binding: H5PageFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString("url")?:""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = H5PageFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.wvContainer.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                request?: return false
                val link = request.url?.path
                if (link?.contains(".mp3") == true || link?.contains("flac") == true) {
                    val intent = Intent(Intent.ACTION_VIEW, request.url)
                    view?.context?.startActivity(intent)
                    return true
                }
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                var title: String = view?.title?:""
                if (title.length > 15) {
                    title = ""
                    binding.tvTitle.visibility = View.GONE
                }
                binding.tvTitle.text = title
                binding.ivFavicon.setImageBitmap(favicon)
            }

            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest?
            ): WebResourceResponse? {
                if (request?.url?.host?.contains("thewind") == true) {
                    val user = AccountHelper.loadUserInfo()
                    request.requestHeaders?.set("uid", "${user.uid}")
                    request.requestHeaders?.set("token", "${user.token}")
                }

                return super.shouldInterceptRequest(view, request)
            }



        }

        binding.wvContainer.settings.javaScriptEnabled = true
        binding.wvContainer.settings.setSupportZoom(true)
        binding.wvContainer.settings.loadWithOverviewMode = true
        binding.wvContainer.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.wvContainer.settings.builtInZoomControls = true
        binding.wvContainer.settings.useWideViewPort = true
        binding.wvContainer.settings.allowContentAccess = true
        binding.wvContainer.settings.allowFileAccess = true
        binding.wvContainer.settings.loadsImagesAutomatically = true
        binding.wvContainer.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        url?.let {
            binding.wvContainer.loadUrl(it)
        }
        binding.ivClose.setOnClickListener {
            activity?.onBackPressed()
        }


    }


    companion object {
        fun newInstance(url: String): H5PageFragment {
            return H5PageFragment().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
        }
    }

}