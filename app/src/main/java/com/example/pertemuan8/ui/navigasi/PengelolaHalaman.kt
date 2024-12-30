package com.example.pertemuan8.ui.navigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pertemuan8.ui.view.DestinasiEntry
import com.example.pertemuan8.ui.view.DestinasiHome
import com.example.pertemuan8.ui.view.DetailMhsScreen
import com.example.pertemuan8.ui.view.EntryMhsScreen
import com.example.pertemuan8.ui.view.HomeScreen
import com.example.pertemuan8.ui.view.UpdateMhsScreen

@Composable
fun PengelolaHalaman(navController: NavHostController = rememberNavController()) {

    // Mengatur navigasi antar layar menggunakan NavHost
    NavHost(
        navController = navController, // Controller navigasi
        startDestination = DestinasiHome.route,
        modifier = Modifier,
    ) {
        // Halaman Home
        composable(DestinasiHome.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(DestinasiEntry.route) }, // Navigasi ke EntryMhsScreen
                onDetailClick = { nim ->
                    navController.navigate("detail/$nim") // Navigasi ke halaman detail dengan NIM
                }
            )
        }

        // Halaman EntryMhs
        composable(DestinasiEntry.route) {
            EntryMhsScreen(navigateBack = {
                navController.navigate(DestinasiHome.route) { // Navigasi kembali ke HomeScreen
                    popUpTo(DestinasiHome.route) {
                        inclusive = true // Bersihkan layar sebelumnya
                    }
                }
            })
        }

        // Halaman UpdateMhs
        composable(
            route = "update/{nim}",
            arguments = listOf(navArgument("nim") { type = NavType.StringType })
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: ""
            UpdateMhsScreen(
                nim = nim,
                navigateBack = {
                    navController.popBackStack() // Kembali ke halaman sebelumnya
                }
            )
        }

        // Halaman DetailMhs
        composable(
            route = "detail/{nim}",
            arguments = listOf(navArgument("nim") { type = NavType.StringType }) // Definisikan parameter "nim"
        ) { backStackEntry ->
            val nim = backStackEntry.arguments?.getString("nim") ?: "" // Ambil nilai NIM dari parameter
            DetailMhsScreen(
                nim = nim,
                navigateEdit = { editNim ->
                    navController.navigate("update/$editNim") // Navigasi ke halaman update dengan NIM
                },
                navigateBack = {
                    navController.popBackStack() // Kembali ke halaman sebelumnya
                }
            )
        }
    }
}