package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Tela "Você". Mantém a organização do protótipo: perfil, dois botões,
 * faixa horizontal de vídeos e cartões de playlist (miniatura + texto).
 * As barras superior e inferior vêm do AppNavigation.
 */
@Composable
fun VoceScreen(
    onPlaylistClick: (Int) -> Unit = {},
    onVideoClick: (Int) -> Unit = {},
    onInscricoesClick: () -> Unit = {},
    onConfiguracoesClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    var videosBanco by remember { mutableStateOf<List<Video>>(emptyList()) }
    var canaisBanco by remember { mutableStateOf<List<Canal>>(emptyList()) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            videosBanco = buscarVideos(db.videoDao())
            canaisBanco = buscarCanais(db.canalDao())
        }
    }

    val totalInscricoes = canaisBanco.count { it.inscrito }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 14.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        // Perfil
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color.Gray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("C", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Caio", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "@caio • $totalInscricoes ${if (totalInscricoes == 1) "inscrição" else "inscrições"}",
                    color = Color.LightGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Dois botões de ação
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onInscricoesClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF272727),
                    contentColor = Color.White
                )
            ) { Text("Inscrições") }
            Button(
                onClick = onConfiguracoesClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF272727),
                    contentColor = Color.White
                )
            ) { Text("Configurações") }
        }

        Spacer(Modifier.height(28.dp))

        // Histórico: faixa horizontal de vídeos
        TituloSecao("Histórico")
        if (videosBanco.isEmpty()) {
            TextoVazio("Nenhum vídeo assistido ainda.")
        } else {
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                videosBanco.forEach { video ->
                    Column(
                        modifier = Modifier
                            .width(170.dp)
                            .clickable { onVideoClick(video.id) }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(96.dp)
                                .background(video.corMiniatura, RoundedCornerShape(8.dp))
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = video.titulo,
                            color = Color.White,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = video.canal,
                            color = Color.LightGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        // Playlists: cartões com miniatura + texto
        TituloSecao("Playlists")
        if (listaPlaylists.isEmpty()) {
            TextoVazio("Você ainda não tem playlists.")
        }
        listaPlaylists.forEach { playlist ->
            val videos = playlist.videoIds.mapNotNull { id -> videosBanco.find { it.id == id } }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onPlaylistClick(playlist.id) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(95.dp)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                videos.firstOrNull()?.corMiniatura ?: Color.Gray,
                                RoundedCornerShape(6.dp)
                            )
                    )
                    Column(
                        modifier = Modifier
                            .weight(1.6f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = playlist.titulo,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${videos.size} ${if (videos.size == 1) "vídeo" else "vídeos"}",
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun TituloSecao(texto: String) {
    Text(
        text = texto,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

@Composable
private fun TextoVazio(texto: String) {
    Text(text = texto, color = Color.LightGray, modifier = Modifier.padding(vertical = 8.dp))
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun VoceScreenPreview() {
    MyApplicationTheme { VoceScreen() }
}
