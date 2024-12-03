package com.example.orgs.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.orgs.R
import com.example.orgs.database.AppDataBase
import com.example.orgs.extensions.toast
import com.example.orgs.model.Usuario
import com.example.orgs.databinding.ActivityFormularioCadastroUsuarioBinding
import com.example.orgs.extensions.toHash
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class FormularioCadastroUsuarioActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityFormularioCadastroUsuarioBinding.inflate(layoutInflater)
    }
    private val dao by lazy {
        AppDataBase.instancia(this).usuarioDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configuraBotaoCadastrar()
    }

    private fun configuraBotaoCadastrar() {
        binding.activityFormularioCadastroBotaoCadastrar.setOnClickListener {
            val novoUsuario = criaUsuario()
            cadastra(novoUsuario)
        }
    }

    private fun validarDado(): Boolean {
        var error = false
        var campo: TextInputEditText? = null
        val usuarioValido = validarCampoUsuario()
        val nomeValido = validarCampoNome()
        val senhaValido = validarCampoSenha()
        if (!usuarioValido || !nomeValido || !senhaValido) {
            error = true
        }
        if (!senhaValido) {
            campo = binding.activityFormularioCadastroSenha
            campo.error = getString(R.string.formulario_usuario_error_senha)
        }
        if (!nomeValido) {
            campo = binding.activityFormularioCadastroNome
            campo.error = getString(R.string.formulario_usuario_error_nome)
        }
        if (!usuarioValido) {
            campo = binding.activityFormularioCadastroUsuario
            campo.error = getString(R.string.formulario_usuario_error_usuario)
        }
        if (error) campo?.requestFocus()
        return error
    }

    private fun validarCampoUsuario(): Boolean {
        val campo = binding.activityFormularioCadastroUsuario
        return campo.text.toString().isNotBlank()
    }

    private fun validarCampoNome(): Boolean {
        val campo = binding.activityFormularioCadastroNome
        return campo.text.toString().isNotBlank()
    }

    private fun validarCampoSenha(): Boolean {
        val campo = binding.activityFormularioCadastroSenha
        return campo.text.toString().isNotBlank() && campo.text.toString().length >= 6
    }

    private fun cadastra(usuario: Usuario) {
        if (!validarDado()) {
            lifecycleScope.launch {
                try {
                    dao.adicionar(usuario)
                    finish()
                } catch (e: Exception) {
                    Log.e("CadastroUsuario", "configuraBotaoCadastrar: ", e)
                    toast("Falha ao cadastrar usuário")
                }
            }
        }
    }

    private fun criaUsuario(): Usuario {
        val usuario = binding.activityFormularioCadastroUsuario.text.toString()
        val nome = binding.activityFormularioCadastroNome.text.toString()
        val senha = binding.activityFormularioCadastroSenha.text.toString().toHash()
        return Usuario(usuario, nome, senha)
    }
}