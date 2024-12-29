package com.example.pertemuan8

import android.app.Application
import com.example.pertemuan8.repository.AppContainer
import com.example.pertemuan8.repository.MahasiswaContainer

class MahasiswaApplications:Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer()
    }
}