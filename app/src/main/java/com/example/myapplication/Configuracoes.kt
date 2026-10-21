package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configuracoes(navController: NavController) {

    var notificacoes by remember { mutableStateOf(true) }
    var reproducaoAutomatica by remember { mutableStateOf(true) }
    var economiaDados by remember { mutableStateOf(false) }
    var sobreExpandido by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF0F0F0F)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // ----- Conta -----
            SecaoTitulo("Conta")

            ItemConfiguracao(
                icone = Icons.Default.Person,
                titulo = "Gerenciar inscrições",
                subtitulo = "Ver, adicionar e remover canais",
                onClick = { navController.navigate(Rotas.INSCRICOES) }
            )
            ItemConfiguracao(
                icone = Icons.Default.List,
                titulo = "Minha playlist",
                subtitulo = "Vídeos que você salvou",
                onClick = { navController.navigate(Rotas.playlist(1)) }
            )

            // ----- Geral -----
            SecaoTitulo("Geral")

            ItemConfiguracao(
                icone = Icons.Default.Notifications,
                titulo = "Notificações",
                subtitulo = if (notificacoes) "Ativadas" else "Desativadas",
                onClick = { notificacoes = !notificacoes },
                acaoDireita = {
                    Checkbox(
                        checked = notificacoes,
                        onCheckedChange = { notificacoes = it },
                        colors = coresCheckbox()
                    )
                }
            )
            ItemConfiguracao(
                icone = Icons.Default.PlayArrow,
                titulo = "Reprodução automática",
                subtitulo = if (reproducaoAutomatica) "Ativada" else "Desativada",
                onClick = { reproducaoAutomatica = !reproducaoAutomatica },
                acaoDireita = {
                    Checkbox(
                        checked = reproducaoAutomatica,
                        onCheckedChange = { reproducaoAutomatica = it },
                        colors = coresCheckbox()
                    )
                }
            )
            ItemConfiguracao(
                icone = Icons.Default.Settings,
                titulo = "Economia de dados",
                subtitulo = if (economiaDados) "Ativada" else "Desativada",
                onClick = { economiaDados = !economiaDados },
                acaoDireita = {
                    Checkbox(
                        checked = economiaDados,
                        onCheckedChange = { economiaDados = it },
                        colors = coresCheckbox()
                    )
                }
            )

            // ----- Sobre -----
            SecaoTitulo("Sobre")

            ItemConfiguracao(
                icone = Icons.Default.Info,
                titulo = "Sobre o aplicativo",
                subtitulo = if (sobreExpandido) "Toque para recolher" else "Toque para ver detalhes",
                onClick = { sobreExpandido = !sobreExpandido }
            )
            if (sobreExpandido) {
                Text(
                    text = "Aplicativo inspirado no YouTube, desenvolvido em Kotlin com " +
                            "Jetpack Compose para fins acadêmicos. Versão 1.0.",
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 72.dp, vertical = 8.dp)
                )
            }

            Spacer(Modifier.padding(16.dp))
        }
    }
}

@Composable
private fun SecaoTitulo(texto: String) {
    Text(
        text = texto,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
private fun ItemConfiguracao(
    icone: ImageVector,
    titulo: String,
    subtitulo: String,
    onClick: () -> Unit,
    acaoDireita: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, contentDescription = titulo, tint = Color.White)
        Spacer(Modifier.width(24.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, color = Color.White, fontSize = 18.sp)
            Text(text = subtitulo, color = Color.LightGray, fontSize = 13.sp)
        }
        if (acaoDireita != null) {
            acaoDireita()
        }
    }
}

@Composable
private fun coresCheckbox() = CheckboxDefaults.colors(
    checkedColor = Color.White,
    checkmarkColor = Color.Black,
    uncheckedColor = Color.Gray
)

@Preview(showBackground = true)
@Composable
fun ConfiguracoesPreview() {
    MyApplicationTheme {
        Configuracoes(navController = rememberNavController())
    }
}
