package testAnalyzer;

import java.util.ArrayList;

import paramaterExtracter.PutInstanceVariable;
import pathExtracter.TraceMethodBlock;
import testMatcher.ExecutePath;

public class Test {

	private String methodName;
	private ArrayList<String> contents = new ArrayList<String>();
	private ArrayList<String> body = new ArrayList<String>();
	private ArrayList<TestMethod> methodLists = new ArrayList<TestMethod>();
	private ArrayList<TestAssertion> assertionLists = new ArrayList<TestAssertion>();
	private TraceMethodBlock traceMethodBlock = null;
	private ArrayList<TraceMethodBlock> traceMethodBlockLists = new ArrayList<TraceMethodBlock>();
	
	private ArrayList<PutInstanceVariable> putInstanceVariableLists = new ArrayList<PutInstanceVariable>();
	private ArrayList<ExecutePath> extractPathLists = new ArrayList<ExecutePath>();
	
	public ArrayList<ExecutePath> getExtractPathLists(){
		return extractPathLists;
	}
	
	public void addExtractPathLists(ExecutePath input) {
		extractPathLists.add(input);
	}
	
	public ArrayList<PutInstanceVariable> getPutInstanceVariableLists(){
		return putInstanceVariableLists;
	}
	
	public void addPutInstanceVaiableLists(PutInstanceVariable input) {
		putInstanceVariableLists.add(input);
	}
	
	public void setTraceMethodBlock(TraceMethodBlock input) {
		traceMethodBlock = input;
	}
	
	public void addTraceMethodBlockLists(TraceMethodBlock input) {
		traceMethodBlockLists.add(input);
	}
	
	public TraceMethodBlock getTraceMethodBlock() {
		return traceMethodBlock;
	}
	
	public ArrayList<TraceMethodBlock> getTraceMethodBlockLists(){
		return traceMethodBlockLists;
	}
	
	public void display() {
		System.out.println("methodName:" + methodName);
		
		for(int i = 0; i < contents.size(); i++) {
			System.out.println(contents.get(i));
		}
		
		System.out.println("******");
		
		for(int i = 0; i < methodLists.size(); i++) {
			methodLists.get(i).display();
		}
		
		System.out.println("******");
		
		for(int i = 0; i < assertionLists.size(); i++) {
			assertionLists.get(i).display();
		}
		
		System.out.println("******");
	}
	
	public void setMethodName(String input) {
		methodName = input;
	}
	
	public void addContents(String input) {
		contents.add(input);
	}
	
	public void addBody(String input) {
		body.add(input);
	}
	
	public void addMethodLists(TestMethod input) {
		methodLists.add(input);
	}
	
	public void addAssertionLists(TestAssertion input) {
		assertionLists.add(input);
	}

	public String getMethodName() {
		return methodName;
	}
	
	public ArrayList<String> getContents(){
		return contents;
	}
	
	public ArrayList<String> getBody(){
		return body;
	}
	
	public ArrayList<TestMethod> getMethodLists(){
		return methodLists;
	}
	
	public ArrayList<TestAssertion> getAssertionLists(){
		return assertionLists;
	}
}
