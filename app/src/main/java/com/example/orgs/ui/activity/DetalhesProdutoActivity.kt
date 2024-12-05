package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.databinding.ActivityDetalhesProdutoBinding
import com.example.orgs.extensions.carregar
import com.example.orgs.extensions.gerarImageLoader
import com.example.orgs.extensions.getLongExtraCompact
import com.example.orgs.extensions.moedaBR
import com.example.orgs.model.Produto
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DetalhesProdutoActivity : UsuarioBaseActivity() {
    private var produtoId: Long = 0L
    private var produto: Produto? = null
    private val binding by lazy {
        ActivityDetalhesProdutoBinding.inflate(layoutInflater)
    }
    private val produtoDAO by lazy {
        AppDataBase.instancia(this).produtoDao()
    }

    private val usuarioDAO by lazy {
        AppDataBase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.menu_detalhes_produto_titulo)
        setContentView(binding.root)
        loadProduto()
    }

    override fun onResume() {
        super.onResume()
        reloadProduto()
    }

    private fun reloadProduto() {
        lifecycleScope.launch {
            produtoDAO.listarPorId(produtoId).collect {
                produto = it
                produto?.let { p ->
                    val usuario = usuarioDAO.listarPorId(p.usuarioId!!)
                    usuario.firstOrNull()?.nome
                    preencherView(p, usuario.firstOrNull()?.nome)
                } ?: finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        lifecycleScope.launch {
            launch {
                usuario.collect {
                    if (it?.id == produto?.usuarioId) {
                        menuInflater.inflate(R.menu.menu_detalhes_produto, menu)
                    }
                }
            }
        }
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
                produto?.let {
                    lifecycleScope.launch {
                        produtoDAO.remover(it)
                    }
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadProduto() {
        produtoId = getLongExtraCompact(CHAVE_PRODUTO_ID, 0L)
    }

    private fun preencherView(produto: Produto, nome: String?) {
        with(binding) {
            val imageLoader = activityDetalhesProdutoImagem.gerarImageLoader(root.context)
            activityDetalhesProdutoImagem.carregar(produto.imagem, imageLoader)
            activityDetalhesProdutoNome.text = produto.nome
            activityDetalhesProdutoDescricao.text = produto.descricao
            activityDetalhesProdutoValor.text = produto.valor.moedaBR()
            nome?.let {
                activityDetalhesProdutoUsuario.text =
                    getString(R.string.visualizar_produto_inserido_por, it)
            }
        }
    }
}