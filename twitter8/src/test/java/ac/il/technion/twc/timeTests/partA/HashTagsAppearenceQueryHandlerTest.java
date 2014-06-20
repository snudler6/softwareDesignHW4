package ac.il.technion.twc.timeTests.partA;

import org.junit.Test;

import ac.il.technion.twc.timeTests.TestUtils;

public class HashTagsAppearenceQueryHandlerTest extends PartA_StressTest{


	@Override
	@Test(timeout = TestUtils.MEDIUM_SAMPLE_LINES/ 2) // 0.5ms to tweet. 1,000,000 tweets . 500 seconds limit
	public final void testMediumSizeTweets() throws Exception {
		target.countHashtagAppearances("hash"); //some random hashtag. this is a stree test
	}


}
