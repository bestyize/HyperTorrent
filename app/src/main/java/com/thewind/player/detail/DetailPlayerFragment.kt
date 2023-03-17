package com.thewind.player.detail

import android.content.pm.ActivityInfo
import android.graphics.SurfaceTexture
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentDetailPlayerBinding
import com.thewind.util.toTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tv.danmaku.ijk.media.player.IjkMediaPlayer

private const val PLAY_URL = "play_url"

class DetailPlayerFragment : Fragment() {
    private lateinit var vm: DetailPlayerViewModel
    private lateinit var binding: FragmentDetailPlayerBinding
    private var playUrl: String = ""
    private var player = IjkMediaPlayer()

    private var showControlPanel = true

    private var notAllowControlPanelClick = false

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
                        adjustPlayArea(it.videoWidth, it.videoHeight)
                        setSeekBar(it.duration)
                        player.start()
                        monitorPlayState()
                    }
                    player.setOnCompletionListener {
                        player.start()
                    }
                }
            }
        }
        binding.svPlayerContainer.surfaceTextureListener = surfaceCallback

        binding.svPlayerContainer.setOnClickListener {
            if (notAllowControlPanelClick) {
                val isPlayLockVisible = !(binding.playLockSwitch.visibility == View.VISIBLE)
                binding.playLockSwitch.visibility = if (isPlayLockVisible) View.VISIBLE else View.GONE
                showControlPanel = false
                binding.controlPanel.visibility = View.GONE
                return@setOnClickListener
            }
            showControlPanel = !showControlPanel
            binding.controlPanel.visibility = if (showControlPanel) View.VISIBLE else View.GONE
            binding.playLockSwitch.visibility = if (showControlPanel) View.VISIBLE else View.GONE
        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.volumeSwitch.setOnCheckedChangeListener{ view, checked ->
            val volume = if (checked) 1f else 0f
            player.setVolume(volume, volume)
        }
        binding.playSwitch.setOnCheckedChangeListener{ view, checked ->
            if (checked) player.start() else player.pause()
        }

        binding.playLockSwitch.setOnCheckedChangeListener { view, checked ->
            notAllowControlPanelClick = !notAllowControlPanelClick
            if(notAllowControlPanelClick) {
                showControlPanel = false
                binding.controlPanel.visibility = View.GONE
            } else {
                showControlPanel = true
                binding.controlPanel.visibility = View.VISIBLE
            }
        }
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

    private fun adjustPlayArea(videoWidth: Int, videoHeight: Int) {
        activity?.requestedOrientation = if (videoWidth > videoHeight) ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

        val lp = binding.svPlayerContainer.layoutParams
        lp.width = if (videoWidth <= videoHeight) binding.root.width else (binding.root.height * (videoWidth.toDouble() / videoHeight)).toInt()
        lp.height = if (videoWidth <= videoHeight) (binding.root.width * (videoHeight.toDouble() / videoWidth)).toInt() else binding.root.height
        binding.svPlayerContainer.layoutParams = lp
    }

    private fun setSeekBar(totalTime: Long) {
        binding.seekBar.max = (totalTime/1000).toInt()
        binding.tvTotalTime.text = totalTime.toTime()
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player.seekTo(progress * 1000L)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun monitorPlayState() {
        lifecycleScope.launch {
            while (true) {
                if (player.isPlaying) {
                    val position = player.currentPosition
                    binding.seekBar.progress = (position/1000).toInt()
                    binding.tvPlayedTime.text = position.toTime()
                }
                delay(1000)
            }
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