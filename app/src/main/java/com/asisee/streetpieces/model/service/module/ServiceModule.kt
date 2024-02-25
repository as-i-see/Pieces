package com.asisee.streetpieces.model.service.module

import com.asisee.streetpieces.model.service.AccountService
import com.asisee.streetpieces.model.service.ConfigurationService
import com.asisee.streetpieces.model.service.LocationService
import com.asisee.streetpieces.model.service.LogService
import com.asisee.streetpieces.model.service.PhotoStorageService
import com.asisee.streetpieces.model.service.PieceStorageService
import com.asisee.streetpieces.model.service.SubscriptionStorageService
import com.asisee.streetpieces.model.service.UserDataStorageService
import com.asisee.streetpieces.model.service.impl.AccountServiceImpl
import com.asisee.streetpieces.model.service.impl.ConfigurationServiceImpl
import com.asisee.streetpieces.model.service.impl.LocationServiceImpl
import com.asisee.streetpieces.model.service.impl.LogServiceImpl
import com.asisee.streetpieces.model.service.impl.PhotoStorageServiceImpl
import com.asisee.streetpieces.model.service.impl.PieceStorageServiceImpl
import com.asisee.streetpieces.model.service.impl.SubscriptionStorageServiceImpl
import com.asisee.streetpieces.model.service.impl.UserDataStorageServiceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {
    @Binds abstract fun provideAccountService(impl: AccountServiceImpl): AccountService

    @Binds abstract fun provideLogService(impl: LogServiceImpl): LogService

    @Binds
    abstract fun provideConfigurationService(impl: ConfigurationServiceImpl): ConfigurationService

    @Binds abstract fun provideLocationService(impl: LocationServiceImpl): LocationService

    @Binds abstract fun provideMyStorageService(impl: PieceStorageServiceImpl): PieceStorageService

    @Binds abstract fun providePhotoService(impl: PhotoStorageServiceImpl): PhotoStorageService

    @Binds
    abstract fun provideUserDataStorageService(
        impl: UserDataStorageServiceImpl
    ): UserDataStorageService

    @Binds
    abstract fun provideSubscriptionStorageService(
        impl: SubscriptionStorageServiceImpl
    ): SubscriptionStorageService
}
