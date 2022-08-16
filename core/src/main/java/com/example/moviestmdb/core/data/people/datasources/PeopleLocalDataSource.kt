package com.example.moviestmdb.core.data.people.datasources

import com.example.moviestmdb.core.data.people.PeopleStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PeopleLocalDataSource @Inject constructor(
    val popularActorStore: PeopleStore
)