package com.example.sportsapp_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        setClicks()

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        if (currentUser != null) {
            openActivity(MainActivity::class.java)
            finish()
        }


    }

    private fun setClicks() {

        bt_create_account.setOnClickListener {
            openActivity(SignUpActivity::class.java)
        }
    }

    fun signInClicked(view: View) {
        val userEmail = tv_username.text.trim().toString()
        val userPassword = tv_password.text.trim().toString()

        auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener() { task ->
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


    private fun openActivity(cls: Class<*>) {
        startActivity(Intent(this, cls))
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerView, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onBackPressed() {
        Log.d("CDA", "onBackPressed Called")
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }


}
