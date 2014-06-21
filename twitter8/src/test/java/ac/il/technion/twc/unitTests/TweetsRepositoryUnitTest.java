package ac.il.technion.twc.unitTests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.dependencies.TweetModule;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TweetsRepositoryUnitTest
{
	ITweetsRepository repository;

	
	private void addToRepository(Tweet tweet)
	{
		List<Tweet> tweets = new ArrayList<Tweet>();
		tweets.add(tweet);
		repository.add(tweets);
	}
	
	@Before
	public void setup()
	{
		Injector injector = Guice.createInjector(new TweetModule());
		repository = injector.getInstance(ITweetsRepository.class);
	}

	@Test
	public void add_rootTweetAsArgument_noExceptions() throws Exception
	{
		Calendar calendar1 = new GregorianCalendar(2014, 4 - 1, 4, 12, 0, 0);
		Tweet tweet1 = new RootTweet(new TweetId("iddqd"), calendar1.getTime(), null, "");

		addToRepository(tweet1);
	}

	@Test(expected = NullPointerException.class)
	public void add_NullArgument_throwsNullPointerException() throws Exception
	{
		addToRepository(null);
	}

	@Test(expected = NullPointerException.class)
	public void getRootTweet_NullArgument_throwsNullPointerException() throws Exception
	{
		repository.getRootTweetById(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRootTweet_retweetIdAsArgument_throwsIllegalArgumentException() throws Exception
	{
		Calendar calendar1 = new GregorianCalendar(2014, 4 - 1, 4, 12, 0, 0);
		Tweet tweet1 = new RootTweet(new TweetId("iddqd"), calendar1.getTime(), null, "");

		Calendar calendar2 = new GregorianCalendar(2014, 4 - 1, 5, 12, 0, 0);
		Tweet tweet2 = new Retweet(new TweetId("idkfa"), calendar2.getTime(), new TweetId("iddqd"), "");

		addToRepository(tweet1);
		addToRepository(tweet2);

		repository.getRootTweetById(new TweetId("idkfa"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void getRootTweet_nonExistingTweetIdAsArgument_throwsIllegalArgumentException() throws Exception
	{
		Calendar calendar1 = new GregorianCalendar(2014, 4 - 1, 4, 12, 0, 0);
		Tweet tweet1 = new RootTweet(new TweetId("iddqd"), calendar1.getTime(), null, "");

		Calendar calendar2 = new GregorianCalendar(2014, 4 - 1, 6, 13, 0, 0);
		Tweet tweet2 = new RootTweet(new TweetId("593393706"), calendar2.getTime(), null, "");

		Calendar calendar3 = new GregorianCalendar(2014, 4 - 1, 6, 11, 0, 0);
		Tweet tweet3 = new RootTweet(new TweetId("40624256"), calendar3.getTime(), null, "");

		Calendar calendar4 = new GregorianCalendar(2014, 4 - 1, 5, 12, 0, 0);
		Tweet tweet4 = new Retweet(new TweetId("idkfa"), calendar4.getTime(), new TweetId("iddqd"), "");

		addToRepository(tweet1);
		addToRepository(tweet4);
		addToRepository(tweet3);
		addToRepository(tweet2);

		repository.getRootTweetById(new TweetId("noSuchId"));
	}

	@Test
	public void getRootTweet_existingRootTweetAsArgument_returnsTheExactRootTweet() throws Exception
	{
		Calendar calendar1 = new GregorianCalendar(2014, 4 - 1, 4, 12, 0, 0);
		Tweet tweet1 = new RootTweet(new TweetId("iddqd"), calendar1.getTime(), null, "");

		Calendar calendar2 = new GregorianCalendar(2014, 4 - 1, 6, 13, 0, 0);
		Tweet tweet2 = new RootTweet(new TweetId("593393706"), calendar2.getTime(), null, "");

		Calendar calendar4 = new GregorianCalendar(2014, 4 - 1, 5, 12, 0, 0);
		Tweet tweet4 = new Retweet(new TweetId("idkfa"), calendar4.getTime(), new TweetId("iddqd"), "");

		addToRepository(tweet1);
		addToRepository(tweet4);
		addToRepository(tweet2);

		Tweet actual = repository.getRootTweetById(new TweetId("593393706"));
		Tweet expected = tweet2;
		assertEquals(expected, actual);
	}
}
