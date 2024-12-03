package com.example.orgs.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.orgs.database.converter.Converters
import com.example.orgs.database.dao.ProdutoDAO
import com.example.orgs.database.dao.UsuarioDAO
import com.example.orgs.model.Produto
import com.example.orgs.model.Usuario

@Database(entities = [Produto::class, Usuario::class], version = 3, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun produtoDao(): ProdutoDAO
    abstract fun usuarioDao(): UsuarioDAO

    companion object {
        @Volatile
        private lateinit var db: AppDataBase

        fun instancia(context: Context): AppDataBase {
            if (::db.isInitialized) return db
            return Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "orgs.db"
            ).addMigrations(
                MIGRATION_1_2,
                MIGRATION_2_3
            ).build().also {
                db = it
            }
        }
    }
}