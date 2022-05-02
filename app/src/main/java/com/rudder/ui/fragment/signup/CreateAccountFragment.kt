package com.rudder.ui.fragment.signup


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentCreateAccountBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
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


        val toastId = Toast.makeText(activity, "ID (4-15 characters) can contain upper/lower letters and numbers.", Toast.LENGTH_SHORT)
        val toastPassword = Toast.makeText(activity, "Password (8-15 characters) should be a combination of letters, numbers, or symbols.", Toast.LENGTH_SHORT)
        val toastPasswordCheck = Toast.makeText(activity, "Incorrect password or confirm password.",Toast.LENGTH_LONG)
        val toastEmailDomain = Toast.makeText(activity, "Please enter your valid university email.", Toast.LENGTH_LONG)
        val toastverifyCodeCheckFail = Toast.makeText(activity, "Wrong verification code", Toast.LENGTH_LONG)
        val toastverifyCodeCheckSuccess = Toast.makeText(activity, "Correct verification code! ", Toast.LENGTH_LONG)


        viewModel.idChangeFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                ChangeUIState.changeCheckBoxFalseState(IDcheckbox)
                //ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                //ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                //ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked)

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
                    Toast.makeText(activity, "This ID already exists. Please try other ID.", Toast.LENGTH_SHORT).show()
                }
            }
            //ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
            //ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
            //ChangeUIState.buttonEnable( createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
            ChangeUIState.buttonEnable( createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked)
        })

//        viewModel.verifiCodeChangeFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let{
//                ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
//                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
//            }})

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
                //ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
                //ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
                //ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked)

            }})

//        viewModel.passwordCheckFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let{ it ->
//                if(it) {
//                    ChangeUIState.changeCheckBoxTrueState(PWcheckbox2)
//                    toastPasswordCheck.cancel()
//                }
//                else{
//                    ChangeUIState.changeCheckBoxFalseState(PWcheckbox2)
//                    if (viewModel.userPassword.value.toString().length == viewModel.userPasswordCheck.value.toString().length)
//                        toastPasswordCheck.show()
//                }
//                ChangeUIState.buttonEnable( verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
//                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
//                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
//            } })
//
//        viewModel.emailDomainFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let{ it ->
//                if(it) {
//                    verifyBtn.isEnabled = true
//                    toastEmailDomain.cancel()
//                }
//                else{
//                    verifyBtn.isEnabled = false
//                    toastEmailDomain.show()
//                }
//                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
//                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
//                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
//        }})
//
//
//        viewModel.emailDomainChangeFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let{
//                ChangeUIState.changeCheckBoxFalseState(emailCheckbox)
//                verifyBtn.isEnabled = true
//                ChangeUIState.buttonEnable(verifyBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked)
//                ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
//                ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
//            }})
//
//
//
//        viewModel.emailCheckFlag.observe(viewLifecycleOwner, Observer { // Send verify code
//            it.getContentIfNotHandled()?.let { it ->
//
//                if (it){
//                    ChangeUIState.changeCheckBoxTrueState(emailCheckbox)
//                    val toastEmailCheckSuccess = Toast.makeText(activity, viewModel.emailToast.value!!, Toast.LENGTH_SHORT)
//                    toastEmailCheckSuccess.show()
//                }
//                else {
//                    val toastEmailCheckFail = Toast.makeText(activity, viewModel.emailToast.value!!, Toast.LENGTH_SHORT)
//                    ChangeUIState.changeCheckBoxFalseState(emailCheckbox)
//                    toastEmailCheckFail.show()
//                }
//            }
//            ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
//            verifyCode.text.clear()
//
//            ChangeUIState.buttonEnable(submitBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked)
//            ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
//        })
//
//        viewModel.verifyCodeCheckFlag.observe(viewLifecycleOwner, Observer {
//            it.getContentIfNotHandled()?.let { it ->
//                if (it) {
//                    ChangeUIState.changeCheckBoxTrueState(veifyCodeCheckbox)
//                    createAccountNextBtn.isEnabled = true
//                    toastverifyCodeCheckSuccess.show()
//                }
//                else {
//                    ChangeUIState.changeCheckBoxFalseState(veifyCodeCheckbox)
//                    createAccountNextBtn.isEnabled = false
//                    toastverifyCodeCheckFail.show()
//                }
//            }
//            ChangeUIState.buttonEnable(createAccountNextBtn, IDcheckbox.isChecked, PWcheckbox1.isChecked, PWcheckbox2.isChecked, emailCheckbox.isChecked, veifyCodeCheckbox.isChecked)
//        })

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