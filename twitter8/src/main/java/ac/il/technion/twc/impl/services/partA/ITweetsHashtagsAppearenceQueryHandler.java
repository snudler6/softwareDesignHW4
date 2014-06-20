package ac.il.technion.twc.impl.services.partA;

import ac.il.technion.twc.api.interfaces.IQueryHandler;

public interface ITweetsHashtagsAppearenceQueryHandler extends IQueryHandler{

	public abstract Integer getPopularity(String hashtag);

}