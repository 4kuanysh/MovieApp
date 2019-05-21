package com.example.movieapp

import com.example.movieapp.model.MovieDetail

interface MovieDetailLoadListener {
    fun onMovieDetailLoaded(movie: MovieDetail)
    fun onMovieDetailError(throwable: Throwable)
}