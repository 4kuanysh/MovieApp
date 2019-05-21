package com.example.movieapp.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.model.MovieCredits
import kotlinx.android.synthetic.main.layout_item_movie_cast.view.*

class HorizontalMovieCastAdapter(private val cast: ArrayList<MovieCredits.Cast> = ArrayList())
    : RecyclerView.Adapter<HorizontalMovieCastAdapter.MovieCastViewHolder>(){

    companion object {
        private const val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieCastViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_movie_cast, parent, false))

    override fun getItemCount(): Int = cast.size

    override fun onBindViewHolder(holder: MovieCastViewHolder, position: Int) {
        holder.bindMovieCast(cast[position])
    }

    inner class MovieCastViewHolder(private val root: View): RecyclerView.ViewHolder(root) {

        fun bindMovieCast(cast: MovieCredits.Cast) {

            val profileUrl = BASE_IMAGE_URL + cast.profilePath

            with(root) {
                Glide
                    .with(context)
                    .load(profileUrl)
                    .into(profile_image)

                actor_name.text = cast.name
            }
        }
    }

    fun setCast(data: List<MovieCredits.Cast>) {
        cast.clear()
        cast.addAll(data)
        notifyDataSetChanged()
    }
}