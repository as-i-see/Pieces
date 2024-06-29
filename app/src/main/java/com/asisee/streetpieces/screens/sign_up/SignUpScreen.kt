package com.asisee.streetpieces.screens.sign_up

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.asisee.streetpieces.R
import com.asisee.streetpieces.common.composable.BasicButton
import com.asisee.streetpieces.common.composable.BasicField
import com.asisee.streetpieces.common.composable.BasicLeadingIconField
import com.asisee.streetpieces.common.composable.BasicToolbar
import com.asisee.streetpieces.common.composable.CenteredColumn
import com.asisee.streetpieces.common.composable.EmailField
import com.asisee.streetpieces.common.composable.PasswordField
import com.asisee.streetpieces.common.composable.ProgressIndicator
import com.asisee.streetpieces.common.composable.RepeatPasswordField
import com.asisee.streetpieces.common.composable.SpacerM
import com.asisee.streetpieces.common.ext.basicButton
import com.asisee.streetpieces.common.ext.fieldModifier
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage
import com.asisee.streetpieces.screens.main.MainScreen
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.PersonAdd
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.asisee.streetpieces.R.string as AppText

class SignUpScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = navigator.getNavigatorScreenModel<SignUpScreenModel>()
        val state by screenModel.collectAsState()
        screenModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is SignUpScreenSideEffect.NavigateToFeedScreen -> {
                    navigator.replaceAll(MainScreen())
                }
                is SignUpScreenSideEffect.DisplayMessage -> {
                    SnackbarManager.showMessage(SnackbarMessage.StringSnackbar(sideEffect.message))
                }
            }
        }
        View(
            state,
            screenModel::onEmailChange,
            screenModel::onPasswordChange,
            screenModel::onRepeatPasswordChange,
            screenModel::onUsernameChange,
            screenModel::onNameChange,
            screenModel::onBioChange,
            screenModel::onProfilePictureChoose,
            screenModel::onSignUpClick
        )
    }

    @Composable
    fun View(
        state: SignUpScreenState,
        onEmailChange: (String) -> Unit,
        onPasswordChange: (String) -> Unit,
        onRepeatPasswordChange: (String) -> Unit,
        onUsernameChange: (String) -> Unit,
        onNameChange: (String) -> Unit,
        onBioChange: (String) -> Unit,
        onProfilePictureChoose: (String) -> Unit,
        onSignUpClick: () -> Unit
    ) {
        when (state) {
            is SignUpScreenState.Loading -> {
                ProgressIndicator()
            }
            is SignUpScreenState.SignUp -> {
                BasicToolbar(AppText.create_account)
                CenteredColumn {
                    ProfilePicture(
                        url = state.profilePictureUrl,
                        onProfilePictureChoose = onProfilePictureChoose,
                    )
                    SpacerM()
                    BasicLeadingIconField(
                        text = AppText.username,
                        value = state.username,
                        onNewValue = onUsernameChange,
                        iconImageVector = EvaIcons.Outline.Person,
                        modifier = Modifier.fieldModifier(),
                    )
                    BasicField(
                        text = AppText.name,
                        value = state.name,
                        onNewValue = onNameChange,
                        modifier = Modifier.fieldModifier(),
                    )
                    BasicField(
                        text = AppText.bio,
                        value = state.bio,
                        onNewValue = onBioChange,
                        modifier = Modifier.fieldModifier(),
                    )
                    EmailField(
                        value = state.email,
                        onNewValue = onEmailChange,
                        modifier = Modifier.fieldModifier(),
                    )
                    PasswordField(
                        value = state.password,
                        onNewValue = onPasswordChange,
                        modifier = Modifier.fieldModifier(),
                    )
                    RepeatPasswordField(
                        value = state.repeatPassword,
                        onNewValue = onRepeatPasswordChange,
                        modifier = Modifier.fieldModifier(),
                    )
                    BasicButton(
                        text = AppText.create_account,
                        action = onSignUpClick,
                        modifier = Modifier.basicButton()
                    )
                }
            }
        }
    }

    @Composable
    private fun ProfilePicture(url: String, onProfilePictureChoose: (String) -> Unit) {
        val photoPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { pictureUri ->
                pictureUri?.toString()?.let { uri ->
                    onProfilePictureChoose(uri)
                }
            }
        )
        if (url.isEmpty()) {
            IconButton(
                onClick = {
                    photoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                }
            ) {
                Icon(
                    imageVector = EvaIcons.Outline.PersonAdd,
                    contentDescription = stringResource(id = AppText.icon),
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                )
            }
        } else {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
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
                        photoPicker.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
            )
        }
    }
}