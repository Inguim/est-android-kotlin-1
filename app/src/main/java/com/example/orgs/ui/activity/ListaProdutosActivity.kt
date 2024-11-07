package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.database.OrdenacaoProdutos
import com.example.orgs.databinding.ActivityListaProdutosBinding
import com.example.orgs.model.Produto
import com.example.orgs.ui.recyclerView.adapter.ListaProdutosAdapter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaProdutosActivity : AppCompatActivity() {
    private val adapter = ListaProdutosAdapter(context = this)
    private val binding by lazy {
        ActivityListaProdutosBinding.inflate(layoutInflater)
    }
    private val produtoDao by lazy {
        val db = AppDataBase.instancia(this)
        db.produtoDao()
    }

    private val scope = MainScope()
    private val job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraRecyclerView()
        configuraFab()
    }

    override fun onResume() {
        super.onResume()
        val handler = CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.i("TAG", "onResume: throwable ${throwable}")
        }
        scope.launch(job + handler) {
            val produtos = withContext(IO) {
                produtoDao.listar()
            }
            adapter.atualizar(produtos)
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

    override fun onDestroy() {
        super.onDestroy()
        // Cancela a coroutine caso a Activity seja destruida
        job.cancel()
    }

    private fun ordenarProdutos(item: MenuItem) {
        scope.launch {
            val produtosOrdenado: List<Produto>? = withContext(IO) {
                when (item.itemId) {
                    R.id.menu_lista_produtos_ordenar_nome_asc ->
                        produtoDao.listar()

                    R.id.menu_lista_produtos_ordenar_nome_desc ->
                        produtoDao.listar(OrdenacaoProdutos.NOME_DESC.order)

                    R.id.menu_lista_produtos_ordenar_descricao_asc ->
                        produtoDao.listar(OrdenacaoProdutos.DESCRICAO_ASC.order)

                    R.id.menu_lista_produtos_ordenar_descricao_desc ->
                        produtoDao.listar(OrdenacaoProdutos.DESCRICAO_DESC.order)

                    R.id.menu_lista_produtos_ordenar_valor_asc ->
                        produtoDao.listar(OrdenacaoProdutos.VALOR_ASC.order)

                    R.id.menu_lista_produtos_ordenar_valor_desc ->
                        produtoDao.listar(OrdenacaoProdutos.VALOR_DESC.order)

                    R.id.menu_lista_produtos_ordenar_sem_ordem ->
                        produtoDao.listar()

                    else -> null
                }
            }
            produtosOrdenado?.let { adapter.atualizar(it) }
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
                scope.launch {
                    val produtos = withContext(IO) {
                        produtoDao.remover(it)
                        return@withContext produtoDao.listar()
                    }
                    adapter.atualizar(produtos)
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