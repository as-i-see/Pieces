package com.asisee.streetpieces.common.composable

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.ext.basicButton
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.AlertCircle
import compose.icons.evaicons.outline.AlertTriangle
import com.asisee.streetpieces.R.string as AppText

@Composable
fun ErrorIndicator(errorText: String = stringResource(AppText.error), onRetry: () -> Unit) {
    CenteredColumn {
        Icon(
            imageVector = EvaIcons.Outline.AlertTriangle,
            contentDescription = stringResource(AppText.error_indicator),
        )
        Text(
            text = errorText,
            fontSize = 32.sp,
        )
        BasicButton(
            text = AppText.try_again,
            modifier = Modifier.basicButton(),
            action = onRetry
        )
    }
}