package com.rudder.ui.fragment.signup


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
import com.rudder.util.ChangeUIState
import kotlinx.android.synthetic.main.fragment_create_account.*
import kotlinx.android.synthetic.main.fragment_create_account.view.*


class CreateAccountFragment : Fragment() {

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


        val toastId = Toast.makeText(activity, "ID (4-15 characters) can be numbers, upper or lower letters.", Toast.LENGTH_LONG)
        val toastPassword = Toast.makeText(activity, "Password (8-15 characters) Should be include two of numbers, letters, and special characters.", Toast.LENGTH_LONG)
        val toastPasswordCheck = Toast.makeText(activity, "Please Check, 'Password' and 'Password Confirm'",Toast.LENGTH_LONG)
        val toastEmailDomain = Toast.makeText(activity, "Please Check, Right Email Address", Toast.LENGTH_LONG)
        val toastverifyCodeCheck = Toast.makeText(activity, "Wrong Verification Code", Toast.LENGTH_LONG)


        viewModel.idChangeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                ChangeUIState.changeCheckBoxFalseState(IDcheckbox)
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
            }})


        viewModel.idRgCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    idDuplicatedCheck.isEnabled = true
                    toastId.cancel()
                }
                else{
                    idDuplicatedCheck.isEnabled = false
                    toastId.show()
                }
            }})


        viewModel.idCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    ChangeUIState.changeCheckBoxTrueState(IDcheckbox)
                    idDuplicatedCheck.isEnabled = false
                } else {
                    ChangeUIState.changeCheckBoxFalseState(IDcheckbox)
                    Toast.makeText(activity, "ID is duplicated", Toast.LENGTH_SHORT).show()
                }
            }
            ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
            ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
            ChangeUIState.buttonEnable( createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        })

        viewModel.verifiCodeChangeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
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
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
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
                ChangeUIState.buttonEnable( verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
            } })

        viewModel.emailDomainFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                if(it) {
                    verifyBtn.isEnabled = true
                    toastEmailDomain.cancel()
                }
                else{
                    verifyBtn.isEnabled = false
                    toastEmailDomain.show()
                }
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        }})


        viewModel.emailDomainChangeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                ChangeUIState.changeCheckBoxFalseState(emailCheckbox)
                verifyBtn.isEnabled = true
                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
            }})



        viewModel.emailCheckFlag.observe(viewLifecycleOwner, Observer { // Send verify code
            it.getContentIfNotHandled()?.let { it ->
                val toastEmailCheck = Toast.makeText(activity, viewModel.emailToast.value!!, Toast.LENGTH_SHORT)

                if (it){
                    ChangeUIState.changeCheckBoxTrueState(emailCheckbox)
                    toastEmailCheck.cancel()
                }
                else {
                    ChangeUIState.changeCheckBoxFalseState(emailCheckbox)
                    toastEmailCheck.show()
                }
            }
            ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
            verifyCode.text.clear()

            ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
            ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
        })

        viewModel.verifyCodeCheckFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    ChangeUIState.changeCheckBoxTrueState(veifyCodeCheckbox)
                    createAccountNextBtn.isEnabled = true
                    toastverifyCodeCheck.cancel()
                }
                else {
                    ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
                    createAccountNextBtn.isEnabled = false
                    toastverifyCodeCheck.show()
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

        createAccountBinding.root.idDuplicatedCheck.isEnabled = false
        createAccountBinding.root.verifyBtn.isEnabled = false
        createAccountBinding.root.submitBtn.isEnabled = false
        createAccountBinding.root.createAccountNextBtn.isEnabled = false

        return createAccountBinding.root
    }
}