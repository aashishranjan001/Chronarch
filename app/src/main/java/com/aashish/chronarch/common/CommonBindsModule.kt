package com.aashish.chronarch.common

import com.aashish.chronarch.common.data.repository.FocusPointsTransactionsRepositoryImpl
import com.aashish.chronarch.common.data.repository.TimerSessionRepositoryImpl
import com.aashish.chronarch.common.domain.repository.FocusPointsTransactionsRepository
import com.aashish.chronarch.common.domain.repository.TimerSessionRepository
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