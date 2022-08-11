package com.example.moviestmdb.ui_movies.fragments.fragments.movie_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestmdb.core.constants.Constants
import com.example.moviestmdb.core.extensions.combine
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.core.util.ObservableLoadingCounter
import com.example.moviestmdb.core.util.UiMessageManager
import com.example.moviestmdb.core.util.collectStatus
import com.example.moviestmdb.domain.interactors.AddFavoriteMovie
import com.example.moviestmdb.domain.interactors.RemoveFavoriteMovie
import com.example.moviestmdb.domain.interactors.UpdateMovieActors
import com.example.moviestmdb.domain.interactors.UpdateMovieRecommended
import com.example.moviestmdb.domain.observers.ObserverIsFavorite
import com.example.moviestmdb.domain.observers.details_observers.ObserveMovieActors
import com.example.moviestmdb.domain.observers.details_observers.ObserveMovieById
import com.example.moviestmdb.domain.observers.details_observers.ObserveRecommendedMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val updateMovieRecommended: UpdateMovieRecommended,
    private val updateMovieActors: UpdateMovieActors,
    private val addFavoriteMovie: AddFavoriteMovie,
    private val removeFavoriteMovie: RemoveFavoriteMovie,
    val observeRecommendedMovies: ObserveRecommendedMovies,
    val observeMovieActors: ObserveMovieActors,
    val observerIsFavorite: ObserverIsFavorite,
    val observeMovieById: ObserveMovieById,
    savedStateHandle: SavedStateHandle,
    private val dispatchers: AppCoroutineDispatchers
) : ViewModel() {
    private val recommendedLoadingState = ObservableLoadingCounter()
    private val actorsLoadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()

    var movieId: Int = savedStateHandle.get(Constants.MOVIE_ID)!!

    init {
        observeRecommendedMovies(ObserveRecommendedMovies.Params(movieId))
        observeMovieActors(ObserveMovieActors.Params(movieId))
        observeMovieById(ObserveMovieById.Params(movieId))

        refresh()
    }

    val detailState: StateFlow<DetailsUiState> = combine(
        recommendedLoadingState.observable,
        actorsLoadingState.observable,
        observerIsFavorite.flow,
        observeRecommendedMovies.flow,
        observeMovieActors.flow,
        observeMovieById.flow,
        uiMessageManager.message
    ) { recommendedRefreshing, actorsRefreshing
        ,isFavorite
        ,recommendedMovies,
        movieActors, movie, message ->
        DetailsUiState(
            actorsRefreshing = actorsRefreshing,
            recommendedRefreshing = recommendedRefreshing,
            recommendedMovies = recommendedMovies,
            isFavorite = isFavorite,
            actorList = movieActors,
            movie = movie,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        DetailsUiState.Empty
    )

    fun refresh() {
        viewModelScope.launch(dispatchers.io) {
            updateMovieRecommended(UpdateMovieRecommended.Params(movieId))
                .collectStatus(
                    recommendedLoadingState,
                    uiMessageManager
                )
        }

        viewModelScope.launch(dispatchers.io) {
            updateMovieActors(UpdateMovieActors.Params(movieId))
                .collectStatus(
                    actorsLoadingState,
                    uiMessageManager
                )
        }
    }

    fun addMovie(movieId: Int){
        viewModelScope.launch(dispatchers.io) {
            addFavoriteMovie(AddFavoriteMovie.Params(movieId)).collect()
        }
    }

    fun removeMovie(movieId: Int){
        viewModelScope.launch(dispatchers.io) {
            removeFavoriteMovie(RemoveFavoriteMovie.Params(movieId)).collect()
        }
    }

    fun clearMessage(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}