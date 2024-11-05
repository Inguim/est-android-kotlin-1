package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.carregar
import com.example.orgs.extensions.gerarImageLoader
import com.example.orgs.extensions.getLongExtraCompact
import com.example.orgs.extensions.moedaBR
import com.example.orgs.model.Produto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetalhesProdutoActivity : AppCompatActivity() {
    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDAO by lazy {
        AppDataBase.instancia(this).produtoDao()
    }
    private val scope = CoroutineScope(IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Detalhes"
        setContentView(binding.root)
        loadProduto()
    }

    override fun onResume() {
        super.onResume()
        reloadProduto()
    }

    private fun reloadProduto() {
        scope.launch {
            produto = produtoDAO.listarPorId(produtoId)
            // em alguns casos, quando precisar chamar a Activity fora da Threah principal o App
            // pode quebrar, ao troca para o Main dnv, evitará o erro
            withContext(Main) {
                produto?.let {
                    preencherView(it)
                } ?: finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_detalhes_produto_editar -> {
                Intent(this, FormularioProdutoActivity::class.java).apply {
                    putExtra(CHAVE_PRODUTO_ID, produtoId)
                    startActivity(this)
                }
            }

            R.id.menu_detalhes_produto_remover -> {
                scope.launch {
                    produto?.let { produtoDAO.remover(it) }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadProduto() {
        produtoId = getLongExtraCompact(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencherView(produto: Produto) {
        with(binding) {
            val imageLoader = activityDetalhesProdutoImagem.gerarImageLoader(root.context)
            activityDetalhesProdutoImagem.carregar(produto.imagem, imageLoader)
            activityDetalhesProdutoNome.text = produto.nome
            activityDetalhesProdutoDescricao.text = produto.descricao
            activityDetalhesProdutoValor.text = produto.valor.moedaBR()
        }
    }
}