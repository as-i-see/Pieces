package com.asisee.streetpieces.common.ext

import java.util.Locale

internal fun Float.roundTo(n: Int): Float {
    return try {
        "%.${n}f".format(Locale.US, this).toFloat()
    } catch (e: NumberFormatException) {
        this
    }
}
