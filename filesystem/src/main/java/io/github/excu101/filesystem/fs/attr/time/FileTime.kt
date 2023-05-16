package io.github.excu101.filesystem.fs.attr.time

class FileTime(
    val seconds: Long,
    val nanos: Long,
) {
    companion object {
        const val NanosPerSecond = 1_000_000_000
        const val NanosPerMillis = 1_000_000
        const val MillisPerSecond = 1_000
        const val SecondPerMinute = 60
        const val MinutePerHour = 60
        const val HourPerDay = 24
        const val DayPerMonth = 30.4167
        const val MonthPerYear = 12
    }

    override fun toString(): String {
        if (nanos <= 0L) return ""

        val builder = StringBuilder()

        val millis = nanos / NanosPerMillis
        val seconds = millis / MillisPerSecond
        val minute = seconds / SecondPerMinute
        val hour = minute / MinutePerHour
        val day = hour / HourPerDay
        val month = day / DayPerMonth
        val year = month / MonthPerYear

        if (month < 0) {
            builder.append(month.toLong())
            builder.append("m:")
        }

        if (day < 0) {
            builder.append(day)
            builder.append("d:")
        }

        if (year < 0) {
            builder.append(year.toLong())
            builder.append("y ")
        }

        if (hour < 0) {
            builder.append(hour)
            builder.append("d:")
        }

        if (minute < 0) {
            builder.append(minute)
            builder.append("m:")
        }

        builder.append(seconds)
        builder.append("s")

        return builder.toString()
    }

}