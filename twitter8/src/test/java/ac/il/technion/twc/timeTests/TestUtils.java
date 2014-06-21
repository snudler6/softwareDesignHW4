package ac.il.technion.twc.timeTests;

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

	private static String JSONstringBuilder(String id,boolean isRetweet,String parent_id,String user){
		String json = "{\"created_at\":\"Sun May 19 10:08:08 +0000 2013\",\"text\":\"Java is #bad language to program with\",\"id_str\":\"";
		json = json + id + "\"," + "\"user\":{\"id_str\":\""+user+"\"}";
		if(isRetweet)
			json = json + ",\"retweeted_status\":{\"id_str\":\""+parent_id+"\"}";
		json = json + "}";
		return json;
	}
	
	public static String createJsonTweet(int id, String user, String date, String[] hashtags, boolean isRetweet, int parentId) {
		String myHashtags = "my hashtags are: ";
		for(String hashtag : hashtags)
			myHashtags += hashtag;
		String json =  "{\"created_at\":\"" +date + "\",\"text\":\""+ myHashtags + "\",\"id_str\":\"" + id;
		if(isRetweet)
			json += ",\"retweeted_status\":{\"id_str\":\""+ parentId +"\"}";
		else
			json += "\",\"retweeted_status\":null";
		json += ",\"user\":{\"id_str\":\"" + user  + "\"}}";
		
		return json;
	}
	
	public static String[] generateTweets(final int n) {
		final String[] tweets = new String[n];

		tweets[0] = JSONstringBuilder("0", false, null, "100");

		for (int i = 1; i < n; ++i) {
			if (RANDOM_GEN.nextFloat() < RETWEETS_PORTION)
				tweets[i] = JSONstringBuilder(Integer.toString(i), true, Integer.toString(RANDOM_GEN.nextInt(i)), Integer.toString(i));
			else
				tweets[i] = JSONstringBuilder(Integer.toString(i), false,null, Integer.toString(i));
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