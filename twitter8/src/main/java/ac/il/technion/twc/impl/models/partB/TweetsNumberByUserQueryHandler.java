package ac.il.technion.twc.impl.models.partB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partB.ITweetsNumberByUserQueryHandler;

public class TweetsNumberByUserQueryHandler implements ITweetsNumberByUserQueryHandler {

	private static final long serialVersionUID = 7508809411352087708L;

	Map<String, Integer> tweetsPerUser;

	public TweetsNumberByUserQueryHandler() {
		tweetsPerUser = new HashMap<String, Integer>();
	}

	@Override
	public Integer getTweetsNumberByUser(String usersId) {
		final Integer tweetsNumber = tweetsPerUser.get(usersId);
		if(tweetsNumber == null)
			return Integer.valueOf(0);
		return tweetsNumber;
	}

	@Override
	public void onTweetsAdded(List<Tweet> tweets,
			ITweetsRepository tweetsRepository) {
		for (Tweet tweet : tweets) {
			final String userId = tweet.getUserId();
			if( userId == null || userId.isEmpty())
				continue;
			final Integer numberOfTweets = tweetsPerUser.get(userId);
			if ( numberOfTweets == null)
				tweetsPerUser.put(userId, 1);
			else
				tweetsPerUser.put(userId, numberOfTweets+1);
		}

	}

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets) {
		//not relevent for this query
	}
}
