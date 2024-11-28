package com.example.wemovies.model;


data class Movie(
        var id: Long? = null,
        var title: String? = null,
        var price: Float = 0.0f,
        var image: String? = null
)