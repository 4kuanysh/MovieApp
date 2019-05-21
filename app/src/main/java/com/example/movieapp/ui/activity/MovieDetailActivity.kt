package com.example.movieapp.ui.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.movieapp.*
import com.example.movieapp.R
import com.example.movieapp.adapter.HorizontalMovieCastAdapter
import com.example.movieapp.adapter.MoviesAdapter
import com.example.movieapp.adapter.MovieVideosAdapter
import com.example.movieapp.adapter.ProductionCompaniesAdapter
import com.example.movieapp.loader.*
import com.example.movieapp.model.*
import com.example.movieapp.model.MovieCredits.Cast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.smarteist.autoimageslider.*
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity(),
    MovieImagesLoadListener, MovieResultsLoadListener, MovieResultsClickListener,
    MovieDetailLoadListener, MovieVideosLoadListener, MovieCreditsLoadListener {



    private val movieImagesLoader by lazy { MovieImagesLoader(this) }
    private val recommendationsLoader by lazy { MoviesLoader(this) }
    private val recommendationsAdapter by lazy { MoviesAdapter() }
    private val movieDetailLoader by lazy { MovieDetailLoader(this) }
    private val movieVideosLoader by lazy {MovieVideosLoader(this)}
    private val movieVideosAdapter by lazy { MovieVideosAdapter(this) }
    private val productionCompaniesAdapter by lazy { ProductionCompaniesAdapter() }
    private val similarLoader by lazy { MoviesLoader(this) }
    private val similarAdapter by lazy { MoviesAdapter() }
    private val movieCreditsLoader by lazy { MovieCreditsLoader(this) }
    private val movieCastAdapter by lazy { HorizontalMovieCastAdapter() }

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val firebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val favoriteMovieCollection by lazy { firebaseFirestore.collection(FAVORITE_MOVIES_COLLECTION)}
    private val user = firebaseAuth.currentUser

    private var isFavorie: Boolean = false
    private var favoriteDocumentId = ""
    private lateinit var title: String
    private lateinit var poster_path: String
    private lateinit var release_date: String
    private var vote_average: Double = 0.0

    companion object {

        private const val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w780"
        private const val MOVIE_ID = "movie_id"
        private const val USER_EMAIL = "user_email"

        private const val FAVORITE_MOVIES_COLLECTION = "FavoriteMovies"

        fun start(context: Context, movie_id: Int) {
            context.startActivity(Intent(context, MovieDetailActivity::class.java).apply {
                putExtra(MOVIE_ID, movie_id)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        initUI()
    }

    private fun initUI() {
        movieDetailLoader.loadMovieDetail(intent.getIntExtra(MOVIE_ID, 0), getString(R.string.language))
        movieImagesLoader.loadMovieImages(intent.getIntExtra(MOVIE_ID, 0), getString(R.string.language))

        if (user != null) {
            Log.d("MovieToFavorite", "not null")
            identifyFavoriteMovie(intent.getIntExtra(MOVIE_ID, 0), user?.email!!)
        }

        favorite_button.setOnClickListener {
            if (user == null) {
                Toast.makeText(this, getString(R.string.you_must_login), Toast.LENGTH_LONG).show()
            } else {
                if (isFavorie) {
                    favorite_button.setImageResource(R.drawable.icon_bookmark_48_outline)
                    deleteMovieToFavorite()
                    isFavorie = false
                } else {
                    favorite_button.setImageResource(R.drawable.icon_bookmark_48_color)
                    saveMovieToFavorite(intent.getIntExtra(MOVIE_ID, 0), user?.email!!)
                    isFavorie = true
                }
            }
        }

        loadTrailers()
        loadRecommendations()
        loadSimilar()
        loadCast()
    }

    private fun identifyFavoriteMovie(movie_id: Int, user_email: String){
        favoriteMovieCollection
            .whereEqualTo(USER_EMAIL, user_email )
            .whereEqualTo(MOVIE_ID, movie_id )
            .get()
            .addOnCompleteListener {
                    task ->
                kotlin.run {
                    if (task.isSuccessful) {
                        val result = task.result

                        if (!result!!.documents.isEmpty()) {
                            favoriteDocumentId = result!!.documents[0].id // Identify ID if document to delete later
                            isFavorie = true
                            favorite_button.setImageResource(R.drawable.icon_bookmark_48_color)
                        }

                    }
                }
            }
    }

    private fun deleteMovieToFavorite() {
        favoriteMovieCollection.document(favoriteDocumentId).delete()
            .addOnCompleteListener {
                    task ->
                kotlin.run {
                    Log.d("MovieToFavorite", "deleteMovieToFavorite()")
                }
            }
    }

    private fun saveMovieToFavorite(movie_id: Int, user_email: String) {
        val favoriteMovie = FavoriteMovie(movie_id, user_email, title, poster_path, release_date, vote_average)
        Log.d("MovieToFavorite", "saveMovieToFavorite()")
        favoriteMovieCollection.add(favoriteMovie).addOnCompleteListener {
            task ->
            kotlin.run {
                if (task.isSuccessful) {
                    identifyFavoriteMovie(movie_id, user_email)
                    Toast.makeText(this, R.string.success_message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show()
                    Log.d("MovieToFavorite", task.exception?.message)
                }
            }
        }
    }

    private fun loadRecommendations() {
        Log.d("test", "loadRecommendations()")
        //set adapter settings
        recommendationsAdapter.setMovieClickListener(this)
        with(recommendation_movie_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationsAdapter
        }

        //Call loader
        recommendationsLoader.loadRecommendations(intent.getIntExtra(MOVIE_ID, 0), getString(R.string.language))
    }

    private fun loadSimilar() {
        Log.d("test", "loadSimilar()")
        //set adapter settings
        similarAdapter.setMovieClickListener(this)
        with(similar_movie_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarAdapter
        }

        //Call loader
        similarLoader.loadSimilar(intent.getIntExtra(MOVIE_ID, 0), getString(R.string.language))
    }

    private fun loadCast() {
        with(movie_cast_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieCastAdapter
        }

        movieCreditsLoader.loadMovieCredits(intent.getIntExtra(MOVIE_ID, 0))
    }

    private fun loadTrailers() {
        //set adapter settings
        with(video_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieVideosAdapter
        }

        //Call loader
        movieVideosLoader.loadVideos(intent.getIntExtra(MOVIE_ID, 0), getString(R.string.language))
    }

    override fun onMovieResultsLoaded(movieResults: MovieResults, category: String) {
        val listOfMovies = movieResults.results

        when(category) {
            MoviesLoader.RECOMMENDATIONS -> recommendationsAdapter.setMovies(listOfMovies)
            MoviesLoader.SIMILAR -> similarAdapter.setMovies(listOfMovies)
        }
    }

    override fun onMovieResultsLoadError(throwable: Throwable) {
        Log.d("taaag",throwable.message)
    }

    override fun onMovieClicked(movie: MovieResults.Results) {
        Toast.makeText(this, movie.id.toString(), Toast.LENGTH_LONG).show()
        MovieDetailActivity.start(this, movie.id)
    }

    override fun onMovieDetailLoaded(movie: MovieDetail) {
        Log.d("test", "onMovieDetailLoaded")
        movie_title.text = movie.title
        movie_overview.text = movie.overview
        movie_average_vote.text = movie.voteAverage.toString()
        movie_release_date.text = movie.releaseDate

        title = movie.title
        poster_path = movie.posterPath
        vote_average = movie.voteAverage
        release_date = movie.releaseDate

        with(production_companies_list) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = productionCompaniesAdapter
        }
        productionCompaniesAdapter.setProductionCompanies(movie.productionCompanies)

//        var production_companies_string:String = ""
//        for (i in 0..movie.productionCompanies.size - 1) {
//            if (i == 0)
//                production_companies_string += movie.productionCompanies[i].name
//            else
//                production_companies_string += ", ${movie.productionCompanies[i].name}"
//        }
//
//        production_companies_list.text = production_companies_string
    }

    override fun onMovieDetailError(throwable: Throwable) {
        Log.d("taaag",throwable.message)
    }

    private fun setSliderView(backdrops: List<MovieImages.Backdrop>) {
        var count = 0
        for (backdrop_url in backdrops) {
            if (count >= 10)
                break
            count++
            Log.d("ggg", backdrop_url.filePath)

            val sliderView = DefaultSliderView(this)
            sliderView.imageUrl = BASE_IMAGE_URL + backdrop_url.filePath
            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP)

            imageSlider.addSliderView(sliderView)
        }
    }

    override fun onMovieImagesLoaded(movieImages: MovieImages) {
        imageSlider.setIndicatorAnimation(IndicatorAnimations.SWAP)
        imageSlider.setSliderTransformAnimation(SliderAnimations.VERTICALFLIPTRANSFORMATION)
        imageSlider.scrollTimeInSec = 3
        Log.d("ggg", "inMovieImagesLoaded'")

        setSliderView(movieImages.backdrops)
    }

    override fun onMovieImagesError(throwable: Throwable) {
        Log.d("taaag",throwable.message)
    }

    override fun onMovieVideosLoaded(movieVideos: MovieVideos) {
        val listOfVideos = movieVideos.results
        movieVideosAdapter.setVideos(listOfVideos)
    }

    override fun onMovieVideosError(throwable: Throwable) {
        Log.d("error", throwable.message)
    }

    override fun onMovieCreditsLoaded(movieCredits: MovieCredits) {
        var cast = movieCredits.cast
        if (cast.size > 10)
            cast = cast.slice(0..10)

        movieCastAdapter.setCast(cast)
    }

    override fun onMovieCreditsError(throwable: Throwable) {
        Log.d("error", throwable.message)
    }
}
