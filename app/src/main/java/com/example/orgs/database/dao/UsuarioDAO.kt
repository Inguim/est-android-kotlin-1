package com.example.orgs.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.orgs.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {
    @Insert
    suspend fun adicionar(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE id = :usuarioId AND senha = :senha")
    suspend fun autenticar(usuarioId: String, senha: String): Usuario?

    @Query("SELECT * from Usuario WHERE id = :usuarioId")
    fun listarPorId(usuarioId: String): Flow<Usuario>
}