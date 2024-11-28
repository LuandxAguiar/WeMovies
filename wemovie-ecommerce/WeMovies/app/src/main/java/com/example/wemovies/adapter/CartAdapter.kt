package com.example.wemovies.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wemovies.R
import com.example.wemovies.manager.CartManager
import com.example.wemovies.model.CartItem
import java.text.SimpleDateFormat
import java.util.*

class CartAdapter(
    private val cartItems: List<CartItem>,
    private val cartManager: CartManager,
    private val updateTotal: () -> Unit,
    private val onCartEmpty: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        private val moviePrice: TextView = itemView.findViewById(R.id.itemSubtotal)
        private val quantityText: TextView = itemView.findViewById(R.id.quantity)
        private val increaseButton: ImageButton = itemView.findViewById(R.id.increaseButton)
        private val decreaseButton: ImageButton = itemView.findViewById(R.id.decreaseButton)
        private val addedDateTextView: TextView = itemView.findViewById(R.id.addedDate)
        private val removeButton: ImageButton = itemView.findViewById(R.id.removeButton)

        fun bind(cartItem: CartItem) {
            movieTitle.text = cartItem.movie.title
            quantityText.text = cartItem.quantity.toString()
            moviePrice.text = String.format("R$ %.2f", cartItem.movie.price * cartItem.quantity)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(cartItem.addedDate)
            addedDateTextView.text = "Adicionado em $formattedDate"
            Glide.with(itemView.context)
                .load(cartItem.movie.image)
                .into(movieImage)
            //acrecenta Filme
            increaseButton.setOnClickListener {
                cartManager.addToCart(cartItem.movie)
                quantityText.text = cartManager.getQuantity(cartItem.movie).toString()
                updateTotal()
            }
            //diminui Filme
            decreaseButton.setOnClickListener {
                cartManager.decreaseFromCart(cartItem.movie)
                quantityText.text = cartManager.getQuantity(cartItem.movie).toString()
                updateTotal()
                if (cartManager.getCartItems().isEmpty()) {
                    onCartEmpty()
                }
            }
            removeButton.setOnClickListener {
                cartManager.removeFromCart(cartItem.movie)
                notifyItemRemoved(adapterPosition)
                updateTotal()
                checkIfCartIsEmpty()
            }
        }
        private fun checkIfCartIsEmpty() {
            if (cartManager.getCartItems().isEmpty()) {
                onCartEmpty()
            }
        }

    }
}