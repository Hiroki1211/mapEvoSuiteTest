package executionPartGenerator;

import paramaterExtracter.ExtractMethod;
import testAnalyzer.TestMethod;

public class MatchMethod {

	private TestMethod testMethod;
	private ExtractMethod extractMethod;
	
	public MatchMethod(TestMethod testMethod, ExtractMethod extractMethod) {
		this.testMethod = testMethod;
		this.extractMethod = extractMethod;
	}
	
	public TestMethod getTestMethod() {
		return testMethod;
	}
	
	public ExtractMethod getExtractMethod() {
		return extractMethod;
	}
}
