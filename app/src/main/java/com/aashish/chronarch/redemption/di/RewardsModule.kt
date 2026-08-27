package com.aashish.chronarch.redemption.di

import com.aashish.chronarch.redemption.data.repository.RewardsRepositoryImpl
import com.aashish.chronarch.redemption.domain.repository.RewardsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RewardsModule {

    @Singleton
    @Binds
    abstract fun bindsRewardsRepository(impl: RewardsRepositoryImpl): RewardsRepository
}