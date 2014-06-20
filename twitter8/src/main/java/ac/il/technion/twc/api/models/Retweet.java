package ac.il.technion.twc.api.models;

import java.util.Date;
import java.util.LinkedList;

import ac.il.technion.twc.api.TweetId;

/**
 * The Retweet class is used to represent a tweet which is a retweet, as opposed
 * to a RootTweet, which is not. Therefore, there is an appropriate original
 * tweet. The original tweet is the tweet to which this tweet was retweeted to,
 * the source.
 */
public class Retweet extends Tweet
{
	private static final long serialVersionUID = -896428252557029362L;

	private TweetId originalTweetId;
	
	/**
	 * A constructor of Retweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param time
	 *            the time the tweet was made
	 * @param originalTweetId
	 *            the tweet to which this tweet was retweeted to
	 */
	public Retweet(TweetId id, Date time, TweetId originalTweetId)
	{
		super(id, time, new LinkedList<String>());
		if (originalTweetId.equals(id))
			throw new IllegalArgumentException("A tweet cannot retweet itself");

		this.originalTweetId = originalTweetId;
	}

	/**
	 * A constructor of Retweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param time
	 *            the time the tweet was made
	 * @param originalTweetId
	 *            the tweet to which this tweet was retweeted to
	 */
	public Retweet(TweetId id, String userId, Date time, TweetId originalTweetId)
	{
		super(id, userId, time, new LinkedList<String>());
		if (originalTweetId.equals(id))
			throw new IllegalArgumentException("A tweet cannot retweet itself");

		this.originalTweetId = originalTweetId;
	}

	/**
	 * Gets the tweet to which this tweet was retweeted to.
	 * 
	 * @return the tweet to which this tweet was retweeted to
	 */
	public TweetId getOriginalTweetId()
	{
		return originalTweetId;
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.models.AbstractTweet#isRetweet()
	 */
	@Override
	public boolean isRetweet()
	{
		return true;
	}
}
