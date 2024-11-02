package paramaterExtracter;

import analyzer.AnalyzerVariable;
import tracer.ValueOption;

public class PutInstanceVariable {

	ValueOption targetInstanceValueOption = null;
	ValueOption putValueOption = null;
	AnalyzerVariable analyzerVariable = null;
	int seqNum;
	
	public PutInstanceVariable(AnalyzerVariable input, ValueOption target, ValueOption value, int sN) {
		analyzerVariable = input;
		targetInstanceValueOption = target;
		putValueOption = value;
		seqNum = sN;
	}
	
	public int getSeqNum() {
		return seqNum;
	}
	
	public AnalyzerVariable getAnalyzerVariable() {
		return analyzerVariable;
	}
	
	public ValueOption getTargetInstanceValueOption() {
		return targetInstanceValueOption;
	}
	
	public ValueOption getPutValueOption() {
		return putValueOption;
	}
}
