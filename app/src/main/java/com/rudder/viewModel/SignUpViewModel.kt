package com.rudder.viewModel


import android.R
import android.app.ProgressDialog
import android.content.ContentValues
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudder.data.CheckVerifyCodeInfo
import com.rudder.data.EmailInfo
import com.rudder.data.IdDuplicatedInfo
import com.rudder.data.SignUpInsertInfo
import com.rudder.data.remote.Category
import com.rudder.data.repository.Repository
import com.rudder.ui.activity.SignUpActivity
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
    val _userSchoolInt = MutableLiveData<Int>()
    val _userNickName = MutableLiveData<String>()
    val _userIntroduce = MutableLiveData<String>()
    val _userSchoolName = MutableLiveData<String>()

    val _termsOfServiceNext = MutableLiveData<Event<Boolean>>()
    val _termsOfServiceBack = MutableLiveData<Event<Boolean>>()
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
    val _termsOfServiceFlag = MutableLiveData<Event<Boolean>>()
    val _schoolList = MutableLiveData<MutableList<String>>()

    val _categories = MutableLiveData<ArrayList<Category>>()
    val _categoryNames = MutableLiveData<ArrayList<String>>()



    val userId: LiveData<String> = _userId
    val userPassword: LiveData<String> = _userPassword
    val userPasswordCheck: LiveData<String> = _userPasswordCheck
    val userRecommendCode: LiveData<String> = _userRecommendCode
    val userEmailID: LiveData<String> = _userEmailID
    val userEmailDomain: LiveData<String> = _userEmailDomain
    val userVerificationCode: LiveData<String> = _userVerificationCode
    val userSchoolInt: LiveData<Int> = _userSchoolInt
    val userNickName: LiveData<String> = _userNickName
    val userIntroduce: LiveData<String> = _userIntroduce
    val userSchoolName: LiveData<String> = _userSchoolName

    val termsOfServiceNext: LiveData<Event<Boolean>> = _termsOfServiceNext
    val termsOfServiceBack: LiveData<Event<Boolean>> = _termsOfServiceBack
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
    val termsOfServiceFlag : LiveData<Event<Boolean>> = _termsOfServiceFlag
    val schoolList : LiveData<MutableList<String>> = _schoolList


    val categories: LiveData<ArrayList<Category>> = _categories
    val categoryNames: LiveData<ArrayList<String>> = _categoryNames

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
        _userSchoolInt.value = 0
        _userIntroduce.value = ""


        getCategories()

    }

    val emailRg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$".toRegex()
    val passwordRg = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)
    val nickNameRg = "^[a-zA-Z0-9-_]{5,10}\$".toRegex()


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        _termsOfServiceFlag.value = Event (check)
    }

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
        if (_userPassword.value!!.trim().matches(passwordRg) && _userPassword.value!!.isNotBlank())
            _passwordFlag.value = Event(true)
        else
            _passwordFlag.value = Event(false)

        if (_userPassword.value!!.trim() == _userPasswordCheck.value!!.trim() && _userPassword.value!!.isNotBlank())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangePWCheck() {
        if (_userPasswordCheck.value!!.trim() == _userPassword.value!!.trim() && _userPasswordCheck.value!!.isNotBlank())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangeEmailDomain() {
        val emailDomainChunk = _userEmailDomain.value!!.split('.')[0]
        Log.d("emailDomainChunk","emailDomainChunk : ${emailDomainChunk}")
        if (_userEmailDomain.value!!.trim().matches(emailRg) && _userEmailID.value!!.isNotBlank()) {
            if (emailDomainChunk == _userSchoolName.value!!)
                _emailDomainFlag.value = Event(true)
            else
                _emailDomainFlag.value = Event(false)
        } else
            _emailDomainFlag.value = Event(false)
    }

    fun onTextChangeNickName() {
        if (_userNickName.value!!.trim().matches(nickNameRg) && _userNickName.value!!.isNotBlank())
            _nickNameFlag.value = Event(true)
        else
            _nickNameFlag.value = Event(false)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("onCleared","onCleared")
    }


    fun clickNextTermsOfService(){
        _termsOfServiceNext.value = Event(true)
    }

    fun clickBackTermsOfService(){
        _termsOfServiceBack.value = Event(true)
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
            _userSchoolInt.value = pos + 1
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
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val idInput = _userId.value!!
            val result = repository.signUpIdDuplicated(IdDuplicatedInfo(idInput))
            Log.d(ContentValues.TAG, "callIdCheck 결과 : ${result}")
            _idCheckFlag.postValue(Event(!result && _userId.value!!.isNotBlank()))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
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
                _userSchoolInt.value!!,"",_userIntroduce.value!!,_userNickName.value!! )

            val result = repository.signUpCreateAccount(inputInfo)
            Log.d(ContentValues.TAG, "callCreateAccount 결과 : ${result}")
            _profileSettingNext.postValue(Event(result))
        }
    }


    fun splitCategoryNames(categoryList: ArrayList<Category>): ArrayList<String> {
        var categoryNames = ArrayList<String>()
        for (category in categoryList) {
            categoryNames.add(category.categoryName)
        }
        return categoryNames
    }


    fun getCategories() {
        GlobalScope.launch {
            var categoryList = Repository().getCategories()
            viewModelScope.launch {
                _categoryNames.value = splitCategoryNames((categoryList))
                _categories.value = categoryList
                Log.d("_categoryNames.value", "${_categoryNames.value}")
            }
        }
    }



}

