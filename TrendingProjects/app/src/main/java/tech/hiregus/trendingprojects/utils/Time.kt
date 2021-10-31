package tech.hiregus.trendingprojects.utils.extensions


import android.content.Context
import tech.hiregus.trendingprojects.R
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.min

/**
 * Get a formatted String (e.g '2011-12-03T10:15:30+01:00') from today's start of the day
 */
fun getToday(): String = LocalDate.now().atStartOfDay().format(DateTimeFormatter.ISO_DATE_TIME)

/**
 * Get a formatted String (e.g '2011-12-03T10:15:30+01:00') from @param date minus the @param days
 */
fun subtractDays(days: Long, date: LocalDate = LocalDate.now()): String {
    val past = date.atStartOfDay().minusDays(days)
    return past.format(DateTimeFormatter.ISO_DATE_TIME)
}

/**
 * Format a LocalDatTime in a descriptive way (e.g "5 days ago", "just now")
 */
fun LocalDateTime.timeAgo(context: Context): String {
    val currentTime = LocalDateTime.now()
    val period: Period = Period.between(this.toLocalDate(), currentTime.toLocalDate())
    val duration: Duration = Duration.between(this, currentTime)

    val minutes = duration.toMinutes().toInt()
    val hours = duration.toHours().toInt()
    val days = period.days

    return if (days >= 28) {
        context.getString(R.string.a_month_ago)
    } else if (days >= 7) {
        context.resources.getQuantityString(R.plurals.weeks_ago, (days / 7), (days / 7))
    } else if (days >= 1) {
        context.resources.getQuantityString(R.plurals.days_ago, days, days)
    } else if (hours >= 1) {
        context.resources.getQuantityString(R.plurals.hours_ago, hours, hours)
    } else if (hours < 1 && minutes > 5) {
        context.resources.getQuantityString(R.plurals.minutes_ago, minutes, minutes)
    } else {
        context.getString(R.string.just_now)
    }
}
