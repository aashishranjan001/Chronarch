package com.aashish.chronarch.redemption.data.local

import com.aashish.chronarch.redemption.data.local.model.RewardEntity
import com.aashish.chronarch.redemption.domain.model.Reward

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