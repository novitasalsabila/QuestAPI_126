package com.example.pertemuan8.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pertemuan8.model.Mahasiswa
import com.example.pertemuan8.ui.CostumWidget.CoustumeTopAppBar
import com.example.pertemuan8.ui.navigasi.DestinasiNavigasi
import com.example.pertemuan8.ui.viewmodel.DetailUiState
import com.example.pertemuan8.ui.viewmodel.DetailViewModel
import com.example.pertemuan8.ui.viewmodel.PenyediaViewModel



object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail/{nim}"
    override val titleRes = "Detail Mahasiswa"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailMhsScreen(
    nim: String,
    navigateBack: () -> Unit,
    navigateEdit: (String) -> Unit,  // Digunakan untuk navigasi ke layar update
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(nim) {
        viewModel.getMahasiswaById(nim)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CoustumeTopAppBar(
                title = DestinasiDetail.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack

            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateEdit(nim)  // Mengirim nim saat ini ke fungsi navigasi
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(18.dp),
                contentColor = Color.White,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Mahasiswa"
                )
            }
        }
    ) { innerPadding ->
        val detailUiState = viewModel.detailUiState.collectAsState().value
        DetailBody(
            detailUiState = detailUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onDeleteClick = {
                viewModel.deleteMhs(nim)
                navigateBack()
            }
        )
    }
}

@Composable
fun DetailBody(
    detailUiState: DetailUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    when (detailUiState) {
        is DetailUiState.Loading -> OnLoading(modifier = modifier)
        is DetailUiState.Success -> Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DetailContent(mahasiswa = detailUiState.mahasiswa)
            Button(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Delete")
            }
        }
        is DetailUiState.Error -> OnError(retryAction = {}, modifier = modifier)
    }

    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteClick()
            },
            onDeleteCancel = { deleteConfirmationRequired = false }
        )
    }
}

@Composable
fun DetailContent(mahasiswa: Mahasiswa, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailMhs(judul = "NIM", isinya = mahasiswa.nim)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailMhs(judul = "Nama", isinya = mahasiswa.nama)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailMhs(judul = "Alamat", isinya = mahasiswa.alamat)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailMhs(judul = "Jenis Kelamin", isinya = mahasiswa.jenisKelamin)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailMhs(judul = "Kelas", isinya = mahasiswa.kelas)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailMhs(judul = "Angkatan", isinya = mahasiswa.angkatan)
        }
    }
}

@Composable
fun ComponentDetailMhs(judul: String, isinya: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = judul,
            Modifier.weight(0.8f),
            fontSize = 20.sp,
            color = Color.LightGray
        )
        Text(
            text = ":",
            Modifier.weight(0.2f),
            fontSize = 20.sp,
            color = Color.LightGray
        )
        Text(
            text = isinya,
            Modifier.weight(2f),
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}