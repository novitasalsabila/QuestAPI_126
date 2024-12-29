package com.example.pertemuan8.repository

import com.example.pertemuan8.service_api.MahasiswaService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val mahasiswaRepository: MahasiswaRepository
}
class MahasiswaContainer: AppContainer {
    // URL dasar API (mengarah ke server lokal untuk emulator Android)
    private val baseUrl = "http://10.0.2.2:8000/umyTI" // `10.0.2.2` digunakan untuk mengakses localhost dari emulator Android

    // Konfigurasi JSON serialization untuk mengabaikan properti yang tidak dikenal dalam respons
    private val json = Json { ignoreUnknownKeys = true }

    // Membuat instance Retrofit dengan konfigurasi converter JSON dan URL dasar
    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        // Menambahkan converter untuk menangani data JSON dengan tipe media `application/json`
        .baseUrl(baseUrl).build()
    // Menentukan URL dasar untuk permintaan API

    private val mahasiswaService: MahasiswaService by lazy { retrofit.create(MahasiswaService::class.java) }
    override val mahasiswaRepository: MahasiswaRepository by lazy { NetworkKontakRepository (mahasiswaService) }
}