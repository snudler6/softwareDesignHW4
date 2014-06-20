package ac.il.technion.twc.timeTests.partB;

import org.junit.Test;

public class RetweetsAmountQueryHandlerTimeTest extends partB_StressTest {

	@Override
	@Test(timeout = 10 * times) // 10ms for each run
	public void testMediumSizeTweets() throws Exception {
		for(int i=0 ; i<times ; i++)
			target.numberTweetsByUser("100");//some existing user
	}

}
