package com.asisee.streetpieces.screens.sign_up

import android.net.Uri

data class SignUpUiState(
    val username: String = "",
    val name: String = "",
    val bio: String = "",
    val photoUri: Uri = Uri.EMPTY,
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = ""
)
