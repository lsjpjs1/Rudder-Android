package com.rudder.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rudder.R
import com.rudder.data.local.App
import com.rudder.databinding.ActivitySplashBinding
import com.rudder.util.ActivityContainer
import com.rudder.util.ForecdTerminationService
import com.rudder.util.StartActivityUtil
import com.rudder.viewModel.LoginViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java)  }
    private var notificationType:Int = -1
    private var itemId:Int = -1

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        ActivityContainer.currentActivity = this
        super.onCreate(savedInstanceState)

        startService(Intent(this, ForecdTerminationService::class.java))
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        binding.loginVM = viewModel
        binding.lifecycleOwner = this

//        Firebase.dynamicLinks
//            .getDynamicLink(intent)
//            .addOnSuccessListener(this) { pendingDynamicLinkData ->
//
//                var deeplink: Uri? = null
//                if(pendingDynamicLinkData != null) {
//                    deeplink = pendingDynamicLinkData.link
//                    Log.d("deep_123","$pendingDynamicLinkData")
//                    val tmp = pendingDynamicLinkData.link!!.getQueryParameter("key1")
//                    Log.d("deep_456","$tmp")
//                    Log.d("deep_456","${Uri.parse(deeplink.toString())}")
//                }
//                if(deeplink != null) {
////                    val tvDeepLinkContent = findViewById<TextView>(R.id.tv_deeplink_content)
////                    tvDeepLinkContent.text = deeplink.toString()
////                    Log.d("deeplink", "${deeplink.toString()}")
////                    Log.d("deeplink.path1", "${deeplink.pathSegments}")
////                    Log.d("deeplink.path2", "${deeplink.getQueryParameter("key1")}")
//                }
//                else {
//                    Log.d("deeplink_null", "getDynamicLink: no link found")
//                }
//            }
//            .addOnFailureListener(this) { e -> Log.w("deeplink_failure", "getDynamicLink:onFailure", e)
//            }


        val deeplinkParameter = initDeepLink()



        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        viewModel.showLoginErrorToast.observe(this, Observer {
            it.getContentIfNotHandled()?.let { it ->
                if (it) {
                    Toast.makeText(this, R.string.auto_login_error, Toast.LENGTH_LONG).show()
                    StartActivityUtil.callActivity(this, LoginActivity())
                    finish()
                }
            }
        })

        viewModel.startMainActivity.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                val intent = Intent(this, MainActivity()::class.java)

                Log.d("deeplink_parameter", "${deeplinkParameter}")
                if (deeplinkParameter != null) {
                    val bundle = Bundle()
                    bundle.putInt("jobId", deeplinkParameter.toInt())
                    intent.putExtras(bundle)

                } else {
                    if(notificationType!=-1 && itemId != -1){
                        intent.putExtra("itemId",itemId)
                        intent.putExtra("notificationType",notificationType)
                    }
                }
                ContextCompat.startActivity(this, intent, null)
                finish()
            }
        })

        notificationType=intent.getIntExtra("notificationType",-1)
        itemId=intent.getIntExtra("itemId",-1)


        autoLogin()


    }


    fun autoLogin(){
        GlobalScope.launch {
            val autoLoginPref = App.prefs.getValue("autoLogin")
            if (autoLoginPref == "true") {
                viewModel.callLogin()

                //viewModel.checkToken()

            } else {
                val mHandler = Handler(Looper.getMainLooper())
                mHandler.postDelayed({
                    StartActivityUtil.callActivity(this@SplashActivity, LoginActivity())
                    finish()
                }, 1000)
            }
        }
    }


    /** DeepLink */
    private fun initDeepLink() : String? {
        var shareParameter : String? = null
        if (Intent.ACTION_VIEW == intent.action) {
            val uri = intent.data
            shareParameter = intent.data!!.getQueryParameter("jobId")
            if (uri != null) {
                Log.d("deeplink_uri", "${uri}")
                Log.d("deeplink_parameter", "${shareParameter}")
            }
        }
        return shareParameter
    }


    override fun onDestroy() {
        ActivityContainer.clearCurrentActivity(this)
        super.onDestroy()
    }
}