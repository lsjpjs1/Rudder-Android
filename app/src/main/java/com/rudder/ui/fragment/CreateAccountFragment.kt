package com.rudder.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentCreateAccountBinding
import com.rudder.viewModel.SignUpViewModel
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.databinding.ActivitySignUpBinding
import com.rudder.util.ChangeUIState
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*


class CreateAccountFragment : Fragment() {

    //private val viewModel: SignUpViewModel by lazy { ViewModelProvider(this).get(SignUpViewModel().getInstance()::class.java) }
    private val viewModel: SignUpViewModel by activityViewModels()

    private lateinit var createAccountBinding : FragmentCreateAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        createAccountBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_account,container,false)
        createAccountBinding.signUpVM = viewModel
        createAccountBinding.lifecycleOwner = this

        val toastPassword = Toast.makeText(activity, "비밀번호는 숫자,문자,특수문자 중 2가지 포함(8~15자)", Toast.LENGTH_SHORT)
        val toastPasswordCheck = Toast.makeText(activity, "Please Check, Password and Password Confirm",Toast.LENGTH_SHORT)
        val toastEmailDomain = Toast.makeText(activity, "Please Check, Right Email Address", Toast.LENGTH_SHORT)
        val toastEmailCheck = Toast.makeText(activity, "Email must be Naver Email", Toast.LENGTH_SHORT)
        val verifyCodeCheck = Toast.makeText(activity, "Wrong Verification Code", Toast.LENGTH_SHORT)


        viewModel.idChangeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                ChangeUIState.changeCheckBoxFalseState(IDcheckbox)
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
            }})

        viewModel.passwordFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(PWcheckbox1)
                    toastPassword.cancel()
                }
                else{
                    ChangeUIState.changeCheckBoxFalseState(PWcheckbox1)
                    toastPassword.show()
                }
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        }})

        viewModel.passwordCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(PWcheckbox2)
                    toastPasswordCheck.cancel()
                }
                else{
                    ChangeUIState.changeCheckBoxFalseState(PWcheckbox2)
                    if (viewModel.userPassword.value.toString().length == viewModel.userPasswordCheck.value.toString().length)
                        toastPasswordCheck.show()
                }
                ChangeUIState.buttonEnable( verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
            } })

        viewModel.emailDomainFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    ChangeUIState.changeCheckBoxTrueState(emailCheckbox)
                    toastEmailDomain.cancel()
                }
                else{
                    ChangeUIState.changeCheckBoxFalseState(emailCheckbox)
                    toastEmailDomain.show()
                }
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        }})

        viewModel.idCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) ChangeUIState.changeCheckBoxTrueState(IDcheckbox)
                else {
                    ChangeUIState.changeCheckBoxFalseState(IDcheckbox)
                    Toast.makeText(activity, "ID is duplicated", Toast.LENGTH_SHORT).show()
                }
            }
            ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
            ChangeUIState.buttonEnable( createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        })

        viewModel.emailCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it){
                    submitBtn.isEnabled = true
                    toastEmailCheck.cancel()
                }
                else {
                    submitBtn.isEnabled = false
                    toastEmailCheck.show()
                }
            }
            ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
            verifyCode.text.clear()
            ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
            ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        })

        viewModel.verifyCodeCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    ChangeUIState.changeCheckBoxTrueState(veifyCodeCheckbox)
                    createAccountNextBtn.isEnabled = true
                    verifyCodeCheck.cancel()
                }
                else {
                    ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
                    createAccountNextBtn.isEnabled = false
                    verifyCodeCheck.show()
                }
            }
            ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        })

        createAccountBinding.root.IDcheckbox.isEnabled = false
        createAccountBinding.root.PWcheckbox1.isEnabled = false
        createAccountBinding.root.PWcheckbox2.isEnabled = false
        createAccountBinding.root.emailCheckbox.isEnabled = false
        createAccountBinding.root.Recommendcheckbox.isChecked = true
        createAccountBinding.root.Recommendcheckbox.isEnabled = false
        createAccountBinding.root.veifyCodeCheckbox.isEnabled = false

        createAccountBinding.root.verifyBtn.isEnabled = false
        createAccountBinding.root.submitBtn.isEnabled = false
        createAccountBinding.root.createAccountNextBtn.isEnabled = false

        return createAccountBinding.root
    }
}