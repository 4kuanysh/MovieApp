package com.example.movieapp.loader

import com.example.movieapp.GenresLoadListener
import com.example.movieapp.api.MovieService
import com.example.movieapp.model.Genre
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GenresLoader(val listener: GenresLoadListener): Callback<Genre> {

    fun loadGenres(language: String) {
        MovieService.movieApi.getGenres(
            MovieService.API_KEY,
            language
        ).enqueue(this)
    }

    override fun onFailure(call: Call<Genre>, t: Throwable) {
        listener.onGenresLoadError(t)
    }

    override fun onResponse(call: Call<Genre>, response: Response<Genre>) {
        listener.onGenresLoaded(response.body()!!.genres)
    }

}