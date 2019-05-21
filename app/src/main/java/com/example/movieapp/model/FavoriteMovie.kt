package com.example.movieapp.model

data class FavoriteMovie(
    val movie_id: Int,
    val user_email: String,
    val title: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double
) {
    constructor(): this(1, "", "", "", "", 0.0)
}