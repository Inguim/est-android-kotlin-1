package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.orgs.database.OrdenacaoProdutos
import com.example.orgs.model.Produto
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDAO {
    private fun buildQueryGetProdutos(usuarioId: String?, ordem: String): String {
        val query = StringBuilder("SELECT * FROM Produto")
        if (usuarioId != null) {
            query.append(" WHERE usuarioId = '$usuarioId'")
        }
        query.append(" ORDER BY $ordem")
        return query.toString()
    }

    @RawQuery(observedEntities = [Produto::class])
    fun listar(query: SupportSQLiteQuery): Flow<List<Produto>>

    fun listar(usuarioId: String?, ordem: String? = OrdenacaoProdutos.NOME_ASC.order): Flow<List<Produto>> {
        val query = SimpleSQLiteQuery(buildQueryGetProdutos(usuarioId, ordem.toString()))
        return listar(query)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun adicionar(produto: Produto)

    @Delete
    suspend fun remover(produto: Produto)

    @Query("SELECT * FROM Produto WHERE id = :id")
    fun listarPorId(id: Long): Flow<Produto?>
}