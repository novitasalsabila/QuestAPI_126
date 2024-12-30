package com.example.pertemuan8.ui.viewmodel

import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.repository.MahasiswaRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// ViewModel untuk mengatur data dan logika form update mahasiswa
class UpdateViewModel(private val mhs: MahasiswaRepository) : ViewModel() {

    // Data untuk menyimpan keadaan form (seperti input dari pengguna)
    var updateUiState by mutableStateOf(UpdateUiState())
        private set

    // Fungsi untuk mendapatkan data mahasiswa berdasarkan NIM
    fun getMahasiswaById(nim: String) {
        viewModelScope.launch {
            try {
                val mahasiswa = mhs.getMahasiswaById(nim)
                updateUiState = updateUiState.copy(
                    updateUiEvent = UpdateUiEvent(
                        nim = mahasiswa.nim,
                        nama = mahasiswa.nama,
                        alamat = mahasiswa.alamat,
                        jenisKelamin = mahasiswa.jenisKelamin,
                        kelas = mahasiswa.kelas,
                        angkatan = mahasiswa.angkatan
                    )
                )
            } catch (e: Exception) {
                // Handle error jika diperlukan
            }
        }
    }

    // Fungsi untuk mengubah data form ketika ada input dari pengguna
    fun updateMhsState(updateUiEvent: UpdateUiEvent) {
        updateUiState = UpdateUiState(updateUiEvent = updateUiEvent) // Perbarui data berdasarkan event
    }

    // Fungsi untuk memuat data mahasiswa ke dalam form untuk di-update
    fun loadMahasiswaData(mahasiswa: Mahasiswa) {
        updateUiState = mahasiswa.toUpdateUiStateMhs()
    }

    // Fungsi untuk memperbarui data mahasiswa ke database
    fun updateMhs() {
        viewModelScope.launch { // Menjalankan proses di latar belakang (tidak mengganggu UI)
            try {
                // Mengambil data dari form dan mengirimnya ke repository
                mhs.updateMahasiswa(
                    nim = updateUiState.updateUiEvent.nim, // Ambil NIM dari updateUiState
                    mahasiswa = updateUiState.updateUiEvent.toMhs() // Konversi event menjadi Mahasiswa
                )
            } catch (e: Exception) {
                e.printStackTrace() // Menangani error jika terjadi masalah
            }
        }
    }
}

// Menyimpan state form update mahasiswa
data class UpdateUiState(
    val updateUiEvent: UpdateUiEvent = UpdateUiEvent() // State default berisi objek kosong dari UpdateUiEvent
)

// Menyimpan data input pengguna untuk form update mahasiswa
data class UpdateUiEvent(
    val nim: String = "",
    val nama: String = "",
    val alamat: String = "",
    val jenisKelamin: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)

// Fungsi untuk mengubah data UpdateUiEvent menjadi Mahasiswa
fun UpdateUiEvent.toMhs(): Mahasiswa = Mahasiswa( // UpdateUiEvent > Mahasiswa > Simpan data Mhs ke db
    nim = nim, // Memindahkan nilai NIM dari UpdateUiEvent ke Mahasiswa
    nama = nama,
    alamat = alamat,
    jenisKelamin = jenisKelamin,
    kelas = kelas,
    angkatan = angkatan
)

// Fungsi untuk mengubah data Mahasiswa menjadi UpdateUiState
fun Mahasiswa.toUpdateUiStateMhs(): UpdateUiState = UpdateUiState( // Mahasiswa > updateUiEvent > Masuk ke UpdateUiState
    updateUiEvent = toUpdateUiEvent() // Memanggil fungsi toUpdateUiEvent untuk mengonversi data Mahasiswa
)

// Fungsi untuk mengubah data Mahasiswa menjadi data UpdateUiEvent
fun Mahasiswa.toUpdateUiEvent(): UpdateUiEvent = UpdateUiEvent(
    nim = nim, // Memindahkan nilai NIM dari Mahasiswa ke UpdateUiEvent
    nama = nama,
    alamat = alamat,
    jenisKelamin = jenisKelamin,
    kelas = kelas,
    angkatan = angkatan
)