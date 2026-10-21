package com.example.myapplication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

/**
 * Barra de navegação inferior (BottomNavBar) padronizada com o tema escuro.
 * Observa a rota ativa para destacar a aba selecionada.
 */
@Composable
fun BottomNavBar(navInterno: NavHostController) {

    // Observa a rota atual em tempo real (atualiza automaticamente ao trocar de aba)
    val backStackEntry by navInterno.currentBackStackEntryAsState()
    val rotaAtual = backStackEntry?.destination?.route

    // Configuração de cores personalizada no estilo do TopNavBar e YouTube
    val coresItem = NavigationBarItemDefaults.colors(
        selectedIconColor = Color.White,
        selectedTextColor = Color.White,
        indicatorColor = Color(0xFF272727), // Pílula de seleção escura
        unselectedIconColor = Color.Gray,
        unselectedTextColor = Color.Gray
    )

    NavigationBar(
        containerColor = Color(0xFF0F0F0F),
        contentColor = Color.White
    ) {
        NavigationBarItem(
            selected = rotaAtual == Rotas.INICIO,
            onClick = { navInterno.irParaAba(Rotas.INICIO) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
            colors = coresItem
        )
        NavigationBarItem(
            selected = rotaAtual == Rotas.INSCRICOES,
            onClick = { navInterno.irParaAba(Rotas.INSCRICOES) },
            icon = { Icon(Icons.Default.Subscriptions, contentDescription = "Inscrições") },
            label = { Text("Inscrições") },
            colors = coresItem
        )
        NavigationBarItem(
            selected = rotaAtual == Rotas.VOCE,
            onClick = { navInterno.irParaAba(Rotas.VOCE) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Você") },
            label = { Text("Você") },
            colors = coresItem
        )
    }
}


/**
 * Troca de aba sem empilhar cópias: volta até o Início e usa launchSingleTop,
 * então tocar várias vezes na mesma aba não cria telas repetidas.
 */
fun NavController.irParaAba(rota: String) {
    navigate(rota) {
        popUpTo(Rotas.INICIO) { inclusive = false }
        launchSingleTop = true
    }
}
