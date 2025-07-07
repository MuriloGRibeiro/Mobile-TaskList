package com.example.todolist.modelo

import java.io.Serializable
import java.util.Date

data class Tarefa(
    val id: String = java.util.UUID.randomUUID().toString(),
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val dataFinalizacao: Date,
    var isConcluida: Boolean = false
) : Serializable { // A data class agora implementa Serializable
    fun estaAtrasada(dataAtual: Date = Date()): Boolean {
        return !isConcluida && dataFinalizacao.before(dataAtual)
    }

    fun estaEmDia(dataAtual: Date = Date()): Boolean {
        return !isConcluida && !dataFinalizacao.before(dataAtual)
    }
}