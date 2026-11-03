package com.example.myapplication.data.repository

import com.example.myapplication.data.VideoDAO
import com.example.myapplication.data.Class_video

class VideoRepository(private val dao: VideoDAO) {
    fun buscarTodos() = dao.buscarTodos()
    suspend fun buscarPorId(id: Int) = dao.buscarPorId(id)
    suspend fun buscarPorCanalId(id: Int) = dao.buscarPorCanalId(id)
    suspend fun inserir(video: Class_video) = dao.inserir(video)
    suspend fun inserirTodos(videos: List<Class_video>) = dao.inserirTodos(videos)
    suspend fun atualizar(video: Class_video) = dao.atualizar(video)
    suspend fun deletar(video: Class_video) = dao.deletar(video)
}
