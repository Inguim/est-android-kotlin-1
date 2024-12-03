package com.example.orgs.extensions

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat

fun Context.vaiPara(clazz: Class<*>, intent: Intent.() -> Unit = {}) {
    Intent(this, clazz)
        .apply {
            intent()
            startActivity(this)
        }
}

fun Context.toast(mensagem: String) {
    Toast.makeText(
        this,
        mensagem,
        Toast.LENGTH_SHORT
    ).show()
}

fun Context.getColorByTheme(color: Int): Int {
    return ResourcesCompat.getColor(resources, color, theme)
}