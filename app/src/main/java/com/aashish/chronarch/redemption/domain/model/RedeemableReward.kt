package com.aashish.chronarch.redemption.domain.model

data class RedeemableReward(
    val id: Long,
    val name: String,
    val cost: Int,
    val isRedeemable: Boolean
)
