package ac.il.technion.twc.timeTests.partA;

import org.junit.Test;

import ac.il.technion.twc.timeTests.TestUtils;

public class HashTagsAppearenceQueryHandlerTimeTest extends PartA_StressTest{


	@Override
	@Test(timeout = TestUtils.MEDIUM_SAMPLE_LINES/ 2) // 0.5ms to tweet. 10,000 tweets . 5 seconds limit
	public final void testMediumSizeTweets() throws Exception {
		target.countHashtagAppearances("hash"); //some random hashtag. this is a stree test
	}


}
