package com.example.myapplication.ui

import com.example.myapplication.*
import com.example.myapplication.data.*
import com.example.myapplication.viewmodel.*

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Ver_Canal(canalId: Int, navController: NavController) {

    val canalVm = canalViewModel()
    val videoVm = videoViewModel()
    val canalState by canalVm.uiState.collectAsState()
    val videoState by videoVm.uiState.collectAsState()
    val canal = canalState.canais.find { it.id == canalId }
    val videosCanal = videoState.videos.filter { it.canalId == canalId }
    val mostrandoFormularioVideo = videoState.mostrandoFormularioVideo

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
                    canalVm.alternarInscricao(canalAtual)
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
                    state = videoState,
                    vm = videoVm,
                    onSalvar = { videoVm.salvarVideo(canalId, canalAtual.nome) },
                    onCancelar = { videoVm.mostrarFormulario(false) }
                )
            } else {
                OutlinedButton(
                    onClick = { videoVm.mostrarFormulario(true) },
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
    state: VideoUiState,
    vm: VideoViewModel,
    onSalvar: () -> Unit,
    onCancelar: () -> Unit
) {
    val titulo = state.tituloFormulario
    val visualizacoes = state.visualizacoesFormulario
    val curtidas = state.curtidasFormulario
    val descricao = state.descricaoFormulario
    val comentarioDestaque = state.comentarioFormulario
    val erro = state.erro

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
                onValorChange = vm::alterarTitulo,
                rotulo = "Título do vídeo",
                modifier = Modifier.fillMaxWidth(),
                erro = erro != null && titulo.isBlank()
            )
            CampoTexto(
                valor = visualizacoes,
                onValorChange = vm::alterarVisualizacoes,
                rotulo = "Visualizações (ex.: 1500)",
                modifier = Modifier.fillMaxWidth()
            )
            CampoTexto(
                valor = curtidas,
                onValorChange = vm::alterarCurtidas,
                rotulo = "Curtidas (ex.: 20 mil)",
                modifier = Modifier.fillMaxWidth()
            )
            CampoTexto(
                valor = descricao,
                onValorChange = vm::alterarDescricao,
                rotulo = "Descrição",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )
            CampoTexto(
                valor = comentarioDestaque,
                onValorChange = vm::alterarComentario,
                rotulo = "Comentário em destaque",
                modifier = Modifier.fillMaxWidth()
            )

            erro?.let { TextoErro(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onSalvar,
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
