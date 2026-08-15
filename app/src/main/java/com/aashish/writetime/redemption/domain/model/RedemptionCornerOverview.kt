package com.aashish.writetime.redemption.domain.model

data class RedemptionCornerOverview(
    val availableBalance: Int,
    val rewards: List<Reward>
)