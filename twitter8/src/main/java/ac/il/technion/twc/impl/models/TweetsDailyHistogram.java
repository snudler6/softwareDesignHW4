package ac.il.technion.twc.impl.models;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import ac.il.technion.twc.api.models.Tweet;

public class TweetsDailyHistogram implements Serializable
{
	private static final long serialVersionUID = 6940139104984975027L;

	/**
	 * The DayOfWeek enum is used to specify a dayOfWeek of the week (eg.
	 * sunday, monday, etc.). Since the way java.util.Calendar uses to indicate
	 * the dayOfWeek of the week is using an integer, We implplemented this
	 * enum. The enum stores the value java assigns to the appropriate dayOfWeek
	 * of the week in the variable calendarDayOfTheWeekCode.
	 * fromCalendarDayOfTheWeek method converts from the java integer value to
	 * the appropriate enum member. The DayOfWeek enum members are intentially
	 * declared in their order. We can use the ordinal() function in order to
	 * retreive thier value in range [0, ... 7].
	 */

	/**
	 * A map from the dayOfWeek of the week to the corresponding info.
	 */
	DayInfo[] weekInfo;

	public TweetsDailyHistogram()
	{
		// initialize DayInfo for each DayOfWeek
		this.weekInfo = new DayInfo[DayOfWeek.values().length];
		for (DayOfWeek dayOfWeek : DayOfWeek.values())
		{
			weekInfo[dayOfWeek.ordinal()] = new DayInfo(dayOfWeek);
		}
	}

	/**
	 * Gets the current statistics of the daily histogram.
	 * 
	 * @return An array of strings, each string describes the info of one
	 *         dayOfWeek.
	 */
	public String[] getStrings()
	{
		String[] info = new String[weekInfo.length];
		for (DayOfWeek dayOfWeek : DayOfWeek.values())
		{
			// The DayOfWeek enum members are intentially declared in their
			// order
			int ordinal = dayOfWeek.ordinal();
			info[ordinal] = weekInfo[dayOfWeek.ordinal()].toString();
		}
		return info;
	}

	/**
	 * Add the specified tweet to the statistics.
	 * 
	 * @param tweets
	 *            the tweet to add to the statistics
	 */
	public void put(Tweet tweet)
	{
		Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		calendar.setTime(tweet.getTime());
		int calendarDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK);

		DayOfWeek dayOfWeek = DayOfWeek.fromCalendarDayOfTheWeek(calendarDayOfTheWeek);
		weekInfo[dayOfWeek.ordinal()].addTweet(tweet);
	}

	/**
	 * Add the specified tweets to the statistics.
	 * 
	 * @param tweets
	 *            the tweet to add to the statistics
	 */
	public void putAll(List<Tweet> tweets)
	{
		for (Tweet tweet : tweets)
		{
			this.put(tweet);
		}
	}

	/**
	 * Add the other daily histogram to this one
	 * 
	 * @param histogram
	 *            the daily histogram to add to this one
	 */
	public void add(TweetsDailyHistogram other)
	{
		for (DayOfWeek dayOfWeek : DayOfWeek.values())
		{
			weekInfo[dayOfWeek.ordinal()].add(((TweetsDailyHistogram) other).weekInfo[dayOfWeek.ordinal()]);
		}
	}

	/**
	 * Add the info of the given day to the day in this histogram
	 * 
	 * @param dayInfo
	 *            the day info to add to this one
	 */
	public void add(DayInfo dayInfo)
	{
		weekInfo[dayInfo.dayOfWeek.ordinal()].add(dayInfo);
	}
	
	/**
	 * Substract the info of the given day from the day in this histogram
	 * 
	 * @param dayInfo
	 *            the day info to substract from this one
	 */
	public void sub(DayInfo dayInfo)
	{
		weekInfo[dayInfo.dayOfWeek.ordinal()].sub(dayInfo);
	}

	/**
	 * Substract the other daily histogram from this one
	 * 
	 * @param other
	 *            the daily histogram to substract from this one
	 */
	public void sub(TweetsDailyHistogram other)
	{
		for (DayOfWeek dayOfWeek : DayOfWeek.values())
		{
			weekInfo[dayOfWeek.ordinal()].sub(((TweetsDailyHistogram) other).weekInfo[dayOfWeek.ordinal()]);
		}
	}

	/**
	 * Clones a daily histogram.
	 */
	public Object clone()
	{
		DayInfo[] newWeekInfo = new DayInfo[DayOfWeek.values().length];
		for (DayOfWeek dayOfWeek : DayOfWeek.values())
		{
			newWeekInfo[dayOfWeek.ordinal()] = (DayInfo) weekInfo[dayOfWeek.ordinal()].clone();
		}
		TweetsDailyHistogram d = new TweetsDailyHistogram();
		d.weekInfo = newWeekInfo;
		return d;
	}
}
