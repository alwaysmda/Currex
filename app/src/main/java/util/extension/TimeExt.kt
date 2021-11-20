package util.extension

import java.text.SimpleDateFormat
import java.util.*


/**
 * Symbol  Meaning                Kind         Example
 * D       day in year             Number        189
 * E       day of week             Text          E/EE/EEE:Tue, EEEE:Tuesday, EEEEE:T
 * F       day of week in month    Number        2 (2nd Wed in July)
 * G       era designator          Text          AD
 * H       hour in day (0-23)      Number        0
 * K       hour in am/pm (0-11)    Number        0
 * L       stand-alone month       Text          L:1 LL:01 LLL:Jan LLLL:January LLLLL:J
 * M       month in year           Text          M:1 MM:01 MMM:Jan MMMM:January MMMMM:J
 * S       fractional seconds      Number        978
 * W       week in month           Number        2
 * Z       time zone (RFC 822)     Time Zone     Z/ZZ/ZZZ:-0800 ZZZZ:GMT-08:00 ZZZZZ:-08:00
 * a       am/pm marker            Text          PM
 * c       stand-alone day of week Text          c/cc/ccc:Tue, cccc:Tuesday, ccccc:T
 * d       day in month            Number        10
 * h       hour in am/pm (1-12)    Number        12
 * k       hour in day (1-24)      Number        24
 * m       minute in hour          Number        30
 * s       second in minute        Number        55
 * w       week in year            Number        27
 * G       era designator          Text          AD
 * y       year                    Number        yy:10 y/yyy/yyyy:2010
 * z       time zone               Time Zone     z/zz/zzz:PST zzzz:Pacific Standard
 */
fun convertTimestampToDate(timestamp: Long, dateFormat: String): String {
    // Create a DateFormatter object for displaying date in specified format.
    val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)

    // Create a calendar object that will convert the date and time value in milliseconds to date.
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp
    return formatter.format(calendar.time)
}


fun convertDateToTimestamp(date: String, dateFormat: String = "yyyy/MM/dd hh:mm"): Long {
    val sdf = SimpleDateFormat(dateFormat, Locale.ENGLISH)
    return try {
        sdf.parse(date).time
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}

fun convertTimestampToCalendar(timestamp: Long): Calendar {
    val d = Date(timestamp)
    val c = Calendar.getInstance()
    c.time = d
    return c
}
