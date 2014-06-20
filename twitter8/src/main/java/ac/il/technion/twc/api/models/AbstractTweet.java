package ac.il.technion.twc.api.models;

import java.io.Serializable;
import java.util.Date;

import ac.il.technion.twc.api.TweetId;

/**
 * The AbstractTweet class is used to represent a tweet, whether it is a real
 * tweet, or a tweet that is only a source of a retweet, but not yet added
 * "formally" to the system (added using importData).
 */
public abstract class AbstractTweet implements Serializable
{
	private static final long serialVersionUID = 643987555284902101L;

	private TweetId id;
	protected int numRetweets;
	protected Date latestRetweetTime;

	private final String userId;

	/**
	 * Get the number of retweets
	 * 
	 * @return
	 * 					the number of retweets
	 */
	public int getNumRetweets()
	{
		return numRetweets;
	}

	/**
	 *  Set the number of retweets
	 * 
	 * @param numRetweets
	 * 					the number of retweets
	 */
	public void setNumRetweets(int numRetweets)
	{
		this.numRetweets = numRetweets;
	}

	/**
	 * A constructor of AbstractTweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param latestRetweetTime
	 *            the latest known retweet time
	 */
	public AbstractTweet(TweetId id, String userId, Date latestRetweetTime)
	{
		if (id == null)
			throw new NullPointerException("id parameter is null");
		if (latestRetweetTime == null)
			throw new NullPointerException("latestRetweetTime parameter is null");

		this.numRetweets = 0;
		this.id = id;
		this.latestRetweetTime = latestRetweetTime;
		this.userId = userId;
	}
	
	/**
	 * A constructor of AbstractTweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param latestRetweetTime
	 *            the latest known retweet time
	 */
	public AbstractTweet(TweetId id, Date latestRetweetTime)
	{
		if (id == null)
			throw new NullPointerException("id parameter is null");
		if (latestRetweetTime == null)
			throw new NullPointerException("latestRetweetTime parameter is null");

		this.numRetweets = 0;
		this.id = id;
		this.latestRetweetTime = latestRetweetTime;
		this.userId = "";
	}

	/**
	 * Increment by 1 the number of retweets to the tweet.
	 */
	public void incrementNumRetweets()
	{
		this.numRetweets++;
	}

	/**
	 * Sets the latest (newest) retweet time. This will be compared against the
	 * currunt latestRetweetTime, and if the parameter is indeed newer, the
	 * value will be updated.
	 * 
	 * @param latestRetweetTime
	 *            the newly known latest retweet time
	 * @return true if LatestRetweetTime was changed. false otherwise.
	 */
	public boolean setLatestRetweetTime(Date latestRetweetTime)
	{
		if (latestRetweetTime == null)
			throw new NullPointerException("latestRetweetTime parameter is null");

		if (this.latestRetweetTime.before(latestRetweetTime))
		{
			this.latestRetweetTime = latestRetweetTime;
			return true;
		}
		return false;
	}

	/**
	 * Gets the latest (newest) retweet time.
	 * 
	 * @return the latest (newest) retweet time.
	 */
	public Date getLatestRetweetTime()
	{
		return this.latestRetweetTime;
	}

	/**
	 * Get the tweet's id.
	 * 
	 * @return the tweet's id.
	 */
	public TweetId getId()
	{
		return id;
	}

	public String getUserId() {
		return userId;
	}

	/**
	 * Get whether this tweet is a retweet or not.
	 * 
	 * @return whether this tweet is a retweet or not.
	 */
	public boolean isRetweet()
	{
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTweet other = (AbstractTweet) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
