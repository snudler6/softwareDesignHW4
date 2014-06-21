package ac.il.technion.twc.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;

import com.twitter.Extractor;

public class TweetFactory
{
	/**
	 * Constructor to create a tweet. Initializes its members according to the
	 * string
	 * 
	 * @param string
	 *            the string to parse to a tweet, in the format <time
	 *            (dd/MM/yyyy HH:mm:ss)>,<tweet id>[,original tweet] examples:
	 *            2009-06-11 16:59:45,38T47 2009-06-11 16:59:45,49284,38T47
	 * @throws ParseException
	 */
	public static Tweet buildTweet(String string) throws ParseException
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		String[] stringParts = string.split("[,\n]");

		if (stringParts.length < 2 || stringParts.length > 3)
			throw new ParseException("A string must be of the format <time>,<tweet id>[,original tweet]", string.lastIndexOf(","));

		Date time = dateFormat.parse(stringParts[0]);
		TweetId id = new TweetId(stringParts[1].trim());
		boolean isRetweet = stringParts.length == 3;

		if (isRetweet)
		{
			TweetId tweetedTweet = new TweetId(stringParts[2].trim());
			return new Retweet(id, time, tweetedTweet, "");
		}
		else
		{
			return new RootTweet(id, time, new LinkedList<String>(), "");
		}
	}

	/**
	 * Constructor to create a tweet. Initializes its members according to the
	 * string
	 * 
	 * @param string
	 *            the string to parse to a tweet, in the format of json
	 * @throws ParseException
	 */
	public static Tweet buildTweetJson(String string) throws JSONException
	{
		final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);

		final String JSON_CREATED_AT = "created_at";
		final String JSON_ID = "id_str";
		final String JSON_TEXT = "text";
		final String JSON_TWEETED_TWEET = "retweeted_status";

		JSONObject jsonObject = new JSONObject(string);

		//getting tweet date
		String timeStr = jsonObject.getString(JSON_CREATED_AT);
		Date time;
		try
		{
			time = dateFormat.parse(timeStr);
		}
		catch (ParseException e)
		{
			throw new JSONException("The time " + timeStr + "is not in the correct format");
		}
		//getting tweet id
		TweetId id = new TweetId(jsonObject.getString(JSON_ID));
		
		//getting user id
		String userId = "";
		if(!jsonObject.isNull("user"))
			userId = jsonObject.getJSONObject("user").getString("id_str");
		
		//extracting the hashtags and the text from the tweet 
		Extractor extractor = new Extractor();
		List<String> hashtags = new ArrayList<String>();
		String text = ""; 
		
		if ( !jsonObject.isNull(JSON_TEXT) ) {
			text = jsonObject.getString(JSON_TEXT);
			hashtags = extractor.extractHashtags(text);
		}
		
		//checking whether the tweet is a retweet
		boolean isRetweet = !jsonObject.isNull(JSON_TWEETED_TWEET);
		if (isRetweet)
		{
			String tweetedTweetStr = jsonObject.getJSONObject(JSON_TWEETED_TWEET).getString(JSON_ID);
			TweetId tweetedTweet = new TweetId(tweetedTweetStr);

			return new Retweet(id, userId, time, tweetedTweet, hashtags, text);
		}
		else
		{
			return new RootTweet(id, userId, time, hashtags, text);
		}

	}
}
