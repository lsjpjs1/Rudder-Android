package com.rudder.util.uiUtils

import androidx.constraintlayout.widget.ConstraintLayout
import com.rudder.data.DisplaySize

class PercentDivideImpl(override val parentLayout: ConstraintLayout,
                        val displaySize: ArrayList<Int>,
                        val weight: Float) : PercentDivide{
    override fun divideChildSameRatio() {
        val childCount = parentLayout.childCount
        val eachRatio = 1.0f / childCount
        for(i in 0 until childCount){
            val child = parentLayout.getChildAt(i)
            val lp = child.layoutParams as ConstraintLayout.LayoutParams
            lp.height = (eachRatio * weight * displaySize.get(1)).toInt()
            child.layoutParams = lp
        }
    }

}