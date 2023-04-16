package com.thewind.user.donate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.thewind.hyper.databinding.DonateDialogFragmentBinding
import com.thewind.util.fillFullScreen
import com.thewind.util.fillWidth
import com.thewind.util.transportBackground

/**
 * @author: read
 * @date: 2023/4/16 下午9:53
 * @description:
 */
class DonateDialogFragment: DialogFragment() {
    private lateinit var binding: DonateDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DonateDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvClose.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    override fun onStart() {
        super.onStart()
        fillWidth()
    }

    companion object {
        fun newInstance() = DonateDialogFragment()
    }
}