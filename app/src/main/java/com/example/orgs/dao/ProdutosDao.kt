package com.example.orgs.dao

import com.example.orgs.model.Produto

class ProdutosDao {

    fun adicionar(produto: Produto) {
        produtos.add(produto)
    }

    fun listar(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf<Produto>()
    }
}