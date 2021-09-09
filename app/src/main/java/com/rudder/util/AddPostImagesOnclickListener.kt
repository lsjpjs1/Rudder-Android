package com.rudder.util

import android.view.View

interface AddPostImagesOnclickListener {
    fun onClickAddImage(view: View, position: Int)
    fun onClickDeleteImage(view: View, position: Int)
}