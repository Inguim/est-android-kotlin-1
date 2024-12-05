package com.example.orgs.ui.recyclerView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.database.dao.ProdutoAndUsuario
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.extensions.carregar
import com.example.orgs.extensions.gerarImageLoader
import com.example.orgs.extensions.moedaBR
import com.example.orgs.model.Produto
import com.example.orgs.model.Usuario

class ListaProdutosAdapter(
    private val context: Context,
    var usuarioAtivo: Usuario?,
    produtos: List<ProdutoAndUsuario> = emptyList(),
    var itemClick: (produto: Produto) -> Unit = {},
    var itemClickByHoldEditar: (produto: Produto) -> Unit = {},
    var itemClickByHoldRemover: (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val dataset = produtos.toMutableList()

    inner class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

        private lateinit var produto: Produto

        init {
            adicionarItemViewClick()
            adicionarItemViewClickHolder()
        }

        private fun adicionarItemViewClickHolder() {
            itemView.setOnLongClickListener {
                if (::produto.isInitialized && usuarioAtivo?.id == produto.usuarioId) {
                    PopupMenu(context, itemView).apply {
                        menuInflater.inflate(
                            R.menu.menu_detalhes_produto,
                            menu
                        )
                        setOnMenuItemClickListener(this@ViewHolder)
                    }.show()
                }
                true
            }
        }

        private fun adicionarItemViewClick() {
            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    itemClick(produto)
                }
            }
        }

        fun vincula(produto: ProdutoAndUsuario) {
            this.produto = produto.produto
            val nome = binding.produtoItemNome
            nome.text = produto.produto.nome
            val descricao = binding.produtoItemDescricao
            descricao.text = produto.produto.descricao
            val valor = binding.produtoItemValor
            valor.text = produto.produto.valor.moedaBR()
            val usuario = binding.produtoItemUsuario
            usuario.text = "(${produto.usuario.nome})"
            val imageLoader = binding.imageView.gerarImageLoader(binding.root.context)
            binding.imageView.visibility = if (produto.produto.imagem != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.imageView.carregar(produto.produto.imagem, imageLoader)
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            item?.let {
                when (it.itemId) {
                    R.id.menu_detalhes_produto_editar -> {
                        itemClickByHoldEditar(produto)
                    }

                    R.id.menu_detalhes_produto_remover -> {
                        itemClickByHoldRemover(produto)
                    }

                    else -> return false
                }
                return true
            } ?: return false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = dataset[position]
        holder.vincula(produto)
    }

    override fun getItemCount(): Int = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualizar(produtos: List<ProdutoAndUsuario>) {
        this.dataset.clear()
        this.dataset.addAll(produtos)
        notifyDataSetChanged()
    }
}
