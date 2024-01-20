package com.asisee.streetpieces.common.ext

import com.asisee.streetpieces.CURRENT_YEAR
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.Locale

fun epochSecondsToPieceDate(epochSeconds: Long) : String {
    val dateTime = Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(TimeZone.currentSystemDefault())
    val month = dateTime.month.toString().lowercase().capitalize(Locale.getDefault());
    val monthAndDay = "$month ${dateTime.dayOfMonth}"
    return if (dateTime.year == CURRENT_YEAR)
        monthAndDay
    else
        "$monthAndDay, ${dateTime.year}"
}