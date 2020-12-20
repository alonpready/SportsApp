package com.example.sportsapp_1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsapp_1.Model.ReservationInfo
import com.example.sportsapp_1.R

class Reservation_RVAdapter(
    private val mContext: Context,
    private val reservationList: ArrayList<ReservationInfo>,
    private val urlListener: (reservationList: ReservationInfo) -> Unit
) :
    RecyclerView.Adapter<Reservation_RVAdapter.CardViewHolderOfDesignObjects>() {


    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var reservationCardView: CardView
        var reservationHour: TextView
        var reservationProgressBar: ProgressBar
        var reservationRatio: TextView
        var reservationIcon: ImageView


        init {
            reservationCardView = view.findViewById(R.id.cv_reservation_cardview)
            reservationHour = view.findViewById(R.id.tv_Reservation_hours)
            reservationProgressBar = view.findViewById(R.id.pb_Reservation_progressBar)
            reservationRatio = view.findViewById(R.id.tv_Reservation_ratio)
            reservationIcon = view.findViewById(R.id.iv_Reservation_reserve)

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolderOfDesignObjects {

        val view =
            LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_reservation, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        val reservation = reservationList[position]

        holder.reservationHour.text = reservation.reservationHour
        holder.reservationProgressBar.progress = reservationList[position].reservationCurrent
        holder.reservationProgressBar.max = reservationList[position].reservationQuota
        holder.reservationRatio.text =
            "${reservationList[position].reservationCurrent}/${reservationList[position].reservationQuota}"
        holder.reservationIcon.setOnClickListener {
            urlListener.invoke(reservation)
        }

    }

    override fun getItemCount(): Int {

        return reservationList.size
    }


}