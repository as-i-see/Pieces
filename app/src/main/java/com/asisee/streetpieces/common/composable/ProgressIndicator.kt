package com.asisee.streetpieces.common.composable

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ProgressIndicator() {
    CenteredColumn {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
    }
}