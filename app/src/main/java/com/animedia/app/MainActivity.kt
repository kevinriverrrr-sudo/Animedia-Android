package com.animedia.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.animedia.app.adapter.AnimeGridAdapter
import com.animedia.app.adapter.EpisodeCardAdapter
import com.animedia.app.data.AmdParser
import com.animedia.app.model.AnimeItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val mainScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerViews()
        setupBottomNav()
        setupSearch()

        loadHomepage()
    }

    private fun loadHomepage() {
        mainScope.launch {
            showLoading(true)
            val html = withContext(Dispatchers.IO) {
                try { AmdParser.fetchUrl("https://amd.online/") }
                catch (e: Exception) { "" }
            }

            if (html.isEmpty()) {
                showLoading(false)
                Toast.makeText(this@MainActivity, "Ошибка загрузки данных. Проверьте интернет.", Toast.LENGTH_LONG).show()
                return@launch
            }

            val newEpisodes = withContext(Dispatchers.IO) {
                AmdParser.parseNewEpisodes(html)
            }
            val allAnime = withContext(Dispatchers.IO) {
                AmdParser.parseAnimeList(html)
            }

            withContext(Dispatchers.Main) {
                updateNewEpisodes(newEpisodes)
                updateTodayReleases(newEpisodes.takeLast(4))
                updateAnimeGrid(allAnime)
                showLoading(false)
            }
        }
    }

    private fun updateNewEpisodes(items: List<AnimeItem>) {
        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerNewEpisodes)
        val adapter = EpisodeCardAdapter(items, this)
        recycler.adapter = adapter
    }

    private fun updateTodayReleases(items: List<AnimeItem>) {
        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerTodayReleases)
        val adapter = EpisodeCardAdapter(items, this)
        recycler.adapter = adapter
    }

    private fun updateAnimeGrid(items: List<AnimeItem>) {
        val unique = items.distinctBy { it.id }
        val grid = unique.filter { it.posterUrl != null && it.posterUrl!!.isNotEmpty() }
        val recycler = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerNewAnime)
        val adapter = AnimeGridAdapter(grid, this)
        recycler.adapter = adapter
    }

    private fun showLoading(show: Boolean) {
        // Could add shimmer loading later
    }

    private fun setupRecyclerViews() {
        val recyclerNewEpisodes = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerNewEpisodes)
        recyclerNewEpisodes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val recyclerTodayReleases = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerTodayReleases)
        recyclerTodayReleases.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val recyclerNewAnime = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerNewAnime)
        recyclerNewAnime.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)

        // Empty collections section (hide for now)
        val recyclerCollections = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerCollections)
        recyclerCollections.visibility = View.GONE
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadHomepage()
                    true
                }
                R.id.nav_catalog -> {
                    Toast.makeText(this, "Каталог", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_search -> {
                    Toast.makeText(this, "Поиск", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_favorites -> {
                    Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    Toast.makeText(this, "Профиль", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupSearch() {
        val searchBar = findViewById<View>(R.id.searchBar)
        searchBar.setOnClickListener {
            Toast.makeText(this, "Поиск аниме...", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
