package ac.il.technion.twc.impl.services.partA;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.IQueryHandler;
 
/**
 * manages the popularity of hashtags
 */
public interface ITweetsTextQueryHandler extends IQueryHandler
{
	public String getTweetText(TweetId id);
}
