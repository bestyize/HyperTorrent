package com.thewind.player.detail

import android.graphics.SurfaceTexture
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentDetailPlayerBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tv.danmaku.ijk.media.player.IjkMediaPlayer

private const val PLAY_URL = "play_url"

class DetailPlayerFragment : Fragment() {
    private lateinit var vm: DetailPlayerViewModel
    private lateinit var binding: FragmentDetailPlayerBinding
    private var playUrl: String = ""
    private var player = IjkMediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            playUrl = it.getString(PLAY_URL) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        vm = ViewModelProvider(this)[DetailPlayerViewModel::class.java]
        vm.playListLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) return@observe
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    player.dataSource = it[0].url
                    player.prepareAsync()
                    player.setOnPreparedListener {
                        player.start()
                    }
                }
            }
        }
        binding.svPlayerContainer.surfaceTextureListener = surfaceCallback
    }

    private var surfaceCallback = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            val sf = Surface(surface)
            player.setSurface(sf)
            if (playUrl.isNotEmpty()) {
                vm.loadPlayList(playUrl)
            }
        }

        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {

        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
            return false
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

        }

    }

    override fun onStop() {
        super.onStop()
        player.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    companion object {
        @JvmStatic
        fun newInstance(playUrl: String) =
            DetailPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(PLAY_URL, playUrl)
                }
            }
    }
}