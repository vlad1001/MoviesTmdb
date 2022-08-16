package com.example.moviestmdb.domain.observers.paging_observers

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.moviestmdb.Movie
import com.example.moviestmdb.PopularActor
import com.example.moviestmdb.core.data.movies.MoviesStore
import com.example.moviestmdb.core.data.people.PeopleStore
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.domain.InvalidatingPagingSourceFactory
import com.example.moviestmdb.domain.PaginatedMovieRemoteMediator
import com.example.moviestmdb.domain.PaginatedPopularActorsRemoteMediator
import com.example.moviestmdb.domain.PagingInteractor
import com.example.moviestmdb.domain.interactors.UpdatePopularActors
import com.example.moviestmdb.domain.interactors.UpdatePopularMovies
import com.example.moviestmdb.domain.interactors.UpdateTopRatedMovies
import com.example.moviestmdb.domain.observers.paging_observers.ObservePagedPopularActors.*
import com.example.moviestmdb.domain.pagingSources.MoviesPagingSource
import com.example.moviestmdb.domain.pagingSources.PopularActorsPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ObservePagedPopularActors @Inject constructor(
    private val popularActorsStore: PeopleStore,
    private val updatePopularActors: UpdatePopularActors,
) : PagingInteractor<Params, PopularActor>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<PopularActor>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedPopularActorsRemoteMediator(popularActorStore = popularActorsStore) { page ->
                updatePopularActors.executeSync(UpdatePopularActors.Params(page))
                pagingSourceFactory.invalidate()
            },
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    private val pagingSourceFactory = InvalidatingPagingSourceFactory(::createPagingSource)

    private fun createPagingSource(): PopularActorsPagingSource {
        return PopularActorsPagingSource(popularActorsStore)
    }

    data class Params(
        override val pagingConfig: PagingConfig,
    ) : Parameters<PopularActor>
}