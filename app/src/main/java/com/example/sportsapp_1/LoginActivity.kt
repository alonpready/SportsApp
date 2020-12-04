package com.example.sportsapp_1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        setClicks()

    }

    private fun setClicks() {
        tv_skip.setOnClickListener {
            openActivity(MainActivity::class.java)
        }
        bt_create_account.setOnClickListener {
            openActivity(SignUpActivity::class.java)
        }
    }


    private fun openActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }
}
