package testMatcher;

import java.util.ArrayList;

import paramaterExtracter.ExtractClass;
import paramaterExtracter.ExtractMethod;
import paramaterExtracter.Instance;
import testAnalyzer.Test;
import testAnalyzer.TestClass;
import tracer.ValueOption;

public class Result {

	private TestClass evoSuiteTestClass;
	private ExtractClass extractClass;
	private ArrayList<MatchingResult> matchingResultLists = new ArrayList<MatchingResult>();
	private ArrayList<PartiallyMatchingResult> partiallyMatchingResultLists = new ArrayList<PartiallyMatchingResult>();
	private ArrayList<SameExecutePath> notMatchingEvoSuiteLists = new ArrayList<SameExecutePath>();
	private ArrayList<SameExecuteExtractPath> notMatchingExtractLists = new ArrayList<SameExecuteExtractPath>();
	
	public Result(TestClass eSTC) {
		evoSuiteTestClass = eSTC;
	}
	
	public void display() {
		System.out.println(evoSuiteTestClass.getClassName());
		
		// matching
		for(int matchingNum = 0; matchingNum < matchingResultLists.size(); matchingNum++) {
			MatchingResult matchingResult = matchingResultLists.get(matchingNum);
			System.out.println("====matching result====");
			
			SameExecutePath sameExecutePath = matchingResult.getSameExecuteEvoSuitePath();
			ArrayList<Test> testLists  = sameExecutePath.getTestLists();
			System.out.println("EvoSuite:");
			for(int testNum = 0; testNum < testLists.size(); testNum++) {
				System.out.print(testLists.get(testNum).getMethodName() + ", ");
			}
			System.out.println();
			
			SameExecuteExtractPath sameExecuteExtractPath = matchingResult.getSameExecuteExtractPath();
			ArrayList<Instance> instanceLists = sameExecuteExtractPath.getInstanecLists();
			System.out.println("Extract:");
			for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
				Instance instance = instanceLists.get(instanceNum);
				ArrayList<ExtractMethod> extractMethodLists = instance.getExtractMethodLists();
				for(int methodNum = 0; methodNum < extractMethodLists.size(); methodNum++) {
					ExtractMethod extractMethod = extractMethodLists.get(methodNum);
					System.out.print(extractMethod.getMethodName() + "(");
					ArrayList<ValueOption> argumentLists = extractMethod.getArgmentLists();
					if(argumentLists.size() == 0) {
						System.out.print(")");
					}
					for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
						if(argNum == argumentLists.size() - 1) {
							System.out.print(argumentLists.get(argNum).getValue() + ")");
						}else {
							System.out.print(argumentLists.get(argNum).getValue() + ", ");
						}
					}
					System.out.print(", ");
				}
				System.out.println();
			}
		}
		System.out.println();
		
		// partially Matching 
		for(int parNum = 0; parNum < partiallyMatchingResultLists.size(); parNum++) {
			PartiallyMatchingResult partiallyMatchingResult = partiallyMatchingResultLists.get(parNum);
			System.out.println("----partially matching result----");
			SameExecutePath sameExecutePath = partiallyMatchingResult.getSameExecuteEvoSuitePath();
			ArrayList<Test> testLists = sameExecutePath.getTestLists();
			System.out.println("EvoSuite:");
			for(int testNum = 0; testNum < testLists.size(); testNum++) {
				Test test = testLists.get(testNum);
				System.out.print(test.getMethodName() + ", ");
			}
			System.out.println();
			
			ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists = partiallyMatchingResult.getPartiallyMatchingExtractPathLists();
			System.out.println("Extract:");
			for(int pathNum = 0; pathNum <  sameExecuteExtractPathLists.size(); pathNum++) {
				SameExecuteExtractPath sameExecuteExtractPath = sameExecuteExtractPathLists.get(pathNum);
				ArrayList<Instance> instanceLists = sameExecuteExtractPath.getInstanecLists();
				for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
					Instance instance = instanceLists.get(instanceNum);
					ArrayList<ExtractMethod> extractMethodLists = instance.getExtractMethodLists();
					for(int methodNum = 0; methodNum < extractMethodLists.size(); methodNum++) {
						ExtractMethod extractMethod = extractMethodLists.get(methodNum);
						System.out.print(extractMethod.getMethodName() + "(");
						ArrayList<ValueOption> argumentLists = extractMethod.getArgmentLists();
						if(argumentLists.size() == 0) {
							System.out.print(")");
						}
						for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
							if(argNum == argumentLists.size() - 1) {
								System.out.print(argumentLists.get(argNum).getValue() + ")");
							}else {
								System.out.print(argumentLists.get(argNum).getValue() + ", ");
							}
						}
						System.out.print(", ");
					}
					System.out.println();
				}
			}
		}
		System.out.println();
		
		// not matching evoSuite
		for(int notEvoNum = 0; notEvoNum < notMatchingEvoSuiteLists.size(); notEvoNum++) {
			SameExecutePath sameExecutePath = notMatchingEvoSuiteLists.get(notEvoNum);
			System.out.println("****not matching EvoSuite****");
			ArrayList<Test> testLists = sameExecutePath.getTestLists();
			System.out.println("EvoSuite:");
			for(int testNum = 0; testNum < testLists.size(); testNum++) {
				Test test = testLists.get(testNum);
				System.out.print(test.getMethodName() + ", ");
			}
			System.out.println();
		}
		System.out.println();
		
		// not matching extract
		for(int notExtractNum = 0; notExtractNum < notMatchingExtractLists.size(); notExtractNum++) {
			SameExecuteExtractPath sameExecuteExtractPath = notMatchingExtractLists.get(notExtractNum);
			System.out.println("++++not matching Extract++++");
			ArrayList<Instance> instanceLists = sameExecuteExtractPath.getInstanecLists();
			for(int instanceNum = 0; instanceNum < instanceLists.size(); instanceNum++) {
				Instance instance = instanceLists.get(instanceNum);
				ArrayList<ExtractMethod> extractMethodLists = instance.getExtractMethodLists();
				for(int methodNum = 0; methodNum < extractMethodLists.size(); methodNum++) {
					ExtractMethod extractMethod = extractMethodLists.get(methodNum);
					System.out.print(extractMethod.getMethodName() + "(");
					ArrayList<ValueOption> argumentLists = extractMethod.getArgmentLists();
					for(int argNum = 0; argNum < argumentLists.size(); argNum++) {
						if(argNum == argumentLists.size() - 1) {
							System.out.print(argumentLists.get(argNum).getValue() + ")");
						}else {
							System.out.print(argumentLists.get(argNum).getValue() + ", ");
						}
					}
					System.out.print(", ");
				}
				System.out.println();
			}
			
		}
	}
	
	public void setExtractClass(ExtractClass input) {
		extractClass = input;
	}
	
	public void addMatchingResultLists(MatchingResult input) {
		matchingResultLists.add(input);
	}
	
	public void addPartiallyMatchingResultLists(PartiallyMatchingResult input) {
		partiallyMatchingResultLists.add(input);
	}
	
	public void addNotMatchingEvoSuiteLists(SameExecutePath input) {
		notMatchingEvoSuiteLists.add(input);
	}
	
	public void addNotMatchingExtractLists(SameExecuteExtractPath input) {
		notMatchingExtractLists.add(input);
	}
	
	public TestClass getEvoSuiteTestClass() {
		return evoSuiteTestClass;
	}
	
	public ExtractClass getExtractClass() {
		return extractClass;
	}
	
	public ArrayList<MatchingResult> getMatchingResultLists(){
		return matchingResultLists;
	}
	
	public ArrayList<PartiallyMatchingResult> getPartiallyMatchingResultLists(){
		return partiallyMatchingResultLists;
	}
	
	public ArrayList<SameExecutePath> getNotMatchingEvoSuiteLists() {
		return notMatchingEvoSuiteLists;
	}
	
	public ArrayList<SameExecuteExtractPath> getNotMatchingExtractLists(){
		return notMatchingExtractLists;
	}
}
