package com.example.moviestmdb.domain.interactors

import com.example.moviestmdb.MovieCredit
import com.example.moviestmdb.core.data.movies.MoviesRepository
import com.example.moviestmdb.core.result.Result
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.FlowInteractor
import com.example.moviestmdb.domain.interactors.UpdateMovieActors.Params
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject
//vlad note
class UpdateMovieActors @Inject constructor(
    private val moviesRepository: MoviesRepository,
    dispatchers: AppCoroutineDispatchers,
) : FlowInteractor<Params, MovieCredit>(dispatchers.io) {

    override suspend fun doWork(params: Params): Flow<Result<MovieCredit>> {
        return moviesRepository.getActors(params.movieId)
            .onEach { result ->
                when (result) {
                    is Result.Error -> Timber.e(result.exception)
                    is Result.Success -> moviesRepository.saveMovieActors(
                        movieId = params.movieId,
                        actors = result.data.cast
                    )
                }
            }
    }

    data class Params(val movieId: Int)
}