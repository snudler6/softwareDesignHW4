package ac.il.technion.twc.impl.models.partA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partA.IUsersFirstTweetQueryHandler;
 
public class UsersFirstTweetQueryHandler implements IUsersFirstTweetQueryHandler
{
	
	private static final long serialVersionUID = -6776388887506217826L;
	
	Map<String, Tweet> firstTweetByUserId = new HashMap<>();

	public TweetId getUsersFirstTweetId(String userId){
		final Tweet tweet = firstTweetByUserId.get(userId);
		if( tweet == null )
			return null;
		return tweet.getId();
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
