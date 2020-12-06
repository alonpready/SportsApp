package com.example.sportsapp_1

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.solver.widgets.Snapshot
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import kotlinx.android.synthetic.main.fragment_user.*
import kotlin.math.sign


class UserFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var userPageTextView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()



        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Log.d("CDA", "onBackPressed Called")
                val setIntent = Intent(Intent.ACTION_MAIN)
                setIntent.addCategory(Intent.CATEGORY_HOME)
                setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(setIntent)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bt_logOut.setOnClickListener() {
            signOut()
        }
        userPageTextView = view.findViewById(R.id.tv_userPage_username)
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(singleSnapshot in snapshot!!.children) {
                    val user = singleSnapshot.getValue(User::class.java)
                    userPageTextView.text = user?.userName
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(getActivity(), LoginActivity::class.java)
        activity?.startActivity(intent)
        val manager: FragmentManager = activity!!.supportFragmentManager
        val trans: FragmentTransaction = manager.beginTransaction()
        trans.remove(this)
        trans.commit()
        manager.popBackStack()
    }



}