package ac.il.technion.twc.impl.models.partA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partA.ITweetsTextQueryHandler;

public class TweetsTextQueryHandler implements ITweetsTextQueryHandler
{
	private static final long serialVersionUID = -5818986663944504124L;
	
	private Map<TweetId,String> tweetsTextsByTweetId;
	
	public TweetsTextQueryHandler()
	{
		this.tweetsTextsByTweetId = new HashMap<TweetId, String>();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util.List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		for (Tweet tweet : tweets)
		{
			tweetsTextsByTweetId.put(tweet.getId(), tweet.getTweetText());
		}
	}
	
	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onRootTweetsDataChanged(java.util.List)
	 */
	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets)
	{
	}

	@Override
	public String getTweetText(TweetId id) {
		return tweetsTextsByTweetId.get(id);
	}
}
