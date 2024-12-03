package com.example.orgs.extensions

import android.content.Context
import android.view.MenuItem
import androidx.core.content.res.ResourcesCompat

fun MenuItem.setTitleColor(context: Context, color: Int) {
    val colorByTheme = ResourcesCompat.getColor(context.resources, color, context.theme)
    val hexColor = Integer.toHexString(colorByTheme).uppercase().substring(2)
    val html = "<font color='#$hexColor'>$title</font>"
    this.title = html.parseAsHtml()
}