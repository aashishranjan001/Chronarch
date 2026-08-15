package com.aashish.writetime.redemption.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("rewards")
class RewardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val focusPointsCost: Int,
)