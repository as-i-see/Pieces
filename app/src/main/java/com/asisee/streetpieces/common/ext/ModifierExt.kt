package com.asisee.streetpieces.common.ext

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun Modifier.textButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp, 16.dp, 0.dp)
}

fun Modifier.permissionButton(): Modifier {
    return this.fillMaxWidth().padding(8.dp, 0.dp, 8.dp, 16.dp)
}

fun Modifier.basicButton(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 8.dp)
}

fun Modifier.card(): Modifier {
    return this.padding(16.dp, 0.dp, 16.dp, 8.dp)
}

fun Modifier.contextMenu(): Modifier {
    return this.wrapContentWidth()
}

fun Modifier.alertDialog(): Modifier {
    return this.wrapContentWidth().wrapContentHeight()
}

fun Modifier.dropdownSelector(): Modifier {
    return this.fillMaxWidth()
}

fun Modifier.fieldModifier(): Modifier {
    return this.fillMaxWidth().padding(16.dp, 4.dp)
}

fun Modifier.toolbarActions(): Modifier {
    return this.wrapContentSize(Alignment.TopEnd)
}

fun Modifier.spacerM(): Modifier {
    return this.fillMaxWidth().height(16.dp)
}

fun Modifier.spacerML(): Modifier {
    return this.fillMaxWidth().height(24.dp)
}

fun Modifier.spacerL(): Modifier {
    return this.fillMaxWidth().height(32.dp)
}

fun Modifier.spacerXL(): Modifier {
    return this.fillMaxWidth().height(48.dp)
}

fun Modifier.spacerXXL(): Modifier {
    return this.fillMaxWidth().height(64.dp)
}

fun Modifier.spacerS(): Modifier {
    return this.fillMaxWidth().height(8.dp)
}

fun Modifier.spacerXS(): Modifier {
    return this.fillMaxWidth().height(2.dp)
}

fun Modifier.row(): Modifier {
    return this.fillMaxWidth().padding(horizontal = 8.dp)
}

fun Modifier.noClickable() = then(Modifier.clickable(enabled = false) {})

fun Modifier.circularProgressIndicator() = this.height(64.dp).width(64.dp)
