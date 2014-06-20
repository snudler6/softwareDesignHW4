package ac.il.technion.twc.unitTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;

import ac.il.technion.twc.api.TweetFactory;
import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.api.models.Tweet;

public class TweetFactoryUnitTest {
    @Test
    public void buildTweet_parsingRootTweet_correctTime() throws Exception {
	Calendar calendar = new GregorianCalendar(2014, 4 - 1, 4, 12, 0, 0);
	calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

	Tweet tweet = TweetFactory.buildTweet("04/04/2014 12:00:00, iddqd");

	assertEquals(calendar.getTime(), tweet.getTime());
    }

    @Test
    public void buildTweet_parsingRootTweet_correctTweetId() throws Exception {
	Tweet tweet = TweetFactory.buildTweet("04/04/2014 12:00:00, iddqd");

	assertEquals(new TweetId("iddqd"), tweet.getId());
    }

    @Test
    public void buildTweet_parsingRootTweet_correctType() throws Exception {
	Tweet tweet = TweetFactory.buildTweet("04/04/2014 12:00:00, iddqd");

	assertTrue(tweet instanceof RootTweet);
    }

    @Test
    public void buildTweet_parsingRetweet_correctTime() throws Exception {
	Calendar calendar = new GregorianCalendar(2014, 4 - 1, 5, 12, 0, 0);
	calendar.setTimeZone(TimeZone.getTimeZone("UTC"));

	Tweet tweet = TweetFactory
		.buildTweet("05/04/2014 12:00:00, idkfa, iddqd");

	assertEquals(calendar.getTime(), tweet.getTime());
    }

    @Test
    public void buildTweet_parsingRetweet_correctTweetId() throws Exception {
	Tweet tweet = TweetFactory
		.buildTweet("05/04/2014 12:00:00, idkfa, iddqd");

	assertEquals(new TweetId("idkfa"), tweet.getId());
    }

    @Test
    public void buildTweet_parsingRetweet_correctType() throws Exception {
	Tweet tweet = TweetFactory
		.buildTweet("05/04/2014 12:00:00, idkfa, iddqd");

	assertTrue(tweet instanceof Retweet);
    }

    @Test
    public void buildTweet_parsingRetweet_correctOriginalTweetId()
	    throws Exception {
	Tweet tweet = TweetFactory
		.buildTweet("05/04/2014 12:00:00, idkfa, iddqd");

	assertEquals(((Retweet) tweet).getOriginalTweetId(), new TweetId(
		"iddqd"));
    }

    @Test
    public void a() {
	String string = "{\"created_at\":\"Wed May 15 10:08:07 +0000 2013\",\"id\":334611141902872576,\"id_str\":\"334611141902872576\",\"text\":\"Cinta hanya akan indah pabila berpondasikan kasih sang pencipta. Karena Cinta berasal dari-Nya.\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twittbot.net\\/\\\" rel=\\\"nofollow\\\"\\u003etwittbot.net\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":608956240,\"id_str\":\"608956240\",\"name\":\"Harapan.....\",\"screen_name\":\"NotesBuatKamu\",\"location\":\"You\\u2665\",\"url\":null,\"description\":\"Kamu tau arti cinta sebenarnya? Ga tau? Huftt..Ya, contohnya kecil aja, seperti Aku cinta Kamu\\u2665\",\"protected\":false,\"followers_count\":383,\"friends_count\":0,\"listed_count\":1,\"created_at\":\"Fri Jun 15 09:54:28 +0000 2012\",\"favourites_count\":0,\"utc_offset\":25200,\"time_zone\":\"Bangkok\",\"geo_enabled\":false,\"verified\":false,\"statuses_count\":13534,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_background_images\\/765054218\\/2ef17a3420c1a99bbd0f35b00c88c50c.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_background_images\\/765054218\\/2ef17a3420c1a99bbd0f35b00c88c50c.jpeg\",\"profile_background_tile\":true,\"profile_image_url\":\"http:\\/\\/a0.twimg.com\\/profile_images\\/3115283251\\/f936656c7391279551924d6e51704e63_normal.jpeg\",\"profile_image_url_https\":\"https:\\/\\/si0.twimg.com\\/profile_images\\/3115283251\\/f936656c7391279551924d6e51704e63_normal.jpeg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/608956240\\/1358311582\",\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"000000\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"symbols\":[],\"urls\":[],\"user_mentions\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"medium\",\"lang\":\"id\"}";

	TweetFactory.buildTweetJson(string);
    }
}
