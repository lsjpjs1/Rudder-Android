package com.rudder.ui.activity


interface MainActivityInterface: ActivityInterface {

    fun showNotificationFragment()

    fun showPostMessageFragment()


    fun showPostMessageRoomFragment(postMessageRoomId: Int)

    }

