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
import com.example.orgs.extensions.getParcelableExtraCompat
import com.example.orgs.extensions.moedaBR
import com.example.orgs.model.Produto

class DetalhesProdutoActivity : AppCompatActivity() {
    private lateinit var produto: Produto
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Detalhes"
        setContentView(binding.root)
        loadProduto()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (::produto.isInitialized) {
            val db = AppDataBase.instancia(this)
            val produtoDAO = db.produtoDao()
            when (item.itemId) {
                R.id.menu_detalhes_produto_editar -> {
                    Intent(this, FormularioProdutoActivity::class.java).apply {
                        putExtra(CHAVE_PRODUTO, produto)
                        startActivity(this)
                    }
                }

                R.id.menu_detalhes_produto_remover -> {
                    produtoDAO.remover(produto)
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadProduto() {
        getParcelableExtraCompat<Produto>(CHAVE_PRODUTO)?.let {
            produto = it
            preencherView(it)
        } ?: run { finish() }
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