package ac.il.technion.twc.impl.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsRetweetsAmountQueryHandler;

public class TweetsRetweetsAmountQueryHandler implements ITweetsRetweetsAmountQueryHandler
{
	private static final long serialVersionUID = -5818986663944504124L;
	
	private final Map<TweetId, Integer> retweetsAmountByTweetId = new HashMap<TweetId, Integer>();
	

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util.List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		for(Tweet tweet : tweets)
			if(!(tweet instanceof RootTweet)){
				TweetId rootTweetId = tweetsRepository.getRootTweet(tweet)
								.getId();
				if(retweetsAmountByTweetId.containsKey(rootTweetId))
					retweetsAmountByTweetId.put(
							rootTweetId, retweetsAmountByTweetId
									.get(rootTweetId) + 1);
				else
					retweetsAmountByTweetId.put(
							rootTweetId, 1);
			}
		
	}
	

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets)
	{
	}


	@Override
	public Integer getRetweetsAmount(TweetId id) {
		return retweetsAmountByTweetId.get(id);
	}


}
