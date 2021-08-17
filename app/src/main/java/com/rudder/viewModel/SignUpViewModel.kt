package com.rudder.viewModel


import android.content.ContentValues
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.rudder.data.*
import com.rudder.data.remote.PostApi
import com.rudder.data.repository.Repository
import com.rudder.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class SignUpViewModel : ViewModel() {
    fun getInstance() : SignUpViewModel{
        return if(this::signUpViewModel.isInitialized){
            signUpViewModel
        }else{
            signUpViewModel = SignUpViewModel()
            signUpViewModel
        }
    }
    private lateinit var signUpViewModel : SignUpViewModel

    val _userId = MutableLiveData<String>()
    val _userPassword = MutableLiveData<String>()
    val _userPasswordCheck = MutableLiveData<String>()
    val _userRecommendCode = MutableLiveData<String>()
    val _userEmailID = MutableLiveData<String>()
    val _userEmailDomain = MutableLiveData<String>()
    val _userVerificationCode = MutableLiveData<String>()
    val _userSchool = MutableLiveData<Int>()
    val _userNickName = MutableLiveData<String>()
    val _userIntroduce = MutableLiveData<String>()
    val _userSchoolName = MutableLiveData<String>()

    val _schoolSelectNext = MutableLiveData<Event<Boolean>>()
    val _schoolSelectBack = MutableLiveData<Event<Boolean>>()
    val _createAccountNext = MutableLiveData<Event<Boolean>>()
    val _createAccountBack = MutableLiveData<Event<Boolean>>()
    val _profileSettingNext = MutableLiveData<Event<Boolean>>()
    val _profileSettingBack = MutableLiveData<Event<Boolean>>()

    val _idChangeFlag = MutableLiveData<Event<Boolean>>()
    val _passwordFlag = MutableLiveData<Event<Boolean>>()
    val _passwordCheckFlag = MutableLiveData<Event<Boolean>>()
    val _emailDomainFlag = MutableLiveData<Event<Boolean>>()
    val _schoolSelectFlag = MutableLiveData<Event<Boolean>>()
    val _idCheckFlag = MutableLiveData<Event<Boolean>>()
    val _emailCheckFlag = MutableLiveData<Event<Boolean>>()
    val _verifyCodeCheckFlag = MutableLiveData<Event<Boolean>>()
    val _nickNameFlag = MutableLiveData<Event<Boolean>>()
    val _verifiCodeChangeFlag = MutableLiveData<Event<Boolean>>()

    val _schoolList = MutableLiveData<MutableList<String>>()

    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID: LiveData<String> = _userEmailID
    val userEmailDomain: LiveData<String> = _userEmailDomain
    val userVerificationCode: LiveData<String> = _userVerificationCode
    val userSchool: LiveData<Int> = _userSchool
    val userNickName: LiveData<String> = _userNickName
    val userIntroduce: LiveData<String> = _userIntroduce
    val userSchoolName: LiveData<String> = _userSchoolName


    val schoolSelectNext: LiveData<Event<Boolean>> = _schoolSelectNext
    val schoolSelectBack: LiveData<Event<Boolean>> = _schoolSelectBack
    val createAccountNext: LiveData<Event<Boolean>> = _createAccountNext
    val createAccountBack: LiveData<Event<Boolean>> = _createAccountBack
    val profileSettingNext: LiveData<Event<Boolean>> = _profileSettingNext
    val profileSettingBack: LiveData<Event<Boolean>> = _profileSettingBack

    val idChangeFlag : LiveData<Event<Boolean>> = _idChangeFlag
    val passwordFlag : LiveData<Event<Boolean>> = _passwordFlag
    val passwordCheckFlag : LiveData<Event<Boolean>> = _passwordCheckFlag
    val emailDomainFlag : LiveData<Event<Boolean>> = _emailDomainFlag
    val schoolSelectFlag : LiveData<Event<Boolean>> = _schoolSelectFlag
    val idCheckFlag : LiveData<Event<Boolean>> = _idCheckFlag
    val emailCheckFlag : LiveData<Event<Boolean>> = _emailCheckFlag
    val verifyCodeCheckFlag: LiveData<Event<Boolean>> = _verifyCodeCheckFlag
    val nickNameFlag: LiveData<Event<Boolean>> = _nickNameFlag
    val verifiCodeChangeFlag : LiveData<Event<Boolean>> = _verifiCodeChangeFlag

    val schoolList : LiveData<MutableList<String>> = _schoolList

    private val repository = Repository()


    init {
        _userId.value = ""
        _userPassword.value = ""
        _userPasswordCheck.value = ""
        _userRecommendCode.value = ""
        _userEmailID.value = ""
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""
        _schoolList.value = mutableListOf("Select Your School!")
        _userSchoolName.value = ""

//        _schoolSelectNext.value = Event(false)
//        _schoolSelectBack.value = Event(false)
//        _createAccountNext.value = Event(false)
//        _createAccountBack.value = Event(false)
//        _profileSettingNext.value = Event(false)
//        _profileSettingBack.value = Event(false)
    }

    val emailRg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$".toRegex()
    val passwordRg = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)
    val nickNameRg = "^[a-zA-Z0-9-_]{5,10}\$".toRegex()

    fun onTextChangeId() {
        _idChangeFlag.value = Event(true)
    }

    fun onTextChangeVerifiCode() {
        _verifiCodeChangeFlag.value = Event(true)
    }

    fun clearValue() {
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""
    }


    fun onTextChangePW() {
        if (_userPassword.value!!.trim().matches(passwordRg) && _userPassword.value!!.isNotEmpty())
            _passwordFlag.value = Event(true)
        else
            _passwordFlag.value = Event(false)

        if (_userPassword.value!!.trim() == _userPasswordCheck.value!!.trim() && _userPassword.value!!.isNotEmpty())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangePWCheck() {
        if (_userPasswordCheck.value!!.trim() == _userPassword.value!!.trim() && _userPasswordCheck.value!!.isNotEmpty())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangeEmailDomain() {
        val emailDomainChunk = _userEmailDomain.value!!.split('.')[0]
        Log.d("emailDomainChunk","emailDomainChunk : ${emailDomainChunk}")
        if (_userEmailDomain.value!!.trim().matches(emailRg) && _userEmailID.value!!.isNotEmpty()) {
            if (emailDomainChunk == _userSchoolName.value!!)
                _emailDomainFlag.value = Event(true)
            else
                _emailDomainFlag.value = Event(false)
        } else
            _emailDomainFlag.value = Event(false)
    }

    fun onTextChangeNickName() {
        if (_userNickName.value!!.trim().matches(nickNameRg) && _userNickName.value!!.isNotEmpty())
            _nickNameFlag.value = Event(true)
        else
            _nickNameFlag.value = Event(false)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("onCleared","onCleared")
    }

    fun clickNextSchoolSelect(){
        _schoolSelectNext.value = Event(true)
    }

    fun clickBackSchoolSelect(){
        _schoolSelectBack.value = Event(true)
    }

    fun clickNextCreateAccount(){
        _createAccountNext.value = Event(true)
    }

    fun clickBackCreateAccount(){
        _createAccountBack.value = Event(true)
    }

    fun clickBackProfileSetting(){
        _profileSettingBack.value = Event(true)
    }


    fun onSelectItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        Log.d("parent.getAdapter","$pos, $id, ${parent.selectedItem}")
        if (pos != 0){
            _schoolSelectFlag.value = Event(true)
            _userSchoolName.value = parent.selectedItem.toString().split(" ")[0].toLowerCase()
            Log.d(ContentValues.TAG, "_userSchoolName.value : ${_userSchoolName.value }")
        }else{
            _schoolSelectFlag.value = Event(false)
        }
    }

    fun callSchoolList() {
        GlobalScope.launch {
            val resultSchoolList = repository.signUpSchoolList()
            Log.d(ContentValues.TAG, "resultSchoolList 결과 : ${resultSchoolList}")

            for (i in 0 until resultSchoolList.size() ) {
                val iObject = resultSchoolList[i].asJsonObject
                val schoolName = iObject.get("school_name").asString
                Log.d(ContentValues.TAG, "callIdCheck 결과 : ${schoolName}")
                _schoolList.value!!.add(schoolName)
            }
        }
    }

    fun callIdCheck() {
        GlobalScope.launch {
            val idInput = _userId.value!!
            val result = repository.signUpIdDuplicated(IdDuplicatedInfo(idInput))
            Log.d(ContentValues.TAG, "callIdCheck 결과 : ${result}")
            _idCheckFlag.postValue(Event(!result && _userId.value!!.isNotEmpty()))
        }
    }

    fun callSendVeriCode() {
        GlobalScope.launch {
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpSendVerifyCode(EmailInfo(emailInput))
            Log.d(ContentValues.TAG, "callSendVeriCode 결과 : ${result}")
            _emailCheckFlag.postValue(Event(result))
            _userVerificationCode.postValue("")
        }
    }

    fun callCheckVeriCode() {
        GlobalScope.launch {
            val verifyCodeInput = _userVerificationCode.value!!
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpCheckVerifyCode(CheckVerifyCodeInfo(emailInput, verifyCodeInput))
            Log.d(ContentValues.TAG, "callCheckVeriCode 결과 : ${result}")
            _verifyCodeCheckFlag.postValue(Event(result))
        }
    }

    fun callCreateAccount() { // Sign Up, Complete!
        GlobalScope.launch {
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val inputInfo = SignUpInsertInfo(_userId.value!!, _userPassword.value!!,emailInput,_userRecommendCode.value!!,
                _userSchoolName.value!!,"",_userIntroduce.value!!,_userNickName.value!! )

            val result = repository.signUpCreateAccount(inputInfo)
            Log.d(ContentValues.TAG, "callCreateAccount 결과 : ${result}")
            _profileSettingNext.postValue(Event(result))
        }
    }

}

