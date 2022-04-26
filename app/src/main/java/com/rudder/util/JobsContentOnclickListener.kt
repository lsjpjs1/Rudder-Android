package com.rudder.util

import android.view.View

interface JobsContentOnclickListener {
    fun onClickContainerView(view: View, position: Int, viewTag : String)
    fun onClickImageView(view: View, position: Int)
}