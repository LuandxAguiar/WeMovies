package com.example.wemovies.api;

import com.example.wemovies.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("movies")
    fun getMovies(): Call<MovieResponse>
}