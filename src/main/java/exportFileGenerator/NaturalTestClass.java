package exportFileGenerator;

import java.util.ArrayList;

import testAnalyzer.Test;
import testAnalyzer.TestImport;

public class NaturalTestClass {

	private String packageName;
	private ArrayList<TestImport> importLists;
	private String className;
	private ArrayList<NaturalTest> matchingTestLists = new ArrayList<NaturalTest>();
	private ArrayList<NaturalTest> partiallyMatchingTestLists = new ArrayList<NaturalTest>();
	private ArrayList<Test> notMatchingEvoSuiteTestLists = new ArrayList<Test>();
	
	private ArrayList<String> contentLists = new ArrayList<String>();
	
	public NaturalTestClass(String pN, ArrayList<TestImport> iL, String cN) {
		packageName = pN;
		importLists = iL;
		className = cN;
	}
	
	public String getClassName() {
		return className;
	}
	
	public void createNaturalTest() {
		contentLists.add("package " + packageName + ";");
		
		contentLists.add("");
		
		for(int i = 0; i < importLists.size(); i++) {
			TestImport testImport = importLists.get(i);
			contentLists.add(testImport.getStatement());
		}
		
		contentLists.add("");
		
		contentLists.add("public class " + className + "{");
		
		contentLists.add("");
		
		contentLists.add("  " + "// matching test");
		for(int i = 0; i < matchingTestLists.size(); i++) {
			NaturalTest matchingTest = matchingTestLists.get(i);
			contentLists.add("  " + "@Test");
			contentLists.add("  " + "public void matchingTest" + i + "() {");
			ArrayList<String> matchingContents = matchingTest.getContentLists();
			for(int j = 0; j < matchingContents.size(); j++) {
				contentLists.add("      " + matchingContents.get(j));
			}
			contentLists.add("  " + "}");
			contentLists.add("");
		}
		
		contentLists.add("  " + "// partially matching test");
		for(int i = 0; i < partiallyMatchingTestLists.size(); i++) {
			NaturalTest matchingTest = partiallyMatchingTestLists.get(i);
			contentLists.add("  " + "@Test");
			contentLists.add("  " + "public void partiallyMatchingTest" + i + "() {");
			ArrayList<String> matchingContents = matchingTest.getContentLists();
			for(int j = 0; j < matchingContents.size(); j++) {
				contentLists.add("      " + matchingContents.get(j));
			}
			contentLists.add("  " + "}");
			contentLists.add("");
		}
		
		contentLists.add("  " + "// not match evosuite test");
		for(int i = 0; i < notMatchingEvoSuiteTestLists.size(); i++) {
			Test test = notMatchingEvoSuiteTestLists.get(i);
			ArrayList<String> evoContents = test.getContents();
			for(int j = 0; j < evoContents.size(); j++) {
				contentLists.add(evoContents.get(j));
			}
			contentLists.add("");
		}
		
		contentLists.add("}");
	}
	
	public ArrayList<String> getContentLists(){
		return contentLists;
	}
	
	public void addMatchingTestLists(NaturalTest input) {
		matchingTestLists.add(input);
	}
	
	public void addPartiallyMatcingTestLists(NaturalTest input) {
		partiallyMatchingTestLists.add(input);
	}
	
	public void addNotMatchingEvoSuiteTestLists(Test input) {
		notMatchingEvoSuiteTestLists.add(input);
	}
	
	
}
