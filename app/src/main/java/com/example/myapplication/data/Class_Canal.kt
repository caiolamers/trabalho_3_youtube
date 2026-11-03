package com.example.myapplication.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

typealias Canal = Class_Canal

@Entity(tableName = "canais")
data class Class_Canal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val identificador: String,
    val inscritos: String,
    val descricao: String,
    val inscrito: Boolean = false
)

fun Class_Canal.toCanal(): Class_Canal = this

fun Class_Canal.toEntity(): Class_Canal = this
