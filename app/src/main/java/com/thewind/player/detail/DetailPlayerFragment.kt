package com.thewind.player.detail

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.*
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.thewind.hypertorrent.databinding.FragmentDetailPlayerBinding
import com.thewind.util.toTime
import com.thewind.util.toast
import com.xunlei.download.config.BASE_DIR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

private const val PLAY_URL = "play_url"

class DetailPlayerFragment : Fragment() {
    private lateinit var vm: DetailPlayerViewModel
    private lateinit var binding: FragmentDetailPlayerBinding
    private var playUrl: String = ""
    private lateinit var player: ExoPlayer

    private var showControlPanel = true

    private var notAllowControlPanelClick = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        player = ExoPlayer.Builder(requireContext()).build()
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
                player.setMediaItem(MediaItem.fromUri(it[0].url))
                player.addListener(object : Listener {

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        if (playbackState == Player.STATE_READY) {
                            adjustPlayArea(player.videoSize.width, player.videoSize.height)
                            setSeekBar(player.duration)
                            monitorPlayState()
                        }
                    }


                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        if (!isPlaying && player.currentPosition >= player.duration) {
                            player.play()
                        }
                    }
                })
                player.playWhenReady = true
                player.prepare()
                player.play()
            }
        }
        binding.svPlayerContainer.surfaceTextureListener = surfaceCallback

        binding.svPlayerContainer.setOnClickListener {
            if (notAllowControlPanelClick) {
                val isPlayLockVisible = binding.playLockSwitch.visibility != View.VISIBLE
                binding.playLockSwitch.visibility =
                    if (isPlayLockVisible) View.VISIBLE else View.GONE
                showControlPanel = false
                binding.controlPanel.visibility = View.GONE
                return@setOnClickListener
            }
            showControlPanel = !showControlPanel
            binding.controlPanel.visibility = if (showControlPanel) View.VISIBLE else View.GONE
            binding.playLockSwitch.visibility = if (showControlPanel) View.VISIBLE else View.GONE
        }

        binding.ivBack.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        binding.volumeSwitch.setOnCheckedChangeListener { _, checked ->
            val volume = if (checked) 1f else 0f
            player.volume = volume
        }
        binding.playSwitch.setOnCheckedChangeListener { _, checked ->
            if (checked) player.play() else player.pause()
        }

        binding.playLockSwitch.setOnCheckedChangeListener { _, _ ->
            notAllowControlPanelClick = !notAllowControlPanelClick
            if (notAllowControlPanelClick) {
                showControlPanel = false
                binding.controlPanel.visibility = View.GONE
            } else {
                showControlPanel = true
                binding.controlPanel.visibility = View.VISIBLE
            }
        }
        binding.ivVideoScreenshot.setOnClickListener {
            val bitmap = binding.svPlayerContainer.bitmap
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    try {
                        val baseDir= File(BASE_DIR + File.separator + "screenshot")
                        if (!baseDir.exists()) {
                            baseDir.mkdirs()
                        }
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.CHINA)
                        val file = File(baseDir, dateFormat.format(System.currentTimeMillis()) + ".png")
                        val outputStream = FileOutputStream(file)
                        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                        outputStream.flush()
                        outputStream.close()
                        true
                    } catch (e: java.lang.Exception) {
                        false
                    }
                }.let {
                    toast(if (it) "截图成功，已保存到本地" else "截图失败")
                }
            }

        }
        binding.videoTitle.text = File(playUrl).name
    }

    private var surfaceCallback = object : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
            val sf = Surface(surface)
            player.setVideoSurface(sf)
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
        activity?.requestedOrientation =
            if (videoWidth > videoHeight) ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE else ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

        val lp = binding.svPlayerContainer.layoutParams
        lp.width =
            if (videoWidth <= videoHeight) binding.root.width else (binding.root.height * (videoWidth.toDouble() / videoHeight)).toInt()
        lp.height =
            if (videoWidth <= videoHeight) (binding.root.width * (videoHeight.toDouble() / videoWidth)).toInt() else binding.root.height
        binding.svPlayerContainer.layoutParams = lp
    }

    private fun setSeekBar(totalTime: Long) {
        binding.seekBar.max = (totalTime / 1000).toInt()
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
                    binding.seekBar.progress = (position / 1000).toInt()
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