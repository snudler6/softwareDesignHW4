package ac.il.technion.twc.unitTests;

import static org.junit.Assert.assertArrayEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.api.TweetFactory;
import ac.il.technion.twc.api.dependencies.TweetModule;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsTemporalHistogramQueryHandler;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TweetsTemporalHistogramUnitTest
{
	ITweetsTemporalHistogramQueryHandler $;

	@Before
	public void setup()
	{
		Injector injector = Guice.createInjector(new TweetModule());
		$ = injector.getInstance(ITweetsTemporalHistogramQueryHandler.class);
	}

	@Test
	public void getTemporalHistogram_NoTweets_t1DifferentFromt2_ReturnEmptyHistogram() throws Exception
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/1000 10:55:22");
		Date t2 = dateFormat.parse("04/04/3000 10:55:22");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_NoTweets_t1SameAst2_ReturnEmptyHistogram() throws Exception
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2014 10:55:22");
		Date t2 = t1;

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_OneTweet_t1BeforeAndt2After_ReturnHistogramWithTheTweet() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2013 10:55:22");
		Date t2 = dateFormat.parse("04/08/2014 18:50:52");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_OneTweet_t1BeforeAndt2Before_ReturnEmptyHistogram() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2014 10:55:22");
		Date t2 = dateFormat.parse("04/04/2014 11:50:52");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_OneTweet_t1AfterAndt2After_ReturnEmptyHistogram() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("20/04/2014 10:55:22");
		Date t2 = dateFormat.parse("25/04/2019 11:50:52");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_OneTweet_t1SameAst2AndEqualToTheTweetDate_ReturnHistogramWithTheTweet() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2014 12:00:00");
		Date t2 = dateFormat.parse("04/04/2014 12:00:00");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}
	
	@Test
	public void getTemporalHistogram_OneTweet_t1SameAst2AndNotEqualToTheTweetDate_ReturnHistogramWithTheTweet() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2014 13:00:00");
		Date t2 = dateFormat.parse("04/04/2014 13:00:00");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_TwoTweets_t1BeforeAllAndt2AfterAll_ReturnHistogramWithTheTwoTweets() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2013 12:00:00");
		Date t2 = dateFormat.parse("04/04/2016 12:00:00");

		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_TwoTweets_t1BeforeAllAndt2BeforeAll_ReturnEmptyHistogram() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2013 12:00:00");
		Date t2 = dateFormat.parse("04/04/2013 12:00:01");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_TwoTweets_t1BeforeAllAndt2BeforeTheSecond_ReturnHistogramWithTheFirstTweet() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2013 12:00:00");
		Date t2 = dateFormat.parse("05/04/2014 12:00:01");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_TwoTweets_t1BeforeTheSecondAndt2BeforeTheSecond_ReturnEmptyHistogram() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("05/04/2014 12:00:00");
		Date t2 = dateFormat.parse("05/04/2014 13:10:01");

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_TwoTweets_t1BeforeTheSecondAndt2AfterAll_ReturnHistogramWithTheSecondTweet() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("05/04/2014 12:00:00");
		Date t2 = dateFormat.parse("06/04/2014 12:00:00");

		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_TwoTweetsOneOfThemIsRetweet_t1BeforeAllAndt2AfterAll_ReturnHistogramWithTheTwoTweets() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2013 12:00:00");
		Date t2 = dateFormat.parse("04/04/2016 12:00:00");

		assertArrayEquals(new String[] { "1,1", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_ThreeTweets_t1BeforeAllAndt2AfterAll_ReturnHistogramWithTheThreeTweets() throws Exception
	{

		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet friday2 = TweetFactory.buildTweet("04/04/2014 12:00:01, friday2");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(friday2);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("05/04/2011 12:00:00");
		Date t2 = dateFormat.parse("06/04/2017 12:00:00");

		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "2,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_ThreeTweetsWhenTwoOfThemAreRetweets_t1BeforeAllAndt2AfterAll_ReturnHistogramWithTheThreeTweets() throws Exception
	{

		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet friday2 = TweetFactory.buildTweet("04/04/2014 12:00:01, friday2 , friday");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday, friday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(friday2);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("05/04/2011 12:00:00");
		Date t2 = dateFormat.parse("06/04/2017 12:00:00");

		assertArrayEquals(new String[] { "1,1", "0,0", "0,0", "0,0", "0,0", "2,1", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_ThreeTweets_t1Andt2AreTheSecondTweetDate_ReturnHistogramWithTheSecondTweet() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet friday2 = TweetFactory.buildTweet("04/04/2014 12:00:01, friday2");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(friday2);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2014 12:00:01");
		Date t2 = t1;

		assertArrayEquals(new String[] { "0,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}

	@Test
	public void getTemporalHistogram_ThreeTweets_t1BeforeTheSecondAndt2AfterAll_ReturnHistogramWithTheSecondAndTheThirdTweets() throws Exception
	{
		Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
		Tweet friday2 = TweetFactory.buildTweet("04/04/2014 12:00:02, friday2");
		Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");

		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(friday);
		tweets.add(friday2);
		tweets.add(sunday);
		$.onTweetsAdded(tweets, null);

		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		Date t1 = dateFormat.parse("04/04/2014 12:00:01");
		Date t2 = dateFormat.parse("06/04/2017 12:00:00");

		assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0", "1,0", "0,0" }, $.getStrings(t1, t2));
	}

	// @Test
	// public void getTemporalHistogram_Complicated_1() throws Exception
	// {
	// Tweet friday = TweetFactory.buildTweet("04/04/2014 12:00:00, friday");
	// Tweet friday2 = TweetFactory.buildTweet("04/04/2014 12:00:02, friday2");
	// Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");
	// Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");
	// Tweet sunday = TweetFactory.buildTweet("06/04/2014 12:00:00, sunday");
	//
	// List<Tweet> tweets = new LinkedList<Tweet>();
	// tweets.add(friday);
	// tweets.add(friday2);
	// tweets.add(sunday);
	// tweets.add(sunday);
	// tweets.add(sunday);
	// tweets.add(sunday);
	// $.add(tweets);
	//
	// final SimpleDateFormat dateFormat = new
	// SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
	// dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	//
	// Date t1 = dateFormat.parse("04/04/2014 12:00:01");
	// Date t2 = dateFormat.parse("06/04/2017 12:00:00");
	//
	// assertArrayEquals(new String[] { "1,0", "0,0", "0,0", "0,0", "0,0",
	// "1,0", "0,0" }, $.getStrings(t1, t2));
	// }
}
