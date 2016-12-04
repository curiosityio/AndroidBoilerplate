package com.curiosityio.androidboilerplate.util

import android.text.format.DateUtils
import android.text.format.DateUtils.FORMAT_ABBREV_RELATIVE
import java.lang.Long
import java.util.*

open class DateUtil {

    companion object {
        // Returns a string describing 'time' as a time relative to 'now'.
        // In the past: "42 minutes ago". In the future: "In 42 minutes"
        // abbreviated returns "42 min ago"
        fun getHumanReadableDate(date: Date, abbreviated: Boolean = false): String {
            val now = System.currentTimeMillis()

            if (abbreviated) {
                return DateUtils.getRelativeTimeSpanString(date.time, now, DateUtils.DAY_IN_MILLIS, FORMAT_ABBREV_RELATIVE).toString()
            } else {
                return DateUtils.getRelativeTimeSpanString(date.time, now, DateUtils.DAY_IN_MILLIS).toString()
            }
        }

        fun hasDatePassed(date: Date): Boolean {
            val now = Date()
            return now.after(date)
        }

        private class TimeSpan {
            var months: Int = 0
            var weeks: Int = 0
            var days: Int = 0
        }

        // Returns "3 months 4 days" or "2 days" or "today" or null if time has passed.
        fun getHumanReadableMonthsDaysRemaining(futureDate: Date): String {
            val nowDate = Date()

            if (isToday(futureDate)) {
                return "today"
            }
            val v = TimeSpan()
            /* Add months until we go past the target, then go back one. */
            while (calculateOffset(nowDate, v).compareTo(futureDate) <= 0) {
                v.months++
            }
            v.months--

            /* Add days until we go past the target, then go back one. */
            while (calculateOffset(nowDate, v).compareTo(futureDate) <= 0) {
                v.days++
            }

            v.weeks = v.days / 7
            v.days = v.days % 7

            var formattedOutput = ""
            if (v.months > 0) {
                if (v.months == 1) {
                    formattedOutput += (v.months.toString() + " month")
                } else {
                    formattedOutput += (v.months.toString() + " months")
                }
            }
            if (v.weeks > 0) {
                if (!formattedOutput.isEmpty()) {
                    formattedOutput += " "
                }

                if (v.weeks == 1) {
                    formattedOutput += (v.weeks.toString() + " week")
                } else {
                    formattedOutput += (v.weeks.toString() + " weeks")
                }
            }
            if (v.days > 0) {
                if (!formattedOutput.isEmpty()) {
                    formattedOutput += " "
                }

                if (v.days == 1) {
                    formattedOutput += (v.days.toString() + " day")
                } else {
                    formattedOutput += (v.days.toString() + " days")
                }
            }

            return formattedOutput
        }

        private fun calculateOffset(start: Date, offset: TimeSpan): Date {
            val c = GregorianCalendar()
            c.time = start
            c.add(Calendar.MONTH, offset.months)
            c.add(Calendar.DAY_OF_YEAR, offset.days)

            return c.time
        }

        fun isSameDay(date1: Date?, date2: Date?): Boolean {
            if (date1 == null || date2 == null) {
                throw IllegalArgumentException("The dates must not be null")
            }
            val cal1 = Calendar.getInstance()
            cal1.time = date1
            val cal2 = Calendar.getInstance()
            cal2.time = date2
            return isSameDay(cal1, cal2)
        }

        fun isSameDay(cal1: Calendar?, cal2: Calendar?): Boolean {
            if (cal1 == null || cal2 == null) {
                throw IllegalArgumentException("The dates must not be null")
            }
            return cal1.get(Calendar.ERA) === cal2.get(Calendar.ERA) &&
                    cal1.get(Calendar.YEAR) === cal2.get(Calendar.YEAR) &&
                    cal1.get(Calendar.DAY_OF_YEAR) === cal2.get(Calendar.DAY_OF_YEAR)
        }

        fun isToday(date: Date): Boolean {
            return isSameDay(date, Calendar.getInstance().time)
        }

        fun isToday(cal: Calendar): Boolean {
            return isSameDay(cal, Calendar.getInstance())
        }

        fun isBeforeDay(date1: Date?, date2: Date?): Boolean {
            if (date1 == null || date2 == null) {
                throw IllegalArgumentException("The dates must not be null")
            }
            val cal1 = Calendar.getInstance()
            cal1.time = date1
            val cal2 = Calendar.getInstance()
            cal2.time = date2
            return isBeforeDay(cal1, cal2)
        }

        fun isBeforeDay(cal1: Calendar?, cal2: Calendar?): Boolean {
            if (cal1 == null || cal2 == null) {
                throw IllegalArgumentException("The dates must not be null")
            }
            if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true
            if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false
            if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true
            if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false
            return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)
        }

