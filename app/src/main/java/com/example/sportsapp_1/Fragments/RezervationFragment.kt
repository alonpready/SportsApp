package com.example.sportsapp_1.Fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sportsapp_1.Adapters.Reservation_RVAdapter
import com.example.sportsapp_1.Model.ReservationInfo
import com.example.sportsapp_1.Model.TrainingVideos
import com.example.sportsapp_1.Model.UserValues
import com.example.sportsapp_1.Model.userResValue
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
import kotlin.collections.ArrayList


class RezervationFragment : Fragment() {

    private var reservationList = ArrayList<ReservationInfo>()
    private var reservationTempList = ArrayList<ReservationInfo>()
    private var userValues: UserValues? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
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

        return inflater.inflate(R.layout.fragment_rezervation, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        setClicks()
        rezpage_cl.Gone()
        rezPage_progressbar.Visible()
        userInfoLoad()

        iv_rezervation_profile_photo.setOnClickListener() {
            loadFragment(UserFragment())
        }
    }


    private fun takenewList(strDate: String) {

        var strDate = strDate

        reservationList = createdefaultResList(strDate)


    }


    private fun createdefaultResList(x: String): ArrayList<ReservationInfo> {

        val strDate = x
        var res: ReservationInfo ?= null
        var users: userResValue ? = null


        var t1 = ReservationInfo("09:00 - 10:30",reservationDate = strDate)
        var t2 = ReservationInfo("10:30 - 12:00",reservationDate = strDate)
        var t3 = ReservationInfo("12:00 - 13:30",reservationDate = strDate)
        var t4 = ReservationInfo("13:30 - 15:00",reservationDate = strDate)
        var t5 = ReservationInfo("15:00 - 16:30",reservationDate = strDate)
        var t6 = ReservationInfo("16:30 - 18:00",reservationDate = strDate)
        var t7 = ReservationInfo("18:00 - 19:30",reservationDate = strDate)
        var t8 = ReservationInfo("19:30 - 21:00",reservationDate = strDate)
        var t9 = ReservationInfo("21:00 - 22:30",reservationDate = strDate)

        reservationList.add(t1)
        reservationList.add(t2)
        reservationList.add(t3)
        reservationList.add(t4)
        reservationList.add(t5)
        reservationList.add(t6)
        reservationList.add(t7)
        reservationList.add(t8)
        reservationList.add(t9)

        reservationTempList.add(t1)
        reservationTempList.add(t2)
        reservationTempList.add(t3)
        reservationTempList.add(t4)
        reservationTempList.add(t5)
        reservationTempList.add(t6)
        reservationTempList.add(t7)
        reservationTempList.add(t8)
        reservationTempList.add(t9)


        for (i in 0 until reservationList.size-1) {
            val reference = FirebaseDatabase.getInstance().reference
            val query = reference.child("reservations").child(strDate)
                .child(reservationList[i].reservationHour)

                query.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        res = snapshot.getValue(ReservationInfo::class.java)
                        if (null != res?.reservationCurrent){
                            var changeRes = ReservationInfo(
                                res!!.reservationHour,
                                res!!.reservationCurrent,
                                res!!.reservationQuota,
                                res!!.reservationDate
                            )

                            reservationList[i] = changeRes
                        }
                        query.setValue(reservationList[i])
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

        }
        return reservationList
    }

    private fun pullCreatedResList(hour: String, current: Int, quota: Int): ReservationInfo{
        val newRestValue = ReservationInfo(
            hour,
            current,
            quota

        )
        return newRestValue
    }

    private fun setClicks() {
        bt_select_date.setOnClickListener {
            pickDateTime()
        }
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
            },
            startYear,
            startMonth,
            startDay
        ).show()
    }

    private fun doSomethingWith(pickedDateTime: Calendar) {
        val date = pickedDateTime.time
        val dateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateFormat2: DateFormat = SimpleDateFormat("ddMMyyyy")
        val strDate: String = dateFormat.format(date)
        val strDate2: String = dateFormat2.format(date)
        editTextDate.setText(strDate)

        takenewList(strDate2)
        reservation_rv.layoutManager = LinearLayoutManager(activity)
        reservation_rv.adapter =
            Reservation_RVAdapter(requireContext(), reservationList, strDate2) {
            }
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
