	package ac.il.technion.twc.impl.models.partA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partA.ITweetsHashtagsAppearenceQueryHandler;

public class TweetsHashtagsAppearencesQueryHandler implements ITweetsHashtagsAppearenceQueryHandler{

	
	private static final long serialVersionUID = 4958073188101045085L;

	private Map<String, Integer> hashtags;

	public TweetsHashtagsAppearencesQueryHandler()
	{
		this.hashtags = new HashMap<String, Integer>();
	}
	
	@Override
	public Integer getPopularity(String hashtag)
	{
		Integer popularity = hashtags.get(hashtag);
		if (popularity == null)
			return 0;
		return popularity;
	}
	
	private void addHashtag(String hashtag)
	{
		Integer popularity = 1;
		if (hashtags.containsKey(hashtag))
			popularity += hashtags.get(hashtag);
		hashtags.put(hashtag, popularity);
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
