package com.dms.test;

import java.time.LocalTime;
import java.util.Random;

public class TestUtils {

	public static int generateRandom24Hour() {
		Random random = new Random();
		LocalTime localTime = LocalTime.MIN.plusSeconds(random.nextLong());
		String hour = localTime.toString().split(":")[0];
		String minute = localTime.toString().split(":")[1];
		String timeStr = hour + minute;
		return Integer.parseInt(timeStr);
	}

	public static int addHoursToTime(int time, int hoursToAdd) {

		String timeStr = String.valueOf(time);
		if (timeStr.length() < 4) {
			if (timeStr.length() == 1)
				timeStr = "000" + timeStr;
			else if (timeStr.length() == 2)
				timeStr = "00" + timeStr;
			else if (timeStr.length() == 3)
				timeStr = "0" + timeStr;
		}
		String hourStr = timeStr.substring(0, 2);
		String minuteStr = timeStr.substring(2, 4);
		int newHour = Integer.parseInt(hourStr) + hoursToAdd;
		int newTime = Integer.parseInt(String.valueOf(newHour) + minuteStr);
		if (newTime > 2359) {
			newTime = 2359;
			System.err.println("######### NEW TIME WILL BE 2359 SINCE GIVEN TIME WAS " + timeStr
					+ " AND YOU WANT TO ADD: " + hoursToAdd);
		}
		return newTime;
	}

	public static int generateRandomNumber(int min, int max) {

		// Usually this can be a field rather than a method variable
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static void main(String[] args) {
		int startHour = 1600;
		int endHour = TestUtils.addHoursToTime(startHour, TestUtils.generateRandomNumber(1, 4));
		System.out.println(endHour);

	}

}
