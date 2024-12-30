package com.example.pertemuan8.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.repository.MahasiswaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(private val mhsRepo: MahasiswaRepository) : ViewModel() {

    private val _detailUiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState.asStateFlow()

    fun getMahasiswaById(nim: String) {
        viewModelScope.launch {
            try {
                val mahasiswa = mhsRepo.getMahasiswaById(nim)
                if (mahasiswa != null) {
                    _detailUiState.value = DetailUiState.Success(mahasiswa)
                } else {
                    _detailUiState.value = DetailUiState.Error("Data Mahasiswa tidak ditemukan.")
                }
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error(e.localizedMessage ?: "Terjadi kesalahan.")
            }
        }
    }

    fun deleteMhs(nim: String) {
        viewModelScope.launch {
            try {
                mhsRepo.deleteMahasiswa(nim)
                _detailUiState.value = DetailUiState.Error("Data Mahasiswa telah dihapus.")
            } catch (e: Exception) {
                _detailUiState.value = DetailUiState.Error(e.localizedMessage ?: "Gagal menghapus data.")
            }
        }
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val mahasiswa: Mahasiswa) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}