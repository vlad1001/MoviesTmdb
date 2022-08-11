package com.example.moviestmdb.ui_movies.fragments.fragments.now_playing_movies

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.Genre
import com.example.moviestmdb.ui_movies.databinding.ChipBinding
import com.example.moviestmdb.ui_movies.databinding.ListItemNowPlayingMovieBinding
import com.example.moviestmdb.ui_movies.fragments.model.MovieAndGenre
import com.example.moviestmdb.util.TmdbImageUrlProvider
import com.google.android.material.chip.ChipGroup

class NowPlayingMoviesAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<MovieAndGenre, NowPlayingMovieViewHolder>(NowPlayingEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayingMovieViewHolder {
        val binding = ListItemNowPlayingMovieBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NowPlayingMovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NowPlayingMovieViewHolder, position: Int) {
        val entry = getItem(position)
        entry?.let { movie ->
            holder.binding.nowPlayingTitle.text = movie.movie.title
            holder.binding.nowPlayingSubtitle.text =
                "${movie.movie.voteCount} votes • ${movie.movie.releaseDate}"

            movie.movie.voteAverage?.let { voteAverage ->
                holder.binding.nowPlayingPopularityBadge.progress = (voteAverage * 10).toInt()
            }


            entry.movie.posterPath?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.nowPlayingImage)
            }


            val filteredList = movie.genre.filter {
                movie.movie.genreList.contains(it.id)
            }
            holder.binding.chipGroup.removeAllViews()
            addChips(holder.binding.chipGroup, filteredList)

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.movie.id)
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
}

class NowPlayingMovieViewHolder(
    internal val binding: ListItemNowPlayingMovieBinding
) : RecyclerView.ViewHolder(binding.root)

object NowPlayingEntryComparator : DiffUtil.ItemCallback<MovieAndGenre>() {
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