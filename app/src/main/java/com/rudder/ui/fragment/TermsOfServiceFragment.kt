package com.rudder.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.rudder.R
import com.rudder.databinding.FragmentTermsOfServiceBinding
import com.rudder.util.ChangeUIState
import com.rudder.viewModel.SignUpViewModel
import kotlinx.android.synthetic.main.fragment_school_select.*
import kotlinx.android.synthetic.main.fragment_school_select.view.*
import kotlinx.android.synthetic.main.fragment_terms_of_service.*
import kotlinx.android.synthetic.main.fragment_terms_of_service.view.*


class TermsOfServiceFragment : Fragment() {

    private val viewModel: SignUpViewModel by activityViewModels()
    private lateinit var termsOfServiceBinding : FragmentTermsOfServiceBinding
    private val termsOfServiceURL = "https://sites.google.com/view/mateprivacyterms"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        termsOfServiceBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_terms_of_service,container,false)
        termsOfServiceBinding.signUpVM = viewModel
        termsOfServiceBinding.lifecycleOwner = this

        termsOfServiceBinding.root.termsOfServiceWebView.apply {
            webViewClient = WebViewClient()
            settings.builtInZoomControls = true
            settings.javaScriptEnabled = true
            settings.cacheMode = WebSettings.LOAD_DEFAULT
        }

        termsOfServiceBinding.root.termsOfServiceWebView.loadUrl(termsOfServiceURL)

        viewModel.termsOfServiceFlag.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{ it ->
                ChangeUIState.buttonEnable(termsOfServiceNextBtn,it)
            }})



        termsOfServiceBinding.root.termsOfServiceNextBtn.isEnabled = false
        return termsOfServiceBinding.root
    }
}