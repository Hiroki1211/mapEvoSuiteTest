package testMatcher;

import java.util.ArrayList;

public class PartiallyMatchingResult {

	private SameExecutePath sameExecuteEvoSuitePath;
	private ArrayList<SameExecuteExtractPath> partiallyMatchingExtractPathLists;
	
	public PartiallyMatchingResult(SameExecutePath sEESP, ArrayList<SameExecuteExtractPath> input) {
		sameExecuteEvoSuitePath = sEESP;
		partiallyMatchingExtractPathLists = input;
	}
	
	public SameExecutePath getSameExecuteEvoSuitePath() {
		return sameExecuteEvoSuitePath;
	}
	
	public ArrayList<SameExecuteExtractPath> getPartiallyMatchingExtractPathLists() {
		return partiallyMatchingExtractPathLists;
	}
}
