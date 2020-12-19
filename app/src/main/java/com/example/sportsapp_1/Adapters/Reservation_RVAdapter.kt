package com.example.sportsapp_1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsapp_1.Model.ReservationInfo
import com.example.sportsapp_1.Model.TrainingVideos
import com.example.sportsapp_1.R

class Reservation_RVAdapter(private val mContext: Context,
                            private val reservationList: ArrayList<ReservationInfo>,
                            private val urlListener:(reservationList: ReservationInfo)-> Unit) :
    RecyclerView.Adapter<Reservation_RVAdapter.CardViewHolderOfDesignObjects>() {

    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var reservationCardView: CardView
        var reservationHour : TextView
        


        init {
            reservationCardView = view.findViewById(R.id.cv_reservation_cardview)
            reservationHour = view.findViewById(R.id.tv_Reservation_hours)
            
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderOfDesignObjects {

        val view = LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_reservation, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        val reservation = reservationList[position]

        holder.reservationHour.text = reservation.reservationHour
        
        holder.reservationCardView.setOnClickListener {
            urlListener.invoke(reservationList[position])
            Toast.makeText(mContext,reservationList[position].reservationHour,Toast.LENGTH_SHORT).show()}
    }

    override fun getItemCount(): Int {

        return reservationList.size
    }
}