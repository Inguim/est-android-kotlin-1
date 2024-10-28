package com.example.orgs.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.model.Produto

// RecyclerView: responsável por exibir informações em lista
class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val dataset = produtos.toMutableList()

    class ViewHolder(private val binding: ProdutoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun vincula(produto: Produto) {
            val nome = binding.activityFormularioProdutoItemNome
            nome.text = produto.nome
            val descricao = binding.activityFormularioProdutoItemDescricao
            descricao.text = produto.descricao
            val valor = binding.activityFormularioProdutoItemValor
            valor.text = produto.valor.toPlainString()
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

    fun atualizar(produtos: List<Produto>) {
        this.dataset.clear()
        this.dataset.addAll(produtos)
        notifyDataSetChanged()
    }
}
