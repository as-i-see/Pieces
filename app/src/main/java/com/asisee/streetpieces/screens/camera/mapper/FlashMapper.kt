package com.asisee.streetpieces.screens.camera.mapper

import com.asisee.streetpieces.screens.camera.model.Flash
import com.ujizin.camposer.state.FlashMode

fun Flash.toFlashMode() =
    when (this) {
        Flash.Auto -> FlashMode.Auto
        Flash.On -> FlashMode.On
        Flash.Off,
        Flash.Always -> FlashMode.Off
    }

fun FlashMode.toFlash(isTorchEnabled: Boolean) =
    when (this) {
        FlashMode.On -> Flash.On
        FlashMode.Auto -> Flash.Auto
        FlashMode.Off -> Flash.Off
    }.takeIf { !isTorchEnabled } ?: Flash.Always
