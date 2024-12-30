package com.example.pertemuan8.ui.viewmodel

import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.repository.MahasiswaRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form tambah mahasiswa
class InsertViewModel(private val mhs: MahasiswaRepository): ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var insertUiState by mutableStateOf(InsertUiState())
        private set

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateInsertMhsState(insertUiEvent: InsertUiEvent) {
        insertUiState = InsertUiState(insertUiEvent = insertUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk menambahkan data mahasiswa ke database
    suspend fun insertMhs() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                mhs.insertMahasiswa(insertUiState.insertUiEvent.toMhs())
            }catch (e:Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form input mahasiswa
data class InsertUiState(
    val insertUiEvent: InsertUiEvent = InsertUiEvent() // State default berisi objek kosong dari InsertUiEvent
)

// Menyimpan data input pengguna untuk form mahasiswa
data class InsertUiEvent(
    val nim: String = "",
    val nama: String = "",
    val alamat: String = "",
    val jenisKelamin: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)

// Fungsi untuk mengubah data InsertUiEvent menjadi Mahasiswa
fun InsertUiEvent.toMhs(): Mahasiswa = Mahasiswa( // InsertUiEvent > Mahasiswa > Simpan data Mhs ke db
    nim = nim, // Memindahkan nilai NIM dari InsertUiEvent ke Mahasiswa
    nama = nama,
    alamat = alamat,
    jenisKelamin = jenisKelamin,
    kelas = kelas,
    angkatan = angkatan
)

// Fungsi untuk mengubah data Mahasiswa menjadi InsertUiState
fun Mahasiswa.toInsertUiStateMhs(): InsertUiState = InsertUiState( // Mahasiswa > insertUiEvent > Masuk ke InsertUiState
    insertUiEvent = toInsertUiEvent() // Memanggil fungsi toInsertUiEvent untuk mengonversi data Mahasiswa
)

// Fungsi untuk mengubah data Mahasiswa menjadi data InsertUiEvent
fun Mahasiswa.toInsertUiEvent(): InsertUiEvent = InsertUiEvent(
    nim = nim, // Memindahkan nilai NIM dari Mahasiswa ke InsertUiEvent
    nama = nama,
    alamat = alamat,
    jenisKelamin = jenisKelamin,
    kelas = kelas,
    angkatan = angkatan
)