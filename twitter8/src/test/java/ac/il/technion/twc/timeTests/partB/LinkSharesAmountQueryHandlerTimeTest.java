package ac.il.technion.twc.timeTests.partB;

import org.junit.Test;

public class LinkSharesAmountQueryHandlerTimeTest extends PartB_StressTest {

	@Override
	@Test(timeout = 10 * times) // 10ms for each run
	public void testMediumSizeTweets() throws Exception {
		for(int i=0 ; i<times ; i++)
			target.numberOfYoutubeShares("http://www.youtube.com/vid1");//some existing user
	}

}
