package com.example.sportsapp_1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.sportsapp_1.Utill.Gone
import com.example.sportsapp_1.Utill.Visible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_training_types.*


class VideosFragment : Fragment() {


    private var listOfTrainingVideos = ArrayList<TrainingVideos>()
    private var user: User? = null
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
        return inflater.inflate(R.layout.fragment_training_types, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        videospage_cl.Gone()
        videosPage_progressbar.Visible()
        userInfoLoad()
        iv_videos_profile_photo.setOnClickListener() {
            loadFragment(UserFragment())
        }

    }

    private fun setUI() {
        inizilatizeRv()
    }

    private fun inizilatizeRv() {

        createTrainTypes()

        recycleview_training_types_1.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_1.adapter =
            TrainingTypesAdapter(requireContext(), listOfTrainingVideos) { videoUrl ->
                Toast.makeText(requireContext(), videoUrl.data_trainingUrl, Toast.LENGTH_SHORT).show()
            }

        recycleview_training_types_2.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_2.adapter =
            TrainingTypesAdapter(requireContext(), listOfTrainingVideos)  { videoUrl ->
                Toast.makeText(requireContext(), videoUrl.data_trainingUrl, Toast.LENGTH_SHORT).show()
            }

        recycleview_training_types_3.layoutManager =
            LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
        recycleview_training_types_3.adapter =
            TrainingTypesAdapter(requireContext(), listOfTrainingVideos)  { trainingVideos ->
                Toast.makeText(requireContext(), trainingVideos.data_trainingUrl, Toast.LENGTH_SHORT).show()
            }
    }

    private fun createTrainTypes() {

        val tVideos1 = TrainingVideos(
            "Başlangıç Seviye",
            "Dead Lift",
            "Set Sayısı: 3x5",
            "15 dakika",
            "youtube.com/s4213adas",
            R.drawable.iv_dead_lift)

        val tVideos2 = TrainingVideos(
            "Orta Seviye",
            "Bench Press",
            "Set Sayısı: 2x10",
            "12 dakika",
            "youtube.com/241%32fdad",
            R.drawable.iv_benc_press)

        val tVideos3 = TrainingVideos(
            "İleri Seviye",
            "Dumbell Lift",
            "Set Sayısı: 3x10",
            "8 dakika",
            "youtube.com/watc514%%23adas",
            R.drawable.iv_dumbell_lift)

        listOfTrainingVideos.add(tVideos1)
        listOfTrainingVideos.add(tVideos2)
        listOfTrainingVideos.add(tVideos3)


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
                    user = singleSnapshot.getValue(User::class.java)

                    minippLoad(user?.userPhotoUrl)
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