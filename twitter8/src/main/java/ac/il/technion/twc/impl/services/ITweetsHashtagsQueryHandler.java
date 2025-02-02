package ac.il.technion.twc.impl.services;

import ac.il.technion.twc.api.interfaces.IQueryHandler;
 
/**
 * manages the popularity of hashtags
 */
public interface ITweetsHashtagsQueryHandler extends IQueryHandler
{

	/**
	 * Returns the popularity of the hashtag
	 * 
	 * @param hashtag the hashtag to get the popularity of
	 * @return 0 if the hashtag does not exist. otherwise, the number of
	 *         retweets to roottweets that contains the given hashtag 
	 */
	public Integer getPopularity(String hashtag);
}
