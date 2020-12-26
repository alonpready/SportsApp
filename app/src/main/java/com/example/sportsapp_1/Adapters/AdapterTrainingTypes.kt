package com.example.sportsapp_1.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsapp_1.Model.TrainingVideos
import com.example.sportsapp_1.R

class AdapterTrainingTypes(
    private val mContext: Context,
    private val trainingVideosList: List<TrainingVideos>,
    private val urlListener: (trainingVideos: TrainingVideos) -> Unit
) :
    RecyclerView.Adapter<AdapterTrainingTypes.CardViewHolderOfDesignObjects>() {

    inner class CardViewHolderOfDesignObjects(view: View) : RecyclerView.ViewHolder(view) {

        var trainingLevel: TextView = view.findViewById(R.id.tv_training_level)
        var trainingName: TextView = view.findViewById(R.id.tv_training_name)
        var trainingPeriod: TextView = view.findViewById(R.id.tv_training_period)
        var trainingTime: TextView = view.findViewById(R.id.tv_training_time)
        var trainingPhoto: ImageView = view.findViewById(R.id.iv_training_photo)
        var trainingButton: Button = view.findViewById(R.id.bt_training_watch)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolderOfDesignObjects {

        val view =
            LayoutInflater.from(mContext).inflate(R.layout.cardview_rv_trainintypes, parent, false)
        return CardViewHolderOfDesignObjects(view)

    }

    override fun onBindViewHolder(holder: CardViewHolderOfDesignObjects, position: Int) {

        val trainingVideos = trainingVideosList[position]

        holder.trainingLevel.text = trainingVideos.trLevel
        holder.trainingName.text = trainingVideos.trName
        holder.trainingPeriod.text = trainingVideos.trPeriod
        holder.trainingTime.text = trainingVideos.trTime
        holder.trainingPhoto.setImageResource(trainingVideos.trPhotoUrl)
        holder.trainingPhoto.setOnClickListener {
            urlListener.invoke(trainingVideosList[position])
        }
        holder.trainingButton.setOnClickListener {
            urlListener.invoke(trainingVideosList[position])
        }

    }

    override fun getItemCount(): Int {
        return trainingVideosList.size
    }
}

