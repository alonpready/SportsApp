package com.example.sportsapp_1

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import coil.load
import com.example.sportsapp_1.Utill.Gone
import com.example.sportsapp_1.Utill.QrCodeInstantType.*
import com.example.sportsapp_1.Utill.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.fragment_qrcode.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class QrcodeFragment : Fragment() {

    private var user : User? = null
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var db : FirebaseDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrcode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        qrpage_cl.Gone()
        qrPage_progressbar.Visible()
        userInfoLoad()

        setClicks()


    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    private fun userInfoLoad(){
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot.children) {
                    user = singleSnapshot.getValue(User::class.java)

                    minippLoad(user?.userPhotoUrl)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
    private fun minippLoad(photoUrl: String?) {
        if (photoUrl != ""){
            iv_qrcode_homepage_photo.load(photoUrl)
        }
        qrpage_cl.Visible()
        qrPage_progressbar.Gone()

    }

    private fun setClicks() {
        iv_qrcode_homepage_photo.setOnClickListener {
            loadFragment(UserFragment())
        }
        bt_qrcode.setOnClickListener {
            val bitmap = generateQRCode(keyanddateToString())
            iv_qrcode.setImageBitmap(bitmap)

            object : CountDownTimer(60000, 1000) {

                override fun onTick(millisUntilFinished: Long) {

                    tv_time?.text = "BEKLEYİN: " + millisUntilFinished / 1000 + "sn"
                    bt_qrcode.isEnabled = false
                }

                override fun onFinish() {
                    bt_qrcode.isEnabled = true
                    tv_time.text = ""
                }

            }.start()

            val reference = FirebaseDatabase.getInstance().reference
            val query = reference.child("users").child(auth.currentUser?.uid.toString()).child("userInstant")
            val queryCurrentUserVal =  reference.child("gymCurrentUser").child("value")


            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                       var instant = snapshot.getValue(Int::class.java)


                    if (instant == FALSE.value) {
                        query.setValue(TRUE.value)

                        queryCurrentUserVal.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var curValue = snapshot.getValue(Int::class.java)!!
                                curValue = curValue.plus(1)
                                queryCurrentUserVal.setValue(curValue)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                    else {
                        query.setValue(FALSE.value)
                        queryCurrentUserVal.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                var curValue = snapshot.getValue(Int::class.java)!!
                                curValue = curValue.minus(1)
                                queryCurrentUserVal.setValue(curValue)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }
    }



    private fun generateQRCode(text: String): Bitmap {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
        } catch (e: WriterException) {
            Log.d(TAG, "generateQRCode: ${e.message}")
        }
        return bitmap
    }

    private fun keyanddateToString(): String{

        var userKey = (user?.userKey).toString()
        var currentTime: String = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm").format(
            LocalDateTime.now())
        var keyandtimeS = "$currentTime + $userKey"

        return keyandtimeS


    }
}