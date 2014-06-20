package ac.il.technion.twc.impl.models;

import java.io.Serializable;
import java.util.Calendar;

import ac.il.technion.twc.api.models.Tweet;

/**
 * The DayInfo class is used to represent number of tweets and retweets in one
 * dayOfWeek
 */
class DayInfo implements Serializable
{
	private static final long serialVersionUID = -5193760844009675592L;

	DayOfWeek dayOfWeek;
	int numTweets;
	int numRetweets;

	public DayInfo(DayOfWeek dayOfWeek)
	{
		this.dayOfWeek = dayOfWeek;
		numTweets = 0;
		numRetweets = 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone()
	{
		DayInfo dayInfo = new DayInfo(this.dayOfWeek);

		dayInfo.numTweets = this.numTweets;
		dayInfo.numRetweets = this.numRetweets;
		return dayInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return numTweets + "," + numRetweets; // +",Day="+dayOfWeek;
	}

	public void addTweet(Tweet tweet)
	{
		++numTweets;
		if (tweet.isRetweet())
			++numRetweets;
	}

	public void add(DayInfo other)
	{
		numTweets += other.numTweets;
		numRetweets += other.numRetweets;
	}

	public void sub(DayInfo other)
	{
		numTweets -= other.numTweets;
		numRetweets -= other.numRetweets;
	}
}

enum DayOfWeek
{
	SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY);

	/**
	 * The value java.util.Calendar uses to indicate the appropriate dayOfWeek
	 * of the week.
	 */
	private int calendarDayOfTheWeekCode;

	/**
	 * Constructor of DayOfWeek enum.
	 * 
	 * @param value
	 *            the value used by java.util.Calendar to represent the
	 *            corresponding dayOfWeek of week.
	 */
	private DayOfWeek(int value)
	{
		this.calendarDayOfTheWeekCode = value;
	}

	/**
	 * Converts from the value used by java.util.Calendar to represent a
	 * dayOfWeek of week to the corresponding enum member.
	 * 
	 * @param calendarDayOfTheWeek
	 *            the value used by java.util.Calendar.
	 * @return the appropriate enum member
	 */
	static public DayOfWeek fromCalendarDayOfTheWeek(int calendarDayOfTheWeek)
	{
		for (DayOfWeek dayOfWeek : values())
			if (dayOfWeek.calendarDayOfTheWeekCode == calendarDayOfTheWeek)
				return dayOfWeek;
		throw new IllegalArgumentException("Illegal calendar dayOfWeek of week value");
	}
}
