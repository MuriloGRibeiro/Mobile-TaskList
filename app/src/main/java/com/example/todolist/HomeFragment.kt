package com.example.todolist

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapter.OnTarefaActionListener
import com.example.todolist.adapter.TarefasAdapter
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.modelo.Tarefa
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import java.util.Date
import com.example.todolist.data.TaskRepository


class HomeFragment : Fragment(), OnTarefaActionListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var tarefasAdapter: TarefasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // O adapter agora é inicializado com uma CÓPIA da lista do repositório.
        // Assim, o adapter manipula sua própria cópia para exibição/filtro.
        tarefasAdapter = TarefasAdapter(TaskRepository.tasks.toMutableList(), this)

        binding.recyclerViewHome.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewHome.adapter = tarefasAdapter

        parentFragmentManager.setFragmentResultListener("requestKeyTarefa", viewLifecycleOwner) { key, bundle ->
            @Suppress("DEPRECATION")
            val novaTarefa = bundle.getSerializable("novaTarefa") as? Tarefa

            novaTarefa?.let {
                // PRIMEIRO: Adiciona a tarefa ao TaskRepository (lista original)
                TaskRepository.tasks.add(it)
                // SEGUNDO: Adiciona a tarefa à lista INTERNA do adapter e notifica a UI
                tarefasAdapter.adicionarTarefa(it)
                binding.recyclerViewHome.scrollToPosition(tarefasAdapter.itemCount - 1)
                Toast.makeText(context, "Tarefa '${it.titulo}' adicionada!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchViewHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterTasks(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTasks(newText)
                return true
            }
        })
    }

    private fun filterTasks(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            // Se a busca estiver vazia, atualiza o adapter com uma CÓPIA da lista completa do repositório
            TaskRepository.tasks.toMutableList()
        } else {
            // Filtra a partir da lista original do repositório e cria uma nova lista filtrada
            TaskRepository.tasks.filter { tarefa ->
                tarefa.titulo.contains(query, ignoreCase = true) ||
                        tarefa.descricao.contains(query, ignoreCase = true) ||
                        tarefa.categoria.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        tarefasAdapter.atualizarLista(filteredList) // Atualiza a lista INTERNA do adapter
    }

    override fun onExcluirClick(tarefa: Tarefa) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirmar_exclusao))
            .setMessage(getString(R.string.mensagem_exclusao_tarefa, tarefa.titulo))
            .setPositiveButton(getString(R.string.sim)) { _, _ ->
                // PRIMEIRO: Remove a tarefa do TaskRepository (lista original)
                val positionInRepository = TaskRepository.tasks.indexOf(tarefa)
                if (positionInRepository != -1) {
                    TaskRepository.tasks.removeAt(positionInRepository)
                }

                // SEGUNDO: Remove a tarefa da lista INTERNA do adapter e notifica a UI
                // O adapter precisa remover a tarefa que ele TEM na sua lista exibida
                tarefasAdapter.removerTarefa(tarefa)

                Toast.makeText(context, getString(R.string.tarefa_excluida_sucesso, tarefa.titulo), Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.nao), null)
            .show()
    }

    override fun onTarefaClick(tarefa: Tarefa) {
        Toast.makeText(context, "Clicou na tarefa: '${tarefa.titulo}'", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}