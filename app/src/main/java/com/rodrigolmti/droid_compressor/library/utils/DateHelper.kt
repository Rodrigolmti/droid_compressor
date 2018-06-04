package com.rodrigolmti.droid_compressor.library.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    private val locale = Locale("pt", "BR")
    var full_date = "dd/MM/yyyy HH:mm"

    fun dateToString(date: Date?, pattern: String): String {
        return try {

            val sdf = SimpleDateFormat(pattern, locale)
            sdf.format(date)

        } catch (error: Exception) {
            error.printStackTrace()
            ""
        }
    }

    fun stringToDate(date: String, pattern: String): Date? {
        return try {

            val fmt = SimpleDateFormat(pattern, locale)
            fmt.parse(date)

        } catch (error: ParseException) {
            error.printStackTrace()
            null
        }
    }
}