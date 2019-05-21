package com.example.movieapp.loader

import com.example.movieapp.MovieDetailLoadListener
import com.example.movieapp.api.MovieService
import com.example.movieapp.model.MovieDetail
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailLoader(val listener: MovieDetailLoadListener): Callback<MovieDetail> {

    fun loadMovieDetail(movie_id: Int, language: String) {
        MovieService.movieApi.getMovieDetail(
            movie_id,
            MovieService.API_KEY,
            language
        ).enqueue(this)
    }

    override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
        listener.onMovieDetailError(t)
    }

    override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
        listener.onMovieDetailLoaded(response.body()!!)
    }

}