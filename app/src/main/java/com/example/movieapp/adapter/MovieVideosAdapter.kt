package com.example.movieapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.movieapp.R
import com.example.movieapp.model.MovieVideos
import kotlinx.android.synthetic.main.layout_item_trailer.view.*

class MovieVideosAdapter(mContext: Context, private val videos: ArrayList<MovieVideos.Result> = ArrayList())
    : RecyclerView.Adapter<MovieVideosAdapter.VideosViewHolder>() {

    val mContext: Context = mContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VideosViewHolder( LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_item_trailer, parent, false))


    override fun getItemCount(): Int = videos.size

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bindVideo(videos[position])
    }

    fun setVideos(data: List<MovieVideos.Result>) {
        videos.clear()
        videos.addAll(data)
        notifyDataSetChanged()
    }

    inner class VideosViewHolder(val root: View): RecyclerView.ViewHolder(root) {

        fun bindVideo(video: MovieVideos.Result){

            with(root) {

                video_title.text = video.name

                setOnClickListener{
                    val videoKey = video.key
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://$videoKey"))
                    intent.putExtra("VIDEO_KEY", videoKey)
                    mContext.startActivity(intent)
                }

            }
        }
    }
}