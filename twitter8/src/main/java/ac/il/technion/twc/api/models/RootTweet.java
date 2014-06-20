package ac.il.technion.twc.api.models;

import java.util.Date;
import java.util.List;

import ac.il.technion.twc.api.TweetId;

/**
 * The RootTweet class is used to represent a tweet which is not a retweet, as
 * opposed to a Retweet.
 */
public class RootTweet extends Tweet
{
	private static final long serialVersionUID = 8191405946058544599L;

	/**
	 * A constructor of Tweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param time
	 *            the time the tweet was made
	 */
	public RootTweet(TweetId id, Date time, List<String> hashtags)
	{
		super(id, time, hashtags);
	}
	
	/**
	 * A constructor of Tweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param time
	 *            the time the tweet was made
	 */
	public RootTweet(TweetId id, String userId, Date time, List<String> hashtags)
	{
		super(id, userId, time, hashtags);
	}

	/**
	 * Gets the difference between the latest known retweet time and this tweet
	 * creation time, in milliseconds.
	 * 
	 * @return the difference between the latest known retweet time and this
	 *         tweet creation time
	 */
	public Long getLifetimeOfTweetInMilliseconds()
	{
		return latestRetweetTime.getTime() - getTime().getTime();
	}
}
