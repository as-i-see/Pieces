package com.asisee.streetpieces.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.asisee.streetpieces.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

open class LogViewModel(private val logService: LogService) : ViewModel() {
    fun launchCatching(snackbar: Boolean = true, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                if (snackbar) {
                    SnackbarManager.showMessage(throwable.toSnackbarMessage())
                }
                logService.logNonFatalCrash(throwable)
            },
            block = block)


}

fun CoroutineScope.launchCatching(onError: () -> Unit, block: suspend CoroutineScope.() -> Unit) {
    launch(CoroutineExceptionHandler { _, _ ->
        onError()
    }, block = block)
}

fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.launchCatching(scope: CoroutineScope, sideEffectOnError: SIDE_EFFECT, block: suspend CoroutineScope.() -> Unit) {
    scope.launch(
        CoroutineExceptionHandler { _, _ ->
            intent {
                postSideEffect(sideEffectOnError)
            }
        }, block = block
    )
}