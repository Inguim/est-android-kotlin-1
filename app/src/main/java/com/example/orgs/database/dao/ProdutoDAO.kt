package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.orgs.model.Produto

@Dao
interface ProdutoDAO {
    @Query("""SELECT * FROM Produto""")
    fun listar(): List<Produto>

    @Insert
    fun adicionar(vararg produto: Produto)

    @Delete
    fun remover(produto: Produto)
}