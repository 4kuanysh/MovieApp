package com.example.movieapp

import com.example.movieapp.model.MovieCredits

interface MovieCreditsLoadListener{
    fun onMovieCreditsLoaded(movieCredits: MovieCredits)
    fun onMovieCreditsError(throwable: Throwable)
}