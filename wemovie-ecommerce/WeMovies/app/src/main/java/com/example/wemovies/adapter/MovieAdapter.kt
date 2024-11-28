package com.example.wemovies.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wemovies.R
import com.example.wemovies.manager.CartManager
import com.example.wemovies.model.Movie

class MovieAdapter(
    private val cartManager: CartManager,
    private val listener: OnAddToCartListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnAddToCartListener {
        fun onAddToCart(movie: Movie)
    }

    private var movies: List<Movie> = listOf()

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }

    fun setMovies(movies: List<Movie>) {
        this.movies = movies
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return movies.size + 1 // +1 para incluir o cabeçalho
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = inflater.inflate(R.layout.header_layout, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.item_movie, parent, false)
            MovieViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            val movie = movies[position - 1]
            val quantity = cartManager.getQuantity(movie)
            holder.bind(movie, quantity)
        }

    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {

        }
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val movieTitle: TextView = itemView.findViewById(R.id.movieTitle)
        private val moviePrice: TextView = itemView.findViewById(R.id.moviePrice)
        private val movieImage: ImageView = itemView.findViewById(R.id.movieImage)
        private val addToCartButton: Button = itemView.findViewById(R.id.addToCartButton)

        fun bind(movie: Movie, quantity: Int) {
            movieTitle.text = movie.title
            moviePrice.text = String.format("R$ %.2f", movie.price)
            addToCartButton.text = "$quantity Adicionar ao Carrinho"

            Glide.with(itemView.context).load(movie.image).into(movieImage)

            addToCartButton.setOnClickListener {
                listener.onAddToCart(movie)
                val updatedQuantity = cartManager.getQuantity(movie)
                addToCartButton.text = "$updatedQuantity Adicionar ao Carrinho"
                addToCartButton.backgroundTintList =
                    ContextCompat.getColorStateList(itemView.context, R.color.green)
            }
            val quantityInCart = cartManager.getQuantity(movie)
            if (quantityInCart > 0) {
                addToCartButton.text = "$quantityInCart Adicionar ao Carrinho"
                addToCartButton.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.green)
            } else {
                addToCartButton.text = "Adicionar ao Carrinho"
                addToCartButton.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.button)
            }

            addToCartButton.setOnClickListener {
                listener.onAddToCart(movie)
                addToCartButton.text = "${cartManager.getQuantity(movie)} Adicionar ao Carrinho"
                addToCartButton.backgroundTintList = ContextCompat.getColorStateList(itemView.context, R.color.green)
            }

        }
    }
}