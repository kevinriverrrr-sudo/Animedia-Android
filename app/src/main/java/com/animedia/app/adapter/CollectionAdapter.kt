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
import com.animedia.app.data.DataProvider
import com.animedia.app.model.CollectionItem

class CollectionAdapter(
    private val items: List<CollectionItem>,
    private val context: Context
) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgBackground: ImageView = view.findViewById(R.id.imgBackground)
        val txtCollectionTitle: TextView = view.findViewById(R.id.txtCollectionTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_collection_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtCollectionTitle.text = item.title

        val color = DataProvider.getRandomColor()
        val drawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.parseColor(color), Color.parseColor("#0D0D1A"))
        )
        holder.imgBackground.background = drawable
    }

    override fun getItemCount(): Int = items.size
}
