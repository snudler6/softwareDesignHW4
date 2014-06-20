package ac.il.technion.twc.unitTests.partA;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.impl.models.partA.TweetsAncestorQueryHandler;

import com.googlecode.totallylazy.Lists;

public class TweetsAncestorQueryHandlerTest {
	
	private static final TweetId ROOT_ID = new TweetId("root");
	private static final TweetId SON1_ID = new TweetId("son1");
	private static final TweetId SON2_ID = new TweetId("son2");
	TweetsAncestorQueryHandler qh = new TweetsAncestorQueryHandler();

	@Before
	public void setUp() throws Exception {
		RootTweet root = Mockito.mock(RootTweet.class);
		Mockito.when(root.getId()).thenReturn(ROOT_ID);
		
		Retweet son1 = Mockito.mock(Retweet.class);
		Mockito.when(son1.getId()).thenReturn(SON1_ID);
		Mockito.when(son1.getOriginalTweetId()).thenReturn(ROOT_ID);
		
		Retweet son2 = Mockito.mock(Retweet.class);
		Mockito.when(son2.getId()).thenReturn(SON2_ID);
		Mockito.when(son2.getOriginalTweetId()).thenReturn(SON1_ID);
		
		ITweetsRepository repository = Mockito.mock(ITweetsRepository.class);
		Mockito.when(repository.getRootTweet(root)).thenReturn(root);
		Mockito.when(repository.getRootTweet(son1)).thenReturn(root);
		Mockito.when(repository.getRootTweet(son2)).thenReturn(root);
		
		
		qh.onTweetsAdded(Lists.list(root, son1, son2), repository);
	}

	@Test
	public void test() {
		assertEquals(qh.getAncestor(ROOT_ID), ROOT_ID);
		assertEquals(qh.getAncestor(SON1_ID), ROOT_ID);
		assertEquals(qh.getAncestor(SON2_ID), ROOT_ID);
	}

}
