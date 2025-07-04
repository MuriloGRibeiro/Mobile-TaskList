package com.example.todolist.ui.cadastro

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.databinding.FragmentCadastroBinding
import com.example.todolist.modelo.Tarefa
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CadastroFragment : Fragment() {

    private var _binding: FragmentCadastroBinding? = null
    private val binding get() = _binding!!

    private val calendar = Calendar.getInstance()

    // NOVAS 10 CATEGORIAS RELACIONADAS A DESENVOLVIMENTO
    private val categorias = arrayOf(
        "Frontend",
        "Backend",
        "Mobile (Android)",
        "Mobile (iOS)",
        "Banco de Dados",
        "DevOps",
        "Segurança",
        "Testes",
        "Documentação",
        "Gerenciamento de Projetos"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCadastroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etDataFinalizacao.setOnClickListener {
            showDatePickerDialog()
        }
        binding.etDataFinalizacao.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePickerDialog()
            }
        }

        // Configurar o AutoCompleteTextView para as categorias
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, categorias)
        binding.atvCategoriaTarefa.setAdapter(adapter)

        binding.btnSalvarTarefa.setOnClickListener {
            salvarTarefa()
        }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etDataFinalizacao.setText(dateFormat.format(calendar.time))
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun salvarTarefa() {
        val titulo = binding.etTituloTarefa.text.toString().trim()
        val descricao = binding.etDescricaoTarefa.text.toString().trim()
        val categoria = binding.atvCategoriaTarefa.text.toString().trim()
        val dataFinalizacaoStr = binding.etDataFinalizacao.text.toString().trim()

        if (titulo.isEmpty() || descricao.isEmpty() || categoria.isEmpty() || dataFinalizacaoStr.isEmpty()) {
            Toast.makeText(context, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!categorias.contains(categoria)) {
            Toast.makeText(context, "Por favor, selecione uma categoria válida da lista.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dataFinalizacao: Date = dateFormat.parse(dataFinalizacaoStr) ?: Date()

            val novaTarefa = Tarefa(
                titulo = titulo,
                descricao = descricao,
                categoria = categoria,
                dataFinalizacao = dataFinalizacao
            )

            val bundle = Bundle().apply {
                putSerializable("novaTarefa", novaTarefa)
            }
            parentFragmentManager.setFragmentResult("requestKeyTarefa", bundle)

            Toast.makeText(context, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()

        } catch (e: Exception) {
            Toast.makeText(context, "Erro ao salvar tarefa: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}