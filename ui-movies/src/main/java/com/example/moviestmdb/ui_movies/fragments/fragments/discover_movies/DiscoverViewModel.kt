package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.observers.ObserveDiscoverPager
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterParams
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    observeDiscoverPager: ObserveDiscoverPager,
    observeGenres: ObserveGenres,
    val dispatchers: AppCoroutineDispatchers,
) : ViewModel() {

    private val _filterParams = MutableStateFlow(FilterParams())
    //val filterParams = _filterParams.asStateFlow()

    init {
        observeGenres(Unit)

        observeDiscoverPager(
            params = ObserveDiscoverPager.Params(
                PAGING_CONFIG,
                _filterParams.asStateFlow()
            )
        )

        viewModelScope.launch(dispatchers.io) {
            _filterParams.collect()
        }
    }

    val pagedList: Flow<PagingData<Movie>> =
        observeDiscoverPager.flow.cachedIn(viewModelScope)

    fun replaceFilters(filterParam: FilterParams){
        viewModelScope.launch(dispatchers.io) {
            _filterParams.tryEmit(filterParam)
        }
    }

    fun requireFilters() = _filterParams.asStateFlow().value

    val genres = observeGenres.flow
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            maxSize = 1000
        )
    }
}