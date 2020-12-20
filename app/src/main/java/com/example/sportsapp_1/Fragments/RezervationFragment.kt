package com.example.sportsapp_1.Fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sportsapp_1.Adapters.Reservation_RVAdapter
import com.example.sportsapp_1.Model.ReservationInfo
import com.example.sportsapp_1.Model.UserValues
import com.example.sportsapp_1.R
import com.example.sportsapp_1.Utill.Gone
import com.example.sportsapp_1.Utill.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_rezervation.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class RezervationFragment : Fragment() {

    private var reservationList = ArrayList<ReservationInfo>()
    private var userValues: UserValues? = null
    lateinit var btnGoster:Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val addCallback = activity?.onBackPressedDispatcher?.addCallback(this, object:
            OnBackPressedCallback(true) { override fun handleOnBackPressed() {
                    Log.d("CDA", "onBackPressed Called")
                    val setIntent = Intent(Intent.ACTION_MAIN)
                    setIntent.addCategory(Intent.CATEGORY_HOME)
                    setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(setIntent)
                } })





    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rezervation, container, false)

    }

    private fun showMyCustomAlertDialog() {

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.custom_dialog_reservation)

        val btSave = dialog.findViewById(R.id.bt_Dialog_save) as Button
        val btCancel = dialog.findViewById(R.id.bt_Dialog_cancel) as Button
        val tvText = dialog.findViewById(R.id.tv_Dialog_text) as TextView


        btSave.setOnClickListener {
            Toast.makeText(requireContext(), "Rezervasyonunuz kaydedildi.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()}

        btCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClicks()
        rezpage_cl.Gone()
        rezPage_progressbar.Visible()
        userInfoLoad()
        initializeRv()
        iv_rezervation_profile_photo.setOnClickListener{
            loadFragment(UserFragment())
        }
    }

    private fun initializeRv() {
        takenewList()
        reservation_rv.layoutManager = LinearLayoutManager(activity)
        reservation_rv.adapter = Reservation_RVAdapter(requireContext(), reservationList) {
            showMyCustomAlertDialog()
        }

    }

    private fun takenewList() {

        val t0 = ReservationInfo("07:30 - 09:00", 4, 10)
        val t1 = ReservationInfo("09:00 - 10:30", 5, 12)
        val t2 = ReservationInfo("10:30 - 12:00", 3, 13)
        val t3 = ReservationInfo("12:00 - 13:30", 1, 9)
        val t4 = ReservationInfo("13:30 - 15:00", 9, 10)
        val t5 = ReservationInfo("15:00 - 16:30", 8, 11)
        val t6 = ReservationInfo("16:30 - 18:00", 4, 9)
        val t7 = ReservationInfo("18:00 - 19:30", 3, 8)
        val t8 = ReservationInfo("19:30 - 21:00", 9, 11)
        val t9 = ReservationInfo("21:00 - 22:30", 10, 13)
        val t10 = ReservationInfo("22:30 - 00:00", 2, 8)

        reservationList.add(t0)
        reservationList.add(t1)
        reservationList.add(t2)
        reservationList.add(t3)
        reservationList.add(t4)
        reservationList.add(t5)
        reservationList.add(t6)
        reservationList.add(t7)
        reservationList.add(t8)
        reservationList.add(t9)
        reservationList.add(t10)
    }


    private fun setClicks() {
        bt_select_date.setOnClickListener { pickDateTime() }
    }

    private fun pickDateTime() {
        val currentDateTime = Calendar.getInstance()
        val startYear = currentDateTime.get(Calendar.YEAR)
        val startMonth = currentDateTime.get(Calendar.MONTH)
        val startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val pickedDateTime = Calendar.getInstance()
                pickedDateTime.set(year, month, day)
                doSomethingWith(pickedDateTime)
                Toast.makeText(requireContext(),"Seçilen tarihe göre doluluk oranları",Toast.LENGTH_SHORT).show()
            },
            startYear,
            startMonth,
            startDay
        ).show()

    }

    private fun doSomethingWith(pickedDateTime: Calendar) {
        val date = pickedDateTime.time
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val strDate: String = dateFormat.format(date)


        editTextDate.setText(strDate)

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

    private fun userInfoLoad() {
        val reference = FirebaseDatabase.getInstance().reference
        val currentUser = FirebaseAuth.getInstance().currentUser
        val query = reference.child("users").orderByKey().equalTo(currentUser?.uid)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (singleSnapshot in snapshot!!.children) {
                    userValues = singleSnapshot.getValue(UserValues::class.java)

                    minippLoad(userValues?.userPhotoUrl)
                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    private fun minippLoad(photoUrl: String?) {
        if (photoUrl != "") {
            iv_rezervation_profile_photo.load(photoUrl)
        }
        rezPage_progressbar.Gone()
        rezpage_cl.Visible()
    }
}
