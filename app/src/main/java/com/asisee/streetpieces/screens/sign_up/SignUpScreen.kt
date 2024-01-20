package com.asisee.streetpieces.screens.sign_up

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicField
import com.asisee.streetpieces.common.composable.BasicFieldLeadingIcon
import com.asisee.streetpieces.common.composable.BasicToolbar
import com.asisee.streetpieces.common.composable.EmailField
import com.asisee.streetpieces.common.composable.PasswordField
import com.asisee.streetpieces.common.composable.RepeatPasswordField
import com.asisee.streetpieces.R.string as AppText

import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.ext.fieldModifier
import com.asisee.streetpieces.common.ext.spacerM

@Composable
fun SignUpScreen(
  openAndPopUp: (String, String) -> Unit,
  modifier: Modifier = Modifier,
  viewModel: SignUpViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState
  val fieldModifier = Modifier.fieldModifier()
  val photoPicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
    onResult = viewModel::onPhotoUriChosen)
  BasicToolbar(AppText.create_account)
  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (uiState.photoUri == Uri.EMPTY) {
      IconButton(onClick = { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
        Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = stringResource(id = AppText.icon), modifier = Modifier
          .width(100.dp)
          .height(100.dp))
      }
    } else {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(uiState.photoUri)
          .crossfade(true)
          .build(),
        contentDescription = stringResource(AppText.uploaded_avatar),
        placeholder = painterResource(id = R.drawable.photo_placeholder),
        contentScale = ContentScale.Fit,
        modifier = Modifier
          .clip(CircleShape)
          .width(100.dp)
          .height(100.dp)
          .clickable {
            photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
          },
      )
    }
    Spacer(modifier = Modifier.spacerM())
    BasicFieldLeadingIcon(text = AppText.username, value = uiState.username, onNewValue = viewModel::onUsernameChange, iconImageVector = Icons.Default.Person, modifier = fieldModifier)
    BasicField(text = AppText.name, value = uiState.name, onNewValue = viewModel::onNameChange, fieldModifier)
    BasicField(text = AppText.bio, value = uiState.bio, onNewValue = viewModel::onBioChange, fieldModifier)
    EmailField(uiState.email, viewModel::onEmailChange, fieldModifier)
    PasswordField(uiState.password, viewModel::onPasswordChange, fieldModifier)
    RepeatPasswordField(uiState.repeatPassword, viewModel::onRepeatPasswordChange, fieldModifier)

    BasicButton(AppText.create_account, Modifier.basicButton()) {
      viewModel.onSignUpClick(openAndPopUp)
    }
  }
}

@Composable
fun SignUpScreenPreview(modifier: Modifier = Modifier,) {
  val fieldModifier = Modifier.fieldModifier()
  val imageAdded = false
  BasicToolbar(AppText.create_account)

  Column(
    modifier = modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .verticalScroll(rememberScrollState()),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    if (imageAdded) {

    } else {
      IconButton(onClick = { /*TODO*/ }) {
        Icon(imageVector = Icons.Default.AddAPhoto, contentDescription = stringResource(id = AppText.icon), modifier = Modifier
          .width(100.dp)
          .height(100.dp))
      }
    }
    Spacer(modifier = Modifier.spacerM())
    BasicFieldLeadingIcon(text = AppText.username, value = "", onNewValue = {}, iconImageVector = Icons.Default.Person, modifier = fieldModifier)
    BasicField(text = AppText.name, value = "", onNewValue = {}, fieldModifier)
    BasicField(text = AppText.bio, value = "", onNewValue = {}, fieldModifier)
    EmailField("dodik@sos@.lka", {}, fieldModifier)
    PasswordField("123456", {}, fieldModifier)
    RepeatPasswordField("123456", {}, fieldModifier)

    BasicButton(AppText.create_account, Modifier.basicButton()) {}
  }
}