package com.thewind.user.center

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.thewind.hypertorrent.databinding.FragmentUserCenterBinding
import com.thewind.util.ViewUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val UID = "uid"

class UserCenterFragment : Fragment() {

    private lateinit var binding: FragmentUserCenterBinding
    private lateinit var vm: UserCenterViewModel

    private val tabs: List<String> = listOf()

    private var uid: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            uid = savedInstanceState.getLong(UID)
        } else {
            uid = arguments?.getLong(UID) ?: -1
        }
        ViewUtils.enterFullScreenMode(activity, true)
        vm = ViewModelProvider(requireActivity())[UserCenterViewModel::class.java]

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(UID, uid)
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
        vm.tabsListData.observe(viewLifecycleOwner) {
//            TabLayoutMediator(binding.userUgcTab, binding.vpContainer, false) { tab, pos ->
//                tab.text = tabs[pos]
//            }.attach()
        }

        vm.loadTabs()
        vm.userInfoLiveData.observe(viewLifecycleOwner) {
            Glide.with(binding.root.context).load(it.headerUrl).into(binding.ivHeader)
            binding.tvUserName.text = it.userName
            binding.tvFans.text = it.fansCount.toString()
            binding.tvFollowingCount.text = it.followCount.toString()
            binding.tvSelfDesc.text = it.selfDesc
        }
        vm.loadUserInfo(uid)

    }

    companion object {
        @JvmStatic
        fun newInstance(uid: Long) =
            UserCenterFragment().apply {
                arguments = Bundle().apply {
                    putLong(UID, uid)
                }
            }
    }
}