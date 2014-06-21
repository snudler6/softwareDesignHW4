package ac.il.technion.twc.unitTests;

import static org.junit.Assert.assertArrayEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.models.TweetsDailyHistogram;

public class TweetsDailyHistogramUnitTest
{
	TweetsDailyHistogram tweetsDailyHistogram;

	private List<Tweet> getAsList(Tweet tweet)
	{
		List<Tweet> tweets = new LinkedList<Tweet>();
		tweets.add(tweet);
		return tweets;
	}

	@Before
	public void setup()
	{
		tweetsDailyHistogram = new TweetsDailyHistogram();
	}

	@Test
	public void getStrings_twoRetweetAtTheSameDay() throws Exception
	{
		Calendar calendar1 = new GregorianCalendar(2014, 5 - 1, 11, 12, 0, 0);
		Tweet tweet1 = new Retweet(new TweetId("iddqd"), calendar1.getTime(), new TweetId("123"), "");

		Calendar calendar2 = new GregorianCalendar(2014, 5 - 1, 11, 13, 0, 0);
		Tweet tweet2 = new Retweet(new TweetId("593393706"), calendar2.getTime(), new TweetId("123"), "");

		tweetsDailyHistogram.putAll(getAsList(tweet1));
		tweetsDailyHistogram.putAll(getAsList(tweet2));
		assertArrayEquals(new String[] { "2,2", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" }, tweetsDailyHistogram.getStrings());
	}

	@Test
	public void getStrings_oneRootTweetAndOneRetweetAtTheSameDay() throws Exception
	{
		Calendar calendar1 = new GregorianCalendar(2014, 5 - 1, 12, 12, 0, 0);
		Tweet tweet1 = new RootTweet(new TweetId("iddqd"), calendar1.getTime(),null, "");

		Calendar calendar2 = new GregorianCalendar(2014, 5 - 1, 12, 13, 0, 0);
		Tweet tweet2 = new Retweet(new TweetId("593393706"), calendar2.getTime(), new TweetId("123"), "");

		tweetsDailyHistogram.putAll(getAsList(tweet1));
		tweetsDailyHistogram.putAll(getAsList(tweet2));
		assertArrayEquals(new String[] { "0,0", "2,1", "0,0", "0,0", "0,0", "0,0", "0,0" }, tweetsDailyHistogram.getStrings());
	}

	@Test
	public void getStrings_100TweetsAnd50RetweetsInEveryDay() throws Exception
	{

		Calendar sunday = new GregorianCalendar(2014, 5 - 1, 11, 14, 0, 0);
		Calendar monday = new GregorianCalendar(2014, 5 - 1, 12, 14, 0, 0);
		Calendar tuseday = new GregorianCalendar(2014, 5 - 1, 13, 14, 0, 0);
		Calendar wednesday = new GregorianCalendar(2014, 5 - 1, 14, 14, 0, 0);
		Calendar thursday = new GregorianCalendar(2014, 5 - 1, 15, 14, 0, 0);
		Calendar friday = new GregorianCalendar(2014, 5 - 1, 16, 14, 0, 0);
		Calendar saturday = new GregorianCalendar(2014, 5 - 1, 17, 14, 0, 0);

		Set<Calendar> days = new HashSet<Calendar>();
		days.add(sunday);
		days.add(monday);
		days.add(tuseday);
		days.add(wednesday);
		days.add(thursday);
		days.add(friday);
		days.add(saturday);

		RootTweet rootTweet;
		Retweet retweet;

		for (int i = 0; i < 50; i++)
		{
			for (Calendar day : days)
			{
				rootTweet = new RootTweet(new TweetId("rootTweet" + i), day.getTime(),null, "");
				tweetsDailyHistogram.putAll(getAsList(rootTweet));
			}
		}
		for (int i = 0; i < 50; i++)
		{
			for (Calendar day : days)
			{
				retweet = new Retweet(new TweetId("retweet" + i), day.getTime(), new TweetId("rootTweet" + i), "");
				tweetsDailyHistogram.putAll(getAsList(retweet));
			}
		}

		assertArrayEquals(new String[] { "100,50", "100,50", "100,50", "100,50", "100,50", "100,50", "100,50" }, tweetsDailyHistogram.getStrings());
	}

}
