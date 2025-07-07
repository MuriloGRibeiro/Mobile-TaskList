package com.example.todolist.ui.busca // VERIFICAR: Pacote real do seu BuscaFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapter.CategoriaAdapter
import com.example.todolist.adapter.OnCategoriaClickListener
import com.example.todolist.data.TaskRepository
import com.example.todolist.databinding.FragmentBuscaBinding // MUDANÇA AQUI: Usa FragmentBuscaBinding
import com.example.todolist.modelo.CategoriaSumario
import androidx.appcompat.widget.SearchView
import android.widget.Toast

// MUDANÇA AQUI: Nome da classe agora é BuscaFragment
class BuscaFragment : Fragment(), OnCategoriaClickListener {

    private var _binding: FragmentBuscaBinding? = null // MUDANÇA AQUI: Usa FragmentBuscaBinding
    private val binding get() = _binding!!

    private lateinit var categoriaAdapter: CategoriaAdapter
    // Lista para manter todos os sumários de categoria (útil para filtragem)
    private var categoriasSumarioCompleta: MutableList<CategoriaSumario> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Calcula os sumários das categorias (inicialmente todos)
        calcularSumariosDeCategorias()
        // Inicializa o adapter com uma cópia da lista completa
        categoriaAdapter = CategoriaAdapter(categoriasSumarioCompleta.toMutableList(), this)

        // MUDANÇA AQUI: Usa o ID correto do RecyclerView
        binding.recyclerViewBusca.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewBusca.adapter = categoriaAdapter

        // MUDANÇA AQUI: Usa o ID correto da SearchView
        binding.searchViewBusca.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterCategorias(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterCategorias(newText)
                return true
            }
        })
    }

    // Função para calcular o total de tarefas por categoria
    private fun calcularSumariosDeCategorias() {
        // Obter todas as categorias que podem ser selecionadas (as 10 do CadastroFragment)
        val todasCategoriasPossiveis = arrayOf(
            "Frontend", "Backend", "Mobile (Android)", "Mobile (iOS)", "Banco de Dados",
            "DevOps", "Segurança", "Testes", "Documentação", "Gerenciamento de Projetos"
        )

        // Agrupar as tarefas existentes no repositório por categoria
        val contagemPorCategoria = TaskRepository.tasks
            .groupBy { it.categoria } // Agrupa por nome da categoria
            .mapValues { it.value.size } // Conta o número de tarefas em cada grupo

        val sumariosGerados = mutableListOf<CategoriaSumario>()

        // Iterar sobre todas as categorias possíveis para garantir que mesmo categorias sem tarefas apareçam com 0
        for (cat in todasCategoriasPossiveis) {
            val total = contagemPorCategoria[cat] ?: 0 // Pega a contagem ou 0 se não houver tarefas nessa categoria
            sumariosGerados.add(CategoriaSumario(cat, total))
        }

        // Opcional: Ordenar por nome da categoria ou total de tarefas
        sumariosGerados.sortBy { it.nomeCategoria } // Ordena por nome da categoria

        // Atualiza a lista completa de sumários que o adapter usará
        categoriasSumarioCompleta.clear()
        categoriasSumarioCompleta.addAll(sumariosGerados)
    }

    // Função para filtrar os sumários de categorias com base na busca
    private fun filterCategorias(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            categoriasSumarioCompleta // Se a busca estiver vazia, mostra todos os sumários completos
        } else {
            categoriasSumarioCompleta.filter { sumario ->
                sumario.nomeCategoria.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        categoriaAdapter.atualizarLista(filteredList) // Atualiza a lista INTERNA do adapter
    }

    // Implementação da interface OnCategoriaClickListener
    override fun onCategoriaClick(categoria: CategoriaSumario) {
        Toast.makeText(context, "Clicou em: ${categoria.nomeCategoria} (${categoria.totalTarefas} tarefas)", Toast.LENGTH_SHORT).show()
        // TODO: Você pode expandir isso para mostrar as tarefas de uma categoria específica (filtrando TaskRepository.tasks)
    }

    override fun onResume() {
        super.onResume()
        // Recalcula e atualiza a lista de categorias sempre que o fragmento se torna visível novamente
        calcularSumariosDeCategorias()
        categoriaAdapter.atualizarLista(categoriasSumarioCompleta)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}