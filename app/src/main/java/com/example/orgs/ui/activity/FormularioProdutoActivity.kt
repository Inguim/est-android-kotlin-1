package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.databinding.ActivityFormularioProdutoBinding
import com.example.orgs.model.Produto
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configurarBotaoSalvar()
    }

    private fun configurarBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        val dao = ProdutosDao()
        botaoSalvar.setOnClickListener {
            val produtoNovo = criarProduto()
            dao.adicionar(produtoNovo)
            finish()
        }
    }

    private fun criarProduto(): Produto {
        val campoNome = binding.produtoItemNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.produtoItemDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.produtoItemValor
        val valorTexto = campoValor.text.toString()
        val valor = if (valorTexto.isBlank()) BigDecimal.ZERO else BigDecimal(valorTexto)
        return Produto(nome, descricao, valor)
    }
}