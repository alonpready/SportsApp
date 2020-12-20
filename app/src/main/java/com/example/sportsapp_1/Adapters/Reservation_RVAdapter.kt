package com.example.sportsapp_1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsapp_1.Model.ReservationInfo
import com.example.sportsapp_1.Model.TrainingVideos
import com.example.sportsapp_1.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Reservation_RVAdapter(private val mContext: Context,
                            private val reservationList: ArrayList<ReservationInfo>,
                            private val takingdate: String,
                            private val urlListener:(reservationList: ReservationInfo)-> Unit) :
    RecyclerView.Adapter<Reservation_RVAdapter.CardViewHolderOfDesignObjects>() {


    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var reservationCardView: CardView
        var reservationHour : TextView
        var strDate2: String

        var reservationProgressBar: ProgressBar
        var reservationRatio: TextView
        var reservationIcon: ImageView


        init {
            reservationCardView = view.findViewById(R.id.cv_reservation_cardview)
            reservationHour = view.findViewById(R.id.tv_Reservation_hours)
            strDate2 = takingdate
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
        var newReservation: ReservationInfo ?= null
        val strDate2 = takingdate
        holder.reservationHour.text = reservation.reservationHour
        holder.reservationProgressBar.progress = reservationList[position].reservationCurrent
        holder.reservationProgressBar.max = reservationList[position].reservationQuota
        holder.reservationRatio.text =
            "${reservationList[position].reservationCurrent}/${reservationList[position].reservationQuota}"
        holder.reservationIcon.setOnClickListener {
            urlListener.invoke(reservation)
            var reference = FirebaseDatabase.getInstance().reference
            var query = reference.child("reservations").child(strDate2).child(reservationList[position].reservationHour)

            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    newReservation = snapshot.getValue(ReservationInfo::class.java)

                    var newRes = createnewRes(
                        newReservation!!.reservationHour,
                        newReservation!!.reservationCurrent,
                        newReservation!!.reservationQuota
                    )
                    if (newRes.reservationCurrent>newRes.reservationQuota){
                        Toast. makeText(mContext,"Kota Doldu!",Toast.LENGTH_SHORT).show()
                    }
                    else {
                        query.setValue(newRes)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
    private fun createnewRes(hour: String, resCurrent: Int, resQuota: Int): ReservationInfo{

        var newRes = ReservationInfo(
            hour,
            resCurrent + 1,
            resQuota
        )
        return newRes


    }

    override fun getItemCount(): Int {

        return reservationList.size
    }
    }






