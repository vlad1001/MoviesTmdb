package com.example.moviestmdb.ui_people

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviestmdb.KnownFor
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.ui_people.databinding.PopularActorRowItemBinding
import com.example.moviestmdb.util.TmdbImageUrlProvider
import java.lang.StringBuilder


class PopularActorsAdapter(
    private val tmdbImageUrlProvider: TmdbImageUrlProvider,
    private val onItemClickListener: (movieId: Int) -> Unit,
) : PagingDataAdapter<PopularActor, PopularActorViewHolder>(PopularActorEntryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularActorViewHolder {
        val binding = PopularActorRowItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PopularActorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularActorViewHolder, position: Int) {
        val entry = getItem(position)

        entry?.let { actor ->
            entry.profile_path?.let { posterPath ->
                Glide.with(holder.itemView)
                    .load(
                        tmdbImageUrlProvider.getPosterUrl(
                            path = posterPath,
                            imageWidth = holder.itemView.width,
                        )
                    )
                    .into(holder.binding.actorImg)
            }

            holder.binding.actorName.text = actor.name
//            val bm = (holder.binding.actorImg.drawable as BitmapDrawable).bitmap
//            holder.binding.actorName.setTextColor(getPalletColorFromImage(bm))

            holder.binding.actorMovies.text = actorToMovieList(actor.known_for)

            holder.binding.root.setOnClickListener {
                onItemClickListener(entry.id)
            }
        }
    }

    fun actorToMovieList(movieList: List<KnownFor>): String {
        val moviesBuilder = StringBuilder()
        repeat(movieList.size) {
            moviesBuilder.append("${movieList[it].original_title} ,")
        }

        return moviesBuilder.toString().removePrefix(",")
    }
}

//fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

fun getPalletColorFromImage(bitmap: Bitmap): Int {
    val palette: Palette = Palette.from(bitmap).generate()
    val colorPalette1: Int = palette.getLightVibrantColor(Color.WHITE)
    val colorPalette2 =
        if (colorPalette1 == -1) palette.getVibrantColor(Color.WHITE) else colorPalette1 //Fallback
    val colorPalette3 =
        if (colorPalette2 == -1) palette.getLightMutedColor(Color.WHITE) else colorPalette2 //Fallback
    return if (colorPalette3 == -1) palette.getDominantColor(Color.WHITE) else colorPalette3
}

class PopularActorViewHolder(
    internal val binding: PopularActorRowItemBinding
) : RecyclerView.ViewHolder(binding.root)

object PopularActorEntryComparator : DiffUtil.ItemCallback<PopularActor>() {
    override fun areItemsTheSame(
        oldItem: PopularActor,
        newItem: PopularActor
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PopularActor,
        newItem: PopularActor
    ): Boolean {
        return oldItem == newItem
    }
}