package com.animedia.app

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.animedia.app.adapter.EpisodeNumberAdapter
import com.animedia.app.data.DataProvider
import com.google.android.material.chip.Chip

class AnimeDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        val animeId = intent.getIntExtra("anime_id", 0)
        val animeTitle = intent.getStringExtra("anime_title") ?: "Аниме"

        setupToolbar(animeTitle)
        loadAnimeDetail(animeId)
        setupActionButtons(animeTitle)
    }

    private fun setupToolbar(title: String) {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = ""
    }

    private fun loadAnimeDetail(id: Int) {
        val anime = DataProvider.getAnimeDetail(id)

        // Title
        findViewById<TextView>(R.id.txtTitle).text = anime.title
        findViewById<TextView>(R.id.txtSubTitle).text = anime.type
        findViewById<TextView>(R.id.txtYear).text = "${anime.year} \u2022 ${anime.totalEpisodes} эпизодов"
        findViewById<TextView>(R.id.txtRating).text = "\u2B50 ${anime.rating}"
        findViewById<TextView>(R.id.txtStatus).text = anime.status
        findViewById<TextView>(R.id.txtDescription).text = anime.description

        // Backdrop
        val imgBackdrop = findViewById<ImageView>(R.id.imgBackdrop)
        val backdropDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.parseColor("#6C3CE1"), Color.parseColor("#0D0D1A"))
        )
        imgBackdrop.background = backdropDrawable

        // Poster
        val imgPoster = findViewById<ImageView>(R.id.imgPoster)
        val posterDrawable = GradientDrawable(
            GradientDrawable.Orientation.BOTTOM_TOP,
            intArrayOf(Color.parseColor("#FF6B6B"), Color.parseColor("#222240"))
        )
        posterDrawable.cornerRadius = 12f
        imgPoster.background = posterDrawable

        // Genre chips
        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGenres)
        chipGroup.removeAllViews()
        for (genre in anime.genres ?: emptyArray()) {
            val chip = Chip(this).apply {
                text = genre
                setChipBackgroundColorResource(R.color.surface_elevated)
                setTextColor(Color.parseColor("#B0B0D0"))
                chipStrokeWidth = 0f
                chipCornerRadius = 100f
                isClickable = true
                isCheckable = false
                chipMinHeight = 32f
            }
            chipGroup.addView(chip)
        }

        // Episodes list
        val recyclerEpisodes = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerEpisodes)
        recyclerEpisodes.layoutManager = GridLayoutManager(this, 4)
        recyclerEpisodes.adapter = EpisodeNumberAdapter(anime.totalEpisodes, this)
    }

    private fun setupActionButtons(title: String) {
        findViewById<View>(R.id.btnWatch).setOnClickListener {
            Toast.makeText(this, "Воспроизведение: $title", Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnFavorite).setOnClickListener {
            Toast.makeText(this, R.string.add_to_favorites, Toast.LENGTH_SHORT).show()
        }

        findViewById<View>(R.id.btnShare).setOnClickListener {
            val shareIntent = android.content.Intent().apply {
                action = android.content.Intent.ACTION_SEND
                putExtra(android.content.Intent.EXTRA_TEXT, "Смотри аниме: $title на Animedia!")
                type = "text/plain"
            }
            startActivity(android.content.Intent.createChooser(shareIntent, "Поделиться"))
        }
    }
}
