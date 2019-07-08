package com.valdizz.cocktails.common

import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Utilities for creating layout components programmatically.
 *
 * @author Vlad Kornev
 */
object ComponentUtils {
    fun createDivider(view: LinearLayout, margin: Int): View {
        return View(view.context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1.toPx()).apply {
                setMargins(margin.toPx(), 0, margin.toPx(), 0)
            }
            background = resources.getDrawable(android.R.color.darker_gray, view.context.theme)
        }
    }

    fun createImageView(view: LinearLayout, layoutWidth: Int, layoutHeight: Int): ImageView {
        return ImageView(view.context).apply {
            layoutParams = LinearLayout.LayoutParams(layoutWidth.toPx(), layoutHeight.toPx()).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
        }
    }

    fun createLinearLayout(
        view: LinearLayout,
        ingredient: String?,
        margin: Int,
        layoutHeight: Int
    ): LinearLayout {
        return LinearLayout(view.context).apply {
            tag = ingredient
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                layoutHeight.toPx()
            ).apply {
                setMargins(margin.toPx(), 0, margin.toPx(), 0)
            }
        }
    }

    fun createTextView(
        view: LinearLayout,
        text: String?,
        layoutWeight: Float,
        gravityTv: Int,
        padding: Int
    ): TextView {
        return TextView(view.context).apply {
            this.text = text
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                layoutWeight
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
            gravity = gravityTv
            setPadding(padding.toPx(), 0, padding.toPx(), 0)
        }
    }
}