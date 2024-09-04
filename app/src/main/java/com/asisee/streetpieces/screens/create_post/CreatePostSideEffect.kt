package com.asisee.streetpieces.screens.create_post

sealed interface CreatePostSideEffect {
    data object NavigateToProfile: CreatePostSideEffect
    data object MissingTitleError: CreatePostSideEffect
    data object LocationStillLoadingError: CreatePostSideEffect
    data object RequestLocationPermission: CreatePostSideEffect

    data object UploadError: CreatePostSideEffect

    data object NavBack : CreatePostSideEffect
}