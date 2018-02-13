@file:Suppress("NOTHING_TO_INLINE")

package com.example.infraymer.trezvoeradio

import android.view.View

/**
 * Created by infraymer on 29.11.17.
 */
inline fun View.visible() {
    visibility = View.VISIBLE
}

inline fun View.invisible() {
    visibility = View.INVISIBLE
}

inline fun View.gone() {
    visibility = View.GONE
}