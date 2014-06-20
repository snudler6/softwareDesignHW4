package ac.il.technion.twc.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ac.il.technion.twc.api.interfaces.IQueryHandler;

import com.google.inject.Inject;

/**
 * The class implements an index, consisting of the given IQueryHandler's, 
 * identified by String's. The IQueryHandler's will be serialized as nessacery when 
 * added. 
 */
public class TweetsIndex implements Serializable
{
	private static final long serialVersionUID = 7629119116256092006L;
	
	private Map<String, IQueryHandler> queryHandlers;
	
	
	@Inject
	public TweetsIndex()
	{	
		this.queryHandlers = new HashMap<String, IQueryHandler>();
	}
	
	/**
	 * Puts an IQueryHandler identified by the given String.
	 * 
	 * @param identifier 
	 * 					the identifier of the queryHandler
	 * @param queryHandler 
	 * 					the IQueryHandler that is stored.
	 */
	public void putQueryHandler(String identifier, IQueryHandler queryHandler)
	{
		this.queryHandlers.put(identifier, queryHandler);
	}
	
	/**
	 * Gets an IQueryHandler identified by the given String.
	 * 
	 * @param identifier 
	 * 					the identifier of the queryHandler
	 * @return
	 * 					the IQueryHandler that was stored.
	 */
	public IQueryHandler getQueryHandler(String identifier)
	{
		return this.queryHandlers.get(identifier);
	}

	/**
	 * Gets all the IQueryHandler that were put in.
	 * 
	 * @return 
	 * 					all the IQueryHandler that were put in.
	 */
	public Collection<IQueryHandler> getQueryHandlers()
	{
		return queryHandlers.values();
	}
}
