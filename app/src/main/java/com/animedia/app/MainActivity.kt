package com.animedia.app

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.animedia.app.adapter.AnimeGridAdapter
import com.animedia.app.adapter.CollectionAdapter
import com.animedia.app.adapter.EpisodeCardAdapter
import com.animedia.app.data.DataProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViews()
        setupClans()
        setupBottomNav()
        setupSearch()
    }

    private fun setupRecyclerViews() {
        // New Episodes
        val recyclerNewEpisodes = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerNewEpisodes)
        recyclerNewEpisodes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val newEpisodes = DataProvider.getNewEpisodes()
        recyclerNewEpisodes.adapter = EpisodeCardAdapter(newEpisodes, this)

        // Today Releases
        val recyclerTodayReleases = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerTodayReleases)
        recyclerTodayReleases.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val todayReleases = DataProvider.getTodayReleases()
        recyclerTodayReleases.adapter = EpisodeCardAdapter(todayReleases, this)

        // Collections
        val recyclerCollections = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerCollections)
        recyclerCollections.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val collections = DataProvider.getCollections()
        recyclerCollections.adapter = CollectionAdapter(collections, this)

        // New Anime Grid
        val recyclerNewAnime = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerNewAnime)
        recyclerNewAnime.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        val newAnime = DataProvider.getNewAnime()
        recyclerNewAnime.adapter = AnimeGridAdapter(newAnime, this)
    }

    private fun setupClans() {
        val clansContainer = findViewById<LinearLayout>(R.id.clansContainer)
        val clans = DataProvider.getTopClans()

        clansContainer.removeAllViews()
        for (clan in clans) {
            val itemView = layoutInflater.inflate(R.layout.item_clan, clansContainer, false) as LinearLayout

            val txtRank = itemView.findViewById<TextView>(R.id.txtRank)
            val txtClanName = itemView.findViewById<TextView>(R.id.txtClanName)
            val txtClanRating = itemView.findViewById<TextView>(R.id.txtClanRating)
            val imgClanIcon = itemView.findViewById<ImageView>(R.id.imgClanIcon)

            txtRank.text = clan.rank.toString()
            txtClanName.text = clan.name
            txtClanRating.text = "Рейтинг ${clan.rating} \u2022 ${clan.members}"

            val drawable = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(Color.parseColor(clan.iconColor), Color.parseColor("#1A1A2E"))
            )
            drawable.cornerRadius = 100f
            imgClanIcon.background = drawable

            clansContainer.addView(itemView)
        }
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, R.string.home, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_catalog -> {
                    Toast.makeText(this, R.string.catalog, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_search -> {
                    Toast.makeText(this, R.string.search_hint, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_favorites -> {
                    Toast.makeText(this, R.string.favorites, Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, R.string.profile, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSearch() {
        val searchBar = findViewById<android.view.View>(R.id.searchBar)
        searchBar.setOnClickListener {
            Toast.makeText(this, R.string.search_hint, Toast.LENGTH_SHORT).show()
        }
    }
}
