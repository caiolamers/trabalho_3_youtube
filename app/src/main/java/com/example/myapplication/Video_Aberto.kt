package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun previewtela1(){
    Video_Aberto(videoId = 1) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Video_Aberto(videoId: Int, onVoltar: () -> Unit) {

    // Busca o vídeo certo pelo id recebido
    val video = listaVideos.find { it.id == videoId }

    var descricaoExpandida by remember { mutableStateOf(false) }
    // Inscrição compartilhada com as outras telas (listaCanais)
    val inscrito = video?.let { buscarCanal(it.canalId)?.inscrito } ?: false
    var curtido by remember { mutableStateOf(false) }
    var descurtido by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFF0F0F0F)
    ) { padding ->

        if (video == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Vídeo não encontrado", color = Color.White)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Player (miniatura)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .background(video.corMiniatura)
            )

            // Título
            Text(
                text = video.titulo,
                color = Color.White,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            // Canal • curtidas • visualizações • ...mais
            Text(
                text = "${video.canal}  ${video.curtidas} curtidas  ${video.visualizacoes}" +
                        if (descricaoExpandida) "" else "  ...mais",
                color = Color.LightGray,
                maxLines = if (descricaoExpandida) Int.MAX_VALUE else 1,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { descricaoExpandida = !descricaoExpandida }
            )

            if (descricaoExpandida) {
                Text(
                    text = video.descricao,
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Linha de ações
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(Color.Gray, CircleShape)
                )

                Button(
                    onClick = { alternarInscricao(video.canalId) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (inscrito) Color.DarkGray else Color.White,
                        contentColor = if (inscrito) Color.White else Color.Black
                    )
                ) {
                    Text(if (inscrito) "Inscrito" else "Inscrever-se")
                }

                Spacer(Modifier.weight(1f))

                IconButton(onClick = { curtido = !curtido; if (curtido) descurtido = false }) {
                    Icon(
                        Icons.Default.ThumbUp, "Curtir",
                        tint = if (curtido) Color(0xFF3EA6FF) else Color.White
                    )
                }
                IconButton(onClick = { descurtido = !descurtido; if (descurtido) curtido = false }) {
                    Icon(
                        Icons.Default.ThumbDown, "Não gostei",
                        tint = if (descurtido) Color(0xFF3EA6FF) else Color.White
                    )
                }
                IconButton(onClick = { /* compartilhar */ }) {
                    Icon(Icons.Default.Share, "Compartilhar", tint = Color.White)
                }
                IconButton(onClick = { /* mais opções */ }) {
                    Icon(Icons.Default.MoreVert, "Mais", tint = Color.White)
                }
            }

            // Card de comentários
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Comentários  ${video.totalComentarios}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.Gray, CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(video.comentarioDestaque, color = Color.White)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
