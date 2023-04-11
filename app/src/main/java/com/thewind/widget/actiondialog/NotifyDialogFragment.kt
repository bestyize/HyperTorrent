package com.thewind.widget.actiondialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.thewind.hypertorrent.databinding.NotifyDialogFragmentBinding


/**
 * @author: read
 * @date: 2023/4/11 下午11:44
 * @description:
 */
class NotifyDialogFragment : DialogFragment() {

    private lateinit var binding: NotifyDialogFragmentBinding
    var actionLeft:((ButtonData) -> Unit)?= null
    var actionRight:((ButtonData) -> Unit)?= null
    var notifyData: NotifyData?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notifyData = arguments?.getParcelable("notify_data")
        if (notifyData == null) {
            dismissAllowingStateLoss()
            return
        }

        isCancelable = notifyData?.cancelable?:true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = NotifyDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = notifyData ?: return
        binding.tvTitle.text = notifyData?.title?:"温馨提示"
        binding.tvContent.text = notifyData?.content
        data.leftButton?.let {bData ->
            binding.tvLeft.visibility = View.VISIBLE
            binding.tvLeft.text = bData.text
            binding.tvLeft.setOnClickListener {
                actionLeft?.invoke(bData)
                dismissAllowingStateLoss()
            }
        }
        data.rightButton?.let { bData->
            binding.tvRight.text = bData.text
            binding.tvRight.setOnClickListener {
                actionRight?.invoke(bData)
                dismissAllowingStateLoss()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }



    companion object {
        fun newInstance(notifyData: NotifyData, actionLeft:(ButtonData) -> Unit = {}, actionRight:(ButtonData) -> Unit) = NotifyDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable("notify_data", notifyData)
            }
            this.actionLeft = actionLeft
            this.actionRight = actionRight
        }
    }

}