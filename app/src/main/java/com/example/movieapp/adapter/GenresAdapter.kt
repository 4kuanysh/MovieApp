package com.example.movieapp.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.GenreClickListener
import com.example.movieapp.R
import com.example.movieapp.model.Genre
import kotlinx.android.synthetic.main.layout_item_button_genre.view.*

class GenresAdapter(private val genres: ArrayList<Genre.Genre> = ArrayList()):
        RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    private lateinit var genreClickListener: GenreClickListener

    override fun onCreateViewHolder(perent: ViewGroup, viewType: Int) =
        GenreViewHolder(LayoutInflater.from(perent.context)
            .inflate(R.layout.layout_item_button_genre, perent, false))

    override fun getItemCount(): Int = genres.size

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bindGenre(genres[position])
    }

    inner class GenreViewHolder(val root: View): RecyclerView.ViewHolder(root) {

        fun bindGenre(genre: Genre.Genre) {
            with(root) {
                genre_button.text = genre.name

                genre_button.setOnClickListener {
                    genreClickListener.onGenreClicked(genre)
                }
            }

        }
    }

    fun setGenres(data: List<Genre.Genre>) {
        genres.clear()
        genres.addAll(data)
        notifyDataSetChanged()
    }

    fun setGenreClickListener(listener: GenreClickListener) {
        genreClickListener = listener
    }

}