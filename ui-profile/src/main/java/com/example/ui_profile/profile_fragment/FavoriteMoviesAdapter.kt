package com.example.moviestmdb.ui_movies.popular

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Movie
import com.example.moviestmdb.util.TmdbImageUrlProvider
import com.example.ui_profile.databinding.ListItemFavoriteMovieBinding

class FavoriteMoviesAdapter(  private val tmdbImageUrlProvider: TmdbImageUrlProvider,
                                           private val onItemClickListener: (movieId: Int) -> Unit,
) : ListAdapter<Movie, FavoriteMovieCarrouselViewHolder>(FavoriteMovieDiff) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteMovieCarrouselViewHolder {
        val binding = ListItemFavoriteMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FavoriteMovieCarrouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteMovieCarrouselViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let {
            holder.binding.title.text = it.title
            holder.binding.subtitle.text = it.releaseDate
            it.voteAverage?.let { voteAverage ->
                holder.binding.popularityBadge.progress = (voteAverage * 10).toInt()
            }

            entry.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width
                        )
                    )
                    .into(holder.binding.imageView)
            }
            holder.itemView.setOnClickListener {
                onItemClickListener(entry.id)
            }
        }
    }
}

class FavoriteMovieCarrouselViewHolder(
    internal val binding: ListItemFavoriteMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object FavoriteMovieDiff : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean {
        return oldItem == newItem
    }
}