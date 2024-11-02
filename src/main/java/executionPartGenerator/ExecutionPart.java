package executionPartGenerator;

import java.util.ArrayList;

import testAnalyzer.Test;
import testAnalyzer.TestMethod;

public class ExecutionPart {

	private ArrayList<TestMethod> methodLists = new ArrayList<TestMethod>();
	private ArrayList<String> assertionTarget = new ArrayList<String>();
	private ArrayList<String> assertionValueLists = new ArrayList<String>();
	private Test test;
	
	public ExecutionPart(Test test) {
		this.test = test;
	}
	
	public void addMethodLists(TestMethod testMethod) {
		methodLists.add(testMethod);
	}
	
	public void addAssertionTarget(String input) {
		assertionTarget.add(input);
	}
	
	public void addAssertionValueLists(String input) {
		assertionValueLists.add(input);
	}
	
	public ArrayList<TestMethod> getMethodLists(){
		return methodLists;
	}
	
	public ArrayList<String> getAssertionTarget(){
		return assertionTarget;
	}
	
	public ArrayList<String> getAssertionValueLists(){
		return assertionValueLists;
	}
	
	public Test getTest() {
		return test;
	}
}
