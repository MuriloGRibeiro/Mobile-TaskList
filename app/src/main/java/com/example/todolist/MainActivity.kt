package com.example.todolist // Certifique-se de que o pacote corresponde ao seu projeto

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.todolist.databinding.ActivityMainBinding // Importa a classe gerada pelo View Binding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // Declara uma variável para o View Binding
    private lateinit var navController: NavController // Declara uma variável para o NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla o layout da Activity usando View Binding e define como conteúdo da tela
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Obter o NavController
        // O NavHostFragment é o container no seu activity_main.xml
        // que hospeda os Fragments. Precisamos do seu NavController
        // para gerenciar a navegação.
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 2. Ligar a BottomNavigationView ao NavController
        // O método setupWithNavController() do Material Design faz toda a mágica
        // de conectar os cliques nos itens da barra inferior aos destinos
        // correspondentes no seu nav_graph.xml.
        binding.bottomNavigation.setupWithNavController(navController)
    }
}