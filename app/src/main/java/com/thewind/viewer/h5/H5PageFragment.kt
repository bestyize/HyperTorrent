package com.thewind.viewer.h5

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

import com.thewind.hyper.databinding.H5PageFragmentBinding
import com.thewind.user.login.AccountHelper

/**
 * @author: read
 * @date: 2023/4/10 上午1:01
 * @description:
 */
class H5PageFragment : Fragment() {

    private var url: String? = null
    private lateinit var binding: H5PageFragmentBinding
    private var mFilePathCallback: ValueCallback<Array<Uri>>? = null
    private val callback: ActivityResultCallback<Uri?>  =
        ActivityResultCallback<Uri?> { result ->
            result?.let {
                mFilePathCallback?.onReceiveValue(arrayOf(it))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments?.getString("url")?:""
        registerForActivityResult(ActivityResultContracts.GetContent(), callback)
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
                if (isDownloadedResource((link?:"").lowercase())) {
                    val intent = Intent(Intent.ACTION_VIEW, request.url)
                    view?.context?.startActivity(intent)
                    return true
                }
                return false
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


        binding.wvContainer.webChromeClient = object : WebChromeClient(){
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                // Create an intent to open the file picker
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"

                mFilePathCallback = filePathCallback
                startActivity(intent)
                return true
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                var webTitle: String = view?.title?:""
                if (webTitle.length > 15) {
                    webTitle = ""
                    binding.tvTitle.visibility = View.GONE
                }
                binding.tvTitle.text = webTitle
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                super.onReceivedIcon(view, icon)
                binding.ivFavicon.setImageBitmap(icon)
            }
        }

        binding.wvContainer.settings.javaScriptEnabled = true
        binding.wvContainer.settings.setSupportZoom(false)
        binding.wvContainer.settings.loadWithOverviewMode = true
        binding.wvContainer.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.wvContainer.settings.builtInZoomControls = true
        binding.wvContainer.settings.useWideViewPort = true
        binding.wvContainer.settings.allowContentAccess = true
        binding.wvContainer.settings.allowFileAccess = true
        binding.wvContainer.settings.loadsImagesAutomatically = true
        binding.wvContainer.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        binding.wvContainer.settings.allowFileAccessFromFileURLs = true
        binding.wvContainer.settings.blockNetworkImage = false
        binding.wvContainer.settings.blockNetworkLoads = false
        binding.wvContainer.settings.domStorageEnabled = true
        binding.wvContainer.settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; SM-G981B) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.162 Mobile Safari/537.36 Edg/112.0.0.0"

        url?.let {
            binding.wvContainer.loadUrl(it)
        }
        binding.ivClose.setOnClickListener {
            activity?.onBackPressed()
        }

        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if (binding.wvContainer.canGoBack()) {
                    binding.wvContainer.goBack()
                } else {
                    isEnabled = false
                    activity?.onBackPressedDispatcher?.onBackPressed()
                }
            }
        })


    }

    private fun isDownloadedResource(link: String): Boolean {
        return link.contains(".mp3") || link.contains(".mp4") || link.contains(".flac") || link.contains(".png") || link.contains("jpeg") || link.contains("webp")
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