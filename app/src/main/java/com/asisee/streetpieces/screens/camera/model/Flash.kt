package com.asisee.streetpieces.screens.camera.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.asisee.streetpieces.R

enum class Flash(@DrawableRes val drawableRes: Int, @StringRes val contentRes: Int) {
    Off(R.drawable.flash_off, R.string.flash_off),
    On(R.drawable.flash_on, R.string.flash_on),
    Auto(R.drawable.flash_auto, R.string.flash_auto),
    Always(R.drawable.flash_always, R.string.flash_always)
}
