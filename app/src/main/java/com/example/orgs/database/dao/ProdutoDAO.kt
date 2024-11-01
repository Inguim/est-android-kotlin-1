package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.orgs.model.Produto

@Dao
interface ProdutoDAO {
    @Query("SELECT * FROM Produto")
    fun listar(): List<Produto>

//    @Insert
//    fun adicionar(vararg produto: Produto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Executa uma edição caso ja exista
    fun adicionar(vararg produto: Produto)

    @Delete
    fun remover(produto: Produto)

//    @Update
//    fun alterar(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun listarPorId(id: Long): Produto?
}