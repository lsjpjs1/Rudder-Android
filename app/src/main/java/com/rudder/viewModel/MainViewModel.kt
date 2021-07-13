package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rudder.R


object MainViewModel : ViewModel() {
    private val _selectedTab = MutableLiveData<Int>()

    val selectedTab: LiveData<Int> = _selectedTab

    init{
        _selectedTab.value = R.id.communityButton
    }

    fun clickCommunity(){
        _selectedTab.value = R.id.communityButton
    }

    fun clickMyPage(){
        _selectedTab.value = R.id.myPageButton
    }

}