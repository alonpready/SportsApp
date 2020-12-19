package com.example.sportsapp_1.Fragments.ToolbarMenuPage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportsapp_1.Fragments.UserFragment
import com.example.sportsapp_1.R
import kotlinx.android.synthetic.main.inside_fragment_connection.*

class EditProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.inside_fragment_edit_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_Connection_back_button.setOnClickListener{
            loadFragment(UserFragment())
        }

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }

}