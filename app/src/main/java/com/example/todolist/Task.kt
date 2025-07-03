package com.example.todolist // Certifique-se de que o pacote corresponde ao seu projeto

import java.util.Date // Importa a classe Date para o campo de data

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(), // Um ID único para cada tarefa, gerado automaticamente
    val titulo: String,
    val descricao: String,
    val categoria: String,
    val dataFinalizacao: Date, // Usaremos java.util.Date para a data de finalização/prazo
    var isConcluida: Boolean = false // Um campo para indicar se a tarefa foi concluída (opcional, mas útil)
)