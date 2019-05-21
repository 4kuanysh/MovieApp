package com.example.movieapp

import com.example.movieapp.model.MovieResults

interface MovieResultsLoadListener {
    fun onMovieResultsLoaded(movieResults: MovieResults, category: String)
    fun onMovieResultsLoadError(throwable: Throwable)
}