package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.movieapp.*
import com.example.movieapp.adapter.GenresAdapter
import com.example.movieapp.adapter.MoviesAdapter
import com.example.movieapp.loader.GenresLoader
import com.example.movieapp.loader.MoviesLoader
import com.example.movieapp.model.Genre
import com.example.movieapp.model.MovieResults
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    MovieResultsLoadListener, MovieResultsClickListener, GenresLoadListener, GenreClickListener{

    private val popularMoviesLoader by lazy { MoviesLoader(this) }
    private val popularMovieAdapter by lazy { MoviesAdapter() }
    private val topRatedMoviesLoader by lazy { MoviesLoader(this) }
    private val topRatedMovieAdapter by lazy { MoviesAdapter() }
    private val nowPlayingMoviesLoader by lazy { MoviesLoader(this) }
    private val nowPlayingMovieAdapter by lazy { MoviesAdapter() }
    private val upComingMoviesLoader by lazy { MoviesLoader(this) }
    private val upComingMovieAdapter by lazy { MoviesAdapter() }
    private val genresLoader by lazy { GenresLoader(this) }
    private val genresAdapter by lazy { GenresAdapter() }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val favoriteMovieCollection by lazy { firebaseFirestore.collection(FAVORITE_MOVIES_COLLECTION)}

    companion object {
        private const val POPULAR = "popular"
        private const val TOP_RATED = "top_rated"
        private const val NOW_PLAYING = "now_playing"
        private const val UPCOMING = "upcoming"
        private const val LATEST = "latest"

        private const val FAVORITE_MOVIES_COLLECTION = "FavoriteMovies"
        fun start(context: Context) =
            context.startActivity(Intent(context, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        initUI()
    }

    private fun initUI() {


        setLogInOutButton()
        setMyMovieListButton()

        loadPopularMovies()
        loadTopRatedMovies()
        nowPlayingMovies()
        upComingMovies()
        loadGenres()
    }

    private fun setMyMovieListButton(){
        val user = firebaseAuth.currentUser
        my_movie_list_button.setOnClickListener {
            if (user != null) {
                    FavoritesMoviesListActivity.start(this)
            } else {
                Toast.makeText(this, getString(R.string.you_must_login), Toast.LENGTH_LONG).show()
            }
        }


    }

    private fun setLogInOutButton() {
        val user = firebaseAuth.currentUser

        if (user != null) {
            log_in_out_button.text = getString(R.string.log_out_button_text)
            log_in_out_button.background = getDrawable(R.drawable.logout_btn_bg)
            log_in_out_button.setTextColor(getColor(R.color.logout_button_text))
            log_in_out_button.setOnClickListener {
                firebaseAuth.signOut()
                MainActivity.start(this)
                finish()
            }
        } else {
            log_in_out_button.text = getString(R.string.login_button_text)
            log_in_out_button.background = getDrawable(R.drawable.login_btn_bg)
            log_in_out_button.setTextColor(getColor(R.color.white))
            log_in_out_button.setOnClickListener {
                LoginActivity.start(this)
            }
        }
    }

    private fun loadPopularMovies() {
        //set adapter settings
        popularMovieAdapter.setMovieClickListener(this)
        with(popular_movie_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularMovieAdapter
        }

        //Call loader
        popularMoviesLoader.loadMovies(POPULAR, getString(R.string.language))
    }

    private fun loadTopRatedMovies() {
        //set adapter settings
        topRatedMovieAdapter.setMovieClickListener(this)
        with(top_rated_movie_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = topRatedMovieAdapter
        }

        //Call loader
        topRatedMoviesLoader.loadMovies(TOP_RATED, getString(R.string.language))

    }

    private fun nowPlayingMovies() {
        //set adapter settings
        nowPlayingMovieAdapter.setMovieClickListener(this)
        with(now_playing_movie_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = nowPlayingMovieAdapter
        }

        //Call loader
        nowPlayingMoviesLoader.loadMovies(NOW_PLAYING, getString(R.string.language))

    }

    private fun upComingMovies() {
        //set adapter settings
        upComingMovieAdapter.setMovieClickListener(this)
        with(upcoming_movie_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = upComingMovieAdapter
        }

        //Call loader
        upComingMoviesLoader.loadMovies(UPCOMING, getString(R.string.language))

    }

    private fun loadGenres() {
        genresAdapter.setGenreClickListener(this)
        with(geners_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapter
        }

        genresLoader.loadGenres(getString(R.string.language))
    }

    override fun onMovieResultsLoaded(movieResults: MovieResults, category: String) {
        val listOfMovies = movieResults.results
        Log.d("category", category )
        Log.d("categoryList", listOfMovies.toString())

        when(category) {
            POPULAR -> popularMovieAdapter.setMovies(listOfMovies)
            TOP_RATED -> topRatedMovieAdapter.setMovies(listOfMovies)
            NOW_PLAYING -> nowPlayingMovieAdapter.setMovies(listOfMovies)
            UPCOMING -> upComingMovieAdapter.setMovies(listOfMovies)
        }
    }

    override fun onMovieResultsLoadError(throwable: Throwable) {
        Log.d("taaag",throwable.message)
    }

    override fun onMovieClicked(movie: MovieResults.Results) {
        Toast.makeText(this, movie.id.toString(), Toast.LENGTH_LONG).show()

        MovieDetailActivity.start(this, movie.id)
    }

    override fun onGenresLoaded(genres: List<Genre.Genre>) {
        Log.d("genres", "genres loading")
        Log.d("genres", genres.toString())
        genresAdapter.setGenres(genres)
    }

    override fun onGenresLoadError(throwable: Throwable) {
        Log.d("taaag", throwable.message)
    }

    override fun onGenreClicked(genre: Genre.Genre) {
        Log.d("onGenreClicked", "GenreClicked")
        Toast.makeText(this, genre.name, Toast.LENGTH_LONG).show()
        GenreMovieListActivity.start(this, genre.id)
    }
}
