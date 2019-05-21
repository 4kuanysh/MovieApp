package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.movieapp.MovieResultsClickListener
import com.example.movieapp.MovieResultsLoadListener
import com.example.movieapp.R
import com.example.movieapp.adapter.VerticalMoviesAdapter
import com.example.movieapp.loader.MoviesLoader
import com.example.movieapp.model.MovieResults
import kotlinx.android.synthetic.main.activity_genre_movie_list.*

class GenreMovieListActivity : AppCompatActivity(), MovieResultsLoadListener, MovieResultsClickListener {

    private val verticalMoviesAdapter by lazy { VerticalMoviesAdapter() }
    private val moviesLoader by lazy { MoviesLoader(this) }

    companion object {
        private const val GENRE_ID = "genre_id"

        fun start(context: Context, genre_id: Int) =
                context.startActivity(Intent(context, GenreMovieListActivity::class.java).apply {
                    putExtra(GENRE_ID, genre_id)
                })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genre_movie_list)

        initUI()
    }

    private fun initUI() {
        loadMovies()
    }

    private fun loadMovies() {
        //Adapter settings
        verticalMoviesAdapter.setMovieClickListener(this)
        with(filter_movies_list) {
            layoutManager = LinearLayoutManager(context)
            adapter = verticalMoviesAdapter
        }

        //Call loader
        moviesLoader.loadMoviesByGenre(intent.getIntExtra(GENRE_ID, 0), getString(R.string.language))
    }

    override fun onMovieResultsLoaded(movieResults: MovieResults, category: String) {
        Log.d("filterMovies", movieResults.toString())
        val listOfMovies = movieResults.results
        verticalMoviesAdapter.setMovies(listOfMovies)
    }

    override fun onMovieResultsLoadError(throwable: Throwable) {
        Log.d("taaag", throwable.message)
    }

    override fun onMovieClicked(movie: MovieResults.Results) {
        MovieDetailActivity.start(this, movie.id)
    }


}
