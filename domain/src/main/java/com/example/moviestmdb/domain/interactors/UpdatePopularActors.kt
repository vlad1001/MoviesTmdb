package com.example.moviestmdb.domain.interactors

import com.example.moviestmdb.PopularActorsResponse
import com.example.moviestmdb.core.data.people.PeopleRepository
import com.example.moviestmdb.core.data.people.PeopleStore
import com.example.moviestmdb.core.di.Popular
import com.example.moviestmdb.core.result.Result
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.FlowInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class UpdatePopularActors @Inject constructor(
    private val peopleRepository: PeopleRepository,
    private val popularActorsStore: PeopleStore,
    dispatchers: AppCoroutineDispatchers,
) : FlowInteractor<UpdatePopularActors.Params, PopularActorsResponse>(dispatchers.io) {

    override suspend fun doWork(params: Params): Flow<Result<PopularActorsResponse>> {
        val page = when {
            params.page >= 1 -> params.page
            params.page == UpdatePopularMovies.Page.NEXT_PAGE -> {
                val lastPage = popularActorsStore.getLastPage()
                lastPage + 1
            }
            else -> 1
        }

        return peopleRepository.getPopularActors(page)
            .onEach { result ->
                when (result) {
                    is Result.Error -> Timber.e(result.exception)
                    is Result.Success -> peopleRepository.savePopularActors(
                        result.data.page,
                        result.data.results
                    )
                }
            }
    }

    data class Params(val page: Int)

    object Page {
        const val NEXT_PAGE = -1
        const val REFRESH = -2
    }
}