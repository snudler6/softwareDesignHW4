package ac.il.technion.twc.impl.services.partA;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.IQueryHandler;
 

public interface IUsersFirstTweetQueryHandler extends IQueryHandler
{

	/**
	 * Gets the id of the left tweet (by publication date) made by a user
	 * 
	 * @param userId the id of the user
	 * @return The id of the user's left tweet
	 * @throws Exception If it is not possible to complete the operation
	 */
	public TweetId getUsersFirstTweetId(String userId);
}
