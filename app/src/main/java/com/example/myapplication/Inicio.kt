package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun previewInicio() { }

@Composable
fun Inicio(onVideoClick: (Int) -> Unit) {
    val context = LocalContext.current
    val dao = remember { AppDatabase.getDatabase(context).videoDao() }
    var videos by remember { mutableStateOf<List<Video>>(emptyList()) }

    LaunchedEffect(Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            videos = buscarVideos(dao)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0F0F0F)),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(videos, key = { it.id }) { video ->
            CardVideo(video = video, onClick = { onVideoClick(video.id) })
        }
    }
}

@Composable
fun CardVideo(video: Video, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Box(
            modifier = Modifier.fillMaxWidth().height(220.dp).background(video.corMiniatura)
        )
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(modifier = Modifier.size(40.dp).background(Color.Gray, CircleShape))
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
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", tint = Color.White)
        }
    }
}
