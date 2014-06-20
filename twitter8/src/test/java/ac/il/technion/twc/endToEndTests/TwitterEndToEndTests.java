package ac.il.technion.twc.endToEndTests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.oldFuntionalityTester;
import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.interfaces.IDataManager;

public class TwitterEndToEndTests
{
	oldFuntionalityTester target;

	public void importLine(String line) throws Exception
	{
		String[] lines = new String[] { line };
		target.importData(lines);
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
	public void courseStuffProvidedTest() throws Exception
	{
		String[] lines = new String[] { "04/04/2014 12:00:00, iddqd", "05/04/2014 12:00:00, idkfa, iddqd" };
		target.importData(lines);
		lines = new String[] { "06/04/2014 13:00:00, 593393706" };
		target.importData(lines);
		target.setupIndex();
		assertEquals("86400000", target.getLifetimeOfTweets("iddqd"));
		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "1,0", "1,1" }, target.getDailyHistogram());
		lines = new String[] { "06/04/2014 11:00:00, 40624256" };
		target.importData(lines);
		target.setupIndex();
		assertArrayEquals(new String[] { "2,0", "0,0", "0,0", "0,0", "0,0", "1,0", "1,1" }, target.getDailyHistogram());
	}

	@Test
	public void getLifetimeOfTweets_lastRetweetAddedFirst_shouldReturnTheDiffBetweenLastRetweetAndRootTweet() throws Exception
	{
		importLine("04/04/2014 12:00:02, C, B");
		importLine("04/04/2014 12:00:01, B, A");
		importLine("04/04/2014 12:00:00, A");
		assertEquals("2000", target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_lastRetweetAddedLast_shouldReturnTheDiffBetweenLastRetweetAndRootTweet() throws Exception
	{
		importLine("04/04/2014 12:00:01, B, A");
		importLine("04/04/2014 12:00:02, C, B");
		importLine("04/04/2014 12:00:00, A");
		assertEquals("2000", target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_1() throws Exception
	{
		importLine("04/04/2014 12:00:00, A");
		importLine("04/04/2014 12:00:01, B, A");
		importLine("04/04/2014 12:00:04, C, B");
		importLine("04/04/2014 12:00:03, D, B");
		importLine("04/04/2014 12:00:05, E, D");
		assertEquals("5000", target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_1_DifferentOrder() throws Exception
	{
		importLine("04/04/2014 12:00:05, E, D");
		importLine("04/04/2014 12:00:03, D, B");
		importLine("04/04/2014 12:00:04, C, B");
		importLine("04/04/2014 12:00:01, B, A");
		importLine("04/04/2014 12:00:00, A");
		assertEquals("5000", target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_2() throws Exception
	{
		importLine("04/04/2014 12:00:00, A");
		importLine("04/04/2014 12:00:05, E, D");
		importLine("04/04/2014 12:00:03, D, C");
		importLine("04/04/2014 12:00:04, C, B");
		importLine("04/04/2014 12:00:01, B, A");
		assertEquals("5000", target.getLifetimeOfTweets("A"));
		importLine("04/04/2014 12:00:06, F, A");
		assertEquals("6000", target.getLifetimeOfTweets("A"));
		importLine("04/04/2014 12:00:07, G, C");
		assertEquals("7000", target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getDailyHistogram_Complicated() throws Exception
	{
		for (int i = 1; i <= 7; i++)
		{
			importLine("1" + i + "/05/2014 12:00:00, " + i);
			for (int j = 1; j <= (7 - i) * 2; j++)
			{
				int id = i * 23 + j;
				importLine("1" + i + "/05/2014 12:00:00, " + id + "," + i);
			}
		}
		assertArrayEquals(new String[] { "13,12", "11,10", "9,8", "7,6", "5,4", "3,2", "1,0" }, target.getDailyHistogram());
	}

	@Test
	public void getLifetimeOfTweets_Complicated_4() throws Exception
	{
		importLine("04/04/2014 12:00:06, A, C");
		importLine("04/04/2014 12:00:05, B, D");
		importLine("04/04/2014 12:00:04, H");
		importLine("04/04/2014 12:00:01, F, G");
		importLine("04/04/2014 12:00:00, G");
		assertEquals("1000", target.getLifetimeOfTweets("G"));

		importLine("04/04/2014 12:00:04, C, E");
		assertEquals("1000", target.getLifetimeOfTweets("G"));

		importLine("04/04/2014 12:00:02, E, F");
		assertEquals("6000", target.getLifetimeOfTweets("G"));

		importLine("04/04/2014 12:00:03, D, E");
		assertEquals("6000", target.getLifetimeOfTweets("G"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_5() throws Exception
	{
		importLine("04/04/2014 12:01:06, A, E");
		importLine("04/04/2014 12:01:05, B, E");
		importLine("04/04/2014 12:01:05, C, E");
		importLine("04/04/2014 12:01:05, D, E");

		importLine("04/04/2014 12:01:00, E, F");

		importLine("04/04/2014 12:00:59, F, G");
		importLine("04/04/2014 12:00:58, G, H");
		importLine("04/04/2014 12:00:57, H");

		assertEquals("9000", target.getLifetimeOfTweets("H"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_6() throws Exception
	{
		importLine("04/04/2014 12:01:06, A, E");
		importLine("04/04/2014 12:01:05, B, E");
		importLine("04/04/2014 12:01:05, C, E");
		importLine("04/04/2014 12:01:05, D, E");
		importLine("04/04/2014 12:01:00, E, F");
		importLine("04/04/2014 12:00:59, F, G");
		importLine("04/04/2014 12:00:58, G, H");
		importLine("04/04/2014 12:00:57, H");
		assertEquals("9000", target.getLifetimeOfTweets("H"));
		importLine("04/04/2014 12:01:07, A1, A");
		assertEquals("10000", target.getLifetimeOfTweets("H"));
		importLine("04/04/2014 12:01:08, B1, B");
		assertEquals("11000", target.getLifetimeOfTweets("H"));
		importLine("04/04/2014 12:01:08, C1, C");
		assertEquals("11000", target.getLifetimeOfTweets("H"));
		importLine("04/04/2014 12:01:09, B2, B1");
		assertEquals("12000", target.getLifetimeOfTweets("H"));
		importLine("04/04/2014 12:01:10, A2, A1");
		assertEquals("13000", target.getLifetimeOfTweets("H"));
		importLine("04/04/2014 12:01:20, D1, D");
		assertEquals("23000", target.getLifetimeOfTweets("H"));
	}

	@Test
	public void courseStaffSampleTestExtended() throws Exception
	{
		String[] lines = new String[] { "04/04/2014 12:00:00, iddqd", "05/04/2014 12:00:00, idkfa, iddqd" };
		target.importData(lines);
		lines = new String[] { "{\"created_at\":\"Sun April 07 13:00:00 +0000 2013\",\"id\":593393706,\"id_str\":\"593393706\",\"text\":\"Software design is fun! #technion #tdd #yolo\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twittbot.net\\/\\\" rel=\\\"nofollow\\\"\\u003etwittbot.net\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":608956240,\"id_str\":\"608956240\",\"name\":\"Harapan.....\",\"screen_name\":\"Swag\",\"location\":\"You\\u2665\",\"url\":null,\"description\":\"Kamu tau arti cinta sebenarnya? Ga tau? Huftt..Ya, contohnya kecil aja, seperti Aku cinta Kamu\\u2665\",\"protected\":false,\"followers_count\":383,\"friends_count\":0,\"listed_count\":1,\"created_at\":\"Fri Jun 15 09:54:28 +0000 2012\",\"favourites_count\":0,\"utc_offset\":25200,\"time_zone\":\"Bangkok\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":13534,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/765054218\\/2ef17a3420c1a99bbd0f35b00c88c50c.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/765054218\\/2ef17a3420c1a99bbd0f35b00c88c50c.jpeg\",\"profile_background_tile\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/3115283251\\/f936656c7391279551924d6e51704e63_normal.jpeg\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/3115283251\\/f936656c7391279551924d6e51704e63_normal.jpeg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/608956240\\/1358311582\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"000000\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"medium\",\"lang\":\"id\"}" };
		target.importDataJson(lines);
		target.setupIndex();
		assertEquals("86400000", target.getLifetimeOfTweets("iddqd"));
		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "1,0", "1,1" }, target.getDailyHistogram());

		lines = new String[] { "{\"created_at\":\"Sun May 19 10:08:08 +0000 2013\",\"id\":334611146080387074,\"id_str\":\"334611146080387074\",\"text\":\"RT @COMMUNITY_ksa: ::\\n\\u0648\\u0632\\u0627\\u0631\\u0629 \\u0627\\u0644\\u062a\\u0631\\u0628\\u064a\\u0629 \\u0648\\u0627\\u0644\\u062a\\u0639\\u0644\\u064a\\u0645 \\u062a\\u0639\\u062a\\u0645\\u062f \\u0628\\u062f\\u0621 \\u0625\\u062c\\u0627\\u0632\\u0629 \\u0645\\u0639\\u0644\\u0645\\u064a \\u0648\\u0645\\u0639\\u0644\\u0645\\u0627\\u062a \\u0631\\u064a\\u0627\\u0636 \\u0627\\u0644\\u0623\\u0637\\u0641\\u0627\\u0644 \\u0648\\u0627\\u0644\\u0645\\u0631\\u062d\\u0644\\u0629 \\u0627\\u0644\\u0627\\u0628\\u062a\\u062f\\u0627\\u0626\\u064a\\u0629 \\u064a\\u0648\\u0645 \\u0627\\u0644\\u0623\\u0631\\u0628\\u0639\\u0627 \\u0621 26\\/7\\/1434 \\u0647\\u0640 \\u0628\\u062f\\u0644\\u0627\\u2026\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":614401374,\"id_str\":\"614401374\",\"name\":\"Abo saleh\",\"screen_name\":\"m_nbvcc\",\"location\":\"\",\"url\":null,\"description\":null,\"protected\":false,\"followers_count\":2225,\"friends_count\":667,\"listed_count\":1,\"created_at\":\"Thu Jun 21 13:36:38 +0000 2012\",\"favourites_count\":29,\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"verified\":false,\"statuses_count\":14358,\"lang\":\"ar\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/2633823366\\/5208a1c14724690cd374102fe94c3931_normal.jpeg\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/2633823366\\/5208a1c14724690cd374102fe94c3931_normal.jpeg\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Wed May 15 09:42:49 +0000 2013\",\"id\":593393706,\"id_str\":\"593393706\",\"text\":\"::\\n\\u0648\\u0632\\u0627\\u0631\\u0629 \\u0627\\u0644\\u062a\\u0631\\u0628\\u064a\\u0629 \\u0648\\u0627\\u0644\\u062a\\u0639\\u0644\\u064a\\u0645 \\u062a\\u0639\\u062a\\u0645\\u062f \\u0628\\u062f\\u0621 \\u0625\\u062c\\u0627\\u0632\\u0629 \\u0645\\u0639\\u0644\\u0645\\u064a \\u0648\\u0645\\u0639\\u0644\\u0645\\u0627\\u062a \\u0631\\u064a\\u0627\\u0636 \\u0627\\u0644\\u0623\\u0637\\u0641\\u0627\\u0644 \\u0648\\u0627\\u0644\\u0645\\u0631\\u062d\\u0644\\u0629 \\u0627\\u0644\\u0627\\u0628\\u062a\\u062f\\u0627\\u0626\\u064a\\u0629 \\u064a\\u0648\\u0645 \\u0627\\u0644\\u0623\\u0631\\u0628\\u0639\\u0627 \\u0621 26\\/7\\/1434 \\u0647\\u0640 \\u0628\\u062f\\u0644\\u0627\\u064b  \\u0645\\u0646 17\\/8\\/1434 \\u0647\\u0640 .\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":317337788,\"id_str\":\"317337788\",\"name\":\"\\u0623\\u062e\\u0628\\u0627\\u0631\\u0627\\u0644\\u0645\\u062c\\u062a\\u0645\\u0639 \\u0627\\u0644\\u0633\\u0639\\u0648\\u062f\\u064a\",\"screen_name\":\"COMMUNITY_ksa\",\"location\":\"\\u0627\\u0644\\u0645\\u0645\\u0644\\u0643\\u0629 \\u0627\\u0644\\u0639\\u0631\\u0628\\u064a\\u0629 \\u0627\\u0644\\u0633\\u0639\\u0648\\u062f\\u064a\\u0629\",\"url\":null,\"description\":\"\\u0633\\u062a\\u0642\\u0631\\u0623\\u0648\\u0646 \\u0647\\u0646\\u0627 \\u0643\\u0644 \\u0645\\u0627 \\u064a\\u0647\\u0645 \\u0627\\u0644\\u0645\\u062c\\u062a\\u0645\\u0639 \\u0627\\u0644\\u0633\\u0639\\u0648\\u062f\\u064a \\u060c \\u0645\\u0646 \\u062f\\u0627\\u062e\\u0644 \\u0627\\u0644\\u0645\\u0645\\u0644\\u0643\\u0629 \\u0648\\u0645\\u0646 \\u062e\\u0627\\u0631\\u062c\\u0647\\u0627\\n\\u0627\\u0644\\u0645\\u0646\\u062a\\u062f\\u0649\\nhttp:\\/\\/forum.wam-kw.com\\/\\n \\u0644\\u0645\\u0631\\u0627\\u0633\\u0644\\u062a\\u0646\\u0627 : Staidman2003@yahoo.com\",\"protected\":false,\"followers_count\":419296,\"friends_count\":20,\"listed_count\":1432,\"created_at\":\"Tue Jun 14 19:50:22 +0000 2011\",\"favourites_count\":86,\"utc_offset\":-18000,\"time_zone\":\"Quito\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":22035,\"lang\":\"ar\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/1900971989\\/image_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/1900971989\\/image_normal.jpg\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":81,\"favorite_count\":8,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false,\"lang\":\"ar\"},\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[{\"screen_name\":\"COMMUNITY_ksa\",\"name\":\"\\u0623\\u062e\\u0628\\u0627\\u0631\\u0627\\u0644\\u0645\\u062c\\u062a\\u0645\\u0639 \\u0627\\u0644\\u0633\\u0639\\u0648\\u062f\\u064a\",\"id\":317337788,\"id_str\":\"317337788\",\"indices\":[3,17]}]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\"}" };
		target.importDataJson(lines);
		target.setupIndex();
		assertArrayEquals(new String[] { "2,1", "0,0", "0,0", "0,0", "0,0", "1,0", "1,1" }, target.getDailyHistogram());
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "1,0", "1,1" }, target.getTemporalHistogram("04/04/2014 12:00:00", "05/04/2014 12:00:00"));
		assertArrayEquals(new String[] { "2,1", "0,0", "0,0", "0,0", "0,0", "1,0", "1,1" }, target.getTemporalHistogram("07/04/2013 13:00:00", "05/04/2014 12:00:00"));
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, target.getTemporalHistogram("04/04/2014 12:00:00", "04/04/2014 12:00:00"));
		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "1,1" }, target.getTemporalHistogram("05/04/2014 12:00:00", "05/04/2014 12:00:00"));
		assertEquals("1", target.getHashtagPopularity("yolo"));
		assertEquals("0", target.getHashtagPopularity("matam"));
	}


	@Test
	public void getLifetimeOfTweets_lastRetweetAddedFirst_shouldReturnTheDiffBetweenLastRetweetAndRootTweet_2() throws Exception
	{
		String[] lines = new String[] { "04/04/2014 12:00:02, C, B", "04/04/2014 12:00:01, B, A", "04/04/2014 12:00:00, A" };
		this.target.importData(lines);

		assertEquals("2000", this.target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_lastRetweetAddedLast_shouldReturnTheDiffBetweenLastRetweetAndRootTweet_2() throws Exception
	{
		String[] lines = new String[] { "04/04/2014 12:00:01, B, A", "04/04/2014 12:00:02, C, B", "04/04/2014 12:00:00, A" };
		this.target.importData(lines);

		assertEquals("2000", this.target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_10() throws Exception
	{
		String[] lines = new String[] { "04/04/2014 12:00:00, A", "04/04/2014 12:00:01, B, A", "04/04/2014 12:00:04, C, B", "04/04/2014 12:00:03, D, B", "04/04/2014 12:00:05, E, D" };
		this.target.importData(lines);

		assertEquals("5000", this.target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_11_DifferentOrder() throws Exception
	{
		String[] lines = new String[] { "04/04/2014 12:00:05, E, D", "04/04/2014 12:00:03, D, B", "04/04/2014 12:00:04, C, B", "04/04/2014 12:00:01, B, A", "04/04/2014 12:00:00, A" };
		this.target.importData(lines);

		assertEquals("5000", this.target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getLifetimeOfTweets_Complicated_12() throws Exception
	{
		this.target.importData(new String[] { "04/04/2014 12:00:00, A" });

		this.target.importData(new String[] { "04/04/2014 12:00:05, E, D" });

		this.target.importData(new String[] { "04/04/2014 12:00:03, D, C" });

		this.target.importData(new String[] { "04/04/2014 12:00:04, C, B" });

		this.target.importData(new String[] { "04/04/2014 12:00:01, B, A" });
		assertEquals("5000", this.target.getLifetimeOfTweets("A"));

		this.target.importData(new String[] { "04/04/2014 12:00:06, F, A" });
		assertEquals("6000", this.target.getLifetimeOfTweets("A"));

		this.target.importData(new String[] { "04/04/2014 12:00:07, G, C" });
		assertEquals("7000", this.target.getLifetimeOfTweets("A"));
	}

	@Test
	public void getDailyHistogram_Complicated_13() throws Exception
	{
		for (int i = 1; i <= 7; i++)
		{
			this.target.importData(new String[] { "1" + i + "/05/2014 12:00:00, " + i });
			for (int j = 1; j <= (7 - i) * 2; j++)
			{
				int id = i * 23 + j;
				this.target.importData(new String[] { "1" + i + "/05/2014 12:00:00, " + id + "," + i });
			}
		}
		assertArrayEquals(new String[] { "13,12", "11,10", "9,8", "7,6", "5,4", "3,2", "1,0" }, this.target.getDailyHistogram());
	}
}
