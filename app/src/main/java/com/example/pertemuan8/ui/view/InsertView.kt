package com.example.pertemuan8.ui.view

import com.example.pertemuan8.ui.viewmodel.InsertUiEvent
import com.example.pertemuan8.ui.viewmodel.InsertUiState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan8.ui.CostumWidget.CoustumeTopAppBar
import com.example.pertemuan8.ui.navigasi.DestinasiNavigasi
import com.example.pertemuan8.ui.viewmodel.InsertViewModel
import com.example.pertemuan8.ui.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

// Membuat objek `DestinasiEntry` yang mengimplementasikan interface `DestinasiNavigasi`
object DestinasiEntry : DestinasiNavigasi {

    // Mendefinisikan properti `route` yang mengacu pada string "item_entry"
    override val route = "item_entry"

    // Mendefinisikan properti `titleRes` yang mengacu pada string "Entry Mhs"
    override val titleRes = "Insert Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryMhsScreen(
    navigateBack: () -> Unit,
    nim: String? = null, // Tambahkan parameter nim dengan nilai default null
    modifier: Modifier = Modifier,
    viewModel: InsertViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    // Membuat coroutine scope untuk operasi asynchronous di dalam composable ini
    val coroutineScope = rememberCoroutineScope()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CoustumeTopAppBar(
                title = DestinasiEntry.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        }
    ) { innerPadding ->
        EntryBody(
            insertUiState = viewModel.insertUiState,
            onSiswaValueChange = viewModel::updateInsertMhsState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertMhs()
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
fun EntryBody(
    insertUiState: InsertUiState,
    onSiswaValueChange: (InsertUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInput(
            insertUiEvent = insertUiState.insertUiEvent,
            onValueChange = onSiswaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    insertUiEvent: InsertUiEvent, // Parameter yang digunakan untuk menyimpan data atau event input
    modifier: Modifier = Modifier,
    onValueChange: (InsertUiEvent) -> Unit = {}, // Lambda untuk menangani perubahan nilai input
    enabled: Boolean = true // Parameter untuk mengontrol apakah input dapat diubah atau tidak (default: true)
) {
    val jenisKelamin = listOf("Laki-laki", "Perempuan")
    val kelas = listOf("A", "B", "C", "D",)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Input teks untuk "Nama"
        OutlinedTextField(
            value = insertUiEvent.nama,  // Nilai saat ini dari input "Nama"
            onValueChange = { onValueChange(insertUiEvent.copy(nama = it)) }, // Mengubah nilai "Nama" dalam `insertUiEvent`
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled, // Input aktif atau nonaktif sesuai parameter
            singleLine = true // Hanya satu baris teks
        )
        // Input teks untuk "NIM"
        OutlinedTextField(
            value = insertUiEvent.nim,
            onValueChange = { onValueChange(insertUiEvent.copy(nim = it)) },
            label = { Text("NIM") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Input teks untuk "Jenis Kelamin"
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Jenis Kelamin")
        Row(modifier = Modifier.fillMaxWidth())
        {
            jenisKelamin.forEach{ jk ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = insertUiEvent.jenisKelamin == jk,
                        onClick = {
                            onValueChange(insertUiEvent.copy(jenisKelamin = jk))
                        },
                    )
                    Text(text = jk,)
                }
            }
        }
        // Input teks untuk "Alamat"
        OutlinedTextField(
            value = insertUiEvent.alamat,
            onValueChange = { onValueChange(insertUiEvent.copy(alamat = it)) },
            label = { Text("Alamat") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Input teks untuk "Kelas"
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Kelas")
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            kelas.forEach{ kelas ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = insertUiEvent.kelas == kelas,
                        onClick = {
                            onValueChange(insertUiEvent.copy(kelas = kelas))
                        },
                    )
                    Text(text = kelas,)
                }
            }
        }
        // Input teks untuk "Angkatan"
        OutlinedTextField(
            value = insertUiEvent.angkatan,
            onValueChange = { onValueChange(insertUiEvent.copy(angkatan = it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = { Text("Angkatan") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        // Menampilkan pesan "Isi Semua Data!" jika input aktif
        if (enabled) {
            Text(
                text = "Isi Semua Data!",
                modifier = Modifier.padding(12.dp),
                color = Color.Red
            )
        }
        // Garis pemisah tebal
        Divider(
            thickness = 8.dp,
            modifier = Modifier.padding(12.dp)
        )
    }
}