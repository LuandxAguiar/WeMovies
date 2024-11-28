package com.example.wemovies.model;

import java.util.*

data class CartItem(
    val movie: Movie,
    var quantity: Int = 1,
    // Define a data de adição como a data atual
    val addedDate: Date = Date()

)
