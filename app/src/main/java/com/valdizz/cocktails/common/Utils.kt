package com.valdizz.cocktails.common

import android.content.res.Resources

/**
 * Extension functions.
 *
 * @author Vlad Kornev
 */
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()