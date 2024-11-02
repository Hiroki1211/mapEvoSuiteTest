package tracer;

public class Trace {

	private String loadedFrom;
	private String filename;
	private String cname;
	private String mname;
	private String mdesc;
	private String mhash;
	private int line;
	private int inst;
	private String event;
	private Attr attr = new Attr();
	private String valuetype;
	private int freq;
	private int record;
	private ValueOption valueOption = null;
	private int seqNum;
	private String thread;
	
	public void displayContent() {
		System.out.println("loadedFrom:" + loadedFrom + ", " + "filename:" + filename + ", " + "cname:" + cname + ", " + "mname:" + mname + ", " + "mdesc:" + mdesc + ", " + "mhash:" + mhash + ", " + "line:" + line + ", " + "inst:" + inst + ", " + "event:" + event + ", ");
		attr.displayContent();
		System.out.println("valuetype:" + valuetype + ", " + "freq:" + freq + ", " + "record:" + record + ", " );
		if(valueOption != null){
			valueOption.display();
		}
		System.out.println("seqnum:" + seqNum + ", " + "thread:" + thread);
		System.out.println();
	}

	public void setLoadedFrom(String input) {
		loadedFrom = input;
	}
	
	public void setFilename(String input) {
		filename = input;
	}
	
	public void setCname(String input) {
		cname = input;
	}
	
	public void setMname(String input) {
		mname = input;
	}
	
	public void setMdesc(String input) {
		mdesc = input;
	}
	
	public void setMhash(String input) {
		mhash = input;
	}
	
	public void setLine(int input) {
		line = input;
	}
	
	public void setInst(int input) {
		inst = input;
	}
	
	public void setEvent(String input) {
		event = input;
	}
	
	public void setAttr(Attr input) {
		attr = input;
	}
	
	public void setValuetype(String input) {
		valuetype = input;
	}
	
	public void setFreq(int input) {
		freq = input;
	}
	
	public void setRecord(int input) {
		record = input;
	}
	
	public void setValueOption(ValueOption input) {
		valueOption = input;
	}
	
	public void setSeqNum(int input) {
		seqNum = input;
	}
	
	public void setThread(String input) {
		thread = input;
	}
	
	public String getLoadedFrom() {
		return loadedFrom;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getCname() {
		return cname;
	}
	
	public String getMname() {
		return mname;
	}
	
	public String getMdesc() {
		return mdesc;
	}
	
	public String getMhash() {
		return mhash;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getInst() {
		return inst;
	}
	
	public String getEvent() {
		return event;
	}
	
	public Attr getAttr() {
		return attr;
	}
	
	public String getValuetype() {
		return valuetype;
	}
	
	public int getFreq() {
		return freq;
	}
	
	public int getRecord() {
		return record;
	}
	
	public ValueOption getValueOption() {
		return valueOption;
	}
	
	public int getSeqNum() {
		return seqNum;
	}
	
	public String getThread() {
		return thread;
}
	
}
