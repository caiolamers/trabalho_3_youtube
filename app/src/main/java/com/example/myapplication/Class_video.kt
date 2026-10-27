package com.example.myapplication

import androidx.compose.ui.graphics.Color
import androidx.room3.Entity
import androidx.room3.PrimaryKey

typealias Video = Class_video

@Entity(tableName = "videos")
data class Class_video(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val canal: String,
    val visualizacoes: String,
    val curtidas: String,
    val descricao: String,
    val totalComentarios: Int,
    val comentarioDestaque: String,
    val corMiniaturaArgb: Int,
    val canalId: Int = 1
)

val Class_video.corMiniatura: Color
    get() = Color(corMiniaturaArgb)

fun Class_video.toVideo(): Class_video = this

fun Class_video.toEntity(): Class_video = this
