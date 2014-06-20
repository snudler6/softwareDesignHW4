package ac.il.technion.twc.api;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.AbstractTweet;
import ac.il.technion.twc.api.models.PlaceholderTweet;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;

/**
 * The TweetsRepository class provides an implementation of the interface
 * ITweetsRepository.
 */
public class TweetsRepository implements ITweetsRepository
{
	private static final long serialVersionUID = -7883730313325411229L;

	private Map<TweetId, AbstractTweet> tweetsById;

	public TweetsRepository()
	{
		this.tweetsById = new HashMap<TweetId, AbstractTweet>();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsRepository#getRootTweetById(ac.il.technion.twc.api.TweetId)
	 */
	public RootTweet getRootTweetById(TweetId tweetId)
	{
		if (tweetId == null)
			throw new NullPointerException("tweetId cannot be null");

		AbstractTweet tweet = this.tweetsById.get(tweetId);
		if (tweet == null || !(tweet instanceof RootTweet))
			throw new IllegalArgumentException("Tweet id must refer to an existing root tweet");
		RootTweet rootTweet = (RootTweet) tweet;
		return rootTweet;
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsRepository#getRootTweet(ac.il.technion.twc.api.models.AbstractTweet)
	 */
	public RootTweet getRootTweet(AbstractTweet tweet)
	{
		if (tweet == null)
			throw new NullPointerException("tweet cannot be null");

		if (tweet.isRetweet())
			return getRootTweet(tweetsById.get(((Retweet) tweet).getOriginalTweetId()));

		if (tweet instanceof RootTweet)
			return (RootTweet) tweet;

		return null;
	}

	private Tweet add(Tweet tweet)
	{
		if (tweet == null)
			throw new NullPointerException("tweet cannot be null");

		if (tweetsById.containsKey(tweet.getId()))
		{
			AbstractTweet existingTweet = tweetsById.get(tweet.getId());
			if (!(existingTweet instanceof PlaceholderTweet))
			{
				throw new IllegalArgumentException("Adding existing tweet is illegal");
			}
			tweet.setNumRetweets(existingTweet.getNumRetweets());
			tweetsById.remove(existingTweet.getId());
			tweet.setLatestRetweetTime(existingTweet.getLatestRetweetTime());
		}
		return addNewTweet(tweet);
	}

	private Tweet addNewTweet(Tweet tweet)
	{
		tweetsById.put(tweet.getId(), tweet);
		if (tweet.isRetweet())
		{
			Retweet retweet = (Retweet) tweet;
			TweetId originalTweetId = retweet.getOriginalTweetId();
			if (this.tweetsById.containsKey(originalTweetId))
			{
				return updateLatestRetweetTimeAndNumRetweets(originalTweetId, retweet.getLatestRetweetTime());
			}
			else
			{
				PlaceholderTweet holder = new PlaceholderTweet(originalTweetId, retweet.getLatestRetweetTime(), tweet.getNumRetweets() + 1);
				this.tweetsById.put(holder.getId(), holder);
				return null;
			}
		}
		else
			return tweet;
	}

	private Tweet updateLatestRetweetTimeAndNumRetweets(TweetId originalTweetId, Date time)
	{
		AbstractTweet tweet = this.tweetsById.get(originalTweetId);
		if (tweet == null)
			throw new IllegalArgumentException("Original tweet of a retweet does not exist");

		tweet.incrementNumRetweets();

		if (tweet.isRetweet())
			return updateLatestRetweetTimeAndNumRetweets(((Retweet) tweet).getOriginalTweetId(), time);
		else
		{
			boolean changed = tweet.setLatestRetweetTime(time);
			if (changed && tweet instanceof RootTweet)
				return (RootTweet) tweet;
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.ITweetsRepository#add(java.util.List)
	 */
	@Override
	public List<Tweet> add(List<Tweet> tweets)
	{
		if (tweets == null)
			throw new NullPointerException("tweets cannot be null");
		
		LinkedList<Tweet> changedRootTweets = new LinkedList<Tweet>();
		for (Tweet tweet : tweets)
		{
			Tweet changedRootTweet = add(tweet);
			if (changedRootTweet != null)
			{
				changedRootTweets.add(changedRootTweet);
			}
		}
		
		return changedRootTweets;
	}
}
