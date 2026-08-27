package com.aashish.chronarch.redemption.domain.model

data class RedemptionCornerOverview(
    val availableBalance: Int,
    val rewards: List<RedeemableReward>
)