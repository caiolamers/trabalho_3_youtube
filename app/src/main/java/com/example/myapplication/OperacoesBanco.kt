package com.example.myapplication

import android.util.Log

private const val TAG_BANCO = "PogVideoRoom"

private val dadosIniciaisCanais = listOf(
    Class_Canal(
        id = 1,
        nome = "Canal 1",
        identificador = "@canal1",
        inscritos = "0 inscritos",
        descricao = "Sem descrição ainda."
    ),
    Class_Canal(
        id = 2,
        nome = "Canal 2",
        identificador = "@canal2",
        inscritos = "0 inscritos",
        descricao = "Sem descrição ainda."
    )
)

private val dadosIniciaisVideos = listOf(
    Class_video(
        1, "UM DIA COMIGO", "@koskynho", "188 mil visualizações", "27 mil",
        "Passei o dia na rua gravando tudo o que aconteceu. Deixa o like!",
        614, "vai ser papo de melhor canal do ytb KKKKKKKK", 0xFF6D7B8D.toInt(),
        canalId = 1
    ),
    Class_video(
        2, "Red Dead Redemption 2 - Parte 6 - Caça e pesca na neve, e outras coisas", "DMenor11", "38 mil visualizações • há 9 dias", "2,3 mil",
        "Nesta parte da gameplay de Red Dead Redemption 2, exploramos as montanhas nevadas, fazemos caça e pesca e cumprimos outras missões. Duração: 4:52:29.",
        412, "Melhor série de gameplay de RDR2, não para de entregar!", 0xFF4A5560.toInt(),
        canalId = 2
    ),
    Class_video(
        3, "BARCELONA DÁ SHOW, KANE CRAVA E MAIS GOLAÇOS! VEJA TODOS OS GOLS DA TERÇA DE CHAMPIONS", "TNT Sports Brasil", "422 mil visualizações • há 12 horas", "18 mil",
        "Todos os gols da terça de Champions League: Barcelona dá show, Kane marca e a rodada tem golaços. Duração: 16:51.",
        1500, "Que rodada incrível, o Barça jogando muito!", 0xFF1B3A6B.toInt(),
        canalId = 1
    )
)

suspend fun inserirVideo(video: Class_video, dao: VideoDAO) {
    try {
        dao.inserir(video.toEntity())
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao inserir vídeo", e)
    }
}

suspend fun buscarVideos(dao: VideoDAO): List<Class_video> = try {
    dao.buscarTodos().map { it.toVideo() }
} catch (e: Exception) {
    Log.e(TAG_BANCO, "Erro ao buscar vídeos", e)
    emptyList()
}

suspend fun buscarVideoPorId(id: Int, dao: VideoDAO): Class_video? = try {
    dao.buscarPorId(id)?.toVideo()
} catch (e: Exception) {
    Log.e(TAG_BANCO, "Erro ao buscar vídeo por ID=$id", e)
    null
}

suspend fun buscarVideosDoCanal(canalId: Int, dao: VideoDAO): List<Class_video> = try {
    dao.buscarPorCanalId(canalId).map { it.toVideo() }
} catch (e: Exception) {
    Log.e(TAG_BANCO, "Erro ao buscar vídeos do canal ID=$canalId", e)
    emptyList()
}

suspend fun atualizarVideo(video: Class_video, dao: VideoDAO) {
    try {
        dao.atualizar(video.toEntity())
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao atualizar vídeo", e)
    }
}

suspend fun deletarVideo(video: Class_video, dao: VideoDAO) {
    try {
        dao.deletar(video.toEntity())
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao deletar vídeo", e)
    }
}

suspend fun inserirCanal(canal: Class_Canal, dao: CanalDAO) {
    try {
        dao.inserir(canal.toEntity())
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao inserir canal", e)
    }
}

suspend fun buscarCanais(dao: CanalDAO): List<Class_Canal> = try {
    dao.buscarTodos().map { it.toCanal() }
} catch (e: Exception) {
    Log.e(TAG_BANCO, "Erro ao buscar canais", e)
    emptyList()
}

suspend fun buscarCanalPorId(id: Int, dao: CanalDAO): Class_Canal? = try {
    dao.buscarPorId(id)?.toCanal()
} catch (e: Exception) {
    Log.e(TAG_BANCO, "Erro ao buscar canal por ID=$id", e)
    null
}

suspend fun atualizarCanal(canal: Class_Canal, dao: CanalDAO) {
    try {
        dao.atualizar(canal.toEntity())
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao atualizar canal", e)
    }
}

suspend fun deletarCanal(canal: Class_Canal, dao: CanalDAO) {
    try {
        dao.deletar(canal.toEntity())
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao deletar canal", e)
    }
}

/**
 * Os dados de demonstração são copiados para o banco somente na primeira execução.
 */
suspend fun inicializarDadosIniciais(db: AppDatabase) {
    try {
        if (db.canalDao().buscarTodos().isEmpty()) {
            db.canalDao().inserirTodos(dadosIniciaisCanais)
        }
        if (db.videoDao().buscarTodos().isEmpty()) {
            db.videoDao().inserirTodos(dadosIniciaisVideos)
        }
    } catch (e: Exception) {
        Log.e(TAG_BANCO, "Erro ao inicializar os dados", e)
    }
}
