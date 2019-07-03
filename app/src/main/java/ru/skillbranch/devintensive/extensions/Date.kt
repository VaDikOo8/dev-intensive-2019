package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

const val SECOND = 1000L
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.add(value: Int, units: TimeUnits = TimeUnits.SECOND): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> value * SECOND
        TimeUnits.MINUTE -> value * MINUTE
        TimeUnits.HOUR -> value * HOUR
        TimeUnits.DAY -> value * DAY
    }
    this.time = time
    return this
}

fun Date.humanizeDiff(): String {
    var date = this.time
    val currentDate = Date().time
    var diffDate = currentDate.minus(date)
    if (diffDate > 0) {
        return when (diffDate) {
            in 0L until SECOND -> "только что"
            in SECOND until 45 * SECOND -> "несколько секунд назад"
            in 45 * SECOND until 75 * SECOND -> "минуту назад"
            in 75 * SECOND until 45 * MINUTE ->
                "${diffDate.div(MINUTE)} ${when (diffDate.div(MINUTE).toInt()) {
                    1, 21, 31, 41 -> "минуту"
                    in 2..4, in 22..24, in 32..34 -> "минуты"
                    else -> "минут"
                }} назад"
            in 45 * MINUTE until 75 * MINUTE -> "час назад"
            in 75 * MINUTE until 22 * HOUR ->
                "${diffDate.div(HOUR)} " +
                        "${when (diffDate.div(HOUR).toInt()) {
                            1, 21 -> "час"
                            in 2..4 -> "часа"
                            else -> "часов"
                        }} назад"
            in 22 * HOUR until 26 * HOUR -> "день назад"
            in 26 * HOUR until 360 * DAY ->
                "${diffDate.div(DAY)} " +
                        "${when (diffDate.div(DAY).toString().substring(diffDate.div(DAY).toString().length - 1)) {
                            "1" -> if (diffDate.div(DAY).toString().length > 1 &&
                                diffDate.div(DAY).toString().substring(diffDate.div(DAY).toString().length - 2) == "11"
                            ) {
                                "дней"
                            } else {
                                "день"
                            }
                            "2", "3", "4" -> if (diffDate.div(DAY).toString().length > 1 &&
                                diffDate.div(DAY).toString().substring(
                                    diffDate.div(DAY).toString().length - 2,
                                    diffDate.div(DAY).toString().length - 1
                                ) == "1"
                            ) {
                                "дней"
                            } else {
                                "дня"
                            }
                            else -> "дней"
                        }} назад"
            else -> "более года назад"
        }
    } else {
        diffDate = abs(diffDate)
        diffDate += SECOND
        return when (diffDate) {
            in 0L until SECOND -> "только что"
            in SECOND until 45 * SECOND -> "через несколько секунд"
            in 45 * SECOND until 75 * SECOND -> "через минуту"
            in 75 * SECOND until 45 * MINUTE -> "через ${diffDate.div(MINUTE)} " +
                    when (diffDate.div(MINUTE).toInt()) {
                        1, 21, 31, 41 -> "минуту"
                        in 2..4, in 22..24, in 32..34 -> "минуты"
                        else -> "минут"
                    }
            in 45 * MINUTE until 75 * MINUTE -> "через час"
            in 75 * MINUTE until 22 * HOUR -> "через ${diffDate.div(HOUR)} " +
                    when (diffDate.div(HOUR).toInt()) {
                        1, 21 -> "час"
                        in 2..4 -> "часа"
                        else -> "часов"
                    }
            in 22 * HOUR until 26 * HOUR -> "через день"
            in 26 * HOUR until 360 * DAY -> "через ${diffDate.div(DAY)} " +
                    when (diffDate.div(DAY).toString().substring(diffDate.div(DAY).toString().length - 1)) {
                        "1" -> if (diffDate.div(DAY).toString().length > 1 &&
                            diffDate.div(DAY).toString().substring(diffDate.div(DAY).toString().length - 2) == "11"
                        ) {
                            "дней"
                        } else {
                            "день"
                        }
                        "2", "3", "4" -> if (diffDate.div(DAY).toString().length > 1 &&
                            diffDate.div(DAY).toString().substring(
                                diffDate.div(DAY).toString().length - 2,
                                diffDate.div(DAY).toString().length - 1
                            ) == "1"
                        ) {
                            "дней"
                        } else {
                            "дня"
                        }
                        else -> "дней"
                    }
            else -> "более чем через год"
        }
    }
}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}