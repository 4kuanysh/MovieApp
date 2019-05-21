package com.example.movieapp.api

import com.example.movieapp.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {

    @GET("3/movie/{category}")
    fun getMovies(
        @Path("category") category: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Call<MovieResults>

    @GET("3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<MovieDetail>

    @GET("3/genre/movie/list")
    fun getGenres(
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<Genre>

    @GET("3/discover/movie")
    fun getFilterMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("sort_by") sort_by: String,
        @Query("page") page: Int,
        @Query("with_genres") with_genres: Int
    ): Call<MovieResults>

    @GET("3/movie/{movie_id}/images")
    fun getMovieImages(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
     ): Call<MovieImages>

    @GET("3/movie/{movie_id}/recommendations")
    fun getRecommendations(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<MovieResults>

    @GET("3/movie/{movie_id}/similar")
    fun getSimilar(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<MovieResults>

    @GET("3/movie/{movie_id}/videos")
    fun getVideos(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("language") language: String
    ): Call<MovieVideos>

    @GET("3/movie/{movie_id}/credits")
    fun getMovieCredits(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Call<MovieCredits>


}