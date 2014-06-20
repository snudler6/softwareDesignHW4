	package ac.il.technion.twc.impl.models;

import java.util.List;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsHashtagsQueryHandler;

public class TweetsHashtagsAppearencesQueryHandler extends TweetsHashtagsQueryHandler implements
		ITweetsHashtagsQueryHandler {

	
	private static final long serialVersionUID = 4958073188101045085L;

	
	public TweetsHashtagsAppearencesQueryHandler(){
		super();
	}
	

	private void addPopularityToHashTags(List<String> hashtags){
		for ( String hashtag : hashtags){
			addHashtag(hashtag);
		}
	}
	
	@Override
	public void onTweetsAdded(List<Tweet> tweets,
			ITweetsRepository tweetsRepository) {
		for ( Tweet tweet : tweets){
			addPopularityToHashTags(tweet.getHashtags());
		}
	}

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets) {
		//this has no effect on this data query
	}

}
