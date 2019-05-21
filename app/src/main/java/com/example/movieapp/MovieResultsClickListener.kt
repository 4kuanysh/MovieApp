package com.example.movieapp

import com.example.movieapp.model.MovieResults

interface MovieResultsClickListener {

    fun onMovieClicked(movie: MovieResults.Results)

}