package com.example.myapplication

object Rotas {
    const val INICIO = "inicio"
    const val VIDEO_ABERTO = "video/{videoId}"

    const val VOCE = "voce"
    const val INSCRICOES = "inscricoes"
    const val PLAYLIST = "playlist/{playlistId}"

    const val VER_CANAL = "canal/{canalId}"
    const val CONFIGURACOES = "configuracoes"

    fun videoAberto(id: Int) = "video/$id"

    fun playlist(id: Int) = "playlist/$id"

    // Monta a rota real do canal, ex.: verCanal(2) -> "canal/2"
    fun verCanal(id: Int) = "canal/$id"
}