        fun isAfterDay(date1: Date?, date2: Date?): Boolean {
            if (date1 == null || date2 == null) {
                throw IllegalArgumentException("The dates must not be null")
            }
            val cal1 = Calendar.getInstance()
            cal1.time = date1
            val cal2 = Calendar.getInstance()
            cal2.time = date2
            return isAfterDay(cal1, cal2)
        }

        fun isAfterDay(cal1: Calendar?, cal2: Calendar?): Boolean {
            if (cal1 == null || cal2 == null) {
                throw IllegalArgumentException("The dates must not be null")
            }
            if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false
            if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true
            if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false
            if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true
            return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)
        }

        fun isWithinDaysFuture(date: Date?, days: Int): Boolean {
            if (date == null) {
                throw IllegalArgumentException("The date must not be null")
            }
            val cal = Calendar.getInstance()
            cal.time = date
            return isWithinDaysFuture(cal, days)
        }

        fun isWithinDaysFuture(cal: Calendar?, days: Int): Boolean {
            if (cal == null) {
                throw IllegalArgumentException("The date must not be null")
            }
            val today = Calendar.getInstance()
            val future = Calendar.getInstance()
            future.add(Calendar.DAY_OF_YEAR, days)
            return isAfterDay(cal, today) && !isAfterDay(cal, future)
        }

        fun getStart(date: Date?): Date? {
            return clearTime(date)
        }

        fun clearTime(date: Date?): Date? {
            if (date == null) {
                return null
            }
            val c = Calendar.getInstance()
            c.time = date
            c.set(Calendar.HOUR_OF_DAY, 0)
            c.set(Calendar.MINUTE, 0)
            c.set(Calendar.SECOND, 0)
            c.set(Calendar.MILLISECOND, 0)
            return c.time
        }

        fun hasAnyTimeValues(date: Date?): Boolean {
            if (date == null) {
                return false
            }
            val c = Calendar.getInstance()
            c.time = date
            if (c.get(Calendar.HOUR_OF_DAY) > 0) return true
            if (c.get(Calendar.MINUTE) > 0) return true
            if (c.get(Calendar.SECOND) > 0) return true
            if (c.get(Calendar.MILLISECOND) > 0) return true
            return false
        }

        fun getEndOfDayTime(date: Date?): Date? {
            if (date == null) {
                return null
            }
            val c = Calendar.getInstance()
            c.time = date
            c.set(Calendar.HOUR_OF_DAY, 23)
            c.set(Calendar.MINUTE, 59)
            c.set(Calendar.SECOND, 59)
            c.set(Calendar.MILLISECOND, 999)
            return c.time
        }

        fun max(d1: Date?, d2: Date?): Date? {
            if (d1 == null && d2 == null) return null
            if (d1 == null) return d2
            if (d2 == null) return d1
            return if (d1.after(d2)) d1 else d2
        }

        fun min(d1: Date?, d2: Date?): Date? {
            if (d1 == null && d2 == null) return null
            if (d1 == null) return d2
            if (d2 == null) return d1
            return if (d1.before(d2)) d1 else d2
        }
    }

}