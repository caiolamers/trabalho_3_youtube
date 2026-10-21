package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
 * Canais inscritos (estado compartilhado em listaCanais) e, abaixo,
 * os canais disponíveis para abrir e se inscrever.
 */
@Composable
fun Inscricoes(
    onCanalClick: (Int) -> Unit,
    onExplorarVideos: () -> Unit = {}
) {
    val inscritos = listaCanais.filter { it.inscrito }
    val disponiveis = listaCanais.filter { !it.inscrito }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        item { TituloLista("Canais inscritos (${inscritos.size})") }

        if (inscritos.isEmpty()) {
            item {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "Você ainda não se inscreveu em nenhum canal.",
                        color = Color.LightGray
                    )
                    Spacer(Modifier.height(12.dp))
                    OutlinedButton(onClick = onExplorarVideos) {
                        Text("Explorar vídeos", color = Color.White)
                    }
                }
            }
        } else {
            items(inscritos, key = { it.id }) { canal ->
                LinhaCanal(
                    canal = canal,
                    textoBotao = "Inscrito",
                    destaque = false,
                    onClick = { onCanalClick(canal.id) },
                    onToggle = { alternarInscricao(canal.id) }
                )
            }
        }

        if (disponiveis.isNotEmpty()) {
            item { TituloLista("Canais disponíveis") }
            items(disponiveis, key = { it.id }) { canal ->
                LinhaCanal(
                    canal = canal,
                    textoBotao = "Inscrever-se",
                    destaque = true,
                    onClick = { onCanalClick(canal.id) },
                    onToggle = { alternarInscricao(canal.id) }
                )
            }
        }
    }
}

@Composable
private fun TituloLista(texto: String) {
    Text(
        text = texto,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 8.dp)
    )
}

@Composable
private fun LinhaCanal(
    canal: Canal,
    textoBotao: String,
    destaque: Boolean,
    onClick: () -> Unit,
    onToggle: () -> Unit
) {
    val corAvatar = videosDoCanal(canal.id).firstOrNull()?.corMiniatura ?: Color.Gray

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(corAvatar, CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = canal.nome,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${canal.identificador} • ${canal.inscritos}",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(8.dp))
        Button(
            onClick = onToggle,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (destaque) Color.White else Color.DarkGray,
                contentColor = if (destaque) Color.Black else Color.White
            )
        ) { Text(textoBotao) }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun InscricoesPreview() {
    MyApplicationTheme { Inscricoes(onCanalClick = {}) }
}
