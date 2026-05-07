package com.animedia.app.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.animedia.app.AnimeDetailActivity
import com.animedia.app.R
import com.animedia.app.data.DataProvider
import com.animedia.app.model.AnimeItem

class EpisodeCardAdapter(
    private val items: List<AnimeItem>,
    private val context: Context
) : RecyclerView.Adapter<EpisodeCardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgPoster)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
        val txtEpisodeInfo: TextView = view.findViewById(R.id.txtEpisodeInfo)
        val txtEpisodeBadge: TextView = view.findViewById(R.id.txtEpisodeBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_episode_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtTitle.text = item.title
        holder.txtTime.text = item.timeInfo
        holder.txtEpisodeInfo.text = item.episodeInfo

        // Show episode badge
        if (item.episodeNumber > 0) {
            holder.txtEpisodeBadge.visibility = View.VISIBLE
            holder.txtEpisodeBadge.text = item.episodeNumber.toString()
        }

        // Generate placeholder color for poster
        val color = DataProvider.getRandomColor()
        val drawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.parseColor(color), Color.parseColor("#1A1A2E"))
        )
        drawable.cornerRadius = 12f
        holder.imgPoster.background = drawable

        holder.itemView.setOnClickListener {
            val intent = Intent(context, AnimeDetailActivity::class.java).apply {
                putExtra("anime_id", item.id)
                putExtra("anime_title", item.title)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}
