package com.asisee.streetpieces.screens.create_piece

sealed interface CreatePieceSideEffect {
    data object NavigateFurther: CreatePieceSideEffect
    data object MissingTitleError: CreatePieceSideEffect
    data object LocationStillLoadingError: CreatePieceSideEffect
    data object RequestLocationPermission: CreatePieceSideEffect

    data object UploadError: CreatePieceSideEffect

    data object NavBack : CreatePieceSideEffect
}