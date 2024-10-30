package com.example.orgs.extensions

import android.content.Context
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import com.example.orgs.R

fun ImageView.carregar(
    url: String? = null,
    imageLoader: ImageLoader,
    fallback: Int = R.drawable.imagem_padrao
) {
    load(url, imageLoader) {
        fallback(fallback)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}

fun ImageView.gerarImageLoader(context: Context): ImageLoader {
    return ImageLoader.Builder(context)
        .componentRegistry {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder(context))
            } else {
                add(GifDecoder())
            }
        }.build()
}