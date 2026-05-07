package com.animedia.app.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.animedia.app.R
import com.animedia.app.model.AnimeItem

class EpisodeNumberAdapter(
    private val totalEpisodes: Int,
    private val context: Context
) : RecyclerView.Adapter<EpisodeNumberAdapter.ViewHolder>() {

    private val selectedEpisodes = mutableSetOf<Int>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEpisodeNumber: TextView = view.findViewById(R.id.txtEpisodeNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_episode_number, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val epNum = position + 1
        holder.txtEpisodeNumber.text = epNum.toString()

        if (selectedEpisodes.contains(position)) {
            holder.txtEpisodeNumber.setTextColor(Color.WHITE)
            holder.itemView.setBackgroundColor(Color.parseColor("#6C3CE1"))
        } else {
            holder.txtEpisodeNumber.setTextColor(Color.parseColor("#B0B0D0"))
        }

        holder.itemView.setOnClickListener {
            if (selectedEpisodes.contains(position)) {
                selectedEpisodes.remove(position)
            } else {
                selectedEpisodes.clear()
                selectedEpisodes.add(position)
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = totalEpisodes
}
