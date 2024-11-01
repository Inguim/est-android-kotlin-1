package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.databinding.ActivityFormularioProdutoBinding
import com.example.orgs.extensions.carregar
import com.example.orgs.extensions.gerarImageLoader
import com.example.orgs.extensions.getParcelableExtraCompat
import com.example.orgs.model.Produto
import com.example.orgs.ui.dialog.FormularioImagemDialog
import com.google.android.material.textfield.TextInputEditText
import java.math.BigDecimal

class FormularioProdutoActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityFormularioProdutoBinding.inflate(layoutInflater)
    }
    private val imageLoader by lazy {
        binding.activityFormularioProdutoImagem.gerarImageLoader(this)
    }
    private var isEdit = false

    private var url: String? = null
    private var idProduto = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.app_formulario_produtos_title)
        configurarBotaoSalvar()
        binding.activityFormularioProdutoImagem.setOnClickListener {
            FormularioImagemDialog(this).show(url) { urlImagem ->
                url = urlImagem
                binding.activityFormularioProdutoImagem.carregar(url, imageLoader)
            }
        }
        onEdit()
    }

    private fun onEdit() {
        getParcelableExtraCompat<Produto>(CHAVE_PRODUTO)?.let {
            title = "Alterar produto"
            isEdit = true
            idProduto = it.id
            url = it.imagem
            preencherFormularioEdicao(it)
        }
    }

    private fun preencherFormularioEdicao(produto: Produto) {
        with(binding) {
            activityFormularioProdutoImagem.carregar(produto.imagem, imageLoader)
            activityFormularioProdutoNome.setText(produto.nome)
            activityFormularioProdutoDescricao.setText(produto.descricao)
            activityFormularioProdutoValor.setText(produto.valor.toPlainString())
        }
    }

    private fun configurarBotaoSalvar() {
        val botaoSalvar = binding.activityFormularioProdutoBotaoSalvar
        val db = AppDataBase.instancia(this)
        val produtoDAO = db.produtoDao()
        botaoSalvar.setOnClickListener {
            if (!validarDado()) {
                val produtoNovo = criarProduto()
                if (!isEdit) {
                    produtoDAO.adicionar(produtoNovo)
                } else {
                    produtoDAO.alterar(produtoNovo)
                }
                finish()
            }
        }
    }

    private fun criarProduto(): Produto {
        val campoNome = binding.activityFormularioProdutoNome
        val nome = campoNome.text.toString()
        val campoDescricao = binding.activityFormularioProdutoDescricao
        val descricao = campoDescricao.text.toString()
        val campoValor = binding.activityFormularioProdutoValor
        val valorTexto = campoValor.text.toString()
        val valor = if (valorTexto.isBlank()) BigDecimal.ZERO else BigDecimal(valorTexto)
        return Produto(
            id = idProduto,
            nome = nome,
            descricao = descricao,
            valor = valor,
            imagem = url
        )
    }

    private fun validarDado(): Boolean {
        var error = false
        var campo: TextInputEditText? = null
        val nomeValido = validarCampoNome()
        val descricaoValido = validarCampoDescricao()
        val valorValido = validarCampoValor()
        if (!nomeValido || !descricaoValido || !valorValido) {
            error = true
        }
        if (!valorValido) {
            campo = binding.activityFormularioProdutoValor
            campo.error = getString(R.string.formulario_produto_error_valor)
        }
        if (!descricaoValido) {
            campo = binding.activityFormularioProdutoDescricao
            campo.error = getString(R.string.formulario_produto_error_descricao)
        }
        if (!nomeValido) {
            campo = binding.activityFormularioProdutoNome
            campo.error = getString(R.string.formulario_produto_error_nome)
        }
        if (error) campo?.requestFocus()
        return error
    }

    private fun validarCampoNome(): Boolean {
        val campo = binding.activityFormularioProdutoNome
        return campo.text.toString().isNotBlank()
    }

    private fun validarCampoDescricao(): Boolean {
        val campo = binding.activityFormularioProdutoDescricao
        return campo.text.toString().isNotBlank()
    }

    private fun validarCampoValor(): Boolean {
        val campo = binding.activityFormularioProdutoValor
        return campo.text.toString().isNotBlank()
    }
}
