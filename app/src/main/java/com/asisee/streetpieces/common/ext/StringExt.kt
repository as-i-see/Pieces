package com.asisee.streetpieces.common.ext

import android.util.Patterns
import java.util.regex.Pattern

private const val MIN_PASS_LENGTH = 6
private const val MIN_USERNAME_LENGTH = 1
const val MAX_USERNAME_LENGTH = 32
private const val MAX_NAME_LENGTH = 64
private const val MAX_BIO_LENGTH = 256
private const val PASS_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"

fun String.isValidEmail(): Boolean {
  return this.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.isValidUsername() : Boolean {
  return this.length in MIN_USERNAME_LENGTH..MAX_USERNAME_LENGTH
}

fun String.isValidName() : Boolean {
  return this.length <= MAX_NAME_LENGTH
}

fun String.isValidBio() : Boolean {
  return this.length <= MAX_BIO_LENGTH
}

fun String.isValidPassword(): Boolean {
  return this.isNotBlank() &&
    this.length >= MIN_PASS_LENGTH &&
    Pattern.compile(PASS_PATTERN).matcher(this).matches()
}

fun String.passwordMatches(repeated: String): Boolean {
  return this == repeated
}

fun String.parameterFromParameterValue(): String {
  return this.substring(1, this.length - 1)
}

fun String.routeWithoutParameters() = substringBefore('?')
