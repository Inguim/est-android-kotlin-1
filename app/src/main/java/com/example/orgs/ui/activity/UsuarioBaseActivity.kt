package com.example.orgs.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.AppDataBase
import com.example.orgs.database.OrdenacaoProdutos
import com.example.orgs.database.preferences.dataStore
import com.example.orgs.database.preferences.usuarioLogadoPreferences
import com.example.orgs.database.preferences.usuarioOrdemProdutosPreferences
import com.example.orgs.extensions.vaiPara
import com.example.orgs.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity : AppCompatActivity() {
    private val _ordenacao: MutableStateFlow<String?> =
        MutableStateFlow(null)
    protected val ordenacao: StateFlow<String?> = _ordenacao
    private val _usuario: MutableStateFlow<Usuario?> = MutableStateFlow(null)
    protected val usuario: StateFlow<Usuario?> = _usuario

    private val usuarioDao by lazy {
        AppDataBase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            buscarPreferencias()
        }
    }

    private suspend fun buscarPreferencias() {
        dataStore.data.collect { preferences ->
            preferences[usuarioLogadoPreferences]?.let { usuarioId ->
                buscaUsuario(usuarioId)
            } ?: vaiParaLogin()
            preferences[usuarioOrdemProdutosPreferences].let {
                _ordenacao.value = it.toString()
            }
        }
    }

    protected suspend fun atualizarPreferenciaOrdenacao(ordem: OrdenacaoProdutos) {
        dataStore.edit { preferences ->
            preferences[usuarioOrdemProdutosPreferences] = ordem.order
        }
    }

    private suspend fun buscaUsuario(usuarioId: String): Usuario? {
        return usuarioDao.listarPorId(usuarioId).firstOrNull().also {
            _usuario.value = it
        }
    }

    protected suspend fun deslogaUsuario() {
        dataStore.edit { preferences ->
            preferences.remove(usuarioLogadoPreferences)
        }
    }

    private fun vaiParaLogin() {
        vaiPara(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }
}