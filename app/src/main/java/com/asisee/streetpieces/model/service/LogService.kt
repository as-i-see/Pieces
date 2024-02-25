package com.asisee.streetpieces.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
