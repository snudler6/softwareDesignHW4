package ac.il.technion.twc.endToEndTests;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.oldFuntionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class TwitterEndToEndGetHashtagPopularityTests
{
	oldFuntionalityTester target;

	private Date fromUTC(int year, int month, int date, int hrs, int min, int sec)
	{
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.set(year, month, date, hrs, min, sec);
		return calendar.getTime();
	}

	public void importJson(String date, String id) throws Exception
	{
		importJson(date, id, null);
	}

	public void importJson(String date, String id, String tweetedId) throws Exception
	{
		importJson(date, id, tweetedId, null);
	}

	public void importJson(String date, String id, String tweetedId, String text) throws Exception
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		importJson(dateFormat.parse(date), id, tweetedId, text);
	}

	public void importJson(Date date, String id) throws Exception
	{
		importJson(date, id, null);
	}

	public void importJson(Date date, String id, String tweetedId) throws Exception
	{
		importJson(date, id, tweetedId, null);
	}

	public void importJson(Date date, String id, String tweetedId, String text) throws Exception
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);

		final String JSON_CREATED_AT = "created_at";
		final String JSON_ID = "id_str";
		final String JSON_TEXT = "text";
		final String JSON_TWEETED_TWEET = "retweeted_status";

		JSONObject jsonObject = new JSONObject();

		jsonObject.put(JSON_CREATED_AT, dateFormat.format(date));
		jsonObject.put(JSON_ID, id);
		jsonObject.put(JSON_TEXT, text);
		if (tweetedId != null)
		{
			JSONObject jsonObjectTweeted = new JSONObject();
			jsonObjectTweeted.put(JSON_ID, tweetedId);
			jsonObject.put(JSON_TWEETED_TWEET, jsonObjectTweeted);
		}

		String line = jsonObject.toString();

		String[] lines = new String[] { line };
		target.importDataJson(lines);
		target.setupIndex();
	}

	@Before
	public void setup() throws Exception
	{
		IDataManager repositoryDataManager = new FileDataManager("./src/test/resources/repositoryDataFile");
		IDataManager indexDataManager = new FileDataManager("./src/test/resources/indexDataFile");
		this.target = new oldFuntionalityTester(repositoryDataManager, indexDataManager);
		this.target.cleanPersistentData();
	}

	@Test
	public void getHashtagPopularity_lastRetweetAddedFirst_shouldReturnTheDiffBetweenLastRetweetAndRootTweet() throws Exception
	{
		importJson(fromUTC(2014, 04, 04, 12, 0, 2), "C", "B");
		importJson(fromUTC(2014, 04, 04, 12, 0, 1), "B");
		importJson(fromUTC(2014, 04, 04, 12, 0, 0), "A", null, "#hashtag1");
		assertEquals("0", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_lastRetweetAddedLast_shouldReturnTheDiffBetweenLastRetweetAndRootTweet() throws Exception
	{
		importJson("04/04/2014 12:00:01", "B", "A");
		importJson("04/04/2014 12:00:02", "C", "B");
		importJson("04/04/2014 12:00:00", "A", null, "#hashtag1");
		assertEquals("2", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_Complicated_1() throws Exception
	{
		importJson("04/04/2014 12:00:00", "A", null, "#hashtag1");
		assertEquals("0", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:00:01", "B", "A");
		assertEquals("1", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:00:04", "C", "B");
		assertEquals("2", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:00:03", "D", "B");
		assertEquals("3", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:00:05", "E", "D");
		assertEquals("4", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_Complicated_1_DifferentOrder() throws Exception
	{
		importJson("04/04/2014 12:00:05", "E", "D");
		importJson("04/04/2014 12:00:03", "D", "B");
		importJson("04/04/2014 12:00:04", "C", "B");
		importJson("04/04/2014 12:00:01", "B", "A");
		importJson("04/04/2014 12:00:00", "A", null, "#hashtag1");
		assertEquals("4", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_Complicated_2() throws Exception
	{
		importJson("04/04/2014 12:00:00", "A", null, "#hashtag1");
		importJson("04/04/2014 12:00:05", "E", "D");
		importJson("04/04/2014 12:00:03", "D", "C");
		importJson("04/04/2014 12:00:04", "C", "B");
		importJson("04/04/2014 12:00:01", "B", "A");
		assertEquals("4", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:00:06", "F", "A");
		assertEquals("5", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:00:07", "G", "C");
		assertEquals("6", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_Complicated_4() throws Exception
	{
		importJson("04/04/2014 12:00:06", "A", "C");
		importJson("04/04/2014 12:00:05", "B", "D");
		importJson("04/04/2014 12:00:04", "H");
		importJson("04/04/2014 12:00:01", "F", "G");
		importJson("04/04/2014 12:00:00", "G", null, "#hashtag1");
		assertEquals("1", target.getHashtagPopularity("hashtag1"));

		importJson("04/04/2014 12:00:04", "C", "E");
		assertEquals("1", target.getHashtagPopularity("hashtag1"));

		importJson("04/04/2014 12:00:02", "E", "F");
		assertEquals("4", target.getHashtagPopularity("hashtag1"));

		importJson("04/04/2014 12:00:03", "D", "E");
		assertEquals("6", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_Complicated_5() throws Exception
	{
		importJson("04/04/2014 12:01:06", "A", "E");
		importJson("04/04/2014 12:01:05", "B", "E");
		importJson("04/04/2014 12:01:05", "C", "E");
		importJson("04/04/2014 12:01:05", "D", "E");

		importJson("04/04/2014 12:01:00", "E", "F");

		importJson("04/04/2014 12:00:59", "F", "G");
		importJson("04/04/2014 12:00:58", "G", "H");
		importJson("04/04/2014 12:00:57", "H", null, "#hashtag1");

		assertEquals("7", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity_Complicated_6() throws Exception
	{
		importJson("04/04/2014 12:01:06", "A", "E");
		importJson("04/04/2014 12:01:05", "B", "E");
		importJson("04/04/2014 12:01:05", "C", "E");
		importJson("04/04/2014 12:01:05", "D", "E");
		importJson("04/04/2014 12:01:00", "E", "F");
		importJson("04/04/2014 12:00:59", "F", "G");
		importJson("04/04/2014 12:00:58", "G", "H");
		importJson("04/04/2014 12:00:57", "H", null, "#hashtag1");
		assertEquals("7", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:01:07", "A1", "A");
		assertEquals("8", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:01:08", "B1", "B");
		assertEquals("9", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:01:08", "C1", "C");
		assertEquals("10", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:01:09", "B2", "B1");
		assertEquals("11", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:01:10", "A2", "A1");
		assertEquals("12", target.getHashtagPopularity("hashtag1"));
		importJson("04/04/2014 12:01:20", "D1", "D");
		assertEquals("13", target.getHashtagPopularity("hashtag1"));
	}

	@Test
	public void getHashtagPopularity2_lastRetweetAddedLast_shouldReturnTheDiffBetweenLastRetweetAndRootTweet() throws Exception
	{
		importJson("04/04/2014 12:00:01", "B", "A", "i have seen 3, but most of them had #yellow_hats");
		importJson("04/04/2014 12:00:02", "C", "B", "i have never seen a #hashtag1 monster");
		importJson("04/04/2014 12:00:00", "A", null, "#hashtag1 and then the monster said #hashtag2,but he hated the monster.");
		assertEquals("2", target.getHashtagPopularity("hashtag1"));
		assertEquals("2", target.getHashtagPopularity("hashtag2"));
		assertEquals("0", target.getHashtagPopularity("yellow_hats"));
		importJson("04/04/2014 12:00:02", "D", null, "the #hashtag1 monster is attacking me!! #fake_help");
		importJson("04/04/2014 12:00:02", "E", null, "i am the monster. does anybody need some #fake_help?");
		assertEquals("2", target.getHashtagPopularity("hashtag1"));
		assertEquals("0", target.getHashtagPopularity("fake_help"));
		importJson("04/04/2014 12:00:02", "F", "D", "#fire_dept says: #fake_help is on the way");
		assertEquals("3", target.getHashtagPopularity("hashtag1"));
		assertEquals("1", target.getHashtagPopularity("fake_help"));
	}

	@Test
	public void getHashtagPopularity3() throws Exception
	{
		importJson("04/04/2014 12:01:06", "A", "E");
		importJson("04/04/2014 12:01:05", "B", "E");
		importJson("04/04/2014 12:01:05", "C", "E", "what???");
		importJson("04/04/2014 12:01:05", "D", "E");
		importJson("04/04/2014 12:01:00", "E", "F");
		importJson("04/04/2014 12:00:59", "F", "G");
		importJson("04/04/2014 12:00:58", "G", "H", "#cant");
		importJson("04/04/2014 12:00:57", "H", null, "my pants caught fire! could anybody give me an #overall instead?");
		assertEquals("7", target.getHashtagPopularity("overall"));

		importJson("04/04/2010 12:01:06", "S", null, "NO WAY! just got my new #overall!!");
		assertEquals("7", target.getHashtagPopularity("overall"));
		importJson("04/04/2011 12:01:06", "T", "S", "NO WAY! just got an #overall too! best farm ever!!");
		assertEquals("8", target.getHashtagPopularity("overall"));

		importJson("04/04/2014 12:01:07", "A1", "A");
		assertEquals("9", target.getHashtagPopularity("overall"));
		importJson("04/04/2014 12:01:08", "B1", "B");
		assertEquals("10", target.getHashtagPopularity("overall"));

		importJson("05/04/2010 12:01:06", "M", null, "just occured to me. you must save your #overall. it might be usefull in some years...");
		assertEquals("10", target.getHashtagPopularity("overall"));
		importJson("05/04/2011 12:01:07", "N", "M", "you're right.");
		assertEquals("11", target.getHashtagPopularity("overall"));

		importJson("04/04/2014 12:01:08", "C1", "C");
		assertEquals("12", target.getHashtagPopularity("overall"));
		importJson("04/04/2014 12:01:09", "B2", "B1");
		assertEquals("13", target.getHashtagPopularity("overall"));

		importJson("04/04/2014 12:01:08", "Enc1", "EncOrig", "#cant say");
		assertEquals("0", target.getHashtagPopularity("cant"));
		importJson("04/04/2014 12:01:07", "EncOrig", null, "#cant you??????");
		assertEquals("1", target.getHashtagPopularity("cant"));

		importJson("04/04/2014 12:01:10", "A2", "A1");
		assertEquals("14", target.getHashtagPopularity("overall"));
		importJson("04/04/2014 12:01:20", "D1", "D");
		assertEquals("15", target.getHashtagPopularity("overall"));
	}
}
