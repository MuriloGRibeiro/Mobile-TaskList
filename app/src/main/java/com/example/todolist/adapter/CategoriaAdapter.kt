package com.example.todolist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView // Importe TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.modelo.CategoriaSumario // Importe a data class CategoriaSumario

// Interface para callbacks de cliques nos itens de categoria (opcional, mas bom ter)
interface OnCategoriaClickListener {
    fun onCategoriaClick(categoria: CategoriaSumario)
}

class CategoriaAdapter(
    private var categoriasSumario: MutableList<CategoriaSumario>,
    private val listener: OnCategoriaClickListener? = null // Listener opcional
) : RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategoriaNome: TextView = itemView.findViewById(R.id.tv_categoria_nome)
        val tvCategoriaTotal: TextView = itemView.findViewById(R.id.tv_categoria_total)

        fun bind(sumario: CategoriaSumario) {
            tvCategoriaNome.text = itemView.context.getString(R.string.prefixo_categoria_sumario_nome, sumario.nomeCategoria)
            tvCategoriaTotal.text = itemView.context.getString(R.string.prefixo_categoria_sumario_total, sumario.totalTarefas)

            itemView.setOnClickListener {
                listener?.onCategoriaClick(sumario)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria_sumario, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        holder.bind(categoriasSumario[position])
    }

    override fun getItemCount(): Int = categoriasSumario.size

    // Método para atualizar a lista de sumários (usado para busca/filtragem)
    fun atualizarLista(novaLista: List<CategoriaSumario>) {
        categoriasSumario.clear()
        categoriasSumario.addAll(novaLista)
        notifyDataSetChanged()
    }
}