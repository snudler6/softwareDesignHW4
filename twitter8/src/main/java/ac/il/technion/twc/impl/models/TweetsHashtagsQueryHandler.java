package ac.il.technion.twc.impl.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsHashtagsQueryHandler;

public class TweetsHashtagsQueryHandler implements ITweetsHashtagsQueryHandler
{
	private static final long serialVersionUID = -569740204746668635L;

	private Map<String, Integer> hashtags;

	public TweetsHashtagsQueryHandler()
	{
		this.hashtags = new HashMap<String, Integer>();
	}

	/*
	 * adding hashtag to the manager. if the hashtag exist, it adds 1 to its
	 * popularity. if the hashtag does not exist, it adds it with popularity 1.
	 */
	public void addHashtag(String hashtag)
	{
		addHashtag(hashtag, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ac.il.technion.twc.impl.services.ITweetsHashtagsQueryHandler#getPopularity
	 * (java.lang.String)
	 */
	@Override
	public Integer getPopularity(String hashtag)
	{
		Integer popularity = hashtags.get(hashtag);
		if (popularity == null)
			return 0;
		return popularity;
	}

	/*
	 * adding hashtag to the manager with a specific popularity
	 */
	public void addHashtag(String hashtag, int numRetweets)
	{
		Integer popularity = numRetweets;
		if (hashtags.containsKey(hashtag))
			popularity += hashtags.get(hashtag);
		hashtags.put(hashtag, popularity);
	}

	protected void addPopularityToHashTags(List<String> hashtags, int numRetweets)
	{
		for (String hashtag : hashtags)
		{
			addHashtag(hashtag, numRetweets);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util
	 * .List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	@Override
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		HashMap<Tweet, Integer> originalNumRetweets = new HashMap<Tweet, Integer>();
		for (Tweet tweet : tweets)
		{
			if (tweet.isRetweet())
			{
				Retweet retweet = (Retweet) tweet;
				Tweet original = tweetsRepository.getRootTweet(retweet);
				if (original != null)
				{
					addPopularityToHashTags(original.getHashtags(), 1 + retweet.getNumRetweets());
					Integer numRetweets = 1;
					Integer existingNumRetweets = originalNumRetweets.get(original);
					if (existingNumRetweets != null)
						numRetweets += existingNumRetweets;
					originalNumRetweets.put(original, numRetweets);
				}
			}
			// RootTweet
			else
			{
				addPopularityToHashTags(tweet.getHashtags(), tweet.getNumRetweets());
			}
		}

		for (Tweet tweet : originalNumRetweets.keySet())
		{
			if (tweets.contains(tweet))
				addPopularityToHashTags(tweet.getHashtags(), -originalNumRetweets.get(tweet));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ac.il.technion.twc.api.interfaces.IQueryHandler#onRootTweetsDataChanged
	 * (java.util.List)
	 */
	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets)
	{
	}
}
