package com.mid.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Helper functions for handling dates.
 *
 * @author Marco Jakob
 */
public class DateUtil {

    /**
     * The date pattern that is used for conversion. Change as you wish.
     */
    private static final String VIEW_DATE_PATTERN = "dd.MM.yyyy";
    private static final String DB_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * The date formatter.
     */
    private static final DateTimeFormatter VIEW_DATE_FORMATTER
	    = DateTimeFormatter.ofPattern(VIEW_DATE_PATTERN);

    private static final DateTimeFormatter DB_DATE_FORMATTER
	    = DateTimeFormatter.ofPattern(DB_DATE_PATTERN);

    /**
     * Returns the given date as a well formatted String. The above defined
     * {@link DateUtil#VIEW_DATE_PATTERN} is used.
     *
     * @param date the date to be returned as a string
     * @return formatted string
     */
    public static String viewFormat(LocalDate date) {
	if (date == null) {
	    return null;
	}
	return VIEW_DATE_FORMATTER.format(date);
    }

    public static String dbFormat(LocalDate date) {
	if (date == null) {
	    return null;
	}
	return DB_DATE_FORMATTER.format(date);
    }

    /**
     * Converts a String in the format of the defined
     * {@link DateUtil#VIEW_DATE_PATTERN} to a {@link LocalDate} object.
     *
     * Returns null if the String could not be converted.
     *
     * @param dateString the date as String
     * @return the date object or null if it could not be converted
     */
    public static LocalDate viewParse(String dateString) {
	try {
	    return VIEW_DATE_FORMATTER.parse(dateString, LocalDate::from);
	}
	catch (DateTimeParseException e) {
	    return null;
	}
    }

    public static LocalDate dbParse(String dateString) {
	try {
	    return DB_DATE_FORMATTER.parse(dateString, LocalDate::from);
	}
	catch (DateTimeParseException e) {
	    return null;
	}
    }

    /**
     * Checks the String whether it is a valid date.
     *
     * @param dateString
     * @return true if the String is a valid date
     */
    public static boolean validDate(String dateString) {
	// Try to parse the String.
	return DateUtil.viewParse(dateString) != null;
    }
}
