package com.thewind.widget.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thewind.hyper.databinding.BottomSheetCommonBinding
import com.thewind.util.fillWidth

/**
 * @author: read
 * @date: 2023/3/18 下午8:21
 * @description:
 */
class CommonBottomSheetDialogFragment private constructor(
    private val items: MutableList<String>,
    private val action: (Int) -> Unit
) : DialogFragment() {

    private lateinit var binding: BottomSheetCommonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetCommonBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvItems.layoutManager = LinearLayoutManager(context)
        binding.rvItems.adapter = BottomSheetAdapter(items) {
            dismissAllowingStateLoss()
            action.invoke(it)
        }
    }

    override fun onStart() {
        super.onStart()
        fillWidth()

    }

    companion object {
        fun newInstance(items: MutableList<String>, action: (Int) -> Unit) =
            CommonBottomSheetDialogFragment(items, action)
    }

}