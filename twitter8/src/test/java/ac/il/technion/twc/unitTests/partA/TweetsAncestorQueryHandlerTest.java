package ac.il.technion.twc.unitTests.partA;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.impl.models.partA.TweetsTextQueryHandler;

import com.googlecode.totallylazy.Lists;

public class TweetsAncestorQueryHandlerTest {
	
	private static final TweetId ROOT_ID = new TweetId("root");
	private static final TweetId SON1_ID = new TweetId("son1");
	private static final TweetId SON2_ID = new TweetId("son2");
	
	private static final String ROOT_STRING = "root";
	private static final String SON1_STRING = "son1";
	private static final String SON2_STRING = "son2";
	
	TweetsTextQueryHandler qh = new TweetsTextQueryHandler();

	@Before
	public void setUp() throws Exception {
		RootTweet root = Mockito.mock(RootTweet.class);
		Mockito.when(root.getId()).thenReturn(ROOT_ID);
		Mockito.when(root.getTweetText()).thenReturn(ROOT_STRING);
		
		Retweet son1 = Mockito.mock(Retweet.class);
		Mockito.when(son1.getId()).thenReturn(SON1_ID);
		Mockito.when(son1.getTweetText()).thenReturn(SON1_STRING);
		
		Retweet son2 = Mockito.mock(Retweet.class);
		Mockito.when(son2.getId()).thenReturn(SON2_ID);
		Mockito.when(son2.getTweetText()).thenReturn(SON2_STRING);
		
		ITweetsRepository repository = Mockito.mock(ITweetsRepository.class);
		Mockito.when(repository.getRootTweet(root)).thenReturn(root);
		Mockito.when(repository.getRootTweet(son1)).thenReturn(root);
		Mockito.when(repository.getRootTweet(son2)).thenReturn(root);
		
		qh.onTweetsAdded(Lists.list(root, son1, son2), repository);
	}

	@Test
	public void test() {
		assertEquals(ROOT_STRING, qh.getTweetText(ROOT_ID));
		assertEquals(SON1_STRING, qh.getTweetText(SON1_ID));
		assertEquals(SON2_STRING, qh.getTweetText(SON2_ID));
	}

}
