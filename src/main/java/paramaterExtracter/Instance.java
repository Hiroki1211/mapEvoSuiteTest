package paramaterExtracter;

import java.util.ArrayList;

import pathExtracter.TraceMethodBlock;
import testMatcher.ExecutePath;
import tracer.ValueOption;

public class Instance {

	private String id;
	private String ownerClass;
	private ArrayList<ExtractMethod> extractMethodLists = new ArrayList<ExtractMethod>();
	private ArrayList<PutInstanceVariable> putInstanceVariableLists = new ArrayList<PutInstanceVariable>();
	private ArrayList<TraceMethodBlock> traceMethodBlockLists = new ArrayList<TraceMethodBlock>();
	
	private ArrayList<ExecutePath> executePathLists = new ArrayList<ExecutePath>();
	
	public Instance(String s, String oC) {
		id = s;
		ownerClass = oC;
	}
	
	public void addExecutePathLists(ExecutePath input) {
		executePathLists.add(input);
	}
	
	public ArrayList<ExecutePath> getExecutePathLists(){
		return executePathLists;
	}
	
	public void display() {
		System.out.println(id);
		for(int i = 0; i < extractMethodLists.size(); i++) {
			System.out.print(extractMethodLists.get(i).getMethodName() + ":");
			ArrayList<ValueOption> argLists = extractMethodLists.get(i).getArgmentLists();
			for(int j = 0; j < argLists.size(); j++) {
				System.out.print(argLists.get(j).getValue() + ", ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public String getId() {
		return id;
	}
	
	public String getOwnerClass() {
		return ownerClass;
	}
	
	public ArrayList<ExtractMethod> getExtractMethodLists(){
		return extractMethodLists;
	}
	
	public ArrayList<PutInstanceVariable> getPutInstanceVariableLists(){
		return putInstanceVariableLists;
	}
	
	public ArrayList<TraceMethodBlock> getTraceMethodBlockLists(){
		return traceMethodBlockLists;
	}
	
	public void addExtractMethodLists(ExtractMethod input) {
		extractMethodLists.add(input);
	}
	
	public void addPutInstanceVariableLists(PutInstanceVariable input) {
		putInstanceVariableLists.add(input);
	}
	
	public void addTraceMethodBlockLists(TraceMethodBlock input) {
		traceMethodBlockLists.add(input);
	}
}
