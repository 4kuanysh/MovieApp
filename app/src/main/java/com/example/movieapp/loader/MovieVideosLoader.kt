package com.example.movieapp.loader

import com.example.movieapp.MovieVideosLoadListener
import com.example.movieapp.api.MovieService
import com.example.movieapp.model.MovieVideos
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieVideosLoader(private val listener: MovieVideosLoadListener): Callback<MovieVideos> {

    fun loadVideos(movie_id: Int, language: String) {
        MovieService.movieApi.getVideos(
            movie_id,
            MovieService.API_KEY,
            language
        ).enqueue(this)
    }

    override fun onFailure(call: Call<MovieVideos>, t: Throwable) {
        listener.onMovieVideosError(t)
    }

    override fun onResponse(call: Call<MovieVideos>, response: Response<MovieVideos>) {
        listener.onMovieVideosLoaded(response.body()!!)
    }
}