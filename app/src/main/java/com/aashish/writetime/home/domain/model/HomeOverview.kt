package com.aashish.writetime.home.domain.model

import com.aashish.writetime.common.domain.model.EarningOverview

data class HomeOverview(
    val earningOverview: EarningOverview,
    val sessionsOverview: SessionsOverview
)