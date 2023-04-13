package com.thewind.player.detail

import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Player.Listener
import androidx.media3.exoplayer.ExoPlayer
import com.tencent.mmkv.MMKV
import com.thewind.hyper.R
import com.thewind.hyper.databinding.FragmentDetailPlayerBinding
import com.thewind.hyper.databinding.SpeedAdjustPopupWindowBinding
import com.thewind.util.ViewUtils
import com.thewind.util.toPx
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

    private var speedSelectPopupWindow: PopupWindow?=null

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
                })
                player.repeatMode = Player.REPEAT_MODE_ONE
                player.playWhenReady = true
                player.prepare()
                player.seekTo(MMKV.defaultMMKV().getLong(LAST_PLAY_POSITION + playUrl, 0L))
                player.play()
            }
        }
        binding.svPlayerContainer.surfaceTextureListener = surfaceCallback

        binding.svPlayerContainer.setOnClickListener {
            ViewUtils.enterImmersiveFullScreenMode(activity)
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
            if (binding.controlPanel.visibility == View.GONE) {
                speedSelectPopupWindow?.dismiss()
            }
        }

        binding.ivUser.setOnClickListener {
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

        binding.tvSpeedAdjust.setOnClickListener {
            if (speedSelectPopupWindow?.isShowing == true) {
                speedSelectPopupWindow?.dismiss()
            } else {
                showPopupWindow(binding.tvSpeedAdjust)
            }
        }
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
                    MMKV.defaultMMKV().encode(LAST_PLAY_POSITION + playUrl, position)
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


    private fun showPopupWindow(view: View) {
        val bind = SpeedAdjustPopupWindowBinding.bind(layoutInflater.inflate(R.layout.speed_adjust_popup_window, null))
        speedSelectPopupWindow = PopupWindow(
            bind.root,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        bind.tvSpeed05.setOnClickListener {
            binding.tvSpeedAdjust.text = "0.5倍"
            player.setPlaybackSpeed(0.5f)
            speedSelectPopupWindow?.dismiss()
        }

        bind.tvSpeed1.setOnClickListener {
            binding.tvSpeedAdjust.text = "1.0倍"
            player.setPlaybackSpeed(1.0f)
            speedSelectPopupWindow?.dismiss()
        }

        bind.tvSpeed15.setOnClickListener {
            binding.tvSpeedAdjust.text = "1.5倍"
            player.setPlaybackSpeed(1.5f)
            speedSelectPopupWindow?.dismiss()
        }

        bind.tvSpeed2.setOnClickListener {
            binding.tvSpeedAdjust.text = "2.0倍"
            player.setPlaybackSpeed(2.0f)
            speedSelectPopupWindow?.dismiss()
        }

        bind.tvSpeed25.setOnClickListener {
            binding.tvSpeedAdjust.text = "2.5倍"
            player.setPlaybackSpeed(2.5f)
            speedSelectPopupWindow?.dismiss()
        }

        bind.tvSpeed3.setOnClickListener {
            binding.tvSpeedAdjust.text = "3.0倍"
            player.setPlaybackSpeed(3.0f)
            speedSelectPopupWindow?.dismiss()

        }

        // Calculate x and y offset
        val xOffset = -(20.toPx())
        val yOffset = -(300.toPx())

        // Show the popup window with the calculated offset
        speedSelectPopupWindow?.showAsDropDown(view, xOffset, yOffset)
    }

    companion object {
        private const val LAST_PLAY_POSITION = "last_play_position"
        @JvmStatic
        fun newInstance(playUrl: String) =
            DetailPlayerFragment().apply {
                arguments = Bundle().apply {
                    putString(PLAY_URL, playUrl)
                }
            }
    }
}