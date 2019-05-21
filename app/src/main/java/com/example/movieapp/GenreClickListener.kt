package com.example.movieapp

import com.example.movieapp.model.Genre

interface GenreClickListener {
    fun onGenreClicked(genre: Genre.Genre)
}