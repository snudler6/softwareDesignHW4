package ac.il.technion.twc.api.models;

import java.util.Date;
import java.util.List;

import ac.il.technion.twc.api.TweetId;

/**
 * The Tweet class is used to represent a real tweet. The tweet was added
 * "formally" to the system (added using importData).
 */
public abstract class Tweet extends AbstractTweet
{
	private static final long serialVersionUID = 643987555284902101L;

	private Date time;
	private List<String> hashtags;
	
	/**
	 * A constructor of Tweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param time
	 *            the time the tweet was made
	 */
	public Tweet(TweetId id, Date time, List<String> hashtags, String text)
	{
		super(id, time, text);
		this.time = time;
		this.hashtags = hashtags;
	}

	/**
	 * A constructor of Tweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param time
	 *            the time the tweet was made
	 */
	public Tweet(TweetId id, String userId, Date time, List<String> hashtags, String text)
	{
		super(id, userId, time, text);
		this.time = time;
		this.hashtags = hashtags;
	}

	/**
	 * Gets the time the tweet was tweeted
	 * 
	 * @return the time the tweet was tweeted
	 */
	public Date getTime()
	{
		return time;
	}
	
	/**
	 * Gets the hashtags within the tweet 
	 * 
	 * @return the hashtags within the tweet
	 */
	public List<String> getHashtags()
	{
		return this.hashtags;
	}

	/**
	 * Gets the lifetime of the tweet in milliseconds
	 * 
	 * @return the lifetime of the tweet
	 */
	public Long getLifeTimeInMilliseconds()
	{
		return getLatestRetweetTime().getTime() - time.getTime();
	}
}
