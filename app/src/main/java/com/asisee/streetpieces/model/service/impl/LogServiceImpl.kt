package com.asisee.streetpieces.model.service.impl

import com.asisee.streetpieces.model.service.LogService
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import org.koin.core.annotation.Single
import javax.inject.Inject
@Single
class LogServiceImpl : LogService {
    override fun logNonFatalCrash(throwable: Throwable) =
        Firebase.crashlytics.recordException(throwable)
}
