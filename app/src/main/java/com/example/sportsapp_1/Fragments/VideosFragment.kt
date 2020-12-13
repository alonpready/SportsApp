package com.example.sportsapp_1.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sportsapp_1.Utill.Gone
import com.example.sportsapp_1.Adapters.TrainingTypesAdapter
import com.example.sportsapp_1.DataClasses.TrainingVideos
import com.example.sportsapp_1.DataClasses.UserValues
import com.example.sportsapp_1.MainActivity
import com.example.sportsapp_1.R
import com.example.sportsapp_1.Utill.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_training_types.*


class VideosFragment() : Fragment() {

    private var listOfTrainingVideos1 = ArrayList<TrainingVideos>()
    private var listOfTrainingVideos2 = ArrayList<TrainingVideos>()
    private var listOfTrainingVideos3 = ArrayList<TrainingVideos>()
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
        return inflater.inflate(R.layout.fragment_training_types, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        videospage_cl.Gone()
        videosPage_progressbar.Visible()
        userInfoLoad()
        iv_videos_profile_photo.setOnClickListener {
            loadFragment(UserFragment())
        }

    }

    private fun setUI() {
        initializeRv1()
        initializeRv2()
        initializeRv3()
    }

    private fun initializeRv1() {

        createTrainTypes1()

        recycleview_training_types_1.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_1.adapter =
            TrainingTypesAdapter(requireContext(), listOfTrainingVideos1) {trainingVideos ->


                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos)
                loadFragment(videospgFr)
            }
    }

    private fun initializeRv2() {

        createTrainTypes2()

        recycleview_training_types_2.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_2.adapter =
            TrainingTypesAdapter(requireContext(), listOfTrainingVideos2)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos)
                loadFragment(videospgFr)
            }
    }

    private fun initializeRv3() {

        createTrainTypes3()

        recycleview_training_types_3.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_3.adapter =
            TrainingTypesAdapter(requireContext(), listOfTrainingVideos3)  { trainingVideos ->
                val videospgFr : VideospageFragment = VideospageFragment(trainingVideos)
                loadFragment(videospgFr)
            }
    }

    private fun createTrainTypes1() {

        val tVideos1 = TrainingVideos(
            "Başlangıç Seviye",
            "Dead Lift",
            "Set Sayısı: 3x5",
            "15 dakika",
            "1VrZ1QLTdUs",
            R.drawable.iv_dead_lift
        )

        val tVideos2 = TrainingVideos(
            "Orta Seviye",
            "Bench Press",
            "Set Sayısı: 2x10",
            "12 dakika",
            "cHwutxa3XLY",
            R.drawable.iv_benc_press
        )

        val tVideos3 = TrainingVideos(
            "İleri Seviye",
            "Dumbell Lift",
            "Set Sayısı: 3x10",
            "8 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_dumbell_lift
        )

        listOfTrainingVideos1.add(tVideos1)
        listOfTrainingVideos1.add(tVideos2)
        listOfTrainingVideos1.add(tVideos3)


    }

    private fun createTrainTypes2() {

        val tVideos1 = TrainingVideos(
            "İleri Seviye",
            "Dumbell Lift",
            "Set Sayısı: 3x10",
            "8 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_dumbell_lift
        )

        val tVideos2 = TrainingVideos(
            "Orta Seviye",
            "Bench Press",
            "Set Sayısı: 2x10",
            "12 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_benc_press
        )

        val tVideos3 = TrainingVideos(
            "Başlangıç Seviye",
            "Dead Lift",
            "Set Sayısı: 3x5",
            "15 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_dead_lift
        )

        listOfTrainingVideos2.add(tVideos1)
        listOfTrainingVideos2.add(tVideos2)
        listOfTrainingVideos2.add(tVideos3)
    }

    private fun createTrainTypes3() {


        val tVideos1 = TrainingVideos(
            "Orta Seviye",
            "Bench Press",
            "Set Sayısı: 2x10",
            "12 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_benc_press
        )

        val tVideos2 = TrainingVideos(
            "Başlangıç Seviye",
            "Dead Lift",
            "Set Sayısı: 3x5",
            "15 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_dead_lift
        )

        val tVideos3 = TrainingVideos(
            "İleri Seviye",
            "Dumbell Lift",
            "Set Sayısı: 3x10",
            "8 dakika",
            "DQnFG4-SVys",
            R.drawable.iv_dumbell_lift
        )

        listOfTrainingVideos3.add(tVideos1)
        listOfTrainingVideos3.add(tVideos2)
        listOfTrainingVideos3.add(tVideos3)


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
                for (singleSnapshot in snapshot.children) {
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
            iv_videos_profile_photo.load(photoUrl)
        }
        videospage_cl.Visible()
        videosPage_progressbar.Gone()
    }


}