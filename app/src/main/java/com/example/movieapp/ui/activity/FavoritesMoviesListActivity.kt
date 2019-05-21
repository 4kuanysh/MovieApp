package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.movieapp.FavoriteMovieClickListener
import com.example.movieapp.R
import com.example.movieapp.adapter.FavoritesMoviesAdaptor
import com.example.movieapp.model.FavoriteMovie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_favorites_movies_list.*

class FavoritesMoviesListActivity : AppCompatActivity(), FavoriteMovieClickListener {

    private val favoritesMoviesAdaptor by lazy { FavoritesMoviesAdaptor() }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val favoriteMovieCollection by lazy { firebaseFirestore.collection(FAVORITE_MOVIES_COLLECTION)}
    private val user = firebaseAuth.currentUser

    companion object {
        private const val FAVORITE_MOVIES_COLLECTION = "FavoriteMovies"
        private const val USER_EMAIL = "user_email"



        fun start(context: Context) =
            context.startActivity(Intent(context, FavoritesMoviesListActivity::class.java))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites_movies_list)

        initUI()
    }

    private fun initUI() {
        favoriteMovieCollection
            .whereEqualTo(USER_EMAIL, user?.email)
            .get()
            .addOnCompleteListener {
                task ->
                kotlin.run {
                    if (task.isSuccessful) {
                        val result = task.result
                        val movies = ArrayList<FavoriteMovie>()

                        result!!.forEach {
                                document -> movies.add(document.toObject(FavoriteMovie::class.java))
                        }
                        Log.d("favmov", "firebase()")

                        favoritesMoviesAdaptor.setMovies(movies)
                        favoritesMoviesAdaptor.setMovieClickListener(this)
                        with(favorites_movies_list) {
                            layoutManager = LinearLayoutManager(context)
                            adapter = favoritesMoviesAdaptor
                        }
                    }
                }
            }

    }

    override fun onMovieClicked(movie: FavoriteMovie) {
        MovieDetailActivity.start(this, movie.movie_id)
    }


}
