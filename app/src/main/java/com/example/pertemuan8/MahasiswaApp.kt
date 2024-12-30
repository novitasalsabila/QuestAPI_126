package com.example.pertemuan8

import com.example.pertemuan8.ui.navigasi.PengelolaHalaman
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaApp(modifier: Modifier = Modifier) {

    // Mengatur perilaku scroll untuk TopAppBar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(

        // Menambahkan scroll behavior ke Scaffold
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        // topBar = { TopAppBar(scrollBehavior = scrollBehavior) }
    ) {
        // Area konten utama
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            // Menampilkan halaman dengan navigasi
            PengelolaHalaman()
        }
    }
}