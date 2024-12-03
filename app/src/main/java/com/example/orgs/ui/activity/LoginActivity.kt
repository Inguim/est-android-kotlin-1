package com.example.orgs.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.database.preferences.dataStore
import com.example.orgs.database.preferences.usuarioLogadoPreferences
import com.example.orgs.databinding.ActivityLoginBinding
import com.example.orgs.extensions.toHash
import com.example.orgs.extensions.toast
import com.example.orgs.extensions.vaiPara
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val usuarioDao by lazy {
        AppDataBase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
        configuraBotaoEntrar()
    }

    private fun configuraBotaoEntrar() {
        binding.activityLoginBotaoEntrar.setOnClickListener {
            val usuario = binding.activityLoginUsuario.text.toString()
            val senha = binding.activityLoginSenha.text.toString()
            autentica(usuario, senha)
        }
    }

    private fun autentica(usuario: String, senha: String) {
        if (!validarDado()) {
            lifecycleScope.launch {
                usuarioDao.autenticar(usuario, senha.toHash())?.let { usuario ->
                    dataStore.edit { preferences ->
                        preferences[usuarioLogadoPreferences] = usuario.id
                    }
                    vaiPara(ListaProdutosActivity::class.java)
                    finish()
                } ?: toast("Fala na autenticação")
            }
        }
    }

    private fun validarDado(): Boolean {
        var error = false
        var campo: TextInputEditText?
        val usuarioValido = validarCampoUsuario()
        val senhaValido = validarCampoSenha()
        if (!usuarioValido || !senhaValido) {
            error = true
        }
        if (!senhaValido) {
            campo = binding.activityLoginSenha
            campo.error = getString(R.string.login_usuario_error_senha)
        }
        if (!usuarioValido) {
            campo = binding.activityLoginUsuario
            campo.error = getString(R.string.login_usuario_error_usuario)
        }
        return error
    }

    private fun validarCampoUsuario(): Boolean {
        val campo = binding.activityLoginUsuario
        return campo.text.toString().isNotBlank()
    }

    private fun validarCampoSenha(): Boolean {
        val campo = binding.activityLoginSenha
        return campo.text.toString().isNotBlank() && campo.text.toString().length >= 6
    }

    private fun configuraBotaoCadastrar() {
        binding.activityLoginBotaoCadastrar.setOnClickListener {
            vaiPara(FormularioCadastroUsuarioActivity::class.java)
        }
    }

}