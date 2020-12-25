package com.example.sportsapp_1.Fragments.ToolbarMenuPage


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.set
import androidx.fragment.app.Fragment
import coil.load
import com.example.sportsapp_1.Fragments.UserFragment
import com.example.sportsapp_1.Model.ReservationInfo
import com.example.sportsapp_1.Model.UserBodydInfo
import com.example.sportsapp_1.Model.UserValues
import com.example.sportsapp_1.R
import com.example.sportsapp_1.Utill.Gone
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.inside_fragment_connection.iv_Connection_back_button
import kotlinx.android.synthetic.main.inside_fragment_edit_profile.*
import org.w3c.dom.Text
import java.util.*

class EditProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    var selectedPicture: Uri? = null
    var storaged = FirebaseStorage.getInstance()
    private lateinit var db: FirebaseDatabase
    var userValues: UserValues? = null

    lateinit var userName: TextView
    lateinit var userLastName: TextView
    lateinit var userWeight: TextView
    lateinit var userHeight: TextView
    lateinit var userMassIndex: TextView
    lateinit var userMassIndex2: TextView



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
        getUserChanges()
    }

    private fun userInfoLoad(){


        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    setUser(
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
        bt_changeprofileinfos.setOnClickListener(){

        }
    }


    private fun getUserChanges(){
        userName = view!!.findViewById(R.id.tv_editName)
        userLastName = view!!.findViewById(R.id.tv_editLastName)
        userWeight = view!!.findViewById(R.id.tv_editWeight)
        userHeight = view!!.findViewById(R.id.tv_editHeight)
        userMassIndex = view!!.findViewById(R.id.tv_calculatedMassIndex)
        userMassIndex2 = view!!.findViewById(R.id.tv_calculatedMassIndex2)

        userMassIndex.text =""
        userMassIndex2.text =""
        userMassIndex.Gone()

        var userBodyInfo: UserBodydInfo ?= null

        bt_calculate.setOnClickListener(){
            var weight: Float = userWeight.text.toString().toFloat()
            var height: Float = userHeight.text.toString().toFloat()
            var massIndex: Float = weight/((height/100)*(height/100))
            userMassIndex2.text = String.format("%.2f",massIndex)
            userMassIndex.text = massIndex.toString()
        }

        bt_changeprofileinfos.setOnClickListener(){

            var reference = FirebaseDatabase.getInstance().reference
            var query = reference.child("users").child(auth.currentUser!!.uid)
                .child("userBodyInfo")
            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userBodyInfo = snapshot.getValue(UserBodydInfo::class.java)

                    var changeUserBodyInfo = UserBodydInfo(
                        userBodyInfo!!.userWeight,
                        userBodyInfo!!.userHeight,
                        userBodyInfo!!.userMassIndex
                    )
                    changeUserBodyInfo.userWeight = userWeight.text.toString().toFloat()
                    changeUserBodyInfo.userHeight = userHeight.text.toString().toFloat()
                    changeUserBodyInfo.userMassIndex = userMassIndex.text.toString().toFloat()

                    query.setValue(changeUserBodyInfo)

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            var query2 = reference.child("users").child(auth.currentUser!!.uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userValues = snapshot.getValue(UserValues::class.java)

                    var changeUserInfo = UserValues(
                        userValues!!.userName,
                        userValues!!.userLastName
                    )
                    changeUserInfo.userName = userName.text.toString()
                    changeUserInfo.userLastName = userLastName.text.toString()


                    query2.child("userName").setValue(changeUserInfo.userName)
                    query2.child("userLastName").setValue(changeUserInfo.userLastName)

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            loadFragment(UserFragment())
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

    private fun setUser(photoUrl: String?) {

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