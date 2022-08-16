package com.example.moviestmdb.core.data.people

import com.example.moviestmdb.PopularActor
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleStore @Inject constructor(){
    private val _popularActors = MutableSharedFlow<Map<Int, List<PopularActor>>>(replay = 1)

    fun observePopularActors(): SharedFlow<Map<Int, List<PopularActor>>> =
        _popularActors.asSharedFlow()

    fun insert(page: Int, popularActors: List<PopularActor>) {
        if (page == 1) {
            _popularActors.resetReplayCache()
            _popularActors.tryEmit(mapOf(page to popularActors))
        } else {
            val map = _popularActors.replayCache.first().toMutableMap()
            map[page] = popularActors
            _popularActors.tryEmit(map)
        }
    }

    fun getLastPage(): Int {
        return _popularActors.replayCache.firstOrNull()?.let { map ->
            map.maxOf { it.key }
        } ?: 0
    }

    fun getActorsForPage(page: Int): List<PopularActor>? {
        return _popularActors.replayCache.firstOrNull()?.let { map ->
            map[page]
        }
    }

    fun getAllStoreActors(): Flow<List<PopularActor>> {
        return _popularActors.map { map ->
            map.values.flatten()
        }
    }
}