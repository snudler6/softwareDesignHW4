package ac.il.technion.twc.timeTests.partA;

import org.junit.Test;

import ac.il.technion.twc.timeTests.TestUtils;

public class ContentQueryHandlerTimeTest extends PartA_StressTest{

	@Override
	@Test(timeout = TestUtils.MEDIUM_SAMPLE_LINES / 2) // 0.5ms to tweet. 10,000 tweets . 5 seconds limit
	public final void testMediumSizeTweets() throws Exception {
		target.getTweetsContent("4"); //some random tweet. this is a stree test
	}

}
