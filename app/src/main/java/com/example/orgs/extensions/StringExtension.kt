package com.example.orgs.extensions

import android.os.Build
import android.text.Html
import android.text.Spanned
import java.security.MessageDigest

fun String.toHash(
    algoritmo: String = "SHA-256"
): String {
    return MessageDigest
        .getInstance(algoritmo)
        .digest(this.toByteArray())
        .fold("") { str, byte ->
            str + "%02x".format(byte)
        }
}

fun String.parseAsHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}