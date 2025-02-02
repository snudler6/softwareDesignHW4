package ac.il.technion.twc.impl.services.partB;

import ac.il.technion.twc.api.interfaces.IQueryHandler;

public interface ITweetsNumberByUserQueryHandler  extends IQueryHandler {

	/**
	 * Gets the number of tweets or retweets made by a specific user
	 * 
	 * @param userId the id of the user
	 * @return The number of tweets made by the user
	 */
	public abstract Integer getTweetsNumberByUser(String usersId);

}