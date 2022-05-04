package com.rudder.ui.activity

import android.content.Intent
import android.net.Uri
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
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
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


        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData ->

                var deeplink: Uri? = null
                if(pendingDynamicLinkData != null) {
                    deeplink = pendingDynamicLinkData.link
                    Log.d("deep_123","$pendingDynamicLinkData")
                    val tmp = pendingDynamicLinkData.link!!.getQueryParameter("key1")
                    Log.d("deep_456","$tmp")
                    Log.d("deep_456","${Uri.parse(deeplink.toString())}")

                }

                if(deeplink != null) {
//                    val tvDeepLinkContent = findViewById<TextView>(R.id.tv_deeplink_content)
//                    tvDeepLinkContent.text = deeplink.toString()
                    Log.d("deeplink", "${deeplink.toString()}")
                    Log.d("deeplink.path1", "${deeplink.pathSegments}")
                    Log.d("deeplink.path2", "${deeplink.getQueryParameter("key1")}")
                }
                else {
                    Log.d("deeplink_null", "getDynamicLink: no link found")
                }
            }
            .addOnFailureListener(this) { e -> Log.w("deeplink_failure", "getDynamicLink:onFailure", e)
            }



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
                if(notificationType!=-1 && itemId != -1){
                    intent.putExtra("itemId",itemId)
                    intent.putExtra("notificationType",notificationType)
                }
                ContextCompat.startActivity(this, intent, null)
                finish()
            }
        })

        notificationType=intent.getIntExtra("notificationType",-1)
        itemId=intent.getIntExtra("itemId",-1)


        initDeepLink()
        val intent = Intent().getIntExtra("asd",0)
        Log.d("deeplink_789", "$intent")

        autoLogin()


    }


    fun autoLogin(){
        GlobalScope.launch {
            val autoLoginPref = App.prefs.getValue("autoLogin")
            if (autoLoginPref == "true") {
                viewModel.callLogin()
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
    private fun initDeepLink() {
        if (Intent.ACTION_VIEW == intent.action) {

            var uri = intent.data
            //var tmtmp = intent.getData().getQueryParameter(hotelId);
            var tmp = intent.data!!.getQueryParameter("key1")


            if (uri != null) {

                Log.d("deeplink_test123", "${uri}")
                Log.d("deeplink_test456", "${tmp}")


//                binding.tvDeeplinkReceive.text = "딥링크 수신받은 값\n" +
//                        "dl_data1: $dl_data1 \n" +
//                        "dl_data2: $dl_data2"
            }
        }
    }


    override fun onDestroy() {
        ActivityContainer.clearCurrentActivity(this)
        super.onDestroy()
    }
}