package com.example.moviestmdb.ui_movies.fragments.fragments.now_playing_movies

import androidx.paging.PagingData
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.util.UiMessage
import com.example.moviestmdb.ui_movies.fragments.model.MovieAndGenre

class NowPlayingViewState (
    val nowPlayingPagingData: PagingData<MovieAndGenre> = PagingData.empty(),
    val nowPlayingRefreshing: Boolean = false,
    val genreList: List<Genre> = emptyList(),
    val message: UiMessage? = null
) {
    val refreshing: Boolean
        get() = nowPlayingRefreshing

    companion object {
        val Empty = NowPlayingViewState()
    }
}