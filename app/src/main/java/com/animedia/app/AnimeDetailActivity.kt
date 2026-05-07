package com.animedia.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.animedia.app.adapter.EpisodeNumberAdapter
import com.animedia.app.data.AmdParser
import com.animedia.app.model.AnimeItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import kotlinx.coroutines.*

class AnimeDetailActivity : AppCompatActivity() {

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anime_detail)

        val animeUrl = intent.getStringExtra("anime_url") ?: ""
        val animeTitle = intent.getStringExtra("anime_title") ?: "Аниме"

        // Setup toolbar back button
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }
        supportActionBar?.title = ""

        // Set basic title immediately
        findViewById<TextView>(R.id.txtTitle).text = animeTitle
        findViewById<TextView>(R.id.txtSubTitle).text = "Загрузка..."

        // Show loading, hide content
        showLoading(true)

        if (animeUrl.isNotEmpty()) {
            loadAnimeDetail(animeUrl)
        } else {
            Toast.makeText(this, "Ссылка не найдена", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun loadAnimeDetail(url: String) {
        scope.launch {
            try {
                val fullUrl = if (url.startsWith("http")) url else "https://amd.online$url"
                val html = withContext(Dispatchers.IO) {
                    AmdParser.fetchUrl(fullUrl)
                }

                if (html.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AnimeDetailActivity, "Ошибка загрузки. Проверьте интернет.", Toast.LENGTH_LONG).show()
                        showLoading(false)
                    }
                    return@launch
                }

                val anime = withContext(Dispatchers.IO) {
                    AmdParser.parseAnimeDetail(html)
                }

                if (anime == null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AnimeDetailActivity, "Не удалось распарсить страницу", Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                    return@launch
                }

                withContext(Dispatchers.Main) {
                    displayAnimeDetail(anime)
                    showLoading(false)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AnimeDetailActivity, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                    showLoading(false)
                }
            }
        }
    }

    private fun displayAnimeDetail(anime: AnimeItem) {
        // Title
        if (!anime.title.isNullOrEmpty()) {
            findViewById<TextView>(R.id.txtTitle).text = anime.title
        }

        // Sub title
        if (!anime.subTitle.isNullOrEmpty()) {
            findViewById<TextView>(R.id.txtSubTitle).text = anime.subTitle
        } else {
            findViewById<TextView>(R.id.txtSubTitle).visibility = View.GONE
        }

        // Year and episode count
        val metaText = buildString {
            anime.year?.let { append(it) }
            anime.totalEpisodesText?.let {
                if (isNotEmpty()) append(" \u2022 ")
                append(it)
            }
            anime.studio?.let {
                if (isNotEmpty()) append(" \u2022 ")
                append(it)
            }
        }
        if (metaText.isNotEmpty()) {
            findViewById<TextView>(R.id.txtYear).text = metaText
        }

        // Rating
        if (!anime.rating.isNullOrEmpty()) {
            findViewById<TextView>(R.id.txtRating).text = "\u2B50 ${anime.rating}"
        } else {
            findViewById<TextView>(R.id.txtRating).visibility = View.GONE
        }

        // Status
        val status = anime.status ?: ""
        if (status.isNotEmpty()) {
            val statusView = findViewById<TextView>(R.id.txtStatus)
            statusView.text = status
            statusView.visibility = View.VISIBLE
        } else {
            findViewById<TextView>(R.id.txtStatus).visibility = View.GONE
        }

        // Load poster image with Glide
        val posterUrl = anime.fullPosterUrl
        val imgBackdrop = findViewById<android.widget.ImageView>(R.id.imgBackdrop)
        val imgPoster = findViewById<android.widget.ImageView>(R.id.imgPoster)

        if (posterUrl.isNotEmpty()) {
            Glide.with(this)
                .load(posterUrl)
                .apply(RequestOptions()
                    .placeholder(R.color.surface_elevated)
                    .error(R.color.surface_elevated)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop())
                .into(imgPoster)

            Glide.with(this)
                .load(posterUrl)
                .apply(RequestOptions()
                    .placeholder(R.color.surface_card)
                    .error(R.color.surface_card)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop())
                .into(imgBackdrop)
        }

        // Genre chips
        val chipGroup = findViewById<com.google.android.material.chip.ChipGroup>(R.id.chipGenres)
        chipGroup.removeAllViews()
        val genres = anime.genres
        if (genres != null && genres.isNotEmpty()) {
            for (genre in genres) {
                if (genre.isBlank()) continue
                val chip = Chip(this).apply {
                    text = genre.trim()
                    setChipBackgroundColorResource(R.color.surface_elevated)
                    setTextColor(android.graphics.Color.parseColor("#B0B0D0"))
                    chipStrokeWidth = 0f
                    chipCornerRadius = 100f
                    isClickable = false
                    isCheckable = false
                    chipMinHeight = 32f
                }
                chipGroup.addView(chip)
            }
        } else {
            chipGroup.visibility = View.GONE
        }

        // Description
        val desc = anime.description
        if (!desc.isNullOrEmpty()) {
            // Strip HTML tags for plain text display
            val plainDesc = android.text.Html.fromHtml(desc, android.text.Html.FROM_HTML_MODE_COMPACT).toString().trim()
            findViewById<TextView>(R.id.txtDescription).text = plainDesc
        } else {
            findViewById<TextView>(R.id.txtDescription).text = "Описание недоступно"
        }

        // Episode list
        val totalEp = anime.totalEpisodes
        if (totalEp > 0) {
            val recyclerEpisodes = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerEpisodes)
            recyclerEpisodes.layoutManager = GridLayoutManager(this, 4)
            recyclerEpisodes.adapter = EpisodeNumberAdapter(totalEp, this)
        }

        // Watch button
        findViewById<View>(R.id.btnWatch).setOnClickListener {
            Toast.makeText(this, "Воспроизведение: ${anime.title}", Toast.LENGTH_SHORT).show()
        }

        // Favorite button
        findViewById<View>(R.id.btnFavorite).setOnClickListener {
            Toast.makeText(this, "Добавлено в избранное", Toast.LENGTH_SHORT).show()
        }

        // Share button
        findViewById<View>(R.id.btnShare).setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Смотри аниме: ${anime.title} на Animedia!")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться"))
        }
    }

    private fun showLoading(show: Boolean) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val scrollView = findViewById<View>(R.id.contentScrollView)

        if (show) {
            if (progressBar.visibility != View.VISIBLE) {
                progressBar.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
            }
        } else {
            progressBar.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}
