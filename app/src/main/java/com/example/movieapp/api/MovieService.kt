package com.example.movieapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieService {

    const val BASE_URL = "https://api.themoviedb.org/"
    const val API_KEY = "99656db9dc7d5215d79e263a9e7f346d"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val movieApi = retrofit.create(MovieApi::class.java)

}