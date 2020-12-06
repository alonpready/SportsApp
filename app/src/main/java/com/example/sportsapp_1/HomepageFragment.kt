package com.example.sportsapp_1

import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_homepage.*
import kotlinx.android.synthetic.main.fragment_user.*


class HomepageFragment : Fragment() {

    private lateinit var listOfTrainingTypes:ArrayList<TrainingTypes>
    private lateinit var adapter:RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true){
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
        return inflater.inflate(R.layout.fragment_homepage, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        iv_homepage_photo.setOnClickListener() {
            loadFragment(UserFragment())
        }

        rv.layoutManager = LinearLayoutManager(activity)

        val t1 = TrainingTypes("Dead Lift","3x5")
        val t2 = TrainingTypes("Bench Press","2x10")
        val t3 = TrainingTypes("Pull Dumbell","4x2")
        val t4 = TrainingTypes("Dumbell Lift","2x5")
        val t5 = TrainingTypes("Barfix","3x5")
        val t6 = TrainingTypes("Squad","2x6")

        listOfTrainingTypes = ArrayList<TrainingTypes>()
        listOfTrainingTypes.add(t1)
        listOfTrainingTypes.add(t2)
        listOfTrainingTypes.add(t3)
        listOfTrainingTypes.add(t4)
        listOfTrainingTypes.add(t5)
        listOfTrainingTypes.add(t6)

        rv.adapter = RVAdapter(requireContext(),listOfTrainingTypes)

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}