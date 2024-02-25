package com.asisee.streetpieces.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asisee.streetpieces.common.snackbar.SnackbarManager
import com.asisee.streetpieces.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import com.asisee.streetpieces.model.service.LogService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
