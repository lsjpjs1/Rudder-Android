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
import com.rudder.data.*
import com.rudder.data.local.App
import com.rudder.data.remote.Category
import com.rudder.data.repository.Repository
import com.rudder.ui.activity.SignUpActivity
import com.rudder.util.Event
import com.rudder.util.ProgressBarUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

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
    val _categorySelectNext = MutableLiveData<Event<Boolean>>()
    val _categorySelectBack = MutableLiveData<Event<Boolean>>()

    val _nickNameDuplicatedCheck = MutableLiveData<Event<Boolean>>()
    val _nickNameChangeFlag = MutableLiveData<Event<Boolean>>()
    val _idRgCheckFlag = MutableLiveData<Event<Boolean>>()
    val _emailDomainChangeFlag = MutableLiveData<Event<Boolean>>()
    val _idChangeFlag = MutableLiveData<Event<Boolean>>()
    val _passwordFlag = MutableLiveData<Event<Boolean>>()
    val _passwordCheckFlag = MutableLiveData<Event<Boolean>>()
    val _emailDomainFlag = MutableLiveData<Event<Boolean>>()
    val _schoolSelectFlag = MutableLiveData<Event<Boolean>>()
    val _idCheckFlag = MutableLiveData<Event<Boolean>>()
    val _emailCheckFlag = MutableLiveData<Event<Boolean>>()
    val _verifyCodeCheckFlag = MutableLiveData<Event<Boolean>>()
    val _nickbnameRgCheck = MutableLiveData<Event<Boolean>>()
    val _verifiCodeChangeFlag = MutableLiveData<Event<Boolean>>()
    val _termsOfServiceFlag = MutableLiveData<Event<Boolean>>()
    val _schoolList = MutableLiveData<MutableList<String>>()

    var _categories = MutableLiveData<ArrayList<Category>>()
    var _categoryNames = MutableLiveData<ArrayList<String>>()
    val _emailToast = MutableLiveData<String>()

    var _profileImageList = MutableLiveData<ArrayList<String>>()


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
    val categorySelectNext: LiveData<Event<Boolean>> = _categorySelectNext
    val categorySelectBack: LiveData<Event<Boolean>> = _categorySelectBack


    val nickNameDuplicatedCheck : LiveData<Event<Boolean>> = _nickNameDuplicatedCheck
    val nickNameChangeFlag : LiveData<Event<Boolean>> = _nickNameChangeFlag
    val idRgCheckFlag : LiveData<Event<Boolean>> = _idRgCheckFlag
    val emailDomainChangeFlag : LiveData<Event<Boolean>> = _emailDomainChangeFlag
    val idChangeFlag : LiveData<Event<Boolean>> = _idChangeFlag
    val passwordFlag : LiveData<Event<Boolean>> = _passwordFlag
    val passwordCheckFlag : LiveData<Event<Boolean>> = _passwordCheckFlag
    val emailDomainFlag : LiveData<Event<Boolean>> = _emailDomainFlag
    val schoolSelectFlag : LiveData<Event<Boolean>> = _schoolSelectFlag
    val idCheckFlag : LiveData<Event<Boolean>> = _idCheckFlag
    val emailCheckFlag : LiveData<Event<Boolean>> = _emailCheckFlag
    val verifyCodeCheckFlag: LiveData<Event<Boolean>> = _verifyCodeCheckFlag
    val nickbnameRgCheck: LiveData<Event<Boolean>> = _nickbnameRgCheck
    val verifiCodeChangeFlag : LiveData<Event<Boolean>> = _verifiCodeChangeFlag
    val termsOfServiceFlag : LiveData<Event<Boolean>> = _termsOfServiceFlag
    val schoolList : LiveData<MutableList<String>> = _schoolList


    var categories: LiveData<ArrayList<Category>> = _categories
    var categoryNames: LiveData<ArrayList<String>> = _categoryNames
    val emailToast: LiveData<String> = _emailToast
    var profileImageList: LiveData<ArrayList<String>> = _profileImageList


    private val repository = Repository()

    init {
        _userId.value = ""
        _userPassword.value = ""
        _userPasswordCheck.value = ""
        _userRecommendCode.value = ""
        _userEmailID.value = ""
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""
        _schoolList.value = mutableListOf("Select Your University!")
        _userSchoolName.value = ""
        _userSchoolInt.value = 0
        _userIntroduce.value = ""
        _emailToast.value = ""

        _categoryNames.value = ArrayList()
        callSchoolList()
        getCategories()
        getProfileImageList()

    }

    val emailRg = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$".toRegex()
    val passwordRg = "^(?=.*[a-zA-Z0-9])(?=.*[a-zA-Z!@#\$%^&*])(?=.*[0-9!@#\$%^&*]).{8,15}\$".toRegex() // 숫자, 문자, 특수문자 중 2가지 포함(8~15자)
    val nickNameRg = "^[a-zA-Z0-9-_]{4,15}\$".toRegex()
    val idRg = "^[a-zA-Z0-9-_]{4,15}\$".toRegex()



    fun onTextChangeId() {
        if (_userId.value!!.matches(idRg) && _userId.value!!.isNotBlank())
            _idRgCheckFlag.value = Event(true)
        else
            _idRgCheckFlag.value = Event(false)

        _idChangeFlag.value = Event(true)
    }

    fun onTextChangeVerifiCode() {
        _verifiCodeChangeFlag.value = Event(true)
    }

    fun clearEmailValue() {
        _userEmailDomain.value = ""
        _userVerificationCode.value = ""
    }


    fun onTextChangePW() {
        if (_userPassword.value!!.matches(passwordRg) && _userPassword.value!!.isNotBlank())
            _passwordFlag.value = Event(true)
        else
            _passwordFlag.value = Event(false)

        if (_userPassword.value!! == _userPasswordCheck.value!! && _userPassword.value!!.isNotBlank())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangePWCheck() {
        if (_userPasswordCheck.value!! == _userPassword.value!! && _userPasswordCheck.value!!.isNotBlank())
            _passwordCheckFlag.value = Event(true)
        else
            _passwordCheckFlag.value = Event(false)
    }

    fun onTextChangeEmailDomain() {
        if (_userEmailDomain.value!!.matches(emailRg) && _userEmailID.value!!.isNotBlank()) {
            _emailDomainFlag.value = Event(true)
        }
        else {
            _emailDomainFlag.value = Event(false)
        }

        _emailDomainChangeFlag.value = Event(true)
    }

    fun onTextChangeNickName() {
        if (_userNickName.value!!.matches(nickNameRg) && _userNickName.value!!.isNotBlank())
            _nickbnameRgCheck.value = Event(true)
        else
            _nickbnameRgCheck.value = Event(false)

        _nickNameChangeFlag.value = Event(true)
    }


    override fun onCleared() {
        super.onCleared()
        Log.d("onClearedSignUpVM","onClearedSignUpVM")
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

    fun clickNextProfileSetting(){
        _profileSettingNext.value = Event(true)
    }

    fun clickBackCategorySelect(){
        _categorySelectBack.value = Event(true)
    }



    fun onSelectItem(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        //pos                                 get selected item position
        //view.getText()                      get lable of selected item
        //parent.getAdapter().getItem(pos)    get item by pos
        //parent.getCount()                   get item count
        //parent.getSelectedItem()            get selected item
        if (pos != 0){
            _schoolSelectFlag.value = Event(true)
            _userSchoolInt.value = pos + 1
        }else{
            _schoolSelectFlag.value = Event(false)
        }
    }

    fun callSchoolList() {
        GlobalScope.launch {
            val resultSchoolList = repository.signUpSchoolList()

            for (i in 0 until resultSchoolList.size() ) {
                val iObject = resultSchoolList[i].asJsonObject
                val schoolName = iObject.get("school_name").asString
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

    fun callSendVeriCode() { //verifyEmail
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpSendVerifyCode(EmailInfoSignUp(emailInput,_userSchoolInt.value!!))
            if (result == "") {
                _emailToast.postValue("-")
                _emailCheckFlag.postValue(Event(true))
            } else {
                _emailToast.postValue(result)
                _emailCheckFlag.postValue(Event(false))
            }
            _userVerificationCode.postValue("")
            ProgressBarUtil._progressBarFlag.postValue(Event(false))
        }
    }

    fun callCheckVeriCode() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val verifyCodeInput = _userVerificationCode.value!!
            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val result = repository.signUpCheckVerifyCode(CheckVerifyCodeInfo(emailInput, verifyCodeInput))
            _verifyCodeCheckFlag.postValue(Event(result))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
        }
    }

    fun callCreateAccount() { // Sign Up, Complete!
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val emailInput = _userEmailID.value!!.plus('@').plus(_userEmailDomain.value!!)
            val inputInfo = SignUpInsertInfo(_userId.value!!, _userPassword.value!!,emailInput,_userRecommendCode.value!!,
                _userSchoolInt.value!!,"",_userIntroduce.value!!,_userNickName.value!! )

            val result = repository.signUpCreateAccount(inputInfo)

            Log.d(ContentValues.TAG, "callCreateAccount 결과 : ${result}")
            _categorySelectNext.postValue(Event(result))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
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
            var categoryList = repository.getCategories()
            viewModelScope.launch {
                _categoryNames.value = splitCategoryNames(categoryList)
                _categories.value = categoryList
            }

            //_profileSettingNext.postValue(Event(result))
        }
    }

    fun callNickNameCheck() {
        GlobalScope.launch {
            ProgressBarUtil._progressBarFlag.postValue(Event(true))

            val result = repository.signUpNickNameDuplicated(nickNameDuplicatedInfo(_userNickName.value!!))
            _nickNameDuplicatedCheck.postValue(Event(!result && _userNickName.value!!.isNotBlank()))

            ProgressBarUtil._progressBarFlag.postValue(Event(false))
        }
    }


    fun onCheckedChange(button: CompoundButton?, check: Boolean) {
        _termsOfServiceFlag.value = Event (check)
    }


    fun getProfileImageList() {
        GlobalScope.launch {
            val asd = repository.profileImageListRepository()
            //val qwe = asd.asJsonObject


            Log.d("_profileImageList.value", "${asd}")
            }
    }


}

