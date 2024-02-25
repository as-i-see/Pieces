package com.asisee.streetpieces.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CamposerTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors =
            MaterialTheme.colors.copy(
                primary = BrightOrange,
                background = DarkOrange,
            ),
        typography = Typography,
        content = content)
}
