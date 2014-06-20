package ac.il.technion.twc.impl.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.IUsersFirstTweetQueryHandler;
 
/**
 * manages the popularity of hashtags
 */
public class UsersFirstTweetQueryHandler implements IUsersFirstTweetQueryHandler
{
	
	private static final long serialVersionUID = -6776388887506217826L;
	
	Map<String, Tweet> firstTweetByUserId = new HashMap<>();

	/**
	 * Returns the popularity of the hashtag
	 * 
	 * @param hashtag the hashtag to get the popularity of
	 * @return 0 if the hashtag does not exist. otherwise, the number of
	 *         retweets to roottweets that contains the given hashtag 
	 */
	public TweetId getUsersFirstTweetId(String userId){
		return firstTweetByUserId.get(userId).getId();
	}

	@Override
	public void onTweetsAdded(List<Tweet> tweets,
			ITweetsRepository tweetsRepository) {
		
		for(Tweet t : tweets){
			String userId = t.getUserId();
			if (!userId.isEmpty()
					&& (!firstTweetByUserId.containsKey(userId) || t.getTime()
							.before(firstTweetByUserId.get(userId).getTime())))
				firstTweetByUserId.put(userId, t);
		}		
	}

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets) {
		
	}
}
