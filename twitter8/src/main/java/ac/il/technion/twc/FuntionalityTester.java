package ac.il.technion.twc;

import java.text.ParseException;

import ac.il.technion.twc.api.FileDataManager;
import ac.il.technion.twc.api.TweetFactory;
import ac.il.technion.twc.api.dependencies.TweetModule;
import ac.il.technion.twc.api.interfaces.IDataManager;
import ac.il.technion.twc.api.interfaces.ITweetBuilder;
import ac.il.technion.twc.api.models.Tweet;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class is meant to act as a wrapper to test your functionality. You
 * should implement all its methods and not change any of their signatures. You
 * can also implement an argumentless constructor if you wish, and any number of
 * new public methods
 * 
 * @author Gal Lalouche
 */
public class FuntionalityTester
{
	private final Injector injector = Guice.createInjector(new TweetModule());
	private IDataManager repositoryDataManager;
	private IDataManager indexDataManager;
	private TweetsKnowledgeCenter tweetsKnowledgeCenter;

	/**
	 * A default constructor.
	 */
	public FuntionalityTester()
	{
		this.tweetsKnowledgeCenter = injector.getInstance(TweetsKnowledgeCenter.class);
		this.repositoryDataManager = new FileDataManager("./src/main/resources/dataFile");
		this.indexDataManager = new FileDataManager("./src/main/resources/indexFile");
	}

	/**
	 * A constructor used for testing.
	 * 
	 * @param tweetsKnowledgeCenter
	 * @param repositoryDataManager
	 * @param indexDataManager
	 */
	public FuntionalityTester(IDataManager repositoryDataManager, IDataManager indexDataManager)
	{
		this.tweetsKnowledgeCenter = injector.getInstance(TweetsKnowledgeCenter.class);
		this.repositoryDataManager = repositoryDataManager;
		this.indexDataManager = indexDataManager;
	}

	/**
	 * Loads the data from an array of lines
	 * 
	 * @param lines
	 *            An array of lines, each line formatted as <time (dd/MM/yyyy
	 *            HH:mm:ss)>,<tweet id>[,original tweet]
	 * @throws Exception
	 *             If for any reason, handling the data failed
	 */
	public void importData(String[] lines) throws Exception
	{
		importData(lines, new ITweetBuilder()
		{
			@Override
			public Tweet buildTweet(String string) throws ParseException
			{
				return TweetFactory.buildTweet(string);
			}
		});
	}

	/**
	 * Loads the data from an array of lines
	 * 
	 * @param lines
	 *            An array of lines, each line formatted according to the
	 *            builder
	 * @param builder
	 *            a builder to parse the lines
	 * @throws Exception
	 *             If for any reason, handling the data failed
	 */
	private void importData(String[] lines, ITweetBuilder builder) throws Exception
	{
		if (lines == null)
			throw new NullPointerException("lines parameter is null");

		tweetsKnowledgeCenter.importRepository(repositoryDataManager);
		tweetsKnowledgeCenter.addTweetsData(lines, builder);
		tweetsKnowledgeCenter.exportRepository(repositoryDataManager);
		tweetsKnowledgeCenter.exportIndex(indexDataManager);
	}

	/**
	 * Loads the data from an array of JSON lines
	 * 
	 * @param lines
	 *            An array of lines, each line is a JSON string
	 * @throws Exception
	 *             If for any reason, handling the data failed
	 */
	public void importDataJson(String[] lines) throws Exception
	{
		importData(lines, new ITweetBuilder()
		{
			@Override
			public Tweet buildTweet(String string) throws ParseException
			{
				return TweetFactory.buildTweetJson(string);
			}
		});
	}

	/**
	 * Loads the index, allowing for queries on the data that was imported using
	 * {@link TwitterKnowledgeCenter#importData(String[])}. setupIndex will be
	 * called before any queries can be run on the system
	 * 
	 * @throws Exception
	 *             If for any reason, loading the index failed
	 */
	public void setupIndex() throws Exception
	{
		tweetsKnowledgeCenter.importIndex(indexDataManager);
	}

	/**
	 * Gets the lifetime of the tweet, in milliseconds. You may assume we will
	 * ask about the lifetime of a retweet, but only about the lifetime of an
	 * original tweet.
	 * 
	 * @param tweetId
	 *            The tweet's identifier
	 * @return A string, counting the number of milliseconds between the tweet's
	 *         publication and its last retweet (recursive)
	 * @throws Exception
	 *             If it is not possible to complete the operation
	 */
	public String getLifetimeOfTweets(String tweetId) throws Exception
	{
		if (tweetId == null)
			throw new NullPointerException("tweetId parameter is null");
		if (tweetId.isEmpty())
			throw new IllegalArgumentException("tweetId parameter is empty");

		return tweetsKnowledgeCenter.getLifetimeOfTweet(tweetId);
	}

	/**
	 * Gets the weekly histogram of all tweet and retweet data
	 * 
	 * @return An array of strings, each string in the format of
	 *         ("<number of tweets (including retweets), number of retweets only>"
	 *         ), for example: ["100, 10","250,20",...,"587,0"]. The 0th index
	 *         of the array is Sunday.
	 */
	public String[] getDailyHistogram()
	{
		return tweetsKnowledgeCenter.getDailyHistogram();
	}

	/**
	 * Gets the number of (recursive) retweets made to all original tweets made
	 * that contain a specific hashtag
	 * 
	 * @param hashtag
	 *            The hashtag to check
	 * @return A string, in the format of a number, contain the number of
	 *         retweets
	 */
	public String getHashtagPopularity(String hashtag)
	{
		return tweetsKnowledgeCenter.getHashtagPopularity(hashtag);
	}

	/**
	 * Gets the weekly histogram of all tweet data
	 * 
	 * @param t1
	 *            A date string in the format of <b>dd/MM/yyyy HH:mm:ss</b>; all
	 *            tweets counted in the histogram should have been published
	 *            <b>after<\b> t1.
	 * @param t2
	 *            A date string in the format of <b>dd/MM/yyyy HH:mm:ss</b>; all
	 *            tweets counted in the histogram should have been published
	 *            <b>before<\b> t2.
	 * @return An array of strings, each string in the format of
	 *         ("<number of tweets (including retweets), number of retweets only>"
	 *         ), for example: ["100, 10","250,20",...,"587,0"]. The 0th index
	 *         of the array is Sunday.
	 * @throws Exception
	 *             If it is not possible to complete the operation
	 */
	public String[] getTemporalHistogram(String t1, String t2) throws Exception
	{
		return this.tweetsKnowledgeCenter.getTemporalHistogram(t1, t2);
	}

	/**
	 * Cleans up all persistent data from the system; this method will be called
	 * before every test, to ensure that all tests are independent.
	 */
	public void cleanPersistentData()
	{
		this.repositoryDataManager.cleanData();
		this.indexDataManager.cleanData();
	}
}