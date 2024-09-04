package com.asisee.streetpieces.screens.sign_in

sealed interface SignInScreenState {
    data class SignIn(val email: String = "", val password: String = "") : SignInScreenState

    data object Loading : SignInScreenState

    fun fill(email: String, password: String) = (this as? SignIn)?.copy(email = email, password = password) ?: this
}