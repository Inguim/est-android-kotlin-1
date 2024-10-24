package com.example.orgs.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.orgs.R
import com.example.orgs.databinding.ProdutoItemBinding
import com.example.orgs.model.Produto

// RecyclerView: responsável por exibir informações em lista
class ListaProdutosAdapter(
    private val context: Context,
    produtos: List<Produto>
) : RecyclerView.Adapter<ListaProdutosAdapter.ViewHolder>() {

    private val dataset = produtos.toMutableList()

    class ViewHolder(binding: ProdutoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val nome = binding.activityFormularioProdutoItemNome
        private val descricao = binding.activityFormularioProdutoItemDescricao
        private val valor = binding.activityFormularioProdutoItemValor
        fun vincula(produto: Produto) {
//            val nome = itemView.findViewById<TextView>(R.id.activity_formulario_produto_item_nome
            nome.text = produto.nome
//            val descricao =
//                itemView.findViewById<TextView>(R.id.activity_formulario_produto_item_descricao)
            descricao.text = produto.descricao
//            val valor = itemView.findViewById<TextView>(R.id.activity_formulario_produto_item_valor)
            valor.text = produto.valor.toPlainString()
        }
    }

    // Responsável por criar a View e efetuar o processo de "Bind" (vinculação de dado e ui)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(context)
//        val view = inflater.inflate(R.layout.produto_item, parent, false)
//        return ViewHolder(view)
        val binding = ProdutoItemBinding.inflate(LayoutInflater.from(context), parent, false)
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
