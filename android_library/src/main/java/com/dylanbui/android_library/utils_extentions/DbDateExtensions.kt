package com.dylanbui.android_library.utils_extentions

import java.text.SimpleDateFormat
import java.util.*

/* Base on : v0.9.0 https://github.com/hotchemi/khronos

=> Add durations to date

val today = DbDate.today
val nextWeek = today + 1.week
val dayBeforeYesterday = today - 2.days

// shortcuts #1
val tomorrow = DbDate.tomorrow
val yesterday = DbDate.yesterday

// shortcuts #2
val yesterday = 1.days.ago
val fiveYearsSince = 5.years.since

=> Initialize by specifying date components

val birthday = DbDate.of(year = 1990, month = 1, day = 21)
val firstCommitDate = DbDate.of(year = 2016, month = 2, day = 26, hour = 18, minute = 58, second = 31, millisecond = 777)

=> Initialize by changing date components

val today = DbDate.today
val christmas = today.with(month = 12, day = 25)
val thisSunday = today.with(weekday = 1)

// shortcuts
val newYearDay = today.beginningOfYear
val newYearsEve = today.endOfYear

=> Check day of the week

DbDate.today.isFriday() // false

=> Format and parse

5.minutes.since.toString("yyyy-MM-dd HH:mm:ss")
    //=> "2015-03-01 12:05:00"

"1987-06-02".toDate("yyyy-MM-dd")
    //=> DbDate.of(year = 1987, month = 6, day = 2)

=> Compare dates

1.day.ago > 2.days.ago // true
1.day.ago in 2.days.ago..DbDate.today // true


* */


/*
 * File DateExtensions.kt
 */

internal val calendar: Calendar by lazy {
    Calendar.getInstance()
}

operator fun Date.plus(duration: Duration): Date {
    calendar.time = this
    calendar.add(duration.unit, duration.value)
    return calendar.time
}

operator fun Date.minus(duration: Duration): Date {
    calendar.time = this
    calendar.add(duration.unit, -duration.value)
    return calendar.time
}

operator fun Date.rangeTo(other: Date) = DateRange(this, other)

fun Date.with(year: Int = -1, month: Int = -1, day: Int = -1, hour: Int = -1, minute: Int = -1, second: Int = -1, millisecond: Int = -1): Date {
    calendar.time = this
    if (year > -1) calendar.set(Calendar.YEAR, year)
    if (month > 0) calendar.set(Calendar.MONTH, month - 1)
    if (day > 0) calendar.set(Calendar.DATE, day)
    if (hour > -1) calendar.set(Calendar.HOUR_OF_DAY, hour)
    if (minute > -1) calendar.set(Calendar.MINUTE, minute)
    if (second > -1) calendar.set(Calendar.SECOND, second)
    if (millisecond > -1) calendar.set(Calendar.MILLISECOND, millisecond)
    return calendar.time
}

fun Date.with(weekday: Int = -1): Date {
    calendar.time = this
    if (weekday > -1) calendar.set(Calendar.WEEK_OF_MONTH, weekday)
    return calendar.time
}

val Date.beginningOfYear: Date
    get() = with(month = 1, day = 1, hour = 0, minute = 0, second = 0, millisecond = 0)

val Date.endOfYear: Date
    get() = with(month = 12, day = 31, hour = 23, minute = 59, second = 59, millisecond = 999)

val Date.beginningOfMonth: Date
    get() = with(day = 1, hour = 0, minute = 0, second = 0, millisecond = 0)

val Date.endOfMonth: Date
    get() = endOfMonth()

fun Date.endOfMonth(): Date {
    calendar.time = this
    val lastDay = calendar.getActualMaximum(Calendar.DATE)
    return with(day = lastDay, hour = 23, minute = 59, second = 59, millisecond = 999)
}

val Date.beginningOfDay: Date
    get() = with(hour = 0, minute = 0, second = 0, millisecond = 0)

val Date.endOfDay: Date
    get() = with(hour = 23, minute = 59, second = 59, millisecond = 999)

val Date.beginningOfHour: Date
    get() = with(minute = 0, second = 0, millisecond = 0)

val Date.endOfHour: Date
    get() = with(minute = 59, second = 59, millisecond = 999)

val Date.beginningOfMinute: Date
    get() = with(second = 0, millisecond = 0)

val Date.endOfMinute: Date
    get() = with(second = 59, millisecond = 999)

fun Date.toString(format: String): String = SimpleDateFormat(format).format(this)

fun Date.isSunday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
}

fun Date.isMonday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
}

fun Date.isTuesday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY
}

fun Date.isWednesday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY
}

fun Date.isThursday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY
}

fun Date.isFriday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY
}

fun Date.isSaturday(): Boolean {
    calendar.time = this
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
}

/**Creates a Duration from this Date to the passed in one, precise to a second.*/
fun Date.to(other: Date): Duration {
    val difference = other.time - time
    return Duration(Calendar.SECOND, (difference / 1000).toInt())
}


/*
* File Duration.kt
* */


class Duration(internal val unit: Int, internal val value: Int) {
    val ago = calculate(from = Date(), value = -value)

    val since = calculate(from = Date(), value = value)

    private fun calculate(from: Date, value: Int): Date {
        calendar.time = from
        calendar.add(unit, value)
        return calendar.time
    }

    override fun hashCode() = Objects.hashCode(unit) * Objects.hashCode(value)

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is Duration) {
            return false
        }
        return unit == other.unit && value == other.value
    }
}

/*
* File Dates.kt
* */

/**
 * NOTE: In Kotlin you can't add companion object extensions to existing Java class.
 */
object DbDate {
    val today = Date()

    val tomorrow = setDate(value = 1)

    val yesterday = setDate(value = -1)

    private fun setDate(value: Int): Date {
        calendar.time = Date()
        calendar.add(Calendar.DATE, value)
        return calendar.time
    }

    fun of(year: Int = -1, month: Int = -1, day: Int = -1, hour: Int = -1, minute: Int = -1, second: Int = -1, millisecond: Int = -1): Date {
        return Date().with(year, month, day, hour, minute, second, millisecond)
    }
}


/*
* File DateRange.kt
* */

/**
 * A range of values of type [Date].
 */
class DateRange(override val start: Date, override val endInclusive: Date): ClosedRange<Date> {
    override fun contains(value: Date) = start < value && value < endInclusive
}

/*
* File IntExtensions.kt
* */


val Int.year: Duration
    get() = Duration(unit = Calendar.YEAR, value = this)

val Int.years: Duration
    get() = year

val Int.month: Duration
    get() = Duration(unit = Calendar.MONTH, value = this)

val Int.months: Duration
    get() = month

val Int.week: Duration
    get() = Duration(unit = Calendar.WEEK_OF_MONTH, value = this)

val Int.weeks: Duration
    get() = week

val Int.day: Duration
    get() = Duration(unit = Calendar.DATE, value = this)

val Int.days: Duration
    get() = day

val Int.hour: Duration
    get() = Duration(unit = Calendar.HOUR_OF_DAY, value = this)

val Int.hours: Duration
    get() = hour

val Int.minute: Duration
    get() = Duration(unit = Calendar.MINUTE, value = this)

val Int.minutes: Duration
    get() = minute

val Int.second: Duration
    get() = Duration(unit = Calendar.SECOND, value = this)

val Int.seconds: Duration
    get() = second

val Int.millisecond: Duration
    get() = Duration(unit = Calendar.MILLISECOND, value = this)

val Int.milliseconds: Duration
    get() = millisecond


/*
* File StringExtensions.kt
* */


fun String.toDate(format: String): Date = SimpleDateFormat(format).parse(this)