package com.example.sportsapp_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.signup.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        auth = FirebaseAuth.getInstance()

        val textView = findViewById<TextView>(R.id.tv_backto_login)
        textView.setOnClickListener {
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
    }


    fun signUpClicked(view: View){

        var mail = tv_mail.text.toString()
        var password = tv_password.text.toString()

        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->

            if(task.isSuccessful){
                val intent = Intent(applicationContext, HomepageFragment::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { exception ->
            if(exception != null){
                Toast.makeText(applicationContext,exception.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

}