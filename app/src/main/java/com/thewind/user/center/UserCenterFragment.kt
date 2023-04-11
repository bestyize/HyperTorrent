package com.thewind.user.center

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.thewind.community.editor.page.EditorActivity
import com.thewind.hypertorrent.R
import com.thewind.hypertorrent.databinding.FragmentUserCenterBinding
import com.thewind.user.bean.FeedChannel
import com.thewind.user.bean.User
import com.thewind.user.login.AccountHelper
import com.thewind.user.login.LoginStateChangeListener
import com.thewind.user.setting.user.page.UserSettingFragment
import com.thewind.util.ViewUtils
import com.thewind.widget.activity.FullScreenContainerActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val UID = "uid"

class UserCenterFragment : Fragment() {

    private lateinit var binding: FragmentUserCenterBinding
    private lateinit var vm: UserCenterViewModel

    private val channels: MutableList<FeedChannel> = mutableListOf()

    private var uid: Long = -1L
    private var isSelfUser = true

    private var localLoginStateChangerListener = object : LoginStateChangeListener {
        override fun onLoginStateChange(user: User) {
            if (isSelfUser) {
                vm.loadLocalUserInfo()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = arguments?.getLong(UID) ?: -1
        ViewUtils.enterFullScreenMode(activity, true)
        vm = ViewModelProvider(requireActivity())[UserCenterViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserCenterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        AccountHelper.registerChangeListener(localLoginStateChangerListener)
        if (AccountHelper.isLogin() && uid == -1L) {
            uid = AccountHelper.loadUserInfo().uid
        }
        binding.vpContainer.offscreenPageLimit = 10
        binding.vpContainer.adapter = UserFeedAdapter(childFragmentManager, lifecycle, channels)
        vm.tabsListData.observe(viewLifecycleOwner) {
            TabLayoutMediator(binding.userUgcTab, binding.vpContainer, false) { tab, pos ->
                tab.text = channels[pos].title
            }.attach()
        }

        vm.loadTabs(uid)
        vm.userInfoLiveData.observe(viewLifecycleOwner) {
            Glide.with(binding.root.context).load(it.headerUrl).into(binding.ivHeader)
            binding.tvUserName.text = it.userName
            binding.tvFansCount.text = it.fansCount.toString()
            binding.tvFollowingCount.text = it.followCount.toString()
            binding.tvSelfDesc.text = it.selfDesc
        }

        vm.localUserInfo.observe(viewLifecycleOwner) {
            Glide.with(binding.root.context).load(it.headerUrl).into(binding.ivHeader)
            binding.tvUserName.text = it.userName
            binding.tvUid.text = "uid:${it.uid}"
            binding.tvSelfDesc.text = it.userDesc?:""
        }

        if (AccountHelper.loadUserInfo().uid == uid) {
            isSelfUser = true
            vm.loadLocalUserInfo()

        } else {
            isSelfUser = false
            vm.loadUserInfo(uid)
        }

        vm.tabsListData.observe(viewLifecycleOwner) {
            channels.clear()
            channels.addAll(it)
            binding.vpContainer.adapter?.notifyDataSetChanged()
        }
        vm.loadTabs(uid)

        binding.ivPublish.setOnClickListener {
            val intent = Intent(activity, EditorActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.activity_bottom_in, R.anim.activity_bottom_out)
        }

        binding.ivSetting.setOnClickListener {
            FullScreenContainerActivity.startWithFragment(activity = activity, UserSettingFragment.newInstance())
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(uid: Long = -1) =
            UserCenterFragment().apply {
                arguments = Bundle().apply {
                    putLong(UID, uid)
                }
            }
    }
}