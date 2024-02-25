package com.asisee.streetpieces.model.service

interface ConfigurationService {
    suspend fun fetchConfiguration(): Boolean

    val isShowTaskEditButtonConfig: Boolean
}
