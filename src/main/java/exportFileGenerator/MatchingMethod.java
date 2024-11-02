package exportFileGenerator;

import paramaterExtracter.ExtractMethod;
import testAnalyzer.TestMethod;

public class MatchingMethod {

	private TestMethod testMethod;
	private ExtractMethod extractMethod;
	
	public MatchingMethod(TestMethod tM, ExtractMethod eM) {
		testMethod = tM;
		extractMethod = eM;
	}
	
	public TestMethod getTestMethod() {
		return testMethod;
	}
	
	public ExtractMethod getExtractMethod() {
		return extractMethod;
	}
}
