package com.schnarbiesnmeowers.interview.utilities;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author Dylan I. Kessler
 *
 */
public class Randomizer {

	public static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final Logger applicationLogger = LogManager.getLogger("FileAppender");
	private static Random rand = new Random();

	/**
	 * generate a random string
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {
		int letterslength = LETTERS.length();
		StringBuilder newword = new StringBuilder();
		for(int i = 0; i < length; i++) {
			char letter = LETTERS.charAt(rand.nextInt(letterslength));
			newword.append(letter);
		}
		return newword.toString();
	}

	/**
	 * generate a random int
	 * @param max
	 * @return
	 */
	public static int randomInt(int max) {
		int leftLimit = 0;
	    int rightLimit = max;
	    return leftLimit + (int) (rand.nextFloat() * (rightLimit - leftLimit));
	}

	/**
	 * generate a random long
	 * @param max
	 * @return
	 */
	public static long randomLong(long max) {
		long leftLimit = 0L;
	    long rightLimit = max;
	    return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
	}

	/**
	 * generate a random float
	 * @param max
	 * @return
	 */
	public static float randomFloat(float max) {
		float leftLimit = 0F;
	    float rightLimit = max;
	    return leftLimit + rand.nextFloat() * (rightLimit - leftLimit);
	}

	/**
	 * generate a random double
	 * @param max
	 * @return
	 */
	public static double randomDouble(double max) {
		double leftLimit = 1D;
	    double rightLimit = max;
	    return leftLimit + rand.nextDouble() * (rightLimit - leftLimit);
	}

	/**
	 * generate a random BigDecimal
	 * @param maximum
	 * @return
	 */
	public static BigDecimal randomBigDecimal(String maximum) {
		BigDecimal max = new BigDecimal(maximum + ".0");
        BigDecimal randFromDouble = BigDecimal.valueOf(Math.random());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        return actualRandomDec;
	}

	/**
	 * generate a random BigInteger
	 * @param maximum
	 * @return
	 */
	public static BigInteger randomBigInteger(String maximum) {
		BigDecimal max = new BigDecimal(maximum + ".0");
        BigDecimal randFromDouble = BigDecimal.valueOf(Math.random());
        BigDecimal actualRandomDec = randFromDouble.multiply(max);
        return actualRandomDec.toBigInteger();
	}

	/**
	 * generate a random Date
	 * @return
	 */
	public static Date randomDate() {
		return new Date();
	}

    /**
	 * generate a random boolean
	 * @return
	 */
	public static boolean randomBoolean() {
		return true;
	}

	/**
	 * generate a random Timestamp
	 * @return
	 */
	public static Timestamp randomTimestamp(int input) {
		return new Timestamp(randomLong(input));
	}

	/**
	 * generate a random Time
	 * @return
	 */
	public static Time randomTime(int input) {
		return new Time(randomLong(input));
	}

	/**
	 * generate a random byte[] array
	 * @param length
	 * @return
	 */
	public static byte[] randomBytes(int length) {
		return randomString(length).getBytes();
	}
	
	private static void logAction(String message) {
    	System.out.println(message);
    	applicationLogger.debug(message);
    }
}
