package com.example.myapplication.ui

import com.example.myapplication.*
import com.example.myapplication.data.*
import com.example.myapplication.viewmodel.*

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Canais inscritos e disponíveis, buscados do banco de dados Room.
 * Formulário retrátil para criar canal (inserirCanal) e lixeira em cada
 * canal para excluir (deletarCanal).
 */
@Composable
fun Inscricoes(
    onCanalClick: (Int) -> Unit,
    onExplorarVideos: () -> Unit = {}
) {
    val canalVm = canalViewModel()
    val videoVm = videoViewModel()
    val canalState by canalVm.uiState.collectAsState()
    val videoState by videoVm.uiState.collectAsState()
    val canais = canalState.canais
    val videos = videoState.videos
    val mostrandoFormularioCanal = canalState.mostrandoFormularioCanal

    val inscritos = canais.filter { it.inscrito }
    val disponiveis = canais.filter { !it.inscrito }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F0F)),
        contentPadding = PaddingValues(vertical = 12.dp)
    ) {
        item {
            if (mostrandoFormularioCanal) {
                FormularioCanal(
                    state = canalState,
                    vm = canalVm,
                    onSalvar = { canalVm.salvarCanal() },
                    onCancelar = { canalVm.mostrarFormulario(false) }
                )
            } else {
                OutlinedButton(
                    onClick = { canalVm.mostrarFormulario(true) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Novo canal", color = Color.White)
                }
            }
        }

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
                    videosDoCanal = videos.filter { it.canalId == canal.id },
                    textoBotao = "Inscrito",
                    destaque = false,
                    onClick = { onCanalClick(canal.id) },
                    onToggle = { canalVm.alternarInscricao(canal) },
                    onExcluir = { canalVm.excluirCanal(canal) }
                )
            }
        }

        if (disponiveis.isNotEmpty()) {
            item { TituloLista("Canais disponíveis") }
            items(disponiveis, key = { it.id }) { canal ->
                LinhaCanal(
                    canal = canal,
                    videosDoCanal = videos.filter { it.canalId == canal.id },
                    textoBotao = "Inscrever-se",
                    destaque = true,
                    onClick = { onCanalClick(canal.id) },
                    onToggle = { canalVm.alternarInscricao(canal) },
                    onExcluir = { canalVm.excluirCanal(canal) }
                )
            }
        }
    }
}

/**
 * Formulário exibido na própria tela (sem AlertDialog).
 * Só chama onSalvar se os campos obrigatórios (nome e identificador) estiverem preenchidos.
 */
@Composable
private fun FormularioCanal(
    state: CanalUiState,
    vm: CanalViewModel,
    onSalvar: () -> Unit,
    onCancelar: () -> Unit
) {
    val nome = state.nomeFormulario
    val identificador = state.identificadorFormulario
    val descricao = state.descricaoFormulario
    val erro = state.erro

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF272727))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Novo canal",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            CampoTexto(
                valor = nome,
                onValorChange = vm::alterarNome,
                rotulo = "Nome do canal",
                modifier = Modifier.fillMaxWidth(),
                erro = erro != null && nome.isBlank()
            )
            CampoTexto(
                valor = identificador,
                onValorChange = vm::alterarIdentificador,
                rotulo = "Identificador (ex.: @meucanal)",
                modifier = Modifier.fillMaxWidth(),
                erro = erro != null && identificador.isBlank()
            )
            CampoTexto(
                valor = descricao,
                onValorChange = vm::alterarDescricao,
                rotulo = "Descrição (opcional)",
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
                ) { Text("Salvar Canal") }

                OutlinedButton(onClick = onCancelar) {
                    Text("Cancelar", color = Color.White)
                }
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
    videosDoCanal: List<Video>,
    textoBotao: String,
    destaque: Boolean,
    onClick: () -> Unit,
    onToggle: () -> Unit,
    onExcluir: () -> Unit
) {
    val corAvatar = videosDoCanal.firstOrNull()?.corMiniatura ?: Color.Gray

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
        IconButton(onClick = onExcluir) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "Excluir canal ${canal.nome}",
                tint = Color.LightGray
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F0F)
@Composable
fun InscricoesPreview() {
    MyApplicationTheme { Inscricoes(onCanalClick = {}) }
}
