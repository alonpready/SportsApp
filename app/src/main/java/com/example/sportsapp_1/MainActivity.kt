package com.example.sportsapp_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.signup.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBottomNavigation()
        loadFragment(HomepageFragment())

    }

    private fun setBottomNavigation() {


        bottom_navigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.homepage ->
                { loadFragment(HomepageFragment()) }

                R.id.rezervationpage ->
                { loadFragment(RezervationFragment()) }

                R.id.qrcodepage ->
                { loadFragment(QrcodeFragment()) }

                R.id.videospage ->
                { loadFragment(VideosFragment()) }

                R.id.bt_logOut ->
                { loadFragment(UserFragment()) }

            }

            true
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






}