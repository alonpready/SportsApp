package com.example.sportsapp_1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.fragment_homepage.*


class HomepageFragment : Fragment() {

    private var listOfTrainingTypes = ArrayList<TrainingTypes>()
    private lateinit var circularProgressXML:View
    private var user : User? = null
    private var gymCapacity:Int = 0
    private var gymCurrentUser:Int = 0

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


    private fun circularProgressBar() {

        gymCapacity = 34
        gymCurrentUser = 12

        circularProgressXML = v_homepage_CircularProgressBar
        circularProgressXML = view!!.findViewById(R.id.v_homepage_CircularProgressBar)

        v_homepage_CircularProgressBar.apply {
            progress = gymCurrentUser.toFloat()
            setProgressWithAnimation(gymCapacity.toFloat(), 3000)
            progressMax = 100f
            progressBarColorStart = Color.GREEN
            progressBarColorEnd = Color.GREEN
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            backgroundProgressBarColor = Color.DKGRAY
            backgroundProgressBarColorStart = Color.DKGRAY
            backgroundProgressBarColorEnd = Color.DKGRAY
            backgroundProgressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM
            progressBarWidth = 24f // in DP
            backgroundProgressBarWidth = 25f // in DP
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homepage_cl.Gone()
        homePage_progressbar.Visible()

        setUI()
        setClicks()
        userInfoLoad()
        circularProgressBar()

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }


    private fun openUserPage() {
        loadFragment(UserFragment())
    }


    private fun setClicks() {
        iv_homepage_photo.setOnClickListener {
            openUserPage()
        }
    }


    private fun setUI() {
        inizilatizeRv()
    }


    private fun createTrainTypes() {
        val t1 = TrainingTypes("Dead Lift", "3x5")
        val t2 = TrainingTypes("Bench Press", "2x10")
        val t3 = TrainingTypes("Pull Dumbell", "4x2")
        val t4 = TrainingTypes("Dumbell Lift", "2x5")
        val t5 = TrainingTypes("Barfix", "3x5")
        val t6 = TrainingTypes("Squad", "2x6")

        listOfTrainingTypes.add(t1)
        listOfTrainingTypes.add(t2)
        listOfTrainingTypes.add(t3)
        listOfTrainingTypes.add(t4)
        listOfTrainingTypes.add(t5)
        listOfTrainingTypes.add(t6)

    }

    private fun inizilatizeRv() {
        createTrainTypes()
        recyclerview_homepage.layoutManager = LinearLayoutManager(activity)
        recyclerview_homepage.adapter = RVAdapter(requireContext(), listOfTrainingTypes)
    }

    private fun userInfoLoad(){
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
    private fun minippLoad(photoUrl:String?) {

        if (photoUrl != ""){
            iv_homepage_photo.load(photoUrl)
        }
        homepage_cl.Visible()
        homePage_progressbar.Gone()

    }


}