package tech.hiregus.trendingprojects.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import tech.hiregus.trendingprojects.R

class ViewHolderSpacingDecoration(private val vertical: Int, private val horizontal: Int) : RecyclerView.ItemDecoration() {

    companion object {
        fun defaultSpacing(context: Context) = ViewHolderSpacingDecoration(
            vertical = context.resources.getDimension(R.dimen.keyline_0).toInt(),
            horizontal = context.resources.getDimension(R.dimen.keyline_1).toInt()
        )
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = vertical
            left = horizontal
            right = horizontal
            bottom = vertical
        }
    }

}