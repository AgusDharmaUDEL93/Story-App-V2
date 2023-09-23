package com.udeldev.storyapp.component.button

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.udeldev.storyapp.R

class OutlinedSecondaryButton : AppCompatButton {

    private lateinit var backgroundBtn : Drawable
    private var textColor : Int = 0

    constructor(context: Context) : super(context) {
        initComponent()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initComponent()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initComponent()
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        background = backgroundBtn
        textSize = 15f
        setTextColor(textColor)
        gravity = Gravity.CENTER

    }

    private fun initComponent (){
        backgroundBtn = ContextCompat.getDrawable(context, R.drawable.btn_outlined_secondary) as Drawable
        textColor = ContextCompat.getColor(context, R.color.red_300)
    }
}