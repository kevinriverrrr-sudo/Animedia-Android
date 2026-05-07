package com.animedia.app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.animedia.app.AnimeDetailActivity
import com.animedia.app.R
import com.animedia.app.model.AnimeItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

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

        holder.txtTitle.text = item.title ?: ""
        holder.txtTime.text = item.timeInfo ?: ""
        holder.txtEpisodeInfo.text = item.episodeInfo ?: ""

        if (item.episodeNumber > 0) {
            holder.txtEpisodeBadge.visibility = View.VISIBLE
            holder.txtEpisodeBadge.text = item.episodeNumber.toString()
        } else {
            holder.txtEpisodeBadge.visibility = View.GONE
        }

        // Load real image with Glide
        val posterUrl = item.fullPosterUrl
        if (posterUrl.isNotEmpty()) {
            Glide.with(context)
                .load(posterUrl)
                .apply(RequestOptions()
                    .placeholder(R.color.surface_elevated)
                    .error(R.color.surface_elevated)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop())
                .into(holder.imgPoster)
        }

        holder.itemView.setOnClickListener {
            if (item.detailUrl != null && item.detailUrl!!.isNotEmpty()) {
                val intent = Intent(context, AnimeDetailActivity::class.java).apply {
                    putExtra("anime_url", item.detailUrl)
                    putExtra("anime_id", item.id ?: "")
                    putExtra("anime_title", item.title ?: "")
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = items.size
}
