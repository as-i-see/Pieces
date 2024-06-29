package com.asisee.streetpieces.screens.sign_up

sealed interface SignUpScreenState {
    data object Loading : SignUpScreenState

    data class SignUp(
        val username: String = "",
        val name: String = "",
        val bio: String = "",
        val profilePictureUrl: String = "",
        val email: String = "",
        val password: String = "",
        val repeatPassword: String = ""
    ) : SignUpScreenState
}