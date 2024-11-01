package com.example.orgs.extensions

import android.app.Activity
import android.os.Build
import android.os.Parcelable

inline fun <reified T : Parcelable> Activity.getParcelableExtraCompat(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        intent.getParcelableExtra(key, T::class.java)
    } else {
        intent.getParcelableExtra(key) as? T
    }
}