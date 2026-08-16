package com.aashish.writetime.common

import com.aashish.writetime.common.data.repository.FocusPointsTransactionsRepositoryImpl
import com.aashish.writetime.common.data.repository.TimerSessionRepositoryImpl
import com.aashish.writetime.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.writetime.common.domain.repository.TimerSessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonBindsModule {

    @Binds
    abstract fun bindsTimerSessionRepository(impl: TimerSessionRepositoryImpl): TimerSessionRepository

    @Binds
    abstract fun bindsFocusPointsTransactionsRepository(impl : FocusPointsTransactionsRepositoryImpl): FocusPointsTransactionsRepository

}