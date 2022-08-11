package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Genre
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.example.moviestmdb.ui_movies.databinding.DiscoverMoviesRowBinding
import com.example.moviestmdb.ui_movies.fragments.model.MovieAndGenre
import com.example.moviestmdb.util.TmdbImageUrlProvider
import com.google.android.material.chip.ChipGroup

class DiscoverMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<MovieAndGenre, DiscoverMovieViewHolder>(DiscoverEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverMovieViewHolder {
        val binding = DiscoverMoviesRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DiscoverMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DiscoverMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movieAndGenre ->
            holder.binding.movieTitle.text = movieAndGenre.movie.title
            holder.binding.movieInfo.text =
                "${movieAndGenre.movie.voteCount} votes • ${movieAndGenre.movie.releaseDate}"

            movieAndGenre.movie.voteAverage?.let { voteAverage ->
                holder.binding.popularityBadge.progress = (voteAverage * 10).toInt()
            }

            entry.movie.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.allMoviesRowImg)
            }

            val filteredList = movieAndGenre.genre.filter {
                movieAndGenre.movie.genreList.contains(it.id)
            }
            holder.binding.chipGroup.removeAllViews()
            addChips(holder.binding.chipGroup, filteredList)

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.movie.id)
            }
        }
    }
}

    fun addChips(chipGroup: ChipGroup, chips: List<Genre>) {
        chips.forEach { genre ->
            val chip = ChipBinding.inflate(LayoutInflater.from(chipGroup.context)).root
            chip.id = genre.id
            chip.text = genre.name
            chip.isCheckable = false
            chipGroup.addView(chip)
        }
    }

    class DiscoverMovieViewHolder(
        internal val binding: DiscoverMoviesRowBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object DiscoverEntryComparator : DiffUtil.ItemCallback<MovieAndGenre>() {
        override fun areItemsTheSame(
            oldItem: MovieAndGenre,
            newItem: MovieAndGenre
        ): Boolean {
            return oldItem.movie.id == newItem.movie.id
        }

        override fun areContentsTheSame(
            oldItem: MovieAndGenre,
            newItem: MovieAndGenre
        ): Boolean {
            return oldItem == newItem
        }
}