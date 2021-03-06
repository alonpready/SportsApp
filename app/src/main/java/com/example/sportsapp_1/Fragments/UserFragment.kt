package com.example.sportsapp_1.Fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import coil.load
import com.example.sportsapp_1.Model.UserValues
import com.example.sportsapp_1.Fragments.ToolbarMenuPage.ConnectionFragment
import com.example.sportsapp_1.Fragments.ToolbarMenuPage.EditProfileFragment
import com.example.sportsapp_1.Fragments.ToolbarMenuPage.SettingsFragment
import com.example.sportsapp_1.Activities.LoginActivity
import com.example.sportsapp_1.Fragments.ToolbarMenuPage.AccountInfoFragment
import com.example.sportsapp_1.Model.UserBodyInfo
import com.example.sportsapp_1.R
import com.example.sportsapp_1.Utill.Gone
import com.example.sportsapp_1.Utill.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_user.*
import java.util.*


class UserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userPageTextView: TextView
    var selectedPicture: Uri? = null
    var storaged = FirebaseStorage.getInstance()
    private lateinit var db: FirebaseDatabase
    private lateinit var circularProgressXML: View
    private var userValues: UserValues? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


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

        user_cl.Gone()
        userPage_progressbar.Visible()
        (activity as AppCompatActivity?)!!.setSupportActionBar(editprofile_toolbar)
        setClicks()
        circularProgressBar()
        userInfoLoad()

    }

    private fun userInfoLoad(){

        userPageTextView = requireView().findViewById(R.id.tv_userPage_username)
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    setUser(
                        userValues?.userName,
                        userValues?.userPhotoUrl
                    )


                }
            }


            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


    private fun signOut() {
        auth.signOut()
        val intent = Intent(activity, LoginActivity::class.java)
        activity?.startActivity(intent)
        val manager: FragmentManager = requireActivity().supportFragmentManager
        val trans: FragmentTransaction = manager.beginTransaction()
        trans.remove(this)
        trans.commit()
    }

    private fun setClicks() {
        iv_profile_photo3.setOnClickListener() {
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
                        iv_profile_photo3.setImageBitmap(bitmap)
                    } else {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            activity?.contentResolver,
                            selectedPicture
                        )
                        iv_profile_photo3.setImageBitmap(bitmap)
                    }

                    val uuid = UUID.randomUUID()
                    val PPname = "$uuid.jpg"

                    val reference = storaged.reference
                    val PPreference = reference.child("profilephotos").child(PPname)



                    PPreference.putFile(selectedPicture!!).addOnSuccessListener { taskSnapshot ->

                        val uploadPPReference =
                            FirebaseStorage.getInstance().reference.child("profilephotos")
                                .child(PPname)
                        uploadPPReference.downloadUrl.addOnSuccessListener { uri ->

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

    private fun setUser(name: String?, photoUrl: String?) {
        userPageTextView.text = name ?: ""
        if (photoUrl != "") {
            iv_profile_photo3.load(photoUrl)
        }
        userPage_progressbar.Gone()
        user_cl.Visible()

    }


    private fun circularProgressBar() {
        var userBodyInfo: UserBodyInfo?
        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("users").child(auth.currentUser!!.uid)
            .child("userBodyInfo")
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userBodyInfo = snapshot.getValue(UserBodyInfo::class.java)

                val changeUserBodyInfo1 = UserBodyInfo(
                    userBodyInfo!!.userWeight,
                    userBodyInfo!!.userHeight,
                    userBodyInfo!!.userMassIndex
                )

                circularProgressXML = v_userpage_CircularProgressBar_1
                circularProgressXML = requireView().findViewById(R.id.v_userpage_CircularProgressBar_1)

                v_userpage_CircularProgressBar_1.apply {

                    setProgressWithAnimation(changeUserBodyInfo1.userWeight, 1900)
                    progressMax = 150f
                    progressBarColorStart = Color.parseColor("#84AC28")
                    progressBarColorEnd = Color.parseColor("#84AC28")
                    progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorDirection =
                        CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    progressBarWidth = 7f // in DP
                    backgroundProgressBarWidth = 7f // in DP
                    roundBorder = true
                    startAngle = 0f
                    progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                }
                tv_in_graph1.text = "${changeUserBodyInfo1.userWeight}\n kg"

                v_userpage_CircularProgressBar_2.apply {

                    setProgressWithAnimation(changeUserBodyInfo1.userHeight, 1900)
                    progressMax = 200f
                    progressBarColorStart = Color.parseColor("#84AC28")
                    progressBarColorEnd = Color.parseColor("#84AC28")
                    progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorDirection =
                        CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    progressBarWidth = 7f // in DP
                    backgroundProgressBarWidth = 7f // in DP
                    roundBorder = true
                    startAngle = 0f
                    progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                }
                tv_in_graph2.text = "${changeUserBodyInfo1.userHeight}\n cm"
                v_userpage_CircularProgressBar_3.apply {

                    setProgressWithAnimation(changeUserBodyInfo1.userMassIndex, 1900)
                    progressMax = 50f
                    progressBarColorStart = Color.parseColor("#84AC28")
                    progressBarColorEnd = Color.parseColor("#84AC28")
                    progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    backgroundProgressBarColor = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorStart = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorEnd = Color.parseColor("#45FFFFFF")
                    backgroundProgressBarColorDirection =
                        CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
                    progressBarWidth = 7f // in DP
                    backgroundProgressBarWidth = 7f // in DP
                    roundBorder = true
                    startAngle = 0f
                    progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
                }
                tv_in_graph3.text = String.format("%.2f \n kg/m2",changeUserBodyInfo1.userMassIndex)

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })





    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.userpage_top_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_editprofile -> {
                loadFragment(EditProfileFragment())
            }
            R.id.menu_account_info -> {
                loadFragment(AccountInfoFragment())
            }
            R.id.menu_connection -> {
                loadFragment(ConnectionFragment())
            }
            R.id.menu_settings -> {
                loadFragment(SettingsFragment())
            }
            R.id.menu_signout -> {
                signOutAlertDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun signOutAlertDialog() {

        val alert = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        alert
            .setPositiveButton("Evet") {_,_-> signOut()}
            .setNegativeButton("Hayır") {_,_->}
            .setTitle("Uyarı")
            .setMessage("Çıkış yapmak istediğinize emin misiniz?")
            .create()
            .show()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}