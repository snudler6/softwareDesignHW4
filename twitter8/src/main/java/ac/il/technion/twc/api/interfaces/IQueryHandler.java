package ac.il.technion.twc.api.interfaces;

import java.io.Serializable;
import java.util.List;

import ac.il.technion.twc.api.models.Tweet;


/**
 * The IQueryHandler interface describes an object that encapsulates a query
 * handler. The object will be serialized as nessacery when added to the 
 * TweetsManger.
 */
public interface IQueryHandler extends Serializable
{
	/**
	 * This method will be invoked when tweets were added to the tweetsRepository
	 * 
	 * @param tweets
	 * 					The tweets that were added
	 * @param tweetsRepository
	 * 					The repository to which the tweets were added
	 */
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository);
	
	/**
	 * This method will be invoked after tweets were added to the tweetsRepository, and
	 *  notifies the IQueryHandler that some changes occured, which might influence 
	 *  the passed root tweets.
	 * 
	 * @param changedRootTweets
	 * 					The root tweets that were added might be influenced
	 */
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets);
}
