package com.thewind.widget.button

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.thewind.hyper.R

/**
 * @author: read
 * @date: 2023/4/2 下午1:50
 * @description:
 */
class CheckButton(context: Context, attr: AttributeSet? = null) : View(context, attr) {

    var isChecked = false
        set(value) {
            field = value
            setBackgroundResource(if (isChecked) checkedIcon else unCheckedIcon)
            //checkChangeListener.invoke(value)
        }

    private var checkedIcon: Int = 0
    private var unCheckedIcon: Int = 0

    var checkChangeListener: (Boolean) -> Unit = {  }

    init {
        val ta = context.obtainStyledAttributes(attr, R.styleable.CheckButton)
        isChecked = ta.getBoolean(R.styleable.CheckButton_isChecked, false)
        checkedIcon = ta.getResourceId(R.styleable.CheckButton_checkedIcon, R.drawable.ic_selected)
        unCheckedIcon = ta.getResourceId(R.styleable.CheckButton_unCheckedIcon, R.drawable.ic_unselected)
        ta.recycle()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                isChecked = !isChecked
                setBackgroundResource(if (isChecked) checkedIcon else unCheckedIcon)
                checkChangeListener.invoke(isChecked)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

}