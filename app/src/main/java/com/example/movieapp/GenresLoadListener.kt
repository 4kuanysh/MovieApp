package com.example.movieapp

import com.example.movieapp.model.Genre

interface GenresLoadListener{
    fun onGenresLoaded(genres: List<Genre.Genre>)
    fun onGenresLoadError(throwable: Throwable)
}
