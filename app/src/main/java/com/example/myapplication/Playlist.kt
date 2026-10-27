package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.myapplication.ui.theme.MyApplicationTheme

/** Mostra a playlist cujo id veio pela rota e seus vídeos. */
@Composable
fun Playlist(
    playlistId: Int,
    onVideoClick: (Int) -> Unit,
    onExplorarVideos: () -> Unit = {}
) {
    val playlist = buscarPlaylist(playlistId)
    val context = LocalContext.current
    val videoDao = remember { AppDatabase.getDatabase(context).videoDao() }
    var videos by remember { mutableStateOf<List<Video>>(emptyList()) }

    LaunchedEffect(playlistId) {
        CoroutineScope(Dispatchers.IO).launch {
            val ids = playlist?.videoIds.orEmpty().toSet()
            videos = buscarVideos(videoDao).filter { it.id in ids }
        }
    }

    if (playlist == null) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Color(0xFF0F0F0F)),
            contentAlignment = Alignment.Center
        ) {
            Text("Playlist não encontrada", color = Color.White)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F))
            .verticalScroll(rememberScrollState())
    ) {
        // Capa
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(videos.firstOrNull()?.corMiniatura ?: Color(0xFF3A3A3A))
        )

        Text(
            text = playlist.titulo,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
        Text(
            text = "${videos.size} ${if (videos.size == 1) "vídeo" else "vídeos"}",
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Text(
            text = playlist.descricao,
            color = Color.LightGray,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // Reproduzir tudo: abre o primeiro vídeo
        Button(
            onClick = { videos.firstOrNull()?.let { onVideoClick(it.id) } },
            enabled = videos.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContainerColor = Color.DarkGray,
                disabledContentColor = Color.Gray
            )
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Reproduzir tudo")
        }

        if (videos.isEmpty()) {
            Text(
                text = "Esta playlist ainda não tem vídeos.",
                color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            OutlinedButton(
                onClick = onExplorarVideos,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) { Text("Explorar vídeos", color = Color.White) }
        }

        videos.forEach { video ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { onVideoClick(video.id) },
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = video.titulo,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${video.canal} • ${video.visualizacoes}",
                            color = Color.LightGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun PlaylistPreview() {
    MyApplicationTheme { Playlist(playlistId = 1, onVideoClick = {}) }
}
