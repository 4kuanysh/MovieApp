package com.example.movieapp.loader

import android.util.Log
import com.example.movieapp.MovieResultsLoadListener
import com.example.movieapp.api.MovieService
import com.example.movieapp.model.MovieResults
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoviesLoader(val listener: MovieResultsLoadListener): Callback<MovieResults>{

    companion object {
        const val RECOMMENDATIONS = "recommendations"
        const val SIMILAR = "similar"
    }
    private var incategory = ""

    fun loadMovies(category: String, language: String) {
        incategory = category

        MovieService.movieApi.getMovies(
            category,
            MovieService.API_KEY,
            language,
            1
        ).enqueue(this)
    }

    fun loadMoviesByGenre(genre: Int, language: String) {
        MovieService.movieApi.getFilterMovies(
            MovieService.API_KEY,
            language,
            "popularity.desc",
            1,
            genre
        ).enqueue(this)
    }

    fun loadRecommendations(movie_id: Int, language: String) {
        incategory = RECOMMENDATIONS

        MovieService.movieApi.getRecommendations(
            movie_id,
            MovieService.API_KEY,
            language
        ).enqueue(this)
    }

    fun loadSimilar(movie_id: Int, language: String) {
        incategory = SIMILAR

        MovieService.movieApi.getSimilar(
            movie_id,
            MovieService.API_KEY,
            language
        ).enqueue(this)
    }

    override fun onFailure(call: Call<MovieResults>, t: Throwable) {
        listener.onMovieResultsLoadError(t)
    }

    override fun onResponse(call: Call<MovieResults>, response: Response<MovieResults>) {
        Log.d("aaab", "Zdes'")
        listener.onMovieResultsLoaded(response.body()!!, incategory)
    }

}