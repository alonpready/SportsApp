package com.example.sportsapp_1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.signup.*
import kotlinx.android.synthetic.main.signup.tv_password

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val dbReferance = FirebaseDatabase.getInstance().reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser


        val textView = findViewById<TextView>(R.id.tv_backto_login)
        textView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    fun signUpClicked(view: View) {

        val name = tv_name.text.toString()
        val surname = tv_surname.text.toString()
        val mail = tv_mail.text.toString()
        val password = tv_password.text.toString()



        auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadFragment(HomepageFragment())
            }
        }.addOnFailureListener { exception ->
            if (exception != null) {
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        dbReferance.push().key?.let {

            val user = User(mail, it, surname, name, password)
            val task = dbReferance.child(it).setValue(user)
            task.addOnSuccessListener {

            }.addOnFailureListener {

                Log.e("Error Firebase ADD", it.toString())
            }
        }




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

    private fun addValueEventListener() {
        dbReferance.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                setAdapterItems(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("DB", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    private fun setAdapterItems(p0: DataSnapshot){
        val userItems = mutableListOf<User>()
        for (item in p0.children) {
            item?.getValue(User::class.java)?.let { user ->
                user.userKey = item.key!!
                userItems.add(user)
            }
        }
    }


}