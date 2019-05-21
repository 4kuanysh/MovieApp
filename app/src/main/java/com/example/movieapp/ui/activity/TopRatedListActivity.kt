package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.movieapp.MovieResultsClickListener
import com.example.movieapp.MovieResultsLoadListener
import com.example.movieapp.R
import com.example.movieapp.adapter.MoviesAdapter
import com.example.movieapp.loader.MoviesLoader
import com.example.movieapp.model.MovieResults
import kotlinx.android.synthetic.main.activity_top_rated_list.*

class TopRatedListActivity : AppCompatActivity(), MovieResultsLoadListener, MovieResultsClickListener {

    companion object {
        private const val TOP_RATED_TITLE = "Top rated"
        private const val TOP_RATED = "top_rated"
        private const val LANGUAGE_EN_US = "en-US"

        fun start(context: Context) {
            context.startActivity(Intent(context, TopRatedListActivity::class.java))
        }
    }

    private val moviesLoader by lazy { MoviesLoader(this) }
    private val moviesAdapter by lazy { MoviesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_rated_list)

        initUI()
    }

    private fun initUI() {
        loadTopRatedMovies()
    }

    private fun loadTopRatedMovies() {
        //set adapter settings
        moviesAdapter.setMovieClickListener(this)
        with(top_rated_movie_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter
        }

        //set title for list
        title_category.text = TOP_RATED_TITLE

        //Call loader
        moviesLoader.loadMovies(TOP_RATED, LANGUAGE_EN_US)

    }


    override fun onMovieResultsLoaded(movieResults: MovieResults, category: String) {
        val listOfMovies = movieResults.results
        moviesAdapter.setMovies(listOfMovies)
    }

    override fun onMovieResultsLoadError(throwable: Throwable) {
        Log.d("Movie results load error", throwable.message)
    }

    override fun onMovieClicked(movie: MovieResults.Results) {
        Toast.makeText(this, movie.id.toString(), Toast.LENGTH_LONG).show()
        MovieDetailActivity.start(this, movie.id)
    }


}
