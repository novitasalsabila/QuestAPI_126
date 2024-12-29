package com.example.pertemuan8.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.repository.MahasiswaRepository
import com.example.pertemuan8.ui.view.DetailDestination
import kotlinx.coroutines.launch

sealed class DetailMahasiswaUiState {
    data class Success(val mahasiswa: Mahasiswa) : DetailMahasiswaUiState()

    object Error : DetailMahasiswaUiState()
    object Loading : DetailMahasiswaUiState()
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val mahasiswaRepository: MahasiswaRepository
) : ViewModel() {
    private val mahasiswaNim: String = checkNotNull(savedStateHandle[DetailDestination.mahasiswaNim])
    var detailMahasiswaUiState: DetailMahasiswaUiState by mutableStateOf(DetailMahasiswaUiState.Loading)
        private set

    init {
        getMahasiswabyId()
    }

    fun getMahasiswabyId() {
        viewModelScope.launch {
            detailMahasiswaUiState = DetailMahasiswaUiState.Loading
            detailMahasiswaUiState = try {
                DetailMahasiswaUiState.Success(mahasiswa = mahasiswaRepository.getMahasiswaById(mahasiswaNim))
            } catch (e: Exception) {
                DetailMahasiswaUiState.Error
            }
        }
    }
}