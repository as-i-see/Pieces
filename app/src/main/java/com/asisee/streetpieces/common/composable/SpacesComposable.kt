package com.asisee.streetpieces.common.composable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.asisee.streetpieces.common.ext.spacerL
import com.asisee.streetpieces.common.ext.spacerM
import com.asisee.streetpieces.common.ext.spacerML
import com.asisee.streetpieces.common.ext.spacerS
import com.asisee.streetpieces.common.ext.spacerXL
import com.asisee.streetpieces.common.ext.spacerXS
import com.asisee.streetpieces.common.ext.spacerXXL

@Composable fun SpacerXS() = Spacer(modifier = Modifier.spacerXS())

@Composable fun SpacerS() = Spacer(modifier = Modifier.spacerS())

@Composable fun SpacerM() = Spacer(modifier = Modifier.spacerM())

@Composable fun SpacerML() = Spacer(modifier = Modifier.spacerML())

@Composable fun SpacerL() = Spacer(modifier = Modifier.spacerL())

@Composable fun SpacerXL() = Spacer(modifier = Modifier.spacerXL())

@Composable fun SpacerXXL() = Spacer(modifier = Modifier.spacerXXL())
