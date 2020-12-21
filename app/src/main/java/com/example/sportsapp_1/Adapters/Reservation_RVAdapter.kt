package com.example.sportsapp_1.Adapters

import android.app.Dialog
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.joinAll

class Reservation_RVAdapter(
    private val mContext: Context,
    private val reservationList: ArrayList<ReservationInfo>,
    private val takingdate: String,
    private val urlListener: (reservationList: ReservationInfo) -> Unit
) :
    RecyclerView.Adapter<Reservation_RVAdapter.CardViewHolderOfDesignObjects>() {


    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var reservationCardView: CardView
        var reservationHour: TextView
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

        var auth = FirebaseAuth.getInstance()
        val reservation = reservationList[position]
        var newReservation: ReservationInfo? = null
        val strDate2 = takingdate

        holder.reservationHour.text = reservation.reservationHour
        holder.reservationProgressBar.progress = reservationList[position].reservationCurrent
        holder.reservationProgressBar.max = reservationList[position].reservationQuota
        holder.reservationRatio.text =
            "${reservationList[position].reservationCurrent}/${reservationList[position].reservationQuota}"

        holder.reservationIcon.setOnClickListener {
            urlListener.invoke(reservation)

            val dialog = Dialog(mContext)
            dialog.setContentView(R.layout.custom_dialog_reservation)

            val btSave = dialog.findViewById(R.id.bt_Dialog_save) as Button
            val btCancel = dialog.findViewById(R.id.bt_Dialog_cancel) as Button
            val tvText = dialog.findViewById(R.id.tv_Dialog_text) as TextView

            tvText.text = "${reservation.reservationHour} saatleri arasına rezervasyon yapmak istiyor musunuz?"


            btSave.setOnClickListener {
                val reference = FirebaseDatabase.getInstance().reference
                val query = reference.child("reservations").child(strDate2)
                    .child(reservationList[position].reservationHour)
                var x: Boolean = true
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        newReservation = snapshot.getValue(ReservationInfo::class.java)

                        val newRes = createnewRes(
                            newReservation!!.reservationHour,
                            newReservation!!.reservationCurrent,
                            newReservation!!.reservationQuota,
                            newReservation!!.reservationDate,
                            newReservation!!.reservationId
                        )
                        if (newRes.reservationCurrent > newRes.reservationQuota) {
                            Toast.makeText(mContext, "Kota Doldu!", Toast.LENGTH_SHORT).show()
                        } else {

                            var query2 = reference.child("users").child(auth.currentUser!!.uid).child("UserResId")
                            query2.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for (singleSnapshot in snapshot!!.children) {
                                        var checkid = singleSnapshot.getValue(String::class.java)
                                        if (checkid == newRes.reservationId){
                                            x = false
                                            Toast.makeText(
                                                mContext,
                                                "Aynı saat aralığına rezervasyon yapılamaz!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                    if (x){
                                        query2.child(newRes.reservationId).
                                        setValue(newRes.reservationId)
                                        query.setValue(newRes)
                                        Toast.makeText(
                                            mContext,
                                            "${reservation.reservationHour} saatleri arasına rezervasyon yapılmıştır.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        holder.reservationRatio.text =
                                            "${reservationList[position].reservationCurrent+1}/${reservationList[position].reservationQuota}"

                                        holder.reservationProgressBar.progress = reservationList[position].reservationCurrent+1
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }
                            })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                dialog.dismiss()
            }
            btCancel.setOnClickListener { dialog.dismiss() }
            dialog.show()
        }
    }

    private fun createnewRes(hour: String, resCurrent: Int, resQuota: Int, date: String, id: String): ReservationInfo {

        val newRes = ReservationInfo(
            hour,
            resCurrent + 1,
            resQuota,
            date,
            id
        )
        return newRes
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }


}






