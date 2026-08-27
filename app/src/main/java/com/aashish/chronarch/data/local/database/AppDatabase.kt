package com.aashish.chronarch.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aashish.chronarch.common.data.local.dao.TimerSessionDao
import com.aashish.chronarch.common.data.local.dao.TransactionHistoryDao
import com.aashish.chronarch.common.data.local.mappers.FocusPointTransactionTypeConvertor
import com.aashish.chronarch.common.data.local.model.FocusPointsTransactionEntity
import com.aashish.chronarch.common.data.local.model.TimerSessionEntity
import com.aashish.chronarch.data.local.database.AppDatabase.Companion.DATABASE_VERSION
import com.aashish.chronarch.redemption.data.local.dao.RewardsDao
import com.aashish.chronarch.redemption.data.local.model.RewardEntity

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