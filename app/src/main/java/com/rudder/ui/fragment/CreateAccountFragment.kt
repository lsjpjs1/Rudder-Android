package com.rudder.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rudder.R
import com.rudder.databinding.FragmentCommunityHeaderBinding
import com.rudder.databinding.FragmentCreateAccountBinding
import com.rudder.databinding.FragmentMainBottomBarBinding
import com.rudder.viewModel.SignUpViewModel


class CreateAccountFragment : Fragment() {

    private val viewModel = SignUpViewModel
    private lateinit var createAccount : FragmentCreateAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        createAccount = DataBindingUtil.inflate<FragmentCreateAccountBinding>(inflater,R.layout.fragment_create_account,container,false)
        createAccount.signUpVM = viewModel

        return createAccount.root
    }
}