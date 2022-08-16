package com.example.moviestmdb.ui_people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviestmdb.Movie
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedPopularActors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PeopleLobbyViewModel @Inject constructor(
    observePagedPopularActors: ObservePagedPopularActors,
    val dispatchers: AppCoroutineDispatchers
): ViewModel() {

    val pagedList: Flow<PagingData<PopularActor>> =
        observePagedPopularActors.flow.cachedIn(viewModelScope)

    init {
        observePagedPopularActors(ObservePagedPopularActors.Params(PAGING_CONFIG))
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            maxSize = 1000
        )
    }
}