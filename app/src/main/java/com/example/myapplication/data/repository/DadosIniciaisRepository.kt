package com.example.myapplication.data.repository

import android.util.Log
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.Class_Canal
import com.example.myapplication.data.Class_video
import kotlinx.coroutines.flow.first

/** Mantém os dados da versão anterior somente para bancos vazios. */
class DadosIniciaisRepository(db: AppDatabase) {
    private val canais = CanalRepository(db.canalDao())
    private val videos = VideoRepository(db.videoDao())

    suspend fun inicializar() {
        try {
            if (canais.buscarTodos().first().isEmpty()) {
                canais.inserirTodos(listOf(
                    Class_Canal(1, "Canal 1", "@canal1", "0 inscritos", "Sem descrição ainda."),
                    Class_Canal(2, "Canal 2", "@canal2", "0 inscritos", "Sem descrição ainda.")
                ))
            }
            if (videos.buscarTodos().first().isEmpty()) {
                videos.inserirTodos(listOf(
                    Class_video(1, "UM DIA COMIGO", "@koskynho", "188 mil visualizações", "27 mil", "Passei o dia na rua gravando tudo o que aconteceu. Deixa o like!", 614, "vai ser papo de melhor canal do ytb KKKKKKKK", 0xFF6D7B8D.toInt(), 1),
                    Class_video(2, "Red Dead Redemption 2 - Parte 6 - Caça e pesca na neve, e outras coisas", "DMenor11", "38 mil visualizações • há 9 dias", "2,3 mil", "Nesta parte da gameplay de Red Dead Redemption 2, exploramos as montanhas nevadas, fazemos caça e pesca e cumprimos outras missões. Duração: 4:52:29.", 412, "Melhor série de gameplay de RDR2, não para de entregar!", 0xFF4A5560.toInt(), 2),
                    Class_video(3, "BARCELONA DÁ SHOW, KANE CRAVA E MAIS GOLAÇOS! VEJA TODOS OS GOLS DA TERÇA DE CHAMPIONS", "TNT Sports Brasil", "422 mil visualizações • há 12 horas", "18 mil", "Todos os gols da terça de Champions League: Barcelona dá show, Kane marca e a rodada tem golaços. Duração: 16:51.", 1500, "Que rodada incrível, o Barça jogando muito!", 0xFF1B3A6B.toInt(), 1)
                ))
            }
        } catch (e: Exception) { Log.e("PogVideoRoom", "Erro ao inicializar os dados", e) }
    }
}
