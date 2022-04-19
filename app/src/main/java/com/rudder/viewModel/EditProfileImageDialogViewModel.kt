package com.rudder.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.BuildConfig
import com.rudder.data.dto.ProfileImage
import com.rudder.data.local.App
import com.rudder.data.remote.MyProfileImageRequest
import com.rudder.data.remote.UpdateNicknameRequest
import com.rudder.data.remote.UpdateProfileImageRequest
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class EditProfileImageDialogViewModel : ViewModel() {


    private val profileImageList = MutableLiveData<ArrayList<ProfileImage>>()
    private val selectedProfileImage = MutableLiveData<ProfileImage?>()
    private val toastMessage = MutableLiveData<String?>()
    private val closeFlag = MutableLiveData<Event<Boolean>>()
    private val myProfileImageUrl = MutableLiveData<String?>()

    val _myProfileImageUrl : LiveData<String?> = myProfileImageUrl
    val _closeFlag : LiveData<Event<Boolean>> = closeFlag
    val _toastMessage : LiveData<String?> = toastMessage
    val _selectedProfileImage : LiveData<ProfileImage?> = selectedProfileImage
    val _profileImageList : LiveData<ArrayList<ProfileImage>> = profileImageList


    init {
        getProfileImages()
    }


    private fun getProfileImages(){
        GlobalScope.launch {
            val profileImages= Repository().getProfileImages()
            viewModelScope.launch {
                profileImageList.value = profileImages
            }
        }
    }

    fun clickProfileImage(profileImage: ProfileImage){
        selectedProfileImage.value = profileImage
    }


    fun updateProfileImage(){
        if (_selectedProfileImage.value == null){
            toastMessage.value = "Please select a profile image"
        }else{
            GlobalScope.launch {
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(true))

                val updateProfileImageRequest = UpdateProfileImageRequest(
                    App.prefs.getValue(BuildConfig.TOKEN_KEY)!!,
                    _selectedProfileImage.value!!.profileImageId
                )
                val updateResponse = Repository().updateProfileImage(updateProfileImageRequest)
                if (updateResponse.isSuccess) {
                    toastMessage.postValue("Successfully changed Character!")
                    closeFlag.postValue(Event(true))
                    getMyProfileImageUrl()

                } else {
                    toastMessage.postValue("Failed to change my Character.")
                }
                ProgressBarUtil._progressBarDialogFlag.postValue(Event(false))
            }
        }
    }

    fun getMyProfileImageUrl(){
        GlobalScope.launch {
            val url = Repository().getMyProfileImageUrl(MyProfileImageRequest(App.prefs.getValue(BuildConfig.TOKEN_KEY)!!)).url
            myProfileImageUrl.postValue(url)
        }
    }
}