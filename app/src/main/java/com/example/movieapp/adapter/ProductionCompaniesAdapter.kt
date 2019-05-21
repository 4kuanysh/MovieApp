package com.example.movieapp.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.movieapp.R
import com.example.movieapp.model.MovieDetail
import kotlinx.android.synthetic.main.layout_item_production_companies.view.*

class ProductionCompaniesAdapter(private val movies: ArrayList<MovieDetail.ProductionCompany> = ArrayList())
    : RecyclerView.Adapter<ProductionCompaniesAdapter.ProductionCompaniesViewHolder>() {

    companion object {
        private const val BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w500"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProductionCompaniesViewHolder(
            LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_production_companies, parent, false))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ProductionCompaniesViewHolder, position: Int) {
        Log.d("taaag", position.toString())

        holder.bindMovie(movies[position])
    }

    inner class ProductionCompaniesViewHolder(val root: View): RecyclerView.ViewHolder(root) {

        fun bindMovie(productionCompany: MovieDetail.ProductionCompany) {



            with(root) {

                if (productionCompany.logoPath != null) {
                    val posterUrl = BASE_IMAGE_URL + productionCompany.logoPath

                    Glide
                        .with(context)
                        .load(posterUrl)
                        .apply(RequestOptions.placeholderOf(R.drawable.loader))
                        .into(production_companies_image)
                }
                production_companies_name.text = productionCompany.name
            }

        }
    }

    fun setProductionCompanies(data: List<MovieDetail.ProductionCompany>) {
        movies.clear()
        movies.addAll(data)
        notifyDataSetChanged()
    }


}