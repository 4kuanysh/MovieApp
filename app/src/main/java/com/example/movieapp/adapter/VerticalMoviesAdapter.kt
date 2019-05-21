package com.example.movieapp.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.MovieResultsClickListener
import com.example.movieapp.R
import com.example.movieapp.model.MovieResults
import kotlinx.android.synthetic.main.layout_vertical_item_movie.view.*

class VerticalMoviesAdapter(private val movies: ArrayList<MovieResults.Results> = ArrayList())
    : RecyclerView.Adapter<VerticalMoviesAdapter.MovieViewHolder>() {

    companion object {
        private const val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500"
    }

    private lateinit var movieClickListener: MovieResultsClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_vertical_item_movie, parent, false))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Log.d("taaag", position.toString())

        holder.bindMovie(movies[position])
    }

    inner class MovieViewHolder(val root: View): RecyclerView.ViewHolder(root) {

        fun bindMovie(movie: MovieResults.Results) {

            val posterUrl = BASE_IMAGE_URL + movie.poster_path

            Log.d("imageUrl", posterUrl)

            with(root) {
                Glide
                    .with(context)
                    .load(posterUrl)
                    .apply(RequestOptions.placeholderOf(R.drawable.loader) )
                    .into(movie_image)

                movie_release_date.text = movie.release_date
                movie_title.text = movie.title
                movie_average_vote.text = movie.vote_average.toString()

                setOnClickListener{
                    movieClickListener.onMovieClicked(movie)
                }
            }
        }
    }

    fun setMovieClickListener(listener: MovieResultsClickListener) {
        movieClickListener = listener
    }

    fun setMovies(data: List<MovieResults.Results>) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }


}