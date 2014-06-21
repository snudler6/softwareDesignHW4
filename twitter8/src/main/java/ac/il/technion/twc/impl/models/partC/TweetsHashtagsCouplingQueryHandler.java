package ac.il.technion.twc.impl.models.partC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partC.ITweetsHashtagsCouplingQueryHandler;

public class TweetsHashtagsCouplingQueryHandler implements
		ITweetsHashtagsCouplingQueryHandler {

	private static final long serialVersionUID = 4958073188101045085L;

	public static class ValueComparator implements Serializable,
			Comparator< Pair<Pair<String,String>,Integer>> {

		private static final long serialVersionUID = -3909198429646950164L;

		
		// Note: this comparator imposes orderings that are inconsistent with
		// equals.
		public int compare(Pair<Pair<String,String>,Integer> a, Pair<Pair<String,String>,Integer> b) {
			final int valueDiff = a.right -b.right;
			if (valueDiff != 0)
				return valueDiff;
			else
				// this "secondary ordering" is for the map not to combine entries.
				return a.left.hashCode() - b.left.hashCode();
		}
	}

	/**
	 * the map is for getting coupling by Pairs.
	 * the set is for keeping the couples ordered.
	 */
	private final Map< Pair<String,String> , Integer > couplesMap; 
	private final NavigableSet< Pair< Pair<String,String> , Integer > > couplesSet;
	
	public TweetsHashtagsCouplingQueryHandler() {
		couplesMap = new HashMap<>();
		couplesSet = new TreeSet<>(new ValueComparator());
	}

	@Override
	public void onTweetsAdded(List<Tweet> tweets,
			ITweetsRepository tweetsRepository) {
		for (Tweet tweet : tweets) {
			if (tweet.isRetweet())
				continue;
			
			List<String> hashtags = tweet.getHashtags();
			for (int i = 0; i < hashtags.size(); ++i)
				for (int j = i + 1; j < hashtags.size(); ++j)
					incPair(hashtags.get(i), hashtags.get(j));
		}
	}
	
	private void putPair(Pair<String,String> key, Integer val) {
		// should put left in values, for the compression will work
		// in hashtagPairs map. 
		couplesMap.put(key, val);
		if ( val > 1) {
			Pair< Pair<String,String> , Integer > oldPair = new Pair< Pair<String,String> , Integer >(key,val-1);
			couplesSet.remove(oldPair);
		}
		Pair< Pair<String,String> , Integer > newPair = new Pair< Pair<String,String> , Integer >(key,val);
		couplesSet.add(newPair);
	}

	private void incPair(String hashtag, String hashtag2) {
		Pair<String,String> pair = new Pair<String,String>(hashtag, hashtag2);
		Integer coupling = couplesMap.get(pair);
		if (coupling == null)
			putPair(pair, new Integer(1));
		else
			putPair(pair, coupling + 1);
	}

	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets) {
		// this has no effect on this data query
	}

	@Override
	public List<String> getMostCoupled(int k) {
		List<String> res = new ArrayList<String>(k);
		Iterator< Pair<Pair<String,String>,Integer> > iter = couplesSet.descendingIterator();
		for (int i = 0; i < k; ++i) {
			if (!iter.hasNext())
				break;
			res.add(iter.next().left.toString());
		}
		return res;

	}

}
