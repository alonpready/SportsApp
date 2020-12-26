package com.example.sportsapp_1.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsapp_1.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setClicks()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        if (currentUser != null) {

            openActivity(MainActivity::class.java)
            finish()
        }


    }

    private fun setClicks() {
        buttonClick()
        bt_create_account.setOnClickListener {
            openActivity(SignUpActivity::class.java)
        }
    }

    private fun buttonClick() {
        bt_login.setOnClickListener {
            val userEmail = tv_username.text.trim().toString()
            val userPassword = tv_password.text.trim().toString()

            if (isEmailOrPasswordValid(userEmail, userPassword)) {

                auth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            openActivity(MainActivity::class.java)
                            finish()
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(
                            applicationContext,
                            exception.localizedMessage.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
            }
        }

    }


    private fun isEmailOrPasswordValid(userEmail: String, userPassword: String): Boolean {
        return if (userEmail.isBlank() || userPassword.isBlank()) {
            Toast.makeText(this, "Kullanıcı adı veya şifre boş olamaz.", Toast.LENGTH_LONG).show()
            false
        } else true
    }

    private fun openActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }


    override fun onBackPressed() {
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }

}
