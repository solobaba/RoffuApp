package com.example.roffuapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //Timber.plant(Timber,Timber.DebugTree())
//        if (BuildConfig.DEBUG) {
//            Timber.plant(Timber,Timber.DebugTree())
//        }
    }
}