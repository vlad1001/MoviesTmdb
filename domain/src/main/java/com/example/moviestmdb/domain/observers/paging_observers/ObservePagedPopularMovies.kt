package com.example.moviestmdb.domain.observers.paging_observers

import androidx.paging.*
import com.example.moviestmdb.Movie
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.domain.PaginatedMovieRemoteMediator
import com.example.moviestmdb.domain.PagingInteractor
import com.example.moviestmdb.domain.interactors.UpdatePopularMovies
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ObservePagedPopularMovies @Inject constructor(
    @Popular private val popularStore: MoviesStore,
    private val updatePopularMovies: UpdatePopularMovies,
) : PagingInteractor<ObservePagedPopularMovies.Params, Movie>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Movie>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedMovieRemoteMediator(moviesStore = popularStore) { page ->
                updatePopularMovies.executeSync(UpdatePopularMovies.Params(page))
                pagingSourceFactory.invalidate()
            },
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)

    private fun createPagingSource(): MoviesPagingSource {
        return MoviesPagingSource(popularStore)
    }

    data class Params(
        override val pagingConfig: PagingConfig,
    ) : Parameters<Movie>


}