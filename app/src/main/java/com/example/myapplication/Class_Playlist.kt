package com.example.myapplication

// Playlist local: guarda só os ids dos vídeos, sem duplicar dados de Video.
data class ListaReproducao(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val videoIds: List<Int>
)

val listaPlaylists = listOf(
    ListaReproducao(
        id = 1,
        titulo = "Assistir mais tarde",
        descricao = "Vídeos que você salvou para ver depois.",
        videoIds = listOf(1, 3)
    ),
    ListaReproducao(
        id = 2,
        titulo = "Gameplays",
        descricao = "Séries de jogos para maratonar.",
        videoIds = listOf(2)
    ),
    ListaReproducao(
        id = 3,
        titulo = "Playlist vazia",
        descricao = "Exemplo de playlist ainda sem vídeos.",
        videoIds = emptyList()
    )
)

fun buscarPlaylist(id: Int): ListaReproducao? = listaPlaylists.find { it.id == id }
