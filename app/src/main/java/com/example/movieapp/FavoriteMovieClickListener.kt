package com.example.movieapp

import com.example.movieapp.model.FavoriteMovie

interface FavoriteMovieClickListener {
    fun onMovieClicked(movie: FavoriteMovie)

}