package com.asisee.streetpieces.common.composable

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.asisee.streetpieces.common.ext.fieldModifier
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Email
import compose.icons.evaicons.outline.Eye
import compose.icons.evaicons.outline.EyeOff2
import compose.icons.evaicons.outline.Lock
import com.asisee.streetpieces.R.string as AppText

@Composable
fun BasicField(
    @StringRes text: Int,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) },
    )
}

@Composable
fun BasicLeadingIconField(
    @StringRes text: Int,
    iconImageVector: ImageVector,
    value: String,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(stringResource(text)) },
        leadingIcon = {
            Icon(
                imageVector = iconImageVector,
                contentDescription = stringResource(id = AppText.icon))
        })
}

@Composable
fun EmailField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier = Modifier.height(IntrinsicSize.Max)) {
        OutlinedTextField(
            singleLine = true,
            modifier = modifier,
            value = value,
            onValueChange = onNewValue,
            placeholder = { Text(stringResource(AppText.email)) },
            leadingIcon = {
                Icon(
                    imageVector = EvaIcons.Outline.Email,
                    contentDescription = stringResource(AppText.email)
                )
            }
        )
    }

}

@Composable
fun PasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier = Modifier) {
    PasswordField(value, AppText.password, onNewValue, modifier)
}

@Composable
fun RepeatPasswordField(value: String, onNewValue: (String) -> Unit, modifier: Modifier) {
    PasswordField(value, AppText.repeat_password, onNewValue, modifier)
}

@Composable
private fun PasswordField(
    value: String,
    @StringRes placeholder: Int,
    onNewValue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    val icon =
        if (isVisible) EvaIcons.Outline.Eye
        else EvaIcons.Outline.EyeOff2

    val visualTransformation =
        if (isVisible) VisualTransformation.None
        else PasswordVisualTransformation()

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { onNewValue(it) },
        placeholder = { Text(text = stringResource(placeholder)) },
        leadingIcon = {
            Icon(
                imageVector = EvaIcons.Outline.Lock,
                contentDescription = stringResource(AppText.lock)
            )
        },
        trailingIcon = {
            IconButton(
                onClick = { isVisible = !isVisible }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = stringResource(AppText.password_visibility)
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}
