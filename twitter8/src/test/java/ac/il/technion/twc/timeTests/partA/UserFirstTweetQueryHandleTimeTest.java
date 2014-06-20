package ac.il.technion.twc.timeTests.partA;

import org.junit.Test;

import ac.il.technion.twc.timeTests.TestUtils;

public class UserFirstTweetQueryHandleTimeTest extends PartA_StressTest {

	@Override
	@Test(timeout = TestUtils.MEDIUM_SAMPLE_LINES / 2) // 0.5ms to tweet. 10,000 tweets . 5 seconds limit
	public final void testMediumSizeTweets() throws Exception {
		target.getFirstTweet("1"); //some random EXISTING id. this is a stree test
	}

}