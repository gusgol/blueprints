package tech.hiregus.trendingprojects.utils.extensions

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.text.HtmlCompat
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import tech.hiregus.trendingprojects.R

/**
 * Load an image with rounded corners using Coil.kt
 *
 * @param res resource id
 */
fun ImageView.loadWithRoundedCorners(@DrawableRes res: Int) {
    val cornerSize = context.resources.getDimension(R.dimen.corner_size)
    this.load(res) {
        crossfade(true)
        transformations(RoundedCornersTransformation(cornerSize))
    }
}

/**
 * Load an image as circle using Coil.kt
 *
 * @param url http url
 */
fun ImageView.loadCircle(url: String, @DrawableRes placeholder: Int? = R.drawable.ic_user) {
     this.load(url) {
         crossfade(true)
         if (placeholder != null) placeholder(placeholder)
         transformations(CircleCropTransformation())
     }
}

/**
 * Set a text view text and apply html tags to it
 *
 * @param text the html
 */
@Suppress("DEPRECATION")
fun TextView.setHtmlText(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        this.text = Html.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
    } else {
        this.text = Html.fromHtml(text)
    }
}

/**
 * Easily display a snack bar
 *
 * @param the string resource id
 */
fun View.showSnackBar(@StringRes resourceId: Int,
                      duration: Int = Snackbar.LENGTH_LONG,
                      dismissAction: (() -> Unit)? = null) {
    val snackBar = Snackbar.make(this, resourceId, duration)
        .addCallback(object : Snackbar.Callback() {
            override fun onDismissed(snackbar: Snackbar?, event: Int) {
                dismissAction?.invoke()
            }
        })
    snackBar.show()
}