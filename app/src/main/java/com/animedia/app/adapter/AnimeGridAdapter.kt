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

class AnimeGridAdapter(
    private val items: List<AnimeItem>,
    private val context: Context
) : RecyclerView.Adapter<AnimeGridAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgPoster: ImageView = view.findViewById(R.id.imgPoster)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtSubtitle: TextView = view.findViewById(R.id.txtSubtitle)
        val txtBadge: TextView = view.findViewById(R.id.txtBadge)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_anime_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtTitle.text = item.title
        holder.txtSubtitle.text = buildString {
            append(item.type ?: "")
            if (item.year?.isNotEmpty() == true) append(" \u2022 ${item.year}")
            if (item.rating?.isNotEmpty() == true) append(" \u2022 \u2B50${item.rating}")
        }

        // Badge
        if (item.status?.isNotEmpty() == true) {
            holder.txtBadge.visibility = View.VISIBLE
            holder.txtBadge.text = item.status
        } else {
            holder.txtBadge.visibility = View.GONE
        }

        // Placeholder poster
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
