package ac.il.technion.twc.impl.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsAncestorQueryHandler;

public class TweetsAncestorQueryHandler implements ITweetsAncestorQueryHandler
{
	private static final long serialVersionUID = -5818986663944504124L;
	
	private Map<TweetId,TweetId> tweetsAncestor;
	
	public TweetsAncestorQueryHandler()
	{
		this.tweetsAncestor = new HashMap<TweetId, TweetId>();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util.List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		for (Tweet tweet : tweets)
		{
			final Tweet originalTweet = tweetsRepository.getRootTweet(tweet);
			if (originalTweet != null)
				tweetsAncestor.put(tweet.getId(), originalTweet.getId());
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
	public TweetId getAncestor(TweetId id) {
		return tweetsAncestor.get(id);
	}
}
