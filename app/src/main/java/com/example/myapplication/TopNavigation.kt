package com.example.myapplication

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayCircleFilled
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * TopNavBar genérica e reutilizável.
 * Observa a rota ativa e exibe o estilo apropriado (Principal vs Detalhes/Voltar).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavBar(
    navController: NavHostController,
    tituloCustomizado: String? = null,
    mostrarBotaoVoltar: Boolean? = null
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    // Define se é uma tela principal (exibe a barra do aplicativo) ou tela secundária
    val eTelaPrincipal = rotaAtual in listOf(Rotas.INICIO, Rotas.INSCRICOES, Rotas.VOCE)
    val exibirVoltar = mostrarBotaoVoltar ?: !eTelaPrincipal

    if (eTelaPrincipal && tituloCustomizado == null) {
        MainTopBar(navController = navController)
    } else {
        SecondaryTopBar(
            titulo = tituloCustomizado ?: obterTituloPorRota(rotaAtual),
            exibirVoltar = exibirVoltar,
            onVoltar = { navController.popBackStack() },
            onConfiguracoesClick = if (rotaAtual != Rotas.CONFIGURACOES) {
                { navController.navigate(Rotas.CONFIGURACOES) }
            } else null
        )
    }
}

/**
 * TopBar para telas principais (Início, Inscrições, Você)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    navController: NavHostController,
    tituloApp: String = "PogVideo"
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PlayCircleFilled,
                    contentDescription = "Logo",
                    tint = Color.Magenta
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = tituloApp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        actions = {
//            IconButton(onClick = { /* Ação de Transmitir */ }) {
//                Icon(Icons.Default.Cast, contentDescription = "Transmitir", tint = Color.White)
//            }
            IconButton(onClick = { /* Ação de Notificações */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = Color.White)
            }
            IconButton(onClick = { /* Ação de Pesquisa */ }) {
                Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = Color.White)
            }
            IconButton(onClick = { navController.navigate(Rotas.CONFIGURACOES) }) {
                Icon(Icons.Default.Settings, contentDescription = "Configurações", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F0F0F))
    )
}

/**
 * TopBar para telas secundárias com botão de voltar e título personalizável
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTopBar(
    titulo: String,
    exibirVoltar: Boolean = true,
    onVoltar: () -> Unit = {},
    onConfiguracoesClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = titulo,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        navigationIcon = {
            if (exibirVoltar) {
                IconButton(onClick = onVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            if (onConfiguracoesClick != null) {
                IconButton(onClick = onConfiguracoesClick) {
                    Icon(Icons.Default.Settings, contentDescription = "Configurações", tint = Color.White)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F0F0F))
    )
}

/**
 * Retorna o título padrão de acordo com a rota atual
 */
private fun obterTituloPorRota(rota: String?): String {
    return when {
        rota == Rotas.CONFIGURACOES -> "Configurações"
        rota == Rotas.PLAYLIST -> "Playlist"
        rota == Rotas.INSCRICOES -> "Inscrições"
        rota == Rotas.VOCE -> "Você"
        rota?.startsWith("canal/") == true -> "Canal"
        rota?.startsWith("video/") == true -> ""
        else -> ""
    }
}
