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
 * This class is meant to act as a wrapper to test your functionality. You should implement <b>only</b> the methods
 * matching the selected functionality and not change any method's signature. The methods you choose not to implement
 * may remain with their default (i.e., exception throwing) behaviour. You can also implement an argumentless
 * constructor if you wish, and any number of new public methods.
 * 
 * @author Gal Lalouche
 */
public class FunctionalityTester {
	
	private final Injector injector = Guice.createInjector(new TweetModule());
	private IDataManager repositoryDataManager;
	private IDataManager indexDataManager;
	private TweetsKnowledgeCenter tweetsKnowledgeCenter;

	/**
	 * A default constructor.
	 */
	public FunctionalityTester()
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
	public FunctionalityTester(IDataManager repositoryDataManager, IDataManager indexDataManager)
	{
		this.tweetsKnowledgeCenter = injector.getInstance(TweetsKnowledgeCenter.class);
		this.repositoryDataManager = repositoryDataManager;
		this.indexDataManager = indexDataManager;
	}
	
	/**
	 * Loads the data from an array of lines
	 * 
	 * @param lines An array of lines, each line formatted as <time (dd/MM/yyyy HH:mm:ss)>,<tweet id>[,original tweet]
	 * @throws Exception If for any reason, handling the data failed
	 */
	public void importData(String[] lines) throws Exception 	{
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
	 * {@link TwitterKnowledgeCenter#importData(String[])}. setupIndex will be called before any queries can be run on
	 * the system
	 * 
	 * @throws Exception If for any reason, loading the index failed
	 */
	public void setupIndex() throws Exception {
		tweetsKnowledgeCenter.importIndex(indexDataManager);
	}
	
	//GROUP A
	
	/**
	 * Gets the content of tweet with the given id
	 * 
	 * @return A string, of the raw content of the tweet
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String getTweetsContent(String tweetId) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Gets original tweet's id from a retweet; if the input itself is an original tweet, then this function returns its
	 * id
	 * 
	 * @return A string, of the raw content of the tweet
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String getOriginalTweetsId(String tweetId) throws Exception {
		return tweetsKnowledgeCenter.getAncestorTweetsId(tweetId);
	}
	
	/**
	 * Gets an id of <i>a</i> tweet that was published between two dates
	 * 
	 * @param t1 A date string in the format of <b>dd/MM/yyyy HH:mm:ss</b>; The tweet should have been published after
	 *        this time.
	 * @param t2 A date string in the format of <b>dd/MM/yyyy HH:mm:ss</b>; The tweet should have been published before
	 *        this time.
	 * @return The id of a tweet published between t1 and t2
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String findTweetInTime(String t1, String t2) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Gets the ids of all the retweets that <b>directly</b> retweeted this tweet
	 * 
	 * @return An array of the retweets ids
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String[] getAllDirectRetweets(String tweetId) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Gets all appearances of the hashtag in different tweets. Note that the hashtag should only be counted one time at
	 * most per tweet.
	 * 
	 * @param hashtag the hashtag
	 * @return The number of appearances
	 */
	public String countHashtagAppearances(String hashtag) {
		return this.tweetsKnowledgeCenter.getHashtagAppearences(hashtag);
	}
	
	/**
	 * Gets the id of the left tweet (by publication date) made by a user
	 * 
	 * @param userId the id of the user
	 * @return The id of the user's left tweet
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String getFirstTweet(String userId) throws Exception {
		return this.tweetsKnowledgeCenter.getUserFirstTweet(userId);
	}
	
	//GROUP B
	
	/**
	 * Checks if two tweets are retweets (recursive) of the same tweet
	 * 
	 * @param tweetId1 the id of the left tweet
	 * @param tweetId2 the id of the right tweet
	 * @return "true" if they originated from the same tweet, "false" otherwise
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String belongToSameTree(String tweetId1, String tweetId2) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Gets the number of retweets (recursive) of an original tweet
	 * 
	 * @param tweetId the id of the tweet
	 * @return The number of retweets
	 */
	public String numberOfRetweets(String tweetId) {
		return this.tweetsKnowledgeCenter.getTweetsRetweetsAmount(tweetId);
	}
	
	/**
	 * Gets the number shares (including in the content) of a youtube link
	 * 
	 * @param youtubeUrl the youtube url
	 * @return The number of shares, or appearances in tweets (counted at most one per tweet)
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String numberOfYoutubeShares(String youtubeUrl) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Gets the number of tweets or retweets made by a specific user
	 * 
	 * @param userId the id of the user
	 * @return The number of tweets made by the user
	 */
	public String numberTweetsByUser(String usersId) {
		return tweetsKnowledgeCenter.getTweetsNumberByUser(usersId);
	}
	
	// GROUP C
	/**
	 * Gets the top k coupled hashtags
	 * 
	 * @param k the number of coupled hashtags to retrieve
	 * @return The top k coupled hashtags, in descending order (i.e., 1st, 2nd,...)
		The format of each couple should be the two hashtags ordered lexicographically, and separated by a comma,
		e.g., String[] {"Bar, Foo", "Eggs, Spam",...}
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String[] hashtagCoupling(int k) throws Exception {
		return this.tweetsKnowledgeCenter.getTweetsHashtagsCoupling(k);
	}
	
	/**
	 * Gets the id of the original tweet of a via retweet
	 * 
	 * @param retweetId the retweet's id
	 * @return The id of the original tweet
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String viaRetweet(String retweetId) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Returns the number of occurrence of the word (as a substring of any sort in the text) in tweets. A word can be
	 * counted multiple times in the same tweet. <b>if you implement this query, you have to also implement
	 * {@link FunctionalityTester#setupWordTrends(String[])}!</b>
	 * 
	 * @param word the word to check
	 * @return The number of occurrences of the word
	 * @throws Exception If it is not possible to complete the operation
	 */
	public String wordTrends(String word) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Sets up the words to look for in word trends. You can assume that this method will be invoked before <b>every</b>
	 * invocation of {@link FunctionalityTester#importData(String[])} or
	 * {@link FunctionalityTester#importDataJson(String[])}, and its input will never change.
	 * 
	 * @param words The words search for
	 * @throws Exception If it is not possible to complete the operation
	 */
	public void setupWordTrends(String[] words) throws Exception {
		throw new UnsupportedOperationException("Not implemented");
	}
	
	/**
	 * Cleans up all persistent data from the system; this method will be called before every test, to ensure that all
	 * tests are independent.
	 */
	public void cleanPersistentData() {
		this.repositoryDataManager.cleanData();
		this.indexDataManager.cleanData();	
	}
	
	
}
