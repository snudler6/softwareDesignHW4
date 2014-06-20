package ac.il.technion.twc.unitTests;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

import ac.il.technion.twc.api.TweetId;

public class TweetIdUnitTest
{
	@Test(expected = IllegalArgumentException.class)
	public void constructor_nullArgument_throwsException() throws Exception
	{
		new TweetId(null);
	}

	@Test
	public void equals_nullArgument_returnFalse() throws Exception
	{
		TweetId tweetId = new TweetId("nju9noi");
		assertNotEquals(tweetId, null);
	}

	@Test
	public void equals_sameTweetIdValue_returnTrue() throws Exception
	{
		TweetId tweetId = new TweetId("nju9noi");
		assertEquals(tweetId, new TweetId("nju9noi"));
	}

	@Test
	public void equals_sameTweetIdReference_returnTrue() throws Exception
	{
		TweetId tweetId = new TweetId("nju9noi");
		assertEquals(tweetId, tweetId);
	}

	@Test
	public void equals_differentTweetId_returnFalse() throws Exception
	{
		TweetId tweetId = new TweetId("nju9noi");
		assertNotEquals(tweetId, new TweetId("cmslka"));
	}

	@Test
	public void hashCode_sameTweetIdValue_sameHashCode() throws Exception
	{
		TweetId tweetId = new TweetId("nju9noi");
		assertEquals(tweetId.hashCode(), (new TweetId("nju9noi")).hashCode());
	}
}
