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
import androidx.compose.ui.unit.sp

private val CorExcluir = Color(0xFFFF6B6B)

@Preview
@Composable
fun previewtela1() { Video_Aberto(videoId = 1) {} }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Video_Aberto(videoId: Int, onVoltar: () -> Unit) {
    val canalVm = canalViewModel()
    val videoVm = videoViewModel()
    val canalState by canalVm.uiState.collectAsState()
    val videoState by videoVm.uiState.collectAsState()
    val video = videoState.videos.find { it.id == videoId }
    val inscrito = canalState.canais.find { it.id == video?.canalId }?.inscrito ?: false
    var descricaoExpandida by remember { mutableStateOf(false) }
    var curtido by remember { mutableStateOf(false) }
    var descurtido by remember { mutableStateOf(false) }
    val modoEdicao = videoState.modoEdicao

    Scaffold(containerColor = Color(0xFF0F0F0F)) { padding ->
        val videoAtual = video
        if (videoAtual == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Carregando vídeo ou vídeo não encontrado", color = Color.White)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(230.dp).background(videoAtual.corMiniatura)
                )
                Text(
                    text = videoAtual.titulo,
                    color = Color.White,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
                Text(
                    text = "${videoAtual.canal}  ${videoAtual.curtidas} curtidas  ${videoAtual.visualizacoes}" +
                        if (descricaoExpandida) "" else "  ...mais",
                    color = Color.LightGray,
                    maxLines = if (descricaoExpandida) Int.MAX_VALUE else 1,
                    modifier = Modifier.padding(horizontal = 16.dp).clickable {
                        descricaoExpandida = !descricaoExpandida
                    }
                )
                if (descricaoExpandida) {
                    Text(videoAtual.descricao, color = Color.White, modifier = Modifier.padding(16.dp))
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(Modifier.size(44.dp).background(Color.Gray, CircleShape))
                    Button(
                        onClick = {
                            canalState.canais.find { it.id == videoAtual.canalId }?.let { canalVm.alternarInscricao(it) }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (inscrito) Color.DarkGray else Color.White,
                            contentColor = if (inscrito) Color.White else Color.Black
                        )
                    ) { Text(if (inscrito) "Inscrito" else "Inscrever-se") }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { curtido = !curtido; if (curtido) descurtido = false }) {
                        Icon(Icons.Default.ThumbUp, "Curtir", tint = if (curtido) Color(0xFF3EA6FF) else Color.White)
                    }
                    IconButton(onClick = { descurtido = !descurtido; if (descurtido) curtido = false }) {
                        Icon(Icons.Default.ThumbDown, "Não gostei", tint = if (descurtido) Color(0xFF3EA6FF) else Color.White)
                    }
                    IconButton(onClick = { /* compartilhar */ }) {
                        Icon(Icons.Default.Share, "Compartilhar", tint = Color.White)
                    }
                    IconButton(onClick = { /* mais opções */ }) {
                        Icon(Icons.Default.MoreVert, "Mais", tint = Color.White)
                    }
                }

                // Ações de CRUD: editar (atualizarVideo) e excluir (deletarVideo)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(onClick = { if (modoEdicao) videoVm.cancelarEdicao() else videoVm.iniciarEdicao(videoAtual) }) {
                        Text(if (modoEdicao) "Fechar edição" else "Editar vídeo", color = Color.White)
                    }
                    OutlinedButton(
                        onClick = {
                            videoVm.excluirVideo(videoAtual, onVoltar)
                        }
                    ) { Text("Excluir vídeo", color = CorExcluir) }
                }

                if (modoEdicao) {
                    FormularioEdicaoVideo(
                        state = videoState,
                        vm = videoVm,
                        onSalvar = { videoVm.salvarEdicao(videoAtual) },
                        onCancelar = { videoVm.cancelarEdicao() }
                    )
                }

                Spacer(Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Comentários  ${videoAtual.totalComentarios}", color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(Modifier.size(32.dp).background(Color.Gray, CircleShape))
                            Spacer(Modifier.width(12.dp))
                            Text(videoAtual.comentarioDestaque, color = Color.White)
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Formulário de edição exibido na própria tela (sem AlertDialog).
 * O título é obrigatório; a descrição pode ficar vazia.
 */
@Composable
private fun FormularioEdicaoVideo(
    state: VideoUiState,
    vm: VideoViewModel,
    onSalvar: () -> Unit,
    onCancelar: () -> Unit
) {
    val titulo = state.tituloEdicao
    val descricao = state.descricaoEdicao
    val erro = state.erro

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Editar vídeo",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            CampoTexto(
                valor = titulo,
                onValorChange = vm::alterarTituloEdicao,
                rotulo = "Título",
                modifier = Modifier.fillMaxWidth(),
                erro = erro != null && titulo.isBlank()
            )
            CampoTexto(
                valor = descricao,
                onValorChange = vm::alterarDescricaoEdicao,
                rotulo = "Descrição",
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            erro?.let { TextoErro(it) }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onSalvar,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) { Text("Salvar Alterações") }

                OutlinedButton(onClick = onCancelar) {
                    Text("Cancelar", color = Color.White)
                }
            }
        }
    }
}
