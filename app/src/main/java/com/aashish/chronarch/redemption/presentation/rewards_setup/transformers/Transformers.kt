package com.aashish.chronarch.redemption.presentation.rewards_setup.transformers

import com.aashish.chronarch.redemption.domain.model.Reward
import com.aashish.chronarch.redemption.presentation.rewards_setup.EditModeRewardItem
import com.aashish.chronarch.redemption.presentation.rewards_setup.RewardSetupItem

fun EditModeRewardItem.toRewardSetupItem(): RewardSetupItem? = RewardSetupItem(
    id = id,
    name = name,
    cost = cost.toIntOrNull() ?: return null
)

fun RewardSetupItem.toEditModeRewardItem() = EditModeRewardItem(
    id = id,
    name = name,
    cost = cost.toString()
)

fun RewardSetupItem.toDomainModel() = Reward(
    id = 0, // 0 so that room can auto generate new primary key instead of colliding strings ids
    name = name,
    cost = cost
)