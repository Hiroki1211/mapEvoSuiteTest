package testMatcher;

import java.util.ArrayList;

public class ExecutePath {

	private ArrayList<Integer> lineLists = new ArrayList<Integer>();
	private String mname = "";
	private String fileName = "";
	
	public ExecutePath(ArrayList<Integer> lists, String m, String f) {
		lineLists = lists;
		mname = m;
		fileName = f;
	}
	
	public void display() {
		System.out.println(mname + lineLists);
	}
	
	public ArrayList<Integer> getLineLists(){
		return lineLists;
	}
	
	public String getMname() {
		return mname;
	}
	
	public String getFileName() {
		return fileName;
	}
	
}
