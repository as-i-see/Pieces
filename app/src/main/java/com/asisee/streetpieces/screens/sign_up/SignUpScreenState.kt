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

    fun fill(username: String, name: String, bio: String, email: String, password: String, repeatPassword: String) = (this as? SignUp)?.copy(username = username, name = name, bio = bio, email = email, password = password, repeatPassword = repeatPassword) ?: this

}