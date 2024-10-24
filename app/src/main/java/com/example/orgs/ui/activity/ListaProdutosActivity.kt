package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.ui.recyclerView.adapter.ListaProdutosAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaProdutosActivity : AppCompatActivity() {
    private val dao = ProdutosDao()
//    private val adapter = ListaProdutosAdapter(context = this, produtos = dao.listar())
    private val adapter by lazy {
        ListaProdutosAdapter(this, produtos = dao.listar())
    }
    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configuraRecyclerView()
        configuraFab()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        adapter.atualizar(dao.listar())
    }

    private fun configuraFab() {
//        val fab = findViewById<FloatingActionButton>(R.id.activity_lista_produtos_fab)
        val fab = binding.activityListaProdutosFab
        fab.setOnClickListener {
            goFormularioProduto()
        }
    }

    private fun goFormularioProduto() {
        val intent = Intent(this, FormularioProdutoActivity::class.java)
        startActivity(intent)
    }

    private fun configuraRecyclerView() {
//        val recyclerView = findViewById<RecyclerView>(R.id.activity_lista_produtos_recyclerView)
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
    }
}