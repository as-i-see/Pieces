package com.asisee.streetpieces.common.exceptions

sealed interface LocationResultException {
    data object PermissionRequestFlow: LocationResultException
    data object LocationFetchException: LocationResultException
}