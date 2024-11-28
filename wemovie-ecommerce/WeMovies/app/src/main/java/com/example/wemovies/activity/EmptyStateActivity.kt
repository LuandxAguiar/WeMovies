package com.example.wemovies.activity;

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.wemovies.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class EmptyStateActivity : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_state)
        supportActionBar?.hide()
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Configura o listener de navegação
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                    true
                }
                // Encerra a Activity atual para evitar pilhas acumuladas
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
        val reloadButton: Button = findViewById(R.id.reloadButton)
        reloadButton.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            Toast.makeText(this, "Recarregando...", Toast.LENGTH_SHORT).show()
            finish() // Fecha a EmptyStateActivity e volta para a HomeActivity
        }
    }
}

