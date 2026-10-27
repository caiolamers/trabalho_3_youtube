package com.example.myapplication

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update

@Dao
interface VideoDAO {
    @Insert
    suspend fun inserir(video: Class_video)

    @Insert
    suspend fun inserirTodos(videos: List<Class_video>)

    @Query("SELECT * FROM videos ORDER BY id")
    suspend fun buscarTodos(): List<Class_video>

    @Query("SELECT * FROM videos WHERE id = :id")
    suspend fun buscarPorId(id: Int): Class_video?

    @Query("SELECT * FROM videos WHERE canalId = :canalId ORDER BY id")
    suspend fun buscarPorCanalId(canalId: Int): List<Class_video>

    @Update
    suspend fun atualizar(video: Class_video)

    @Delete
    suspend fun deletar(video: Class_video)
}
