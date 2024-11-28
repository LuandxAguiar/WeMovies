package com.example.wemovies.activity;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wemovies.R
import com.example.wemovies.manager.CartManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.*

class OrderConfirmationActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_confirmation)

        CartManager.clearCart()
        supportActionBar?.hide()
        // Inicialize o bottomNavigationView e o cartManager
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.navigation_cart

        // Configura o listener de navegação
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
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
        // Adiciona um listener para atualizar o badge quando houver mudança no carrinho
        CartManager.addCartChangeListener {
            updateCartBadge()
        }

        // Inicializa o badge com o valor atual do carrinho
        updateCartBadge()
        //home
        val reloadButton: Button = findViewById(R.id.reloadButton)
        reloadButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Encerra a tela de confirmação
        }

        // Define a data atual para exibição
        val txtOrderConfirm: TextView = findViewById(R.id.txtOrderConfirm)
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        txtOrderConfirm.text = "Compra realizada em $currentDate às $currentTime"
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
