package ac.il.technion.twc.impl.services.partC;

import java.util.List;

import ac.il.technion.twc.api.interfaces.IQueryHandler;
import ac.il.technion.twc.impl.models.partC.Pair;

public interface ITweetsHashtagsCouplingQueryHandler extends IQueryHandler{

	public abstract List<Pair<String>> getMostCoupled(int k);

}