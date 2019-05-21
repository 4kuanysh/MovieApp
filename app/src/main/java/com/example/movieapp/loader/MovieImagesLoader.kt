package com.example.movieapp.loader

import android.util.Log
import com.example.movieapp.MovieImagesLoadListener
import com.example.movieapp.api.MovieService
import com.example.movieapp.model.MovieImages
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieImagesLoader(private val listener: MovieImagesLoadListener): Callback<MovieImages> {

    fun loadMovieImages(movie_id: Int, language: String) {
        val nullLanguage = "null, $language"

        Log.d("ggg", movie_id.toString())

        MovieService.movieApi.getMovieImages(
            movie_id,
            MovieService.API_KEY,
            nullLanguage
        ).enqueue(this)

        Log.d("ggg", movie_id.toString()+"aaa")

    }

    override fun onFailure(call: Call<MovieImages>, t: Throwable) {
        listener.onMovieImagesError(t)
    }

    override fun onResponse(call: Call<MovieImages>, response: Response<MovieImages>) {
        listener.onMovieImagesLoaded(response.body()!!)
    }

}