package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.database.OrdenacaoProdutos
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerView.adapter.ListaProdutosAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ListaProdutosActivity : UsuarioBaseActivity() {
    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        val db = AppDataBase.instancia(this)
        db.produtoDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            launch {
                ordenacao.filterNotNull().collect {
                    buscarProdutos()
                }
            }
        }
    }

    private suspend fun buscarProdutos() {
        ordenacao.collect {
            produtoDao.listar(it).collect { produtos ->
                adapter.atualizar(produtos)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_lista_produtos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        ordenarProdutos(item)
        return super.onOptionsItemSelected(item)
    }

    private fun ordenarProdutos(item: MenuItem) {
        val novaOrdem: OrdenacaoProdutos? = when (item.itemId) {
            R.id.menu_lista_produtos_ordenar_nome_asc ->
                OrdenacaoProdutos.NOME_ASC

            R.id.menu_lista_produtos_ordenar_nome_desc ->
                OrdenacaoProdutos.NOME_DESC

            R.id.menu_lista_produtos_ordenar_descricao_asc ->
                OrdenacaoProdutos.DESCRICAO_ASC

            R.id.menu_lista_produtos_ordenar_descricao_desc ->
                OrdenacaoProdutos.DESCRICAO_DESC

            R.id.menu_lista_produtos_ordenar_valor_asc ->
                OrdenacaoProdutos.VALOR_ASC

            R.id.menu_lista_produtos_ordenar_valor_desc ->
                OrdenacaoProdutos.VALOR_DESC

            R.id.menu_lista_produtos_ordenar_sem_ordem ->
                OrdenacaoProdutos.NOME_ASC

            else -> null
        }
        novaOrdem?.let {
            val produtosOrdenado: Flow<List<Produto>> = produtoDao.listar(novaOrdem.order)
            produtosOrdenado.let {
                lifecycleScope.launch {
                    atualizarPreferenciaOrdenacao(novaOrdem)
                    it.collect { produtos ->
                        adapter.atualizar(produtos)
                    }
                }
            }
        }
    }

    private fun configuraFab() {
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
        val recyclerView = binding.activityListaProdutosRecyclerView
        recyclerView.adapter = adapter
        configurarItemClick()
        configurarItemClickByHold()
    }

    private fun configurarItemClickByHold() {
        adapter.itemClickByHoldEditar = {
            Intent(this, FormularioProdutoActivity::class.java).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
                startActivity(this)
            }
        }
        adapter.itemClickByHoldRemover = {
            it.let {
                lifecycleScope.launch {
                    produtoDao.remover(it)
                    buscarProdutos()
                }
            }
        }
    }

    private fun configurarItemClick() {
        adapter.itemClick = {
            val intent = Intent(this, DetalhesProdutoActivity::class.java).apply {
                putExtra(CHAVE_PRODUTO_ID, it.id)
            }
            startActivity(intent)
        }
    }
}