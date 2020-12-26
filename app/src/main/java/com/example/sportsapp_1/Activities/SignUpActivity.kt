

package com.example.sportsapp_1.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsapp_1.Model.UserBodyInfo
import com.example.sportsapp_1.Model.UserValues
import com.example.sportsapp_1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        val textView = findViewById<TextView>(R.id.tv_backto_login)
        textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
        bt_signup.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {

        val name = tv_name.text.toString()
        val surname = tv_surname.text.toString()
        val mail = tv_mail.text.toString()
        val password = tv_password.text.toString()




        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                var reference = FirebaseDatabase.getInstance().reference
                val user = UserValues(mail, auth.currentUser?.uid, surname, name, password)
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(auth.currentUser?.uid ?: "").setValue(user)
                FirebaseDatabase.getInstance().reference.child("users")
                    .child(auth.currentUser?.uid ?: "").child("UserResId")
                    .child("defaultId").setValue("defaultId")

                val userBodyInfo = UserBodyInfo()

                var query =  reference.child("users").child(auth.currentUser!!.uid)
                    .child("userBodyInfo").setValue(userBodyInfo)
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(
                applicationContext,
                exception.localizedMessage?.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onBackPressed() {
        Log.d("CDA", "onBackPressed Called")
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }


}