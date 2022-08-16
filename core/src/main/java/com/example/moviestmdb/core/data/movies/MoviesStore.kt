package com.example.moviestmdb.core.data.movies

import com.example.moviestmdb.Movie
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesStore @Inject constructor() {
    private val _movies = MutableSharedFlow<Map<Int, List<Movie>>>(replay = 1)

    fun insert(page: Int, movies: List<Movie>) {
        if (page == 1) {
            _movies.resetReplayCache()
            _movies.tryEmit(mapOf(page to movies))
        } else {
            val map = _movies.replayCache.first().toMutableMap()
            map[page] = movies
            _movies.tryEmit(map)
        }
    }

    fun observeMovies(): SharedFlow<Map<Int, List<Movie>>> = _movies.asSharedFlow()

    fun updatePage(page: Int, movies: List<Movie>) {
        val map = _movies.replayCache.first().toMutableMap()
        map[page] = movies
        _movies.tryEmit(map)
    }

    fun deletePage(page: Int) {
        val map = _movies.replayCache.first().toMutableMap()
        map.remove(page)
        _movies.tryEmit(map)
    }

    fun deleteAll() {
        _movies.resetReplayCache()
    }

    fun getLastPage(): Int {
        return _movies.replayCache.firstOrNull()?.let { map ->
            map.maxOf { it.key }
        } ?: 0
    }

    fun getMoviesForPage(page: Int): List<Movie>? {
        return _movies.replayCache.firstOrNull()?.let {
            it[page]
        }
    }

    fun getAllStoreMovies(): Flow<List<Movie>> {
        return _movies.map {
            it.values.flatten()
        }
    }
}