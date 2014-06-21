package ac.il.technion.twc.timeTests.partC;


public class HashtagCouplingQueryTimeTest extends PartC_StressTest {

	@Override
	public void testMediumSizeTweetsDepth1() throws Exception {
		for(int i=0; i<times; ++i)
			target.hashtagCoupling(K1);
		
	}

	@Override
	public void testMediumSizeTweetsDepth4() throws Exception {
		for(int i=0; i<times; ++i)
			target.hashtagCoupling(K2);
		
	}

}
