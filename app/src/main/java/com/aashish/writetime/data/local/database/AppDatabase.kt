package com.aashish.writetime.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aashish.writetime.common.data.repository.local.dao.TimerSessionDao
import com.aashish.writetime.common.data.repository.local.dao.TransactionHistoryDao
import com.aashish.writetime.common.data.repository.local.mappers.FocusPointTransactionTypeConvertor
import com.aashish.writetime.common.data.repository.local.model.FocusPointsTransactionEntity
import com.aashish.writetime.common.data.repository.local.model.TimerSessionEntity
import com.aashish.writetime.data.local.database.AppDatabase.Companion.DATABASE_VERSION
import com.aashish.writetime.redemption.data.local.dao.RewardsDao
import com.aashish.writetime.redemption.data.local.model.RewardEntity

@Database(
    entities = [
        TimerSessionEntity::class, FocusPointsTransactionEntity::class, RewardEntity::class
    ],
    version = DATABASE_VERSION
)
@TypeConverters(FocusPointTransactionTypeConvertor::class)
abstract class AppDatabase: RoomDatabase() {
    abstract val timerSessionDao: TimerSessionDao
    abstract val transactionHistoryDao: TransactionHistoryDao
    abstract val rewardsDao: RewardsDao

    companion object {
        const val DATABASE_NAME = "app_database"
        const val DATABASE_VERSION = 1
    }
}