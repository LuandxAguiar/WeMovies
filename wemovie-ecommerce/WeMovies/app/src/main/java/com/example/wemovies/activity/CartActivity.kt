package com.example.wemovies.activity;

import android.app.ProgressDialog
import android.app.ProgressDialog.show
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wemovies.R
import com.example.wemovies.adapter.CartAdapter
import com.example.wemovies.manager.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var finalizeOrderButton: Button
    private lateinit var loadingProgressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        supportActionBar?.hide()
        finalizeOrderButton = findViewById(R.id.finalizeOrderButton)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_cart
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                R.id.navigation_cart -> {
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
        // Verifica se o carrinho está vazio
        if (CartManager.getCartItems().isEmpty()) {
            val intent = Intent(this, EmptyStateActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            setupCartUI()
        }
        // Adiciona um listener para atualizar o badge quando houver mudança no carrinho
        CartManager.addCartChangeListener {
            updateCartBadge()
        }

        // Inicializa o badge com o valor atual do carrinho
        updateCartBadge()
        // Configura o clique no botão para exibir o loading e abrir a tela de confirmação
        finalizeOrderButton.setOnClickListener {
            showLoading()
        }
    }

    private fun setupCartUI() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicialize o CartAdapter com os itens do carrinho, o CartManager e a função de atualização do total
        val cartAdapter = CartAdapter(
            CartManager.getCartItems(),
            CartManager,
            { updateTotalPrice() },
            { navigateToEmptyState() }
        )
        recyclerView.adapter = cartAdapter

        // Atualiza o valor total do carrinho inicialmente
        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalPriceTextView: TextView = findViewById(R.id.totalPrice)
        val total = CartManager.getTotalPrice()
        totalPriceTextView.text = String.format("R$ %.2f", total)
    }
    private fun navigateToEmptyState() {
        val intent = Intent(this, EmptyStateActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun showLoading() {
        // loading
        findViewById<View>(R.id.loadingOverlay).visibility = View.VISIBLE
        loadingProgressBar.visibility = View.VISIBLE

        // carregamento por 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            //Esconder loading
            loadingProgressBar.visibility = View.GONE
            findViewById<View>(R.id.loadingOverlay).visibility = View.GONE
            navigateToOrderConfirmation()
        }, 3000)
    }

    private fun navigateToOrderConfirmation() {
        val intent = Intent(this, OrderConfirmationActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun updateCartBadge() {
        val cartItemCount = CartManager.getTotalItemCount()
        val badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_cart)

        if (cartItemCount > 0) {
            badge.isVisible = true
            badge.number = cartItemCount
        } else {
            badge.isVisible = false
        }
    }
}
