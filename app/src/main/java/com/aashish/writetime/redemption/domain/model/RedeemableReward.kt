package com.aashish.writetime.redemption.domain.model

data class RedeemableReward(
    val id: Long,
    val name: String,
    val cost: Int,
    val isRedeemable: Boolean
)
