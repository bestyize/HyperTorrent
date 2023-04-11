package com.thewind.user.setting.user.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.thewind.hypertorrent.databinding.FragmentUserSettingBinding
import com.thewind.user.bean.User
import com.thewind.user.login.AccountHelper
import com.thewind.user.login.LoginStateChangeListener
import com.thewind.util.toast
import com.thewind.viewer.imagepicker.ImagePickerFragment
import com.thewind.widget.inputdialog.InputDialogFragment
import com.thewind.widget.inputdialog.InputDialogModel

class UserSettingFragment : Fragment() {

    private lateinit var binding: FragmentUserSettingBinding
    private lateinit var vm: UserSettingViewModel

    private var localLoginStateChangerListener = object : LoginStateChangeListener {
        override fun onLoginStateChange(user: User) {
            binding.optionName.desc = user.userName
            binding.optionHeader.icon = user.headerUrl
            binding.optionUid.desc = user.uid.toString()
            binding.optionDesc.desc = user.userDesc?:""
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = ViewModelProvider(this)[UserSettingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AccountHelper.registerChangeListener(localLoginStateChangerListener)
        val user = AccountHelper.loadUserInfo()
        binding.optionName.desc = user.userName
        binding.optionHeader.icon = user.headerUrl
        binding.optionUid.desc = user.uid.toString()
        binding.optionDesc.desc = user.userDesc?:""
        binding.optionDesc.setOnClickListener {
            InputDialogFragment.newInstance(InputDialogModel().apply {
                title = "修改个人简介"
                hint = binding.optionDesc.desc.toString()
            }) { action, data ->
                when (action) {
                    1 -> {
                        vm.updateDesc(data)
                    }
                }
            }.showNow(childFragmentManager, "mod")
        }
        binding.optionName.setOnClickListener {
            InputDialogFragment.newInstance(InputDialogModel().apply {
                title = "修改用户名"
                hint = binding.optionName.desc.toString()
            }) { action, data ->
                when (action) {
                    1 -> {
                        vm.updateUserName(data)
                    }
                }
            }.showNow(childFragmentManager, "mod")
        }
        binding.optionPassword.setOnClickListener {
            InputDialogFragment.newInstance(InputDialogModel().apply {
                title = "修改密码"
                hint = "******"
            }) { action, data ->
                when (action) {
                    1 -> {
                        vm.updatePassword(data)
                    }
                }
            }.showNow(childFragmentManager, "mod")

        }

        binding.optionUid.setOnClickListener {
            toast("暂不支持修改")
        }

        binding.ivClose.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.optionHeader.setOnClickListener{
            val frag = ImagePickerFragment.newInstance{ image->
                image?.let {
                    vm.updateHeader(it)
                }
            }
            frag.showNow(childFragmentManager, frag.hashCode().toString())
        }

        binding.tvLogout.setOnClickListener {
            AccountHelper.logout()
            toast("您已退出登录")
        }

        vm.userNameLiveData.observe(viewLifecycleOwner) {
            if (it.code == -1) toast(it.msg ?: "修改用户名失败，未知错误")
            if (it.code == 0) {
                binding.optionName.desc = it.data?.userName ?: ""
                toast(it.msg ?: "修改用户名成功!")
            }
        }

        vm.passwordLiveData.observe(viewLifecycleOwner) {
            if (it.code == -1) toast(it.msg ?: "修改用户名失败，未知错误")
            if (it.code == 0) {
                toast(it.msg ?: "修改密码成功!")
            }
        }

        vm.headerLiveData.observe(viewLifecycleOwner) {
            if (it.code == -1) toast(it.msg ?: "修改头像失败，未知错误")
            if (it.code == 0) {
                toast(it.msg ?: "修改头像成功!")
            }
        }

        vm.descLiveData.observe(viewLifecycleOwner) {
            if (it.code == -1) toast(it.msg ?: "修改简介失败，未知错误")
            if (it.code == 0) {
                binding.optionDesc.desc = it.data?.userDesc
                toast(it.msg ?: "修改简介成功!")
            }
        }
    }

    companion object {
        fun newInstance(): UserSettingFragment {
            return UserSettingFragment()
        }
    }

}