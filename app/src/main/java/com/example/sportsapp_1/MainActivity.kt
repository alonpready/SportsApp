package com.example.sportsapp_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomepageFragment())
        setBottomNavigation()
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

                R.id.userpage ->
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

}