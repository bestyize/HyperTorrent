package com.thewind.widget.inputdialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.thewind.hyper.databinding.InputDialogFragmentBinding
import com.thewind.util.fillWidth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author: read
 * @date: 2023/3/21 上午12:39
 * @description:
 */
class InputDialogFragment private constructor(private val model: InputDialogModel, private val action: (Int,String) -> Unit): DialogFragment() {

    private lateinit var binding: InputDialogFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = InputDialogFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.preInput?.let {
            binding.etInput.setText(it)
        }
        binding.etInput.hint = model.hint
        binding.tvTitle.text = model.title
        binding.tvLeft.text = model.leftBtnText
        binding.tvRight.text = model.rightBtnText
        binding.tvLeft.setOnClickListener {
            dismissAllowingStateLoss()
            action.invoke(0, "")

        }
        binding.tvRight.setOnClickListener {
            dismissAllowingStateLoss()
            action.invoke(1, binding.etInput.text.toString())
        }

        binding.etInput.requestFocusFromTouch()
        binding.etInput.requestFocus()
        //显示软键盘
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    companion object {
        fun newInstance(model: InputDialogModel, action:(Int, String) -> Unit) = InputDialogFragment(model, action)
    }
}