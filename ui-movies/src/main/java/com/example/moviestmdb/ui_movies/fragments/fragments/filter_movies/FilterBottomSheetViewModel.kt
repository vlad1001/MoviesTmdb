package com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviestmdb.Genre
import com.example.moviestmdb.core.util.AppCoroutineDispatchers
import com.example.moviestmdb.domain.observers.ObserveGenres
import com.example.moviestmdb.ui_movies.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FilterBottomSheetViewModel @Inject constructor(
    observeGenres: ObserveGenres,
    val dispatchers: AppCoroutineDispatchers
) : ViewModel() {

    var filerParams = FilterParams()

    val geners = observeGenres.flow
        .stateIn(
            scope = viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        observeGenres(Unit)
    }

    fun addFilter(filterParam: FilterKey, value: Any){
        filerParams.addFilter(filterParam, value)
    }

    fun removeFilter(filterParam: FilterKey, value: Any){
        filerParams.removeFilter(filterParam, value)
    }

//    fun saveParams(filterParam: FilterParams){
//        this.filerParams = filterParam
//    }

    fun getLanguageList(context: Context) =
    listOf(
        context.getString(R.string.english),
        context.getString(R.string.french),
        context.getString(R.string.german),
        context.getString(R.string.italian),
        context.getString(R.string.chinese)
    )
}