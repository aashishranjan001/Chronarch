package com.aashish.writetime.common.ui

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDurationHhMmSs(seconds: Long): String {
    val hh = "%02d".format(seconds/3600)
    val mm = "%02d".format((seconds % 3600)/60)
    val ss = "%02d".format(seconds % 60)
    return "$hh : $mm : $ss"
}

fun toHumanReadableDurationFormat(seconds: Long): String {
    val hh = seconds/3600
    val mm = (seconds % 3600)/60
    val ss = seconds % 60

    return buildString {
        if (hh != 0L) append("${hh}h ")
        if (mm != 0L) append("${mm}m ")
        if (ss != 0L) append("${ss}s")
    }
}

fun calculatePercent(value: Double, total: Double): Double {
    return value * 100 / total
}

fun Double.toMax1DecimalPlace(): String {
    return when {
        this % 1.0 == 0.0 -> "%.0f".format(this)
        else -> "%.1f".format(this)
    }
}

fun Instant.toReadableLocalTime(): String {
    return atZone(ZoneId.systemDefault())
        .format(
            DateTimeFormatter.ofPattern("dd MMM, h:mm a")
        )
}