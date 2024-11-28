package com.example.wemovies.activity;

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wemovies.R
import com.example.wemovies.adapter.MovieAdapter
import com.example.wemovies.api.RetrofitClient
import com.example.wemovies.manager.CartManager
import com.example.wemovies.model.Movie
import com.example.wemovies.response.MovieResponse
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity(), MovieAdapter.OnAddToCartListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MovieAdapter
    private lateinit var cartManager: CartManager
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.hide()
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_home
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
        updateCartUI()
        // Configuração do RecyclerView e Adapter
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MovieAdapter(CartManager, this) // Passa cartManager para o adapter
        recyclerView.adapter = adapter

        // Carrega os filmes
        fetchMovies()
    }

    // Função para atualizar o badge no carrinho
    private fun updateCartUI() {
        val badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_cart)
        val totalItems = CartManager.getTotalItemCount()

        if (totalItems > 0) {
            badge.isVisible = true
            badge.number = totalItems
        } else {
            badge.isVisible = false
        }
    }

    // Implementação do método da interface OnAddToCart
    override fun onAddToCart(movie: Movie) {
        CartManager.addToCart(movie)
        updateCartUI()
    }

    private fun fetchMovies() {
        val apiService = RetrofitClient.apiService
        val call = apiService.getMovies()

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()?.products

                    if (!movies.isNullOrEmpty()) {
                        recyclerView.visibility = View.VISIBLE
                        adapter.setMovies(movies)
                    } else {
                        openEmptyStateActivity()
                    }
                } else {
                    openEmptyStateActivity()
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                openEmptyStateActivity()
            }
        })
    }

    private fun openEmptyStateActivity() {
        // Abre a EmptyStateActivity
        val intent = Intent(this, EmptyStateActivity::class.java)
        startActivity(intent)
    }
}