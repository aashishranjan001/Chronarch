package com.aashish.chronarch.di

import android.content.Context
import com.aashish.chronarch.home.notification.TimerNotificationManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {

    @Singleton
    @Provides
    fun providesNotificationManager(@ApplicationContext context: Context): TimerNotificationManager {
        return TimerNotificationManager(context)
    }
}
