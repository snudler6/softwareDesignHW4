package ac.il.technion.twc.unitTests.partA;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ac.il.technion.twc.api.TweetId;
import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Retweet;
import ac.il.technion.twc.api.models.RootTweet;
import ac.il.technion.twc.impl.models.partA.TweetsHashtagsAppearencesQueryHandler;

import com.googlecode.totallylazy.Lists;

public class TweetsHashtagAppearenceQueryHandlerTest {

	private static final TweetId ROOT_ID = new TweetId("root");
	private static final TweetId SON1_ID = new TweetId("son1");
	private static final TweetId SON2_ID = new TweetId("son2");
	private RootTweet root;
	private Retweet son1,son2;
	private ITweetsRepository repository;
	TweetsHashtagsAppearencesQueryHandler qh = new TweetsHashtagsAppearencesQueryHandler();

	@Before
	public void setUp() throws Exception {
		root = Mockito.mock(RootTweet.class);
		Mockito.when(root.getId()).thenReturn(ROOT_ID);
		
		son1 = Mockito.mock(Retweet.class);
		Mockito.when(son1.getId()).thenReturn(SON1_ID);
		Mockito.when(son1.getOriginalTweetId()).thenReturn(ROOT_ID);
		
		
		son2 = Mockito.mock(Retweet.class);
		Mockito.when(son2.getId()).thenReturn(SON2_ID);
		Mockito.when(son2.getOriginalTweetId()).thenReturn(SON1_ID);
		
		repository = Mockito.mock(ITweetsRepository.class);
		Mockito.when(repository.getRootTweet(root)).thenReturn(root);
		Mockito.when(repository.getRootTweet(son1)).thenReturn(root);
		Mockito.when(repository.getRootTweet(son2)).thenReturn(root);
		
	}


	@Test
	public final void test() {
		String h1 = "hashtag1",h2="hashtag2", h3="hashtag3";
		Mockito.when(root.getHashtags()).thenReturn(Lists.list(h1,h2,h3));
		Mockito.when(son1.getHashtags()).thenReturn(Lists.list(h3));
		Mockito.when(son2.getHashtags()).thenReturn(Lists.list(h1,h3));
		
		qh.onTweetsAdded(Lists.list(root, son1, son2), repository);
		
		assertEquals("2",qh.getHashtagAppearences(h1).toString());
		assertEquals("1",qh.getHashtagAppearences(h2).toString());
		assertEquals("3",qh.getHashtagAppearences(h3).toString());
	}

}
