package com.example.myapplication.data.repository

import com.example.myapplication.data.CanalDAO
import com.example.myapplication.data.Class_Canal

class CanalRepository(private val dao: CanalDAO) {
    fun buscarTodos() = dao.buscarTodos()
    suspend fun buscarPorId(id: Int) = dao.buscarPorId(id)
    suspend fun inserir(canal: Class_Canal) = dao.inserir(canal)
    suspend fun inserirTodos(canais: List<Class_Canal>) = dao.inserirTodos(canais)
    suspend fun atualizar(canal: Class_Canal) = dao.atualizar(canal)
    suspend fun deletar(canal: Class_Canal) = dao.deletar(canal)
}
