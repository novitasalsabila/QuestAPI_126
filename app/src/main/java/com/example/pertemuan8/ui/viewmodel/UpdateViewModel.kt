package com.example.pertemuan8.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.repository.MahasiswaRepository
import kotlinx.coroutines.launch

sealed class UpdtUIState{
    data class  Success(val mahasiswa: List<Mahasiswa>): UpdtUIState()
    object Error : UpdtUIState()
    object Loading : UpdtUIState()
}

class UpdateViewModel(private val mhs: MahasiswaRepository) : ViewModel() {

    var updateUiState by mutableStateOf(UpdateUiState())
        private set

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
                UpdtUIState.Error
            }
        }
    }

    fun updateMhsState(updateUiEvent: UpdateUiEvent) {
        updateUiState = UpdateUiState(updateUiEvent = updateUiEvent)
    }

    fun loadMahasiswa(mahasiswa: Mahasiswa) {
        updateUiState = mahasiswa.toUpdateUiStateMhs()
    }

    fun updateMhs() {
        viewModelScope.launch {
            try {
                mhs.updateMahasiswa(
                    nim = updateUiState.updateUiEvent.nim,
                    mahasiswa = updateUiState.updateUiEvent.toMhs()
                )
            } catch (e: Exception) {
                UpdtUIState.Error
            }
        }
    }
}

data class UpdateUiState(
    val updateUiEvent: UpdateUiEvent = UpdateUiEvent()
)

data class UpdateUiEvent(
    val nim: String = "",
    val nama: String = "",
    val alamat: String = "",
    val jenisKelamin: String = "",
    val kelas: String = "",
    val angkatan: String = ""
)


fun UpdateUiEvent.toMhs(): Mahasiswa = Mahasiswa(
    nim = nim,
    nama = nama,
    alamat = alamat,
    jenisKelamin = jenisKelamin,
    kelas = kelas,
    angkatan = angkatan
)


fun Mahasiswa.toUpdateUiStateMhs(): UpdateUiState = UpdateUiState(
    updateUiEvent = toUpdateUiEvent()
)

fun Mahasiswa.toUpdateUiEvent(): UpdateUiEvent = UpdateUiEvent(
    nim = nim,
    nama = nama,
    alamat = alamat,
    jenisKelamin = jenisKelamin,
    kelas = kelas,
    angkatan = angkatan
)