package com.example.moviestmdb

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MoviesApplication: Application() {

    @Inject
    lateinit var mainLifecycleListener: MainLifecycleListener

    private val lifecycleListener: MainLifecycleListener by lazy {
        mainLifecycleListener
    }

    override fun onCreate() {
        super.onCreate()
        setupLifecycleListener()

        Timber.plant(Timber.DebugTree())
    }



    private fun setupLifecycleListener() {
        ProcessLifecycleOwner.get()
            .lifecycle.addObserver(lifecycleListener)
    }
}