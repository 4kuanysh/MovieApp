package com.example.movieapp

import com.example.movieapp.model.MovieImages

interface MovieImagesLoadListener {
    fun onMovieImagesLoaded(movieImages: MovieImages)
    fun onMovieImagesError(throwable: Throwable)
}
