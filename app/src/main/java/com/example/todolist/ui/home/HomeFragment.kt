package com.example.todolist.ui.home // Certifique-se de que o pacote corresponde à sua estrutura de pastas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todolist.databinding.FragmentHomeBinding // Importa a classe gerada pelo View Binding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // Esta propriedade só é válida entre onCreateView e onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla o layout para este fragmento usando View Binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Limpa a referência de binding para evitar vazamentos de memória
    }
}