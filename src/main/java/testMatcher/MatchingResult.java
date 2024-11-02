package testMatcher;

public class MatchingResult {

	private SameExecutePath sameExecuteEvoSuitePath;
	private SameExecuteExtractPath sameExecuteExtractPath;
	
	public MatchingResult(SameExecutePath sEESP, SameExecuteExtractPath sEEP) {
		sameExecuteEvoSuitePath = sEESP;
		sameExecuteExtractPath = sEEP;
	}
	
	public SameExecutePath getSameExecuteEvoSuitePath() {
		return sameExecuteEvoSuitePath;
	}
	
	public SameExecuteExtractPath getSameExecuteExtractPath() {
		return sameExecuteExtractPath;
	}
}
