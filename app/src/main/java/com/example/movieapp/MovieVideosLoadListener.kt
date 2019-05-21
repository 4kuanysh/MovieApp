package com.example.movieapp

import com.example.movieapp.model.MovieVideos

interface MovieVideosLoadListener {
    fun onMovieVideosLoaded(movieVideos: MovieVideos)
    fun onMovieVideosError(throwable: Throwable)
}