package com.example.sportsapp_1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsapp_1.Model.TrainingVideos
import com.example.sportsapp_1.R

class AdapterHomepage (private val mContext: Context,
                      private val trainingList: List<TrainingVideos>,
                      private val urlListener:(trainingVideos: TrainingVideos)-> Unit) :
    RecyclerView.Adapter<AdapterHomepage.CardViewHolderOfDesignObjects>() {

    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var homepageCardView: CardView = view.findViewById(R.id.cv_reservation_cardview)
        var tvTrainingType: TextView = view.findViewById(R.id.tv_training_type)
        var tvTrainingRepetition: TextView = view.findViewById(R.id.tv_training_repetition)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolderOfDesignObjects {

        val view = LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_homepage, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        val training = trainingList[position]

        holder.tvTrainingType.text = training.trName

        holder.tvTrainingRepetition.text = training.trPeriod

        holder.homepageCardView.setOnClickListener {
            urlListener.invoke(trainingList[position])

        }
    }

    override fun getItemCount(): Int {

        return trainingList.size
    }
}