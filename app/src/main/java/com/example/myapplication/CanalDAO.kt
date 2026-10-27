package com.example.myapplication

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface CanalDAO {
    @Insert
    suspend fun inserir(canal: Class_Canal)

    @Insert
    suspend fun inserirTodos(canais: List<Class_Canal>)

    @Query("SELECT * FROM canais ORDER BY id")
    suspend fun buscarTodos(): List<Class_Canal>

    @Query("SELECT * FROM canais WHERE id = :id")
    suspend fun buscarPorId(id: Int): Class_Canal?

    @Update
    suspend fun atualizar(canal: Class_Canal)

    @Delete
    suspend fun deletar(canal: Class_Canal)
}
