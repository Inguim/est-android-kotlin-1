package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.orgs.database.OrdenacaoProdutos
import com.example.orgs.model.Produto

@Dao
interface ProdutoDAO {
    @Query(
        "SELECT * FROM Produto ORDER BY " +
                "CASE WHEN :ordem = 'nome ASC' THEN nome END ASC, " +
                "        CASE WHEN :ordem = 'nome DESC' THEN nome END DESC, " +
                "        CASE WHEN :ordem = 'descricao ASC' THEN descricao END ASC, " +
                "        CASE WHEN :ordem = 'descricao DESC' THEN descricao END DESC, " +
                "        CASE WHEN :ordem = 'valor ASC' THEN valor END ASC, " +
                "        CASE WHEN :ordem = 'valor DESC' THEN valor END DESC"
    )
    fun listar(ordem: String? = OrdenacaoProdutos.NOME_ASC.order): List<Produto>

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