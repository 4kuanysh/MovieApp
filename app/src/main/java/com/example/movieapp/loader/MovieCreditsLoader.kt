package com.example.movieapp.loader

import com.example.movieapp.MovieCreditsLoadListener
import com.example.movieapp.api.MovieService
import com.example.movieapp.model.MovieCredits
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieCreditsLoader(val listener: MovieCreditsLoadListener): Callback<MovieCredits> {

    fun loadMovieCredits(movie_id: Int) {
        MovieService.movieApi.getMovieCredits(
            movie_id,
            MovieService.API_KEY
        ).enqueue(this)
    }

    override fun onFailure(call: Call<MovieCredits>, t: Throwable) {
        listener.onMovieCreditsError(t)
    }

    override fun onResponse(call: Call<MovieCredits>, response: Response<MovieCredits>) {
        listener.onMovieCreditsLoaded(response.body()!!)
    }

}