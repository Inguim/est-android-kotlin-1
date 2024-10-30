package com.example.orgs.ui.recyclerView.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.extensions.carregar
import com.example.orgs.extensions.gerarImageLoader
import com.example.orgs.extensions.moedaBR
import com.example.orgs.model.Produto

// RecyclerView: responsável por exibir informações em lista
class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>,
    var itemClick: (produto: Produto) -> Unit = {}
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val dataset = produtos.toMutableList()

    inner class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var produto: Produto

        init {
            itemView.setOnClickListener {
                if (::produto.isInitialized) {
                    itemClick(produto)
                }
            }
        }

        fun vincula(produto: Produto) {
            this.produto = produto
            val nome = binding.produtoItemNome
            nome.text = produto.nome
            val descricao = binding.produtoItemDescricao
            descricao.text = produto.descricao
            val valor = binding.produtoItemValor
            valor.text = produto.valor.moedaBR()
            val imageLoader = binding.imageView.gerarImageLoader(binding.root.context)
            binding.imageView.visibility = if (produto.imagem != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            binding.imageView.carregar(produto.imagem, imageLoader)
        }
    }

    // Responsável por criar a View e efetuar o processo de "Bind" (vinculação de dado e ui)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProdutoItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    // Indica o item, posição e o ViewHolder que do mesmo
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = dataset[position]
        holder.vincula(produto)
    }

    // Representa a quantidade de items que o mesmo irá representar
    override fun getItemCount(): Int = dataset.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualizar(produtos: List<Produto>) {
        this.dataset.clear()
        this.dataset.addAll(produtos)
        notifyDataSetChanged()
    }
}
