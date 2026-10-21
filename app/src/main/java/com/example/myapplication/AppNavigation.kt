package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    // A barra inferior aparece nas três áreas principais e na tela do canal
    val rotasComBarra = listOf(
        Rotas.INICIO,
        Rotas.INSCRICOES,
        Rotas.VOCE,
        Rotas.VER_CANAL
    )

    Scaffold(
        topBar = {
            TopNavBar(navController = navController)
        },
        bottomBar = {
            if (rotaAtual in rotasComBarra) {
                BottomNavBar(navInterno = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.INICIO,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Rotas.INICIO) {
                Inicio(onVideoClick = { id -> navController.navigate(Rotas.videoAberto(id)) })
            }

            composable(Rotas.INSCRICOES) {
                Inscricoes(
                    onCanalClick = { id -> navController.navigate(Rotas.verCanal(id)) },
                    onExplorarVideos = { navController.irParaAba(Rotas.INICIO) }
                )
            }

            composable(Rotas.VOCE) {
                VoceScreen(
                    onPlaylistClick = { id -> navController.navigate(Rotas.playlist(id)) },
                    onVideoClick = { id -> navController.navigate(Rotas.videoAberto(id)) },
                    onInscricoesClick = { navController.irParaAba(Rotas.INSCRICOES) },
                    onConfiguracoesClick = { navController.navigate(Rotas.CONFIGURACOES) }
                )
            }

            composable(
                route = Rotas.VIDEO_ABERTO,
                arguments = listOf(navArgument("videoId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("videoId") ?: 0
                Video_Aberto(videoId = id, onVoltar = { navController.popBackStack() })
            }

            composable(
                route = Rotas.VER_CANAL,
                arguments = listOf(navArgument("canalId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("canalId") ?: 0
                Ver_Canal(canalId = id, navController = navController)
            }

            composable(Rotas.CONFIGURACOES) {
                Configuracoes(navController = navController)
            }

            composable(
                route = Rotas.PLAYLIST,
                arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("playlistId") ?: 0
                Playlist(
                    playlistId = id,
                    onVideoClick = { videoId -> navController.navigate(Rotas.videoAberto(videoId)) },
                    onExplorarVideos = { navController.irParaAba(Rotas.INICIO) }
                )
            }
        }
    }
}
