package com.example.orgs.ui.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.dao.ProdutosDao
import com.example.orgs.model.Produto
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity(R.layout.activity_formulario_produto) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurarBotaoSalvar()
    }

    private fun configurarBotaoSalvar() {
        val botaoSalvar = findViewById<Button>(R.id.activity_formulario_produto_botao_salvar)
        val dao = ProdutosDao()
        botaoSalvar.setOnClickListener {
            val produtoNovo = criarProduto()
            dao.adicionar(produtoNovo)
            finish()
        }
    }

    private fun criarProduto(): Produto {
        val campoNome = findViewById<EditText>(R.id.activity_formulario_produto_item_nome)
        val nome = campoNome.text.toString()
        val campoDescricao = findViewById<EditText>(R.id.activity_formulario_produto_item_descricao)
        val descricao = campoDescricao.text.toString()
        val campoValor = findViewById<EditText>(R.id.activity_formulario_produto_item_valor)
        val valorTexto = campoValor.text.toString()
        val valor = if (valorTexto.isBlank()) BigDecimal.ZERO else BigDecimal(valorTexto)
        return Produto(nome, descricao, valor)
    }
}