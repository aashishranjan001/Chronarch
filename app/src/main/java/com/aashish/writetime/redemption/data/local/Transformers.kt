package com.aashish.writetime.redemption.data.local

import com.aashish.writetime.redemption.data.local.model.RewardEntity
import com.aashish.writetime.redemption.domain.model.Reward

fun RewardEntity.toDomain() = Reward(
    id = id,
    name = name,
    cost = focusPointsCost
)

fun Reward.toEntity() = RewardEntity(
    id = id,
    name = name,
    focusPointsCost = cost
)