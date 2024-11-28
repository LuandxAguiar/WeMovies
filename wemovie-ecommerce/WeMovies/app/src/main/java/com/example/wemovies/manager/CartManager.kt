package com.example.wemovies.manager;

import android.content.Context
import com.example.wemovies.model.CartItem
import com.example.wemovies.model.Movie

object CartManager {

    private val cartItems = mutableListOf<CartItem>()
    private val cartChangeListeners = mutableListOf<() -> Unit>()

    fun addToCart(movie: Movie) {
        val cartItem = cartItems.find { it.movie.id == movie.id }
        if (cartItem != null) {
            cartItem.quantity++
        } else {
            cartItems.add(CartItem(movie))
        }
        notifyCartChange()
    }

    fun decreaseFromCart(movie: Movie) {
        val cartItem = cartItems.find { it.movie.id == movie.id }
        if (cartItem != null) {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
            } else {
                cartItems.remove(cartItem)
            }
        }
    }

    fun getTotalPrice(): Double {
        return cartItems.fold(0.0) { total, item ->
            total + (item.movie.price * item.quantity)
        }
    }

    fun getQuantity(movie: Movie): Int {
        return cartItems.find { it.movie.id == movie.id }?.quantity ?: 0
    }

    fun getTotalItemCount(): Int {
        return cartItems.sumOf { it.quantity }
    }

    fun getCartItems(): List<CartItem> {
        return cartItems
    }
    fun removeFromCart(movie: Movie) {
        val cartItem = cartItems.find { it.movie.id == movie.id }
        if (cartItem != null) {
            cartItems.remove(cartItem)
        }
    }
    fun clearCart() {
        cartItems.clear()
    }
    fun addCartChangeListener(listener: () -> Unit) {
        cartChangeListeners.add(listener)
    }

    private fun notifyCartChange() {
        for (listener in cartChangeListeners) {
            listener()
        }
    }
}


