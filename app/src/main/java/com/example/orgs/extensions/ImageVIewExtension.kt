package com.example.orgs.extensions

import android.widget.ImageView
import coil.ImageLoader
import coil.load
import com.example.orgs.R

fun ImageView.carregar(url: String? = null, imageLoader: ImageLoader) {
    load(url, imageLoader) {
        fallback(R.drawable.erro)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}