package com.example.orgs.dao

import com.example.orgs.model.Produto
import java.math.BigDecimal

class ProdutosDao {

    fun adicionar(produto: Produto) {
        produtos.add(produto)
    }

    fun listar(): List<Produto> {
        return produtos.toList()
    }

    companion object {
        private val produtos = mutableListOf<Produto>(
            Produto(
                "Banana",
                "Banana nanica",
                BigDecimal("12.35"),
                "https://images.pexels.com/photos/2316466/pexels-photo-2316466.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
            )
        )
    }
}