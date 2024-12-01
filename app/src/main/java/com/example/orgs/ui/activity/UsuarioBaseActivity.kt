package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.database.OrdenacaoProdutos
import com.example.orgs.database.preferences.dataStore
import com.example.orgs.database.preferences.usuarioOrdemProdutosPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class UsuarioBaseActivity : AppCompatActivity() {
    private val _ordenacao: MutableStateFlow<String?> =
        MutableStateFlow(null)
    protected val ordenacao: StateFlow<String?> = _ordenacao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            buscarPreferenciaOrdenacao()
        }
    }

    private suspend fun buscarPreferenciaOrdenacao() {
        dataStore.data.collect { preferences ->
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
}