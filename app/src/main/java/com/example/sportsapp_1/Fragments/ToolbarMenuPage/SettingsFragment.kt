package com.example.sportsapp_1.Fragments.ToolbarMenuPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sportsapp_1.Fragments.UserFragment
import com.example.sportsapp_1.R
import kotlinx.android.synthetic.main.inside_fragment_connection.*


class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.inside_fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_Connection_back_button.setOnClickListener {
            loadFragment(UserFragment())
        }

        val switch = view.findViewById<Switch>(R.id.s_Settings_swtich)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Gece modu açık", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(requireContext(), "Gece modu kapalı", Toast.LENGTH_SHORT).show()
        }

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
