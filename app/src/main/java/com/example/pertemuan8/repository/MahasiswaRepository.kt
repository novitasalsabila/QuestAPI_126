package com.example.pertemuan8.repository

import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.service_api.MahasiswaService
import okio.IOException

interface MahasiswaRepository{
    suspend fun insertMahasiswa(mahasiswa: Mahasiswa)

    suspend fun getMahasiswa(): List<Mahasiswa>

    suspend fun updateMahasiswa(nim: String, mahasiswa: Mahasiswa)

    suspend fun deleteMahasiswa(nim: String)

    suspend fun getMahasiswaById(nim: String): Mahasiswa
}

