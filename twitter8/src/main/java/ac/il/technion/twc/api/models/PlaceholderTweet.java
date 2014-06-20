package ac.il.technion.twc.api.models;

import java.util.Date;

import ac.il.technion.twc.api.TweetId;

/**
 * The PlaceholderTweet class is used to represent a tweet that is only a source
 * of a retweet, but not yet added "formally" to the system (added using
 * importData). Therefore, it is only a place holder for a tweet that will be
 * added later. This is, actually, a fake tweet.
 */
public class PlaceholderTweet extends AbstractTweet
{
	private static final long serialVersionUID = 5937673521883279735L;

	/**
	 * A constructor of PlaceholderTweet.
	 * 
	 * @param id
	 *            the id of the tweet
	 * @param latestRetweetTime
	 *            the latest known retweet time
	 * @param numRetweets
	 */
	public PlaceholderTweet(TweetId id, Date latestRetweetTime, int numRetweets)
	{
		super(id, latestRetweetTime);
		setNumRetweets(numRetweets);
	}
}
