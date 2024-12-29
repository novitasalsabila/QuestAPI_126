package com.example.pertemuan8.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan8.ui.navigasi.DestinasiNavigasi
import com.example.pertemuan8.ui.viewmodel.PenyediaViewModel
import com.example.pertemuan8.ui.viewmodel.UpdateUiEvent
import com.example.pertemuan8.ui.viewmodel.UpdateUiState
import com.example.pertemuan8.ui.viewmodel.UpdateViewModel
import kotlinx.coroutines.launch

object DestinasiUpdate : DestinasiNavigasi {
    override val route = "update_view"
    override val titleRes = "Update Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateScreen(
    nim: String,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: UpdateViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(nim) {
        viewModel.getMahasiswaById(nim)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection).padding(horizontal = 10.dp),
        topBar = {
            CostumeTopAppBar(
                title = DestinasiUpdate.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        UpdateBody(
            updateUiState = viewModel.updateUiState,
            onSiswaValueChange = viewModel::updateMhsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateMhs()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun UpdateBody(
    updateUiState: UpdateUiState,
    onSiswaValueChange: (UpdateUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInput(
            updateUiEvent = updateUiState.updateUiEvent,
            onValueChange = onSiswaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Save Icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Update",
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    updateUiEvent: UpdateUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (UpdateUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    val kelas = listOf("A", "B", "C", "D")
    val JenisKelamin = listOf("Laki - Laki", "Perempuan")

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = updateUiEvent.nama,
            onValueChange = {onValueChange(updateUiEvent.copy(nama = it))},
            label = { Text("Nama")},
            placeholder = { Text("Masukkan Nama") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = updateUiEvent.nim,
            onValueChange = {onValueChange(updateUiEvent.copy(nim = it))},
            label = { Text("Nim")},
            placeholder = { Text("Masukkan Nim") },
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        Text("Jenis Kelamin", style = MaterialTheme.typography.bodyMedium)
        Row() {
            JenisKelamin.forEach { jK ->
                Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    androidx.compose.material3.RadioButton(
                        selected = updateUiEvent.jenisKelamin == jK,
                        onClick = { onValueChange(updateUiEvent.copy(jenisKelamin = jK)) }
                    )
                    Text(
                        text = jK,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        OutlinedTextField(
            value = updateUiEvent.alamat,
            onValueChange = {onValueChange(updateUiEvent.copy(alamat = it))},
            label = { Text("Alamat")},
            placeholder = { Text("Masukkan Alamat") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Text("Kelas", style = MaterialTheme.typography.bodyMedium)
        Row() {
            kelas.forEach { kelas ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    androidx.compose.material3.RadioButton(
                        selected = updateUiEvent.kelas == kelas,
                        onClick = { onValueChange(updateUiEvent.copy(kelas = kelas)) }
                    )
                    Text(
                        text = kelas,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        OutlinedTextField(
            value = updateUiEvent.angkatan,
            onValueChange = {onValueChange(updateUiEvent.copy(angkatan = it))},
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Angkatan")},
            placeholder = { Text("Masukkan Angkatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        Divider(
            thickness = 5.dp,
            modifier = Modifier.padding(5.dp)
        )
    }
}