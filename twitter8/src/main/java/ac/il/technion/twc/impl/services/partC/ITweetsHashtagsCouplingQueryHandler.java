package ac.il.technion.twc.impl.services.partC;

import java.util.List;

import ac.il.technion.twc.api.interfaces.IQueryHandler;

public interface ITweetsHashtagsCouplingQueryHandler extends IQueryHandler{

	public abstract List<String> getMostCoupled(int k);

}