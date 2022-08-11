package com.example.moviestmdb.ui_movies.fragments.fragments.discover_movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.observers.ObserveDiscoverPager
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.domain.pagingSources.DiscoverPagingSource
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterParams
import com.example.moviestmdb.ui_movies.fragments.model.MovieAndGenre
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

    val genres = observeGenres.flow
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val pagedList: Flow<PagingData<Movie>> =
        observeDiscoverPager.flow.cachedIn(viewModelScope)


    val pagedList2: Flow<PagingData<MovieAndGenre>> =
        observeGenres.flow.flatMapLatest { genres ->
            observeDiscoverPager.flow.map {  pagingData ->
                pagingData.map { movie ->
                    MovieAndGenre(movie, genres)
                }
            }
        }

//    viewModel.pagedList.collectLatest { pagingData ->
//        val data = pagingData.map {
//            MovieAndGenre(it, viewModel.genres.value)
//        }
//        discoverAdapter.submitData(data)
//    }

    fun replaceFilters(filterParam: FilterParams) {
        viewModelScope.launch(dispatchers.io) {
            _filterParams.tryEmit(filterParam)
        }
    }

    fun requireFilters() = _filterParams.asStateFlow()


    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 40,
            maxSize = 1000
        )
    }
}