package tech.hiregus.trendingprojects.utils.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.use

/**
 * Retrieve a color from the current [android.content.res.Resources.Theme].
 */
@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

/**
 * Opens the Chrome custom tabs with the url provided
 */
fun Context.openCustomTabs(url: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setShowTitle(true)
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}