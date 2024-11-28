package com.example.wemovies

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wemovies.activity.CartActivity
import com.example.wemovies.activity.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        // Define o item "Home" como selecionado por padrão
        bottomNavigationView.selectedItemId = R.id.navigation_home

        // Configura o listener usando o novo método setOnItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_cart -> {
                    val intent = Intent(this, CartActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.navigation_profile -> {
//                    val intent = Intent(this, ProfileActivity::class.java)
//                    startActivity(intent)
//                    finish()  // Encerra a Activity atual para evitar pilhas acumuladas
                    true
                }
                else -> false
            }
        }
    }
}
