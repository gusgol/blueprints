package tech.hiregus.trendingprojects.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter {

    @ToJson
    fun toJson(value: LocalDateTime): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, FORMATTER)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }
}