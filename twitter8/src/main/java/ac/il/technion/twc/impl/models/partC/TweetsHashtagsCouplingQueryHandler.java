package ac.il.technion.twc.impl.models.partC;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import ac.il.technion.twc.api.interfaces.ITweetsRepository;
import ac.il.technion.twc.api.models.Tweet;
import ac.il.technion.twc.impl.services.partC.ITweetsHashtagsCouplingQueryHandler;

public class TweetsHashtagsCouplingQueryHandler implements ITweetsHashtagsCouplingQueryHandler{

	
	private static final long serialVersionUID = 4958073188101045085L;
	
	 public static class ValueComparator<T> implements Serializable, Comparator<Pair<T>> {

		private static final long serialVersionUID = -3909198429646950164L;
			Map<Pair<T>, Integer> base;
	        
	        public ValueComparator(Map<Pair<T>, Integer> base) {
	            this.base = base;
	        }

	        // Note: this comparator imposes orderings that are inconsistent with equals.    
	        public int compare(Pair<T> a, Pair<T> b) {
	            if (base.get(a) >= base.get(b)) {
	                return -1;
	            } else {
	                return 1;
	            } // returning 0 would merge keys
	        }
	    }

	private NavigableMap<Pair<String>, Integer> hashtagsPairs;

	public TweetsHashtagsCouplingQueryHandler()
	{
		this.hashtagsPairs = new TreeMap<Pair<String>, Integer>(new ValueComparator<>(hashtagsPairs));
	}

	
	
	@Override
	public void onTweetsAdded(List<Tweet> tweets,
			ITweetsRepository tweetsRepository) {
		for ( Tweet tweet : tweets){
			List<String> hashtags = tweet.getHashtags();
			for(int i=0; i<hashtags.size(); ++i)
				for(int j=i+1; j<hashtags.size(); ++j)
					incPair(hashtags.get(i), hashtags.get(j));
		}
	}

	private void incPair(String hashtag, String hashtag2) {
		Pair<String> pair = new Pair<String>(hashtag, hashtag2);
		Integer coupling = hashtagsPairs.get(pair);
		if(coupling == null)
			hashtagsPairs.put(pair, new Integer(1));
		else
			hashtagsPairs.put(pair, coupling + 1);
			
	}



	@Override
	public void onRootTweetsDataChanged(List<Tweet> changedRootTweets) {
		//this has no effect on this data query
	}



	@Override
	public List<Pair<String>> getMostCoupled(int k) {
		List<Pair<String>> res = new ArrayList<Pair<String>>(k);
		Iterator<Pair<String>> iter = hashtagsPairs.navigableKeySet().descendingIterator();
		for(int i=0; i<k; ++i){
			if(!iter.hasNext())
				break;
			res.add(iter.next());
		}
		return res;
			
	}

}
