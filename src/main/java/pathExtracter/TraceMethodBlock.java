package pathExtracter;

import java.util.ArrayList;

import tracer.Trace;

public class TraceMethodBlock {

	private ArrayList<Trace> traceLists = new ArrayList<Trace>();
	
	public void addTraceLists(Trace trace) {
		traceLists.add(trace);
	}
	
	public ArrayList<Trace> getTraceLists(){
		return traceLists;
	}
}
