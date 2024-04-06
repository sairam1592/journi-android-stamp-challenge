package com.journiapp.stampapplication.presentation.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.journiapp.stampapplication.R
import com.journiapp.stampapplication.model.Stamp
import com.squareup.picasso.Picasso
import kotlin.random.Random

class StampAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var stamps: List<Stamp> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_stamp, parent, false)
        return StampViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return stamps.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StampViewHolder).bind(stamps[position])
    }

    class StampViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var BASE_API_URL = "https://www.journiapp.com/picture/"

        fun bind(stamp: Stamp) {
            val ivStamp = itemView.findViewById<ImageView>(R.id.iv_stamp)

            ivStamp.rotation = Random.nextInt(-60, 60).toFloat()

            Picasso.get()
                .load(BASE_API_URL + stamp.pictureGuid + "_stamp.png")
                .into(ivStamp)
        }
    }
}