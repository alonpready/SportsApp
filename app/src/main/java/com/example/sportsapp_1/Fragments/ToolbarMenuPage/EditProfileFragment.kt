package com.example.sportsapp_1.Fragments.ToolbarMenuPage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import coil.load
import com.example.sportsapp_1.Activities.LoginActivity
import com.example.sportsapp_1.Fragments.UserFragment
import com.example.sportsapp_1.Model.UserValues
import com.example.sportsapp_1.R
import com.example.sportsapp_1.Utill.Gone
import com.example.sportsapp_1.Utill.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.editprofile_toolbar
import kotlinx.android.synthetic.main.inside_fragment_connection.*
import kotlinx.android.synthetic.main.inside_fragment_connection.iv_Connection_back_button
import kotlinx.android.synthetic.main.inside_fragment_edit_profile.*
import java.util.*
import kotlin.properties.Delegates

class EditProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userPageTextView: TextView
    var selectedPicture: Uri? = null
    var storaged = FirebaseStorage.getInstance()
    private lateinit var db: FirebaseDatabase
    private var userValues: UserValues? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
        return inflater.inflate(R.layout.inside_fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_Connection_back_button.setOnClickListener{
            loadFragment(UserFragment())
        }

        userInfoLoad()
        setClicks()
    }

    private fun userInfoLoad(){

        userPageTextView = requireView().findViewById(R.id.tv_Settings_header)
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    setUser(
                        userValues?.userName?.capitalize(Locale.getDefault()),
                        userValues?.userLastName?.capitalize(Locale.getDefault()),
                        userValues?.userPhotoUrl
                    )


                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun setClicks() {
        iv_Account_profile_photo.setOnClickListener() {
            openGallery()
        }
        textView2.setOnClickListener() {
            openGallery()
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
                requireActivity(),
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
                if (selectedPicture != null) {

                    if (Build.VERSION.SDK_INT >= 28) {
                        val source =
                            ImageDecoder.createSource(
                                requireActivity().contentResolver,
                                selectedPicture!!
                            )
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        iv_Account_profile_photo.setImageBitmap(bitmap)
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPicture
                        )
                        iv_Account_profile_photo.setImageBitmap(bitmap)
                    }

                    val uuid = UUID.randomUUID()
                    val ppname = "$uuid.jpg"

                    val reference = storaged.reference
                    val ppreference = reference.child("profilephotos").child(ppname)



                    ppreference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->

                        val uploadPPReferance =
                            FirebaseStorage.getInstance().reference.child("profilephotos")
                                .child(ppname)
                        uploadPPReferance.downloadUrl.addOnSuccessListener { uri ->

                            val downloadUrl = uri.toString()

                            db.reference.child("users")
                                .child(auth.currentUser?.uid ?: "").child("userPhotoUrl")
                                .setValue(downloadUrl)


                        }
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setUser(name: String?, surname: String?, photoUrl: String?) {
        userPageTextView.text = "$name $surname"
        if (photoUrl != "") {
            iv_Account_profile_photo.load(photoUrl)
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

}