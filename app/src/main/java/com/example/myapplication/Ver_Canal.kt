package com.example.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.ui.theme.MyApplicationTheme

// Cores usadas como miniatura dos vídeos criados pelo formulário.
private val coresMiniatura = listOf(
    0xFF6D7B8D.toInt(),
    0xFF4A5560.toInt(),
    0xFF1B3A6B.toInt(),
    0xFF5B3A6B.toInt(),
    0xFF2E5E4E.toInt(),
    0xFF6B3A3A.toInt()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ver_Canal(canalId: Int, navController: NavController) {

    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    var canal by remember { mutableStateOf<Canal?>(null) }
    var videosCanal by remember { mutableStateOf<List<Video>>(emptyList()) }
    var mostrandoFormularioVideo by remember { mutableStateOf(false) }

    LaunchedEffect(canalId) {
        CoroutineScope(Dispatchers.IO).launch {
            canal = buscarCanalPorId(canalId, db.canalDao())
            videosCanal = buscarVideosDoCanal(canalId, db.videoDao())
        }
    }

    // Insere o vídeo no banco, ligado a este canal, e recarrega a lista.
    fun salvarVideo(
        canalNome: String,
        titulo: String,
        visualizacoes: String,
        curtidas: String,
        descricao: String,
        comentarioDestaque: String
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            inserirVideo(
                Class_video(
                    titulo = titulo,
                    canal = canalNome,
                    visualizacoes = visualizacoes,
                    curtidas = curtidas,
                    descricao = descricao,
                    totalComentarios = 0,
                    comentarioDestaque = comentarioDestaque,
                    corMiniaturaArgb = coresMiniatura[(System.currentTimeMillis() % coresMiniatura.size).toInt()],
                    canalId = canalId
                ),
                db.videoDao()
            )
            videosCanal = buscarVideosDoCanal(canalId, db.videoDao())
        }
        mostrandoFormularioVideo = false
    }

    var descricaoExpandida by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = Color(0xFF0F0F0F),
    ) { padding ->

        val canalCarregado = canal
        if (canalCarregado == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Carregando canal ou canal não encontrado", color = Color.White)
            }
            return@Scaffold
        }

        val canalAtual = canalCarregado
        val inscrito = canalAtual.inscrito

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
                        text = canalAtual.nome,
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = canalAtual.identificador, color = Color.LightGray)
                    Text(text = canalAtual.inscritos, color = Color.LightGray)
                }
            }

            // Descrição (toque para expandir, como no Video_Aberto)
            Text(
                text = canalAtual.descricao + if (descricaoExpandida) "" else "  ...mais",
                color = Color.LightGray,
                maxLines = if (descricaoExpandida) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { descricaoExpandida = !descricaoExpandida }
            )

            // Botão Inscrever-se
            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val atualizado = canalAtual.copy(inscrito = !canalAtual.inscrito)
                        atualizarCanal(atualizado, db.canalDao())
                        canal = buscarCanalPorId(canalId, db.canalDao())
                    }
                },
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

            // Botão que abre o formulário de novo vídeo (na própria tela)
            if (mostrandoFormularioVideo) {
                FormularioVideo(
                    onSalvar = { titulo, visualizacoes, curtidas, descricao, comentario ->
                        salvarVideo(
                            canalAtual.nome,
                            titulo,
                            visualizacoes,
                            curtidas,
                            descricao,
                            comentario
                        )
                    },
                    onCancelar = { mostrandoFormularioVideo = false }
                )
            } else {
                OutlinedButton(
                    onClick = { mostrandoFormularioVideo = true },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Adicionar vídeo neste canal", color = Color.White)
                }
            }

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

/**
 * Formulário de novo vídeo, exibido na própria tela (sem AlertDialog).
 * O título é obrigatório; os demais campos têm valor padrão se ficarem vazios.
 */
@Composable
private fun FormularioVideo(
    onSalvar: (
        titulo: String,
        visualizacoes: String,
        curtidas: String,
        descricao: String,
        comentarioDestaque: String
    ) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var visualizacoes by remember { mutableStateOf("") }
    var curtidas by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var comentarioDestaque by remember { mutableStateOf("") }
    var erro by remember { mutableStateOf<String?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Novo vídeo",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            CampoTexto(
                valor = titulo,
                onValorChange = { titulo = it; erro = null },
                rotulo = "Título do vídeo",
                modifier = Modifier.fillMaxWidth(),
                erro = erro != null && titulo.isBlank()
            )
            CampoTexto(
                valor = visualizacoes,
                onValorChange = { visualizacoes = it },
                rotulo = "Visualizações (ex.: 1500)",
                modifier = Modifier.fillMaxWidth()
            )
            CampoTexto(
                valor = curtidas,
                onValorChange = { curtidas = it },
                rotulo = "Curtidas (ex.: 20 mil)",
                modifier = Modifier.fillMaxWidth()
            )
            CampoTexto(
                valor = descricao,
                onValorChange = { descricao = it },
                rotulo = "Descrição",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )
            CampoTexto(
                valor = comentarioDestaque,
                onValorChange = { comentarioDestaque = it },
                rotulo = "Comentário em destaque",
                modifier = Modifier.fillMaxWidth()
            )

            erro?.let { TextoErro(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val tituloLimpo = titulo.trim()
                        if (tituloLimpo.isEmpty()) {
                            erro = "Informe o título do vídeo."
                        } else {
                            val views = visualizacoes.trim()
                            onSalvar(
                                tituloLimpo,
                                when {
                                    views.isEmpty() -> "0 visualizações"
                                    views.all { it.isDigit() } -> "$views visualizações"
                                    else -> views
                                },
                                curtidas.trim().ifEmpty { "0" },
                                descricao.trim().ifEmpty { "Sem descrição." },
                                comentarioDestaque.trim().ifEmpty { "Ainda não há comentários." }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) { Text("Salvar Vídeo") }

                OutlinedButton(onClick = onCancelar) {
                    Text("Cancelar", color = Color.White)
                }
            }
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
