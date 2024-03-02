package com.asisee.streetpieces.screens.create_piece

sealed interface CreatePieceSideEffect {
    data object NavigateFurther: CreatePieceSideEffect
    data object MissingTitleError: CreatePieceSideEffect
    data object LocationFetchError: CreatePieceSideEffect
}