package ac.il.technion.twc.impl.services;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.IQueryHandler;

public interface ITweetsLifetimeQueryHandler extends IQueryHandler
{
    /**
     * Get life time of root tweet.
     * 
     * @param tweetId
     *            The tweedId of the RootTweet we want to get its life time.
     * @return the life time (string) of the corresponding tweet.
     */
	public Long getLifetimeOfTweetInMilliseconds(TweetId tweetId);
}