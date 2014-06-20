package ac.il.technion.twc.impl.services;

import java.io.Serializable;
import java.util.Date;

import ac.il.technion.twc.api.interfaces.IQueryHandler;

/**
 * The ITweetsTemporalHistogramQueryHandler interface describes an object that encapsulates
 * the functionality of the temporal histogram.
 */
public interface ITweetsTemporalHistogramQueryHandler extends Serializable, IQueryHandler {

    /**
	 * Gets the current statistics of the temporal histogram in interval [t1,t2].
	 * 
	 * @param t1
	 *            The first time.
	 * 
	 * @param t2
	 *            The second time
	 * 
	 * @return An array of strings, each string describes the info of one
	 *         dayOfWeek.
	 */
    public String[] getStrings(Date t1, Date t2);
}
