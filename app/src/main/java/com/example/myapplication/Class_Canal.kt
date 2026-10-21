package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf

data class Canal(
    val id: Int,
    val nome: String,
    val identificador: String,
    val inscritos: String,
    val descricao: String,
    val inscrito: Boolean = false
)

// Lista única de canais, usada por Inscricoes e Ver_Canal
val listaCanais = mutableStateListOf(
    Canal(
        id = 1,
        nome = "Canal 1",
        identificador = "@canal1",
        inscritos = "0 inscritos",
        descricao = "Sem descrição ainda."
    ),
    Canal(
        id = 2,
        nome = "Canal 2",
        identificador = "@canal2",
        inscritos = "0 inscritos",
        descricao = "Sem descrição ainda."
    )
)

// Vídeos de um canal, sem duplicar dados dentro do Canal
fun videosDoCanal(canalId: Int): List<Video> = listaVideos.filter { it.canalId == canalId }

fun buscarCanal(id: Int): Canal? = listaCanais.find { it.id == id }

// Estado de inscrição compartilhado: todas as telas leem listaCanais
fun alternarInscricao(canalId: Int) {
    val i = listaCanais.indexOfFirst { it.id == canalId }
    if (i >= 0) listaCanais[i] = listaCanais[i].copy(inscrito = !listaCanais[i].inscrito)
}
