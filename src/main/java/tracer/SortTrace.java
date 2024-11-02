package tracer;

import java.util.ArrayList;

public class SortTrace {

	private String fileName;
	private String mname;
	private ArrayList<Trace> traceLists = new ArrayList<Trace>();
	
	public SortTrace(String f, String m) {
		fileName = f;
		mname = m;
	}
	
	public void sortTraceLists() {
		for(int i = 0; i < traceLists.size(); i++) {
			for(int j = 0; j < traceLists.size() - 1; j++) {
				Trace frontTrace = traceLists.get(j);
				Trace backTrace = traceLists.get(j+1);
				
				if(frontTrace.getSeqNum() > backTrace.getSeqNum()) {
					traceLists.set(j, backTrace);
					traceLists.set(j+1, frontTrace);
				}
			}
		}
	}
	
	public void addTraceLists(Trace trace) {
		traceLists.add(trace);
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public String getMname() {
		return mname;
	}
	
	public ArrayList<Trace> getTraceLists(){
		return traceLists;
	}
}
