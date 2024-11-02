package testAnalyzer;

import java.util.ArrayList;
import java.util.regex.Pattern;

import analyzer.AnalyzerMethod;
import pathExtracter.TraceMethodBlock;
import testMatcher.ExecutePath;
import tracer.ValueOption;

public class TestMethod {

	private String returnType = "";
	private String returnVariable = "";
	private String instance = "";
	private String methodName = "";
	private String constructorClass = "";
	private ArrayList<String> argumentLists = new ArrayList<String>();
	
	private String statement = "";
	
	private AnalyzerMethod analyzerMethod = null;
	
	private ValueOption returnValueOption = null;
	
	private TraceMethodBlock traceMethodBlock = null;
	private ExecutePath executePath = null;
	
	public void setExecutePath(ExecutePath input) {
		executePath = input;
	}
	
	public ExecutePath getExecutePath() {
		return executePath;
	}
	
	public TraceMethodBlock getTraceMethodBlock() {
		return traceMethodBlock;
	}
	
	public void setTraceMethodBlock(TraceMethodBlock input) {
		traceMethodBlock = input;
	}
	
	public ValueOption getReturnValueOption() {
		return returnValueOption;
	}
	
	public void setReturnValueOption(ValueOption input) {
		returnValueOption = input;
	}
	
	public void display() {
		System.out.println("returnType:" + returnType);
		System.out.println("returnVariable:" + returnVariable);
		System.out.println("instance:" + instance);
		System.out.println("methodName:" + methodName);
		System.out.println("constructorClass:" + constructorClass);
		System.out.println("argumentLists:" + argumentLists);
		System.out.println("statement:" + statement);
		System.out.println();
	}
	
	public TestMethod(String input, String mN) {
		statement = input;
		methodName = mN;
	}
	
	public TestMethod(String input) {
		statement = input;
//		System.out.println(input);
		String[] splitBracket = input.split("[()]", 2);
		if(splitBracket.length > 1) {
			splitBracket[1] = splitBracket[1].replace(");", "");
//			System.out.println(splitBracket[1]);
		}
		String[] splitSpace = splitBracket[0].split(" +");		
		
//		for(int i = 0; i < splitSpace.length; i++) {
//			System.out.print(splitSpace[i] + ", ");
//		}
//		System.out.println();
		
		if(splitSpace[1].contains(".")) {
			String[] split = splitSpace[1].split(Pattern.quote("."));
			instance = split[0];
			methodName = split[1];
		}else {
			if(splitSpace.length == 2) {
				methodName = splitSpace[1];
			}else {
				returnType = splitSpace[1];
				returnVariable = splitSpace[2];
				
				if(splitSpace[4].equals("new")) {
					methodName = "<init>";
					constructorClass = splitSpace[5];
				}else {
					String[] splitQuote = splitSpace[4].split(Pattern.quote("."));
					if(splitQuote.length != 2) {
						instance = "Null";
						methodName = "Null";
						constructorClass = "Null";
					}else {
						instance = splitQuote[0];
						methodName = splitQuote[1];
					}

				}
			}
		}
		
		if(splitBracket.length > 1 && !splitBracket[1].equals("")){
			splitBracket[1] = splitBracket[1].replace(" ", "");
			String[] splitArgument = splitBracket[1].split(",");
			
			for(int argNum = 0; argNum < splitArgument.length; argNum++) {
				String arg = splitArgument[argNum].replace(" ", "");
				arg = arg.replace("(", "");
				arg = arg.replace(")", "");
				argumentLists.add(arg);
			}
		}
	}
	
	public void setAnalyzerMethod(AnalyzerMethod input) {
		analyzerMethod = input;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public String getReturnVariable() {
		return returnVariable;
	}
	
	public String getInstance() {
		return instance;
	}
	
	public String getMethodName() {
		return methodName;
	}
	
	public String getConstructorClass() {
		return constructorClass;
	}
	
	public ArrayList<String> getArgumentLists(){
		return argumentLists;
	}
	
	public String getStatement() {
		return statement;
	}
	
	public AnalyzerMethod getAnalyzerMethod() {
		return analyzerMethod;
	}
}
