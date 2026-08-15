package com.aashish.writetime.di

import android.content.Context
import androidx.room.Room
import com.aashish.writetime.common.data.local.dao.TimerSessionDao
import com.aashish.writetime.common.data.local.dao.TransactionHistoryDao
import com.aashish.writetime.data.local.database.AppDatabase
import com.aashish.writetime.redemption.data.local.dao.RewardsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        AppDatabase.DATABASE_NAME,
    ).build()

    @Provides
    @Singleton
    fun providesTimerSessionDao(database: AppDatabase): TimerSessionDao = database.timerSessionDao

    @Provides
    @Singleton
    fun providesTransactionHistoryDao(database: AppDatabase): TransactionHistoryDao = database.transactionHistoryDao

    @Provides
    @Singleton
    fun providesRewardsDao(database: AppDatabase): RewardsDao = database.rewardsDao
}