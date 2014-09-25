package com.edulix.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.edulix.constants.Dates;

public class DateUtility {

	/**
	 * Sets the date according to the Gregorian Calendar
	 * 
	 * @param dateValues: Integer array containing year, month and day
	 * @param dateFormat: Format in which date is required
	 * @return Date String in the required format
	 */
	public static String getStandardDate(int[] dateValues, String dateFormat) {

		int calenderMonth = dateValues[Dates.MONTH] - 1;
		SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		Calendar reqDate = new GregorianCalendar(dateValues[Dates.YEAR], calenderMonth, dateValues[Dates.DAY]);
		
		return formatter.format(reqDate.getTime());
	}
	
	public static int getNumericMonth(String stringMonth) {
		
		if (stringMonth.contains("jan")) return 1;
		else if (stringMonth.contains("feb")) return 2;
		else if (stringMonth.contains("mar")) return 3;
		else if (stringMonth.contains("apr")) return 4;
		else if (stringMonth.contains("may")) return 5;
		else if (stringMonth.contains("jun")) return 6;
		else if (stringMonth.contains("jul")) return 7;
		else if (stringMonth.contains("aug")) return 8;
		else if (stringMonth.contains("sep")) return 9;
		else if (stringMonth.contains("oct")) return 10;
		else if (stringMonth.contains("nov")) return 11;
		else if (stringMonth.contains("dec")) return 12;
		else return 0;
	}

}
