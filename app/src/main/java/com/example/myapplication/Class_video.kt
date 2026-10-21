package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

data class Video(
    val id: Int,
    val titulo: String,
    val canal: String,
    val visualizacoes: String,
    val curtidas: String,
    val descricao: String,
    val totalComentarios: Int,
    val comentarioDestaque: String,
    val corMiniatura: Color,
    val canalId: Int = 1
)

// Lista única, acessível pelo Inicio e pelo Video_Aberto
val listaVideos = mutableStateListOf(
    Video(
        1, "UM DIA COMIGO", "@koskynho", "188 mil visualizações", "27 mil",
        "Passei o dia na rua gravando tudo o que aconteceu. Deixa o like!",
        614, "vai ser papo de melhor canal do ytb KKKKKKKK", Color(0xFF6D7B8D),
        canalId = 1
    ),
    Video(
        2, "Red Dead Redemption 2 - Parte 6 - Caça e pesca na neve, e outras coisas", "DMenor11", "38 mil visualizações • há 9 dias", "2,3 mil",
        "Nesta parte da gameplay de Red Dead Redemption 2, exploramos as montanhas nevadas, fazemos caça e pesca e cumprimos outras missões. Duração: 4:52:29.",
        412, "Melhor série de gameplay de RDR2, não para de entregar!", Color(0xFF4A5560),
        canalId = 2
    ),
    Video(
        3, "BARCELONA DÁ SHOW, KANE CRAVA E MAIS GOLAÇOS! VEJA TODOS OS GOLS DA TERÇA DE CHAMPIONS", "TNT Sports Brasil", "422 mil visualizações • há 12 horas", "18 mil",
        "Todos os gols da terça de Champions League: Barcelona dá show, Kane marca e a rodada tem golaços. Duração: 16:51.",
        1500, "Que rodada incrível, o Barça jogando muito!", Color(0xFF1B3A6B),
        canalId = 1
    )
)
