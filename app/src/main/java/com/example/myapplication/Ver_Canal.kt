package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ver_Canal(canalId: Int, navController: NavController) {

    // Busca o canal pelo id recebido na rota (estado compartilhado em listaCanais)
    val canal = buscarCanal(canalId)
    val videosCanal = videosDoCanal(canalId)

    var descricaoExpandida by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color(0xFF0F0F0F),
    ) { padding ->

        if (canal == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Canal não encontrado", color = Color.White)
            }
            return@Scaffold
        }

        val inscrito = canal.inscrito

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Banner do canal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color(0xFF3A3A3A))
            )

            // Avatar + nome, @ e inscritos
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(Color.Gray, CircleShape)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = canal.nome,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = canal.identificador, color = Color.LightGray)
                    Text(text = canal.inscritos, color = Color.LightGray)
                }
            }

            // Descrição (toque para expandir, como no Video_Aberto)
            Text(
                text = canal.descricao + if (descricaoExpandida) "" else "  ...mais",
                color = Color.LightGray,
                maxLines = if (descricaoExpandida) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { descricaoExpandida = !descricaoExpandida }
            )

            // Botão Inscrever-se
            Button(
                onClick = { alternarInscricao(canal.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (inscrito) Color.DarkGray else Color.White,
                    contentColor = if (inscrito) Color.White else Color.Black
                )
            ) {
                Text(if (inscrito) "Inscrito" else "Inscrever-se")
            }

            // Seção de vídeos
            Text(
                text = "Vídeos",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (videosCanal.isEmpty()) {
                Text(
                    text = "Este canal ainda não tem vídeos.",
                    color = Color.LightGray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // Só os vídeos que pertencem a este canal
            videosCanal.forEach { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clickable { navController.navigate(Rotas.videoAberto(video.id)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 120.dp, height = 68.dp)
                                .background(video.corMiniatura, RoundedCornerShape(8.dp))
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = video.titulo,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = video.visualizacoes,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Ver_CanalPreview() {
    MyApplicationTheme {
        Ver_Canal(canalId = 1, navController = rememberNavController())
    }
}
