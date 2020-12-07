package com.example.sportsapp_1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_user.*
import java.util.*
import kotlin.collections.HashMap


class UserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userPageTextView: TextView
    var selectedPicture: Uri? = null
    var storaged = FirebaseStorage.getInstance()
    private lateinit var db: FirebaseDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        auth = FirebaseAuth.getInstance()

        db = FirebaseDatabase.getInstance()
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
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
        setClicks()

        userPageTextView = view.findViewById(R.id.tv_userPage_username)
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
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

    private fun setClicks() {
        iv_profile_photo3.setOnClickListener() {
            openGallery()
        }
        bt_logOut.setOnClickListener() {
            signOut()
        }
    }

    private fun openGallery() {
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            val intent =
                Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 2)
        }




    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent =
                    Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPicture = data.data

            try {
                if(selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {
                        val source =
                            ImageDecoder.createSource(activity!!.contentResolver, selectedPicture!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        iv_profile_photo3.setImageBitmap(bitmap)
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPicture
                        )
                        iv_profile_photo3.setImageBitmap(bitmap)
                    }

                    val uuid = UUID.randomUUID()
                    val ppname = "$uuid.jpg"

                    val reference = storaged.reference
                    val ppreference = reference.child("profilephotos").child(ppname)



                    ppreference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->

                        val uploadPPReferance = FirebaseStorage.getInstance().reference.child("profilephotos").child(ppname)
                        uploadPPReferance.downloadUrl.addOnSuccessListener { uri ->

                            val downloadUrl = uri.toString()

                            db.reference.child("users")
                                .child(auth.currentUser?.uid ?: "").child("userPhotoUrl").setValue(downloadUrl)


                        }
                        }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }


}



