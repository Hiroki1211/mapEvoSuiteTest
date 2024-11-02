package paramaterExtracter;

import java.util.ArrayList;

import testMatcher.SameExecuteExtractPath;

public class ExtractClass {

	private String ownerClass;
	private ArrayList<Instance> instanceLists = new ArrayList<Instance>();
	
	private ArrayList<SameExecuteExtractPath> sameExecuteExtractPathLists = new ArrayList<SameExecuteExtractPath>();
	
	public ExtractClass(String oC) {
		ownerClass = oC;
	}
	
	public void addInstanceLists(Instance input) {
		instanceLists.add(input);
	}
	
	public void setSameExecuteExtractPathLists(ArrayList<SameExecuteExtractPath> input) {
		sameExecuteExtractPathLists = input;
	}
	
	public String getOwnerClass() {
		return ownerClass;
	}
	
	public ArrayList<Instance> getInstanceLists(){
		return instanceLists;
	}
	
	public ArrayList<SameExecuteExtractPath> getSameExcuteExtractPathLists(){
		return sameExecuteExtractPathLists;
	}
}
