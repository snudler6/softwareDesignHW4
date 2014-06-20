package ac.il.technion.twc.impl.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TimeZone;
import java.util.TreeMap;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.ITweetsTemporalHistogramQueryHandler;

public class TweetsTemporalHistogram implements ITweetsTemporalHistogramQueryHandler
{
	private static final long serialVersionUID = -2982470923435190236L;

	transient NavigableMap<Date, HistogramsData> searchTree = null;
	

	public TweetsTemporalHistogram()
	{
		this.searchTree = new TreeMap<Date, HistogramsData>();
	}


	static class HistogramsData implements Serializable
	{
		private static final long serialVersionUID = 8578152065827054959L;

		public TweetsDailyHistogram accumulator;
		public DayInfo current;

		public HistogramsData(DayOfWeek dayOfWeek)
		{
			this.accumulator = new TweetsDailyHistogram();
			this.current = new DayInfo(dayOfWeek);
		}

		public HistogramsData(Date date)
		{
			this.accumulator = new TweetsDailyHistogram();
			setCurrent(date);
		}

		public void setCurrent(Date date)
		{
			Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
			calendar.setTime(date);
			this.current = new DayInfo(DayOfWeek.fromCalendarDayOfTheWeek(calendar.get(Calendar.DAY_OF_WEEK)));
		}

		public void add(Tweet tweet)
		{
			accumulator.put(tweet);
			current.addTweet(tweet);
		}

		@Override
		public Object clone()
		{
			HistogramsData histogramsData = new HistogramsData(current.dayOfWeek);
			histogramsData.accumulator = (TweetsDailyHistogram) this.accumulator.clone();
			histogramsData.current = (DayInfo) this.current.clone();
			return histogramsData;
		}
	}

	private NavigableMap<Date, HistogramsData> createMap(List<Tweet> tweets)
	{
		NavigableMap<Date, HistogramsData> newMap = new TreeMap<Date, HistogramsData>();
		for (Tweet tweet : tweets)
		{
			HistogramsData histogramsData;
			if (newMap.containsKey(tweet.getTime()))
			{
				histogramsData = newMap.get(tweet.getTime());
			}
			else
			{
				histogramsData = new HistogramsData(tweet.getTime());
				newMap.put(tweet.getTime(), histogramsData);
			}
			histogramsData.add(tweet);
		}
		return newMap;
	}

	private void addDatesIfNotExist(NavigableMap<Date, HistogramsData> newMap)
	{
		for (Date date : newMap.keySet())
		{
			if (!searchTree.containsKey(date))
			{
				Date floor = searchTree.floorKey(date);
				HistogramsData newData = new HistogramsData(date);
				if (floor != null)
				{
					newData = (HistogramsData) searchTree.get(floor).clone();
					newData.setCurrent(date);
				}
				searchTree.put(date, newData);
			}
		}
	}

	private void add(List<Tweet> tweets)
	{	    
		NavigableMap<Date, HistogramsData> newMap = createMap(tweets);

		addDatesIfNotExist(newMap);

		TweetsDailyHistogram accumulator = new TweetsDailyHistogram();
		for (Date key : searchTree.keySet())
		{
		    	HistogramsData value =  searchTree.get(key);
		    
			if (newMap.containsKey(key))
			{
				DayInfo day = (DayInfo) newMap.get(key).current.clone();

				value.current.add(day);
				accumulator.add(day);
			}
			value.accumulator.add(accumulator);
		}
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.impl.services.ITweetsTemporalHistogramQueryHandler#getStrings(java.util.Date, java.util.Date)
	 */
	public String[] getStrings(Date t1, Date t2)
	{
		if (t1.after(t2))
			throw new IllegalArgumentException("date t1 is not before date t2");

		TweetsDailyHistogram returnedHistogram = new TweetsDailyHistogram();

		if (t1.equals(t2) && searchTree.containsKey(t1))
		{
			returnedHistogram.add(searchTree.get(t1).current);
			return returnedHistogram.getStrings();

		}
		if (searchTree.isEmpty())
			return returnedHistogram.getStrings();

		Entry<Date, HistogramsData> lowerBound = searchTree.floorEntry(t1);
		TweetsDailyHistogram lowerBoundAccumulator = new TweetsDailyHistogram();
		if (lowerBound != null)
		{
			lowerBoundAccumulator = (TweetsDailyHistogram) lowerBound.getValue().accumulator.clone();
			if (lowerBound.getKey().equals(t1))
			{
				lowerBoundAccumulator.sub(lowerBound.getValue().current);
			}
		}

		Entry<Date, HistogramsData> upperBound = searchTree.floorEntry(t2);
		TweetsDailyHistogram upperBoundAccumulator = new TweetsDailyHistogram();
		if (upperBound != null)
		{
			upperBoundAccumulator = (TweetsDailyHistogram) upperBound.getValue().accumulator.clone();
		}
		
		//System.out.println(Arrays.deepToString(upperBoundAccumulator.getStrings()));

		upperBoundAccumulator.sub(lowerBoundAccumulator);
		

		//System.out.println(Arrays.deepToString(upperBoundAccumulator.getStrings()));
		returnedHistogram = upperBoundAccumulator;

		return returnedHistogram.getStrings();
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onTweetsAdded(java.util.List, ac.il.technion.twc.api.interfaces.ITweetsRepository)
	 */
	@Override
	public void onTweetsAdded(List<Tweet> tweets, ITweetsRepository tweetsRepository)
	{
		add(tweets);
		
	}

	/* (non-Javadoc)
	 * @see ac.il.technion.twc.api.interfaces.IQueryHandler#onRootTweetsDataChanged(java.util.List)
	 */
	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets)
	{
	}

	
	
	
	static class Ser implements Serializable {
	    private static final long serialVersionUID = -6464877089516526735L;
	    
	    public Ser(Date key, HistogramsData value) {
		super();
		this.key = key;
		this.value = value;
	    }
	    public Date key;
	    public HistogramsData value;
	}
	Ser[] searchMap;
	 
	
	private void writeObject(java.io.ObjectOutputStream out)
		     throws IOException
		     {
		
		searchMap = new Ser[searchTree.size()];
		int i = 0;
		for (Entry<Date, HistogramsData> entry : searchTree.entrySet()) {
		    searchMap[i++] = new Ser(entry.getKey(), entry.getValue());
		}
		out.defaultWriteObject();
		     }
	
	
	private void readObject(java.io.ObjectInputStream in)
		    throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		searchTree = new TreeMap<Date, HistogramsData>();
		for (Ser entry : searchMap) {
		    searchTree.put(entry.key, entry.value);
		}
	}
}
