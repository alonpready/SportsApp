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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.inside_fragment_connection.*
import kotlinx.android.synthetic.main.inside_fragment_connection.iv_Connection_back_button
import kotlinx.android.synthetic.main.inside_fragment_settings.*


class SettingsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.inside_fragment_settings, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        iv_Connection_back_button.setOnClickListener {
            loadFragment(UserFragment())
        }

        val switch = view.findViewById<Switch>(R.id.s_Settings_swtich)
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
            {
                SettingsFrame.setBackgroundResource(R.color.colorOne)
                v_Account_line3.setBackgroundResource(R.color.colorBackgraund)
                Toast.makeText(requireContext(), "Tema değiştirildi", Toast.LENGTH_SHORT).show()
            }
            else
            {
                SettingsFrame.setBackgroundResource(R.color.colorBackgraund)
                v_Account_line3.setBackgroundResource(R.color.colorOne)
                Toast.makeText(requireContext(), "Standar temaya geçildi.", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.fragmentContainerView, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }
}
