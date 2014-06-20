package ac.il.technion.twc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.TimeZone;

public class TestUtils {

	public static Date newGMTDate(int year, int month, int day, int hour,
			int minute) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+0"));
		cal.set(year, month, day, hour, minute);
		return cal.getTime();

	}

	private static final double RETWEETS_PORTION = 0.33;
	private static final Random RANDOM_GEN = new Random();
	public static final int SMALL_SAMPLE_LINES = 100;
	public static final int MEDIUM_SAMPLE_LINES = 10000;
	public static final int LARGE_SAMPLE_LINES = 1000000;

	public static String[] generateTweets(final int n) {
		final String[] tweets = new String[n];

		tweets[0] = PostFactory.DATE_FORMATTER.format(new Date(0)) + ", 0";

		for (int i = 1; i < n; ++i) {
			tweets[i] = PostFactory.DATE_FORMATTER.format(new Date(i)) + ", "
					+ Integer.toString(i);

			if (RANDOM_GEN.nextFloat() < RETWEETS_PORTION)
				tweets[i] += ", " + RANDOM_GEN.nextInt(i);
		}
		return tweets;
	}

	public static final String[] parseFile(final String path)
			throws FileNotFoundException {

		String token1 = "";

		final File f = new File(path);
		final Scanner inFile1 = new Scanner(f);
		inFile1.useDelimiter("\n");

		final List<String> temps = new LinkedList<String>();

		while (inFile1.hasNext()) {
			token1 = inFile1.next();
			temps.add(token1);
		}
		inFile1.close();

		final String[] tempsArray = temps.toArray(new String[0]);

		return tempsArray;
	}

}