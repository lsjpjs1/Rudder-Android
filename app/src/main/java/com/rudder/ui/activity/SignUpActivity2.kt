package com.rudder.ui.activity

import android.icu.util.Currency
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rudder.R
import com.rudder.ui.fragment.CreateAccountFragment

class SignUpActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(R.id.signUp_container, CreateAccountFragment())

        transaction.commit()
    }
}