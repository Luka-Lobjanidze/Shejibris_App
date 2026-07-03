package com.example.finaluri_shejibris_app

import com.example.finaluri_shejibris_app.Data.DatabaseProvider

class App : android.app.Application() {
    override fun onCreate() {
        super.onCreate()
        DatabaseProvider.init(this)
    }
}