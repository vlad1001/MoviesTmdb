package com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies

import com.example.moviestmdb.ui_movies.fragments.fragments.filter_movies.FilterKey.*

enum class FilterKey {
    FROM_DATE,
    TO_DATE,
    CHOSEN_GENRES,
    USER_MIN_SCORE,
    USER_MAX_SCORE,
    USER_VOTES,
    USER_MIN_RUNTIME,
    USER_MAX_RUNTIME,
    LANGUAGE
}

data class FilterParams(
//    var language: String = "",
    var dateFrom: String = "",
    var dateTo: String = "",
    var user_min_score: String = "0",
    var user_max_score: String = "10",
    var user_min_votes: String = "0",
    var duration_min: String = "0",
    var duration_max: String = "400",
    var genres: MutableSet<Int> = mutableSetOf()
) {

    fun clearAll() {
        this.dateFrom = ""
        this.dateTo= ""
        this.user_min_score = "0"
        this.user_max_score = "10"
        this.user_min_votes = "0"
        this.duration_min = "0"
        this.duration_max = "400"
        this.genres = mutableSetOf()
    }

    fun isEmpty() =
        //language.isEmpty() &&
                genres.isEmpty()
                && dateFrom.isEmpty()
                && dateTo.isEmpty()
                && user_min_score == "0"
                && user_max_score == "10"
                && user_min_votes == "0"
                && duration_min == "0"
                && duration_max == "400"

    fun removeFilter(filterParam: FilterKey, value: Any) {
        when (filterParam) {
            CHOSEN_GENRES -> {
                (value as? Int)?.let { removeId ->
                    genres.remove(removeId)
                }
            }
//            USER_MIN_SCORE -> {
//                this.user_min_score = 0f
//            }
//            USER_MAX_SCORE -> {
//                this.user_max_score = ""
//            }
//            USER_VOTES -> {
//                this.user_min_votes = ""
//            }
//            USER_MIN_RUNTIME -> {
//                this.duration_min = ""
//            }
//            USER_MAX_RUNTIME -> {
//                this.duration_max = ""
//            }
//            LANGUAGE -> {
//                this.dateFrom = ""
//            }
//            FROM_DATE -> {
//                this.dateFrom = ""
//            }
//            TO_DATE -> {
//                this.dateTo = ""
//            }
        }
    }

    fun addFilter(filterParam: FilterKey, value: Any) {
        when (filterParam) {
            USER_MIN_SCORE -> {
                this.user_min_score = (value as? String) ?: "0"
            }
            USER_MAX_SCORE -> {
                this.user_max_score = (value as? String) ?: "10"
            }
            USER_VOTES -> {
                this.user_min_votes = (value as? String) ?: "0"
            }
            USER_MIN_RUNTIME -> {
                this.duration_min = (value as? String) ?: "0"
            }
            USER_MAX_RUNTIME -> {
                this.duration_max = (value as? String) ?: "400"
            }
//            LANGUAGE -> {
//                this.language = (value as? String) ?: "en-us"
//            }
            CHOSEN_GENRES -> {
                (value as? Int)?.let { id ->
                    this.genres.add(id)
                }
            }
            FROM_DATE -> {
                this.dateFrom = (value as? String) ?: ""
            }
            TO_DATE -> {
                this.dateTo = (value as? String) ?: ""
            }
        }
    }

    fun toMap(): Map<String, String> =
        mutableMapOf<String, String>().apply {
            put("vote_count.gte", this@FilterParams.user_min_votes)
            put("with_runtime.gte", this@FilterParams.duration_min)
            put("with_runtime.lte", this@FilterParams.duration_max)
            put("vote_average.gte", this@FilterParams.user_min_score)
            put("vote_average.lte", this@FilterParams.user_max_score)
            put("release_date.gte", this@FilterParams.dateFrom)
            put("release_date.lte", this@FilterParams.dateTo)
            //put("language", this@FilterParams.language)
            put("with_genres", this@FilterParams.genres.joinToString(separator = ","))
        }
}