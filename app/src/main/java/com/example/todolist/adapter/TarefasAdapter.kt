package com.example.todolist.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.ItemExampleListBinding
import com.example.todolist.modelo.Tarefa
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

interface OnTarefaActionListener {
    fun onExcluirClick(tarefa: Tarefa)
    fun onTarefaClick(tarefa: Tarefa)
}

class TarefasAdapter(
    // O adapter recebe uma LISTA INICIAL (uma cópia), não a referência direta do TaskRepository
    initialTasks: List<Tarefa>,
    private val listener: OnTarefaActionListener
) : RecyclerView.Adapter<TarefasAdapter.TarefaViewHolder>() {

    // O adapter agora mantém sua PRÓPRIA lista mutável para exibição
    private val tarefas: MutableList<Tarefa> = initialTasks.toMutableList()

    inner class TarefaViewHolder(private val binding: ItemExampleListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tarefa: Tarefa) {
            binding.tvItemTitulo.text = tarefa.titulo
            binding.tvItemDescricao.text = tarefa.descricao
            binding.tvItemCategoria.text = binding.root.context.getString(R.string.prefixo_categoria, tarefa.categoria)

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.tvItemDataFinalizacao.text = binding.root.context.getString(R.string.prefixo_prazo, dateFormat.format(tarefa.dataFinalizacao))

            val dataAtual = Date()
            if (tarefa.isConcluida) {
                binding.ivStatusIcon.setImageResource(R.drawable.ic_check)
                binding.ivStatusIcon.setColorFilter(Color.GREEN)
                binding.tvItemDataFinalizacao.setTextColor(Color.BLACK)
            } else if (tarefa.estaAtrasada(dataAtual)) {
                binding.ivStatusIcon.setImageResource(R.drawable.ic_alert)
                binding.ivStatusIcon.setColorFilter(Color.RED)
                binding.tvItemDataFinalizacao.setTextColor(Color.RED)
            } else {
                binding.ivStatusIcon.setImageResource(R.drawable.ic_check)
                binding.ivStatusIcon.setColorFilter(Color.BLUE)
                binding.tvItemDataFinalizacao.setTextColor(Color.BLACK)
            }

            binding.ivDeleteIcon.setOnClickListener {
                listener.onExcluirClick(tarefa)
            }

            binding.root.setOnClickListener {
                listener.onTarefaClick(tarefa)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val binding = ItemExampleListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TarefaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        holder.bind(tarefas[position])
    }

    override fun getItemCount(): Int = tarefas.size

    // Métodos para o adapter gerenciar sua própria lista e notificar mudanças
    fun adicionarTarefa(tarefa: Tarefa) {
        tarefas.add(tarefa)
        notifyItemInserted(tarefas.size - 1)
    }

    fun removerTarefa(tarefa: Tarefa) {
        val position = tarefas.indexOf(tarefa)
        if (position != -1) {
            tarefas.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    // Este método é usado para atualizar a lista exibida (por exemplo, após uma busca)
    fun atualizarLista(novaLista: List<Tarefa>) {
        tarefas.clear() // Limpa a lista INTERNA do adapter
        tarefas.addAll(novaLista)
        notifyDataSetChanged()
    }
}