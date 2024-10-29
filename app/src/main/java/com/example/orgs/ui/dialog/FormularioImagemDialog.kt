package com.example.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import coil.ImageLoader
import com.example.orgs.databinding.FormularioImagemBinding
import com.example.orgs.extensions.carregar
import com.example.orgs.extensions.gerarImageLoader

class FormularioImagemDialog(private val context: Context) {
    private val binding = FormularioImagemBinding.inflate(LayoutInflater.from(context))
    private val imageLoader: ImageLoader =
        binding.formularioImagemImagemview.gerarImageLoader(context)

    fun show(
        urlPadrao: String? = null,
        onLoad: (urlImagem: String) -> Unit
    ) {
        binding.apply {
            urlPadrao?.let {
                formularioImagemImagemview.carregar(it, imageLoader)
                formularioImagemUrl.setText(it)
            }
            formularioImagemBotaoCarregar.setOnClickListener() {
                val url = formularioImagemUrl.text.toString()
                formularioImagemImagemview.carregar(url, imageLoader)
            }
            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                    val url = formularioImagemUrl.text.toString()
                    onLoad(url)
                }
                .setNegativeButton("Cancelar") { _, _ -> }
                .show()
        }

    }
}