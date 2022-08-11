package com.example.moviestmdb.domain.observers

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.PagingInteractor
import com.example.moviestmdb.domain.pagingSources.DiscoverPagingSource
import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterParams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

class ObserveDiscoverPager @Inject constructor(
    private val moviesRepository: MoviesRepository,
    val dispatchers: AppCoroutineDispatchers,
) : PagingInteractor<ObserveDiscoverPager.Params, Movie>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Movie>> {
        return params.requestMap.flatMapLatest { filters ->
            Pager(
                config = params.pagingConfig,
                pagingSourceFactory = {
                    DiscoverPagingSource(
                        movieRepository = moviesRepository,
                        dispatchers = dispatchers,
                        paramMap = filters.toMap()
                    )
                }
            ).flow
        }
    }

    data class Params(
        override val pagingConfig: PagingConfig,
        val requestMap: StateFlow<FilterParams>
    ) : Parameters<Movie>

}