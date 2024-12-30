package com.example.pertemuan8.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.repository.MahasiswaRepository
import kotlinx.coroutines.launch
import java.io.IOException

sealed class HomeUiState{ // Digunakan untuk membatasi subclass yang dapat di-extend dari kelas ini.

    // Subclass Success
    data class Success(val mahasiswa: List<Mahasiswa>): HomeUiState()

    // Subclass Error berupa object. Menunjukkan bahwa terjadi kesalahan tanpa detail tambahan.
    object Error: HomeUiState()

    // Subclass Loading. Menunjukkan bahwa aplikasi sedang dalam proses memuat data.
    object Loading: HomeUiState()
}

class HomeViewModel(private val mhs: MahasiswaRepository): ViewModel() {

    // mhsUiState digunakan untuk menyimpan keadaan UI (state) mahasiswa.
    // mutableStateOf digunakan untuk membuat state yang dapat berubah dan otomatis memicu pembaruan UI ketika nilainya berubah.
    // State awalnya diset ke HomeUiState.Loading.
    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set // Setter-nya dibuat private agar state hanya dapat diubah oleh ViewModel.

    init {
        getMhs()
    }

    fun getMhs() {
        viewModelScope.launch {

            // Set state ke Loading untuk menunjukkan bahwa data sedang diproses.
            mhsUiState = HomeUiState.Loading

            // Mencoba mengambil data mahasiswa dari repository menggunakan blok try-catch.
            mhsUiState = try {

                // Jika berhasil, state diubah menjadi Success dengan daftar mahasiswa sebagai datanya.
                HomeUiState.Success(mhs.getMahasiswa())
            }catch (e: IOException) {

                // Jika terjadi kesalahan jaringan atau I/O, set state ke Error.
                HomeUiState.Error
            }catch (e: HttpException) {

                // Jika terjadi kesalahan HTTP (misalnya, 404 atau 500), set state ke Error.
                HomeUiState.Error
            }
        }
    }

    fun deleteMhs(nim: String) {
        viewModelScope.launch {

            // Menggunakan blok try-catch untuk menangani kemungkinan kesalahan selama proses penghapusan.
            try {

                // Memanggil fungsi deleteMahasiswa pada repository untuk menghapus data mahasiswa berdasarkan NIM.
                mhs.deleteMahasiswa(nim)
            }catch (e:IOException) {
                HomeUiState.Error
            }catch (e:HttpException) {
                HomeUiState.Error
            }
        }
    }
}