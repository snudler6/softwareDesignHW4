package ac.il.technion.twc.impl.models.partB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partB.ITweetsYoutubeLinkQueryHandler;

public class TweetsYoutubeLinkQueryHandler implements ITweetsYoutubeLinkQueryHandler
{
	private static final long serialVersionUID = -5818986663944504124L;
	
	private final Map<String, Integer> linksAmountByTweetId = new HashMap<String, Integer>();
	
	private static String youtubeLinkRegex = "(http://www.youtube.com/[^\\s-]*)";
	
	 public static List<String> getAllLinkMatches(String text) {
	        List<String> matches = new ArrayList<String>();
	        Matcher m = Pattern.compile(youtubeLinkRegex).matcher(text);
	        while(m.find()) {
	            matches.add(m.group(1));
	        }
	        return matches;
	    }
	

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util.List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		for(Tweet tweet : tweets)
			for(String link : getAllLinkMatches(tweet.getTweetText())){
				Integer linkShares = linksAmountByTweetId.get(link);
				if(linkShares == null)
					linksAmountByTweetId.put(link, new Integer(1));
				else
					linksAmountByTweetId.put(link, linkShares + 1);
			}
				
					
	}
	

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets)
	{
	}


	@Override
	public Integer getYoutubeLinkAmount(String youtubeLink) {
		final Integer retweetAmount = linksAmountByTweetId.get(youtubeLink);
		if (retweetAmount==null)
			return Integer.valueOf(0);
		return retweetAmount;
	}


}
