package de.atomfrede.forest.alumni.application.wicket.util;

public class StringCheckUtil {

	/**
	 * Checks if a string is correctly set. In particular this methods return
	 * true iff toCheck is not null and the content is not equal to the string
	 * NULL
	 * 
	 * @param toCheck
	 * @return
	 */
	public static boolean isStringSet(String toCheck) {
		return toCheck != null && !toCheck.trim().equals("NULL");
	}
}
