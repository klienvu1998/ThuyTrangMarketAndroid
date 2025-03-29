package com.hyvu.thuytrangmarket

import android.app.Application
import android.content.Context

class MainApplication : Application() {

    companion object {
        private lateinit var appContext: Context

        fun getAppContext(): Context {
            return appContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}