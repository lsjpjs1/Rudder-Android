package com.rudder.util

import android.view.View

interface JobsContentOnclickListener {
    fun onClickContainerView(view: View, position: Int)
    fun onClickImageView(view: View, position: Int)
}