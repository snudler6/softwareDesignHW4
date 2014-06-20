package ac.il.technion.twc.impl.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsLifetimeQueryHandler;

public class TweetsLifetimeQueryHandler implements ITweetsLifetimeQueryHandler
{
	private static final long serialVersionUID = -5818986663944504124L;
	
	private Map<TweetId, Long> tweetslifeTimes;
	
	public TweetsLifetimeQueryHandler()
	{
		this.tweetslifeTimes = new HashMap<TweetId, Long>();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util.List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		for (Tweet tweet : tweets)
		{
			if (!tweet.isRetweet() && tweet.getLifeTimeInMilliseconds() != 0)
				tweetslifeTimes.put(tweet.getId(), tweet.getLifeTimeInMilliseconds());
		}
	}
	
	/* (non-Javadoc)
	 * @see ac.il.technion.twc.impl.services.ITweetsLifetimeQueryHandler#getLifetimeOfTweetInMilliseconds(ac.il.technion.twc.api.TweetId)
	 */
	@Override
	public Long getLifetimeOfTweetInMilliseconds(TweetId tweetId)
	{
		Long lifeTime = tweetslifeTimes.get(tweetId);
		if (lifeTime == null)
			throw new IllegalArgumentException("tweetId does not exist in the index or it has no retweet");
		return lifeTime;
	}
	
	private void updateLifetimeOfTweetInMilliseconds(Tweet tweet)
	{
		if (tweetslifeTimes.containsKey(tweet.getId()))
		{
			Long lifeTime = tweetslifeTimes.get(tweet.getId());
			if (tweet.getLifeTimeInMilliseconds() < lifeTime)
				return;
		}
		tweetslifeTimes.put(tweet.getId(), tweet.getLifeTimeInMilliseconds());
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onRootTweetsDataChanged(java.util.List)
	 */
	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets)
	{
		for (Tweet changedRootTweet : changedRootTweets)
		{
			if (changedRootTweet.getLifeTimeInMilliseconds() != 0)
			{
				updateLifetimeOfTweetInMilliseconds(changedRootTweet);
			}
		}
	}
}
