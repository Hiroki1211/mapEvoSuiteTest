package pathExtracter;

import java.io.File;
import java.util.ArrayList;

import analyzer.Analyzer;
import testAnalyzer.TestAnalyzer;
import testAnalyzer.TestAssertion;
import testAnalyzer.TestClass;
import testAnalyzer.Test;
import tracer.Lexer;
import tracer.Trace;

public class PathExtracter {

	public static void main(String[] args) {
		PathExtracter pathExtracter = new PathExtracter();
		
		ArrayList<String> inputFileNameLists = pathExtracter.getInputFileNameLists();
		ArrayList<String> inputtestTestFileNameLists = pathExtracter.getInputtestTestFileNameLists();
		ArrayList<String> inputtestTestTraceFileNameLists = pathExtracter.getInputtestTraceFileNameLists();
		
		// product code analyze
		Analyzer analyzer = new Analyzer();
		analyzer.run(inputFileNameLists);
		
		// test code analyze
		TestAnalyzer testAnalyzer = new TestAnalyzer();
		ArrayList<TestClass> testClassLists = testAnalyzer.run(inputtestTestFileNameLists, analyzer.getMethodLists());
		
		// Extract test Path
		pathExtracter.testTestPathExtract(inputtestTestTraceFileNameLists, testClassLists);
	}
	
	public ArrayList<TestClass> run(Analyzer analyzer, ArrayList<String> inputTestFileNameLists, ArrayList<String> inputTraceFileNameLists){
		
		// test code analyze
		TestAnalyzer testAnalyzer = new TestAnalyzer();
		ArrayList<TestClass> testClassLists = testAnalyzer.run(inputTestFileNameLists, analyzer.getMethodLists());
		
		// Extract test Path
		this.testTestPathExtract(inputTraceFileNameLists, testClassLists);
		
		return testClassLists;
	}
	
	private ArrayList<String> getInputFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("src/main/resources/ex06/code/Human.java");
		result.add("src/main/resources/ex06/code/Executer.java");
		
		return result;
	}
	
	private ArrayList<String> getInputtestTestFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("src/main/resources/ex06/evoSuiteTest/Human_ESTest.java");
		result.add("src/main/resources/ex06/evoSuiteTest/Executer_ESTest.java");
		
		return result;
	}
	
	private ArrayList<String> getInputtestTraceFileNameLists(){
		ArrayList<String> result = new ArrayList<String>();
		result.add("src/main/resources/ex06/evoSuiteTrace/Human_ESTest.json");
		result.add("src/main/resources/ex06/evoSuiteTrace/Executer_ESTest.json");
		
		return result;
	}
	
	public ArrayList<TraceMethodBlock> getTraceMethodBlockLists(ArrayList<Trace> traceLists){
		ArrayList<TraceMethodBlock> traceMethodBlockLists = new ArrayList<TraceMethodBlock>();
		TraceMethodBlock traceMethodBlock = null;
		
		for(int traceNum = 0; traceNum < traceLists.size(); traceNum++) {
			Trace trace = traceLists.get(traceNum);
			if(trace.getEvent().equals("METHOD_ENTRY")) {
				traceMethodBlock = new TraceMethodBlock();
				traceMethodBlockLists.add(traceMethodBlock);
			}
			
			traceMethodBlock.addTraceLists(trace);
		}
		
//		for(int i = 0; i < traceMethodBlockLists.size(); i++) {
//			traceMethodBlock = traceMethodBlockLists.get(i);
//			Trace trace = traceMethodBlock.getTraceLists().get(0);
//			System.out.println(trace.getCname() + ":" + trace.getMname() + ":" + trace.getSeqNum() + ":" + trace.getLine());
//		}
		
		for(int i = 0; i < traceMethodBlockLists.size(); i++) {
			for(int j = 0; j < traceMethodBlockLists.size() - 1; j++) {
				TraceMethodBlock frontTraceMethodBlock = traceMethodBlockLists.get(j);
				TraceMethodBlock backTraceMethodBlock = traceMethodBlockLists.get(j + 1);
				
				int frontSeqNum = frontTraceMethodBlock.getTraceLists().get(0).getSeqNum();
				int backSeqNum = backTraceMethodBlock.getTraceLists().get(0).getSeqNum();
				
				if(frontSeqNum > backSeqNum) {
					traceMethodBlockLists.set(j, backTraceMethodBlock);
					traceMethodBlockLists.set(j+1, frontTraceMethodBlock);
				}
				
			}
		}
		
//		for(int i = 0; i < traceMethodBlockLists.size(); i++) {
//			System.out.print(traceMethodBlockLists.get(i).getTraceLists().get(0).getSeqNum() +  ", ");
//		}
//		System.out.println();
		
		return traceMethodBlockLists;
	}
	
	public void testTestPathExtract(ArrayList<String> inputTraceFileNameLists, ArrayList<TestClass> testClassLists) {
		for(int inputFileNum = 0; inputFileNum < inputTraceFileNameLists.size(); inputFileNum++) {
			File file = new File(inputTraceFileNameLists.get(inputFileNum));
			Lexer lexer = new Lexer(file);
			ArrayList<Trace> traceLists = lexer.getTraceLists();
			ArrayList<TraceMethodBlock> traceMethodBlockLists = this.getTraceMethodBlockLists(traceLists);
			ArrayList<Integer> borderSeqNumLists = new ArrayList<Integer>();
			
			// detect testClass
			TestClass testClass = null;
			TraceMethodBlock firstTraceMethodBlock = traceMethodBlockLists.get(0);
			Trace firstTrace = firstTraceMethodBlock.getTraceLists().get(0);
			for(int testClassNum = 0; testClassNum < testClassLists.size(); testClassNum++) {
				if(firstTrace.getCname().contains(testClassLists.get(testClassNum).getClassName())) {
					testClass = testClassLists.get(testClassNum);
				}
			}
			
			// 1. test のテスト自体の登録
			for(int blockNum = 0; blockNum < traceMethodBlockLists.size(); blockNum++) {
				TraceMethodBlock traceMethodBlock = traceMethodBlockLists.get(blockNum);
				Trace trace = traceMethodBlock.getTraceLists().get(0);
				String methodName = trace.getMname();
				ArrayList<Test> testTestLists = testClass.getTestLists();
				
				for(int testNum = 0; testNum < testTestLists.size(); testNum++) {
					Test testTest = testTestLists.get(testNum);
					if(testTest.getMethodName().equals(methodName)) {
						testTest.setTraceMethodBlock(traceMethodBlock);
						borderSeqNumLists.add(trace.getSeqNum());
						break;
					}
				}
			}
			
//			System.out.println(testClass.getClassName());
//			System.out.println("border:" + borderSeqNumLists);
			
			// testの並び替え
			ArrayList<Test> testLists = testClass.getTestLists();
			for(int i = 0; i < testLists.size(); i++) {
				for(int j = 0; j < testLists.size() - 1; j++) {
					int frontTestSeqNum = testLists.get(j).getTraceMethodBlock().getTraceLists().get(0).getSeqNum();
					int backTestSeqNum = testLists.get(j+1).getTraceMethodBlock().getTraceLists().get(0).getSeqNum();
					
					if(frontTestSeqNum > backTestSeqNum) {
						Test frontTest = testLists.get(j);
						Test backTest = testLists.get(j+1);
						testLists.set(j, backTest);
						testLists.set(j+1, frontTest);
					}
				}
			}
			
			for(int i = 0; i < borderSeqNumLists.size(); i++) {
				for(int j = 0; j < borderSeqNumLists.size() - 1; j++) {
					int frontSeqNum = borderSeqNumLists.get(j);
					int backSeqNum = borderSeqNumLists.get(j+1);
					
					if(frontSeqNum > backSeqNum) {
						borderSeqNumLists.set(j, backSeqNum);
						borderSeqNumLists.set(j+1, frontSeqNum);
					}
				}
			}
			
//			System.out.println("border:" + borderSeqNumLists);
			
			// 2. test のメソッドの振り分け
			for(int blockNum = 0; blockNum < traceMethodBlockLists.size(); blockNum++) {
				TraceMethodBlock traceMethodBlock = traceMethodBlockLists.get(blockNum);
				Trace trace = traceMethodBlock.getTraceLists().get(0);
				if(trace.getFilename().contains(testClass.getClassName())) {
					continue;
				}
				
				// System.out.println("a:" + trace.getFilename() + ":" + trace.getMname() + ":" + trace.getSeqNum() + ":" + trace.getLine());
				
				for(int borderNum = 0; borderNum < borderSeqNumLists.size(); borderNum++) {
					if(borderNum == borderSeqNumLists.size() - 1) {
						if(borderSeqNumLists.get(borderNum) >= trace.getSeqNum()) {
							break;
						}
						
						Test testTest = testClass.getTestLists().get(borderNum);
						testTest.addTraceMethodBlockLists(traceMethodBlock);
					}else{
						if(trace.getSeqNum() < borderSeqNumLists.get(borderNum + 1) && trace.getSeqNum() > borderSeqNumLists.get(borderNum)) {
							Test testTest = testClass.getTestLists().get(borderNum);
							testTest.addTraceMethodBlockLists(traceMethodBlock);
							break;
						}else if(trace.getSeqNum() == borderSeqNumLists.get(borderNum)) {
							break;
						}
					}
				}
			}
			
//			System.out.println("method block:");
//			for(int i = 0; i < testClass.getTestLists().size(); i++) {
//				testTest testTest = testClass.getTestLists().get(i);
//				System.out.println(testTest.getMethodName());
//				
//				for(int j = 0; j < testTest.getTraceMethodBlockLists().size(); j++) {
//					TraceMethodBlock traceMethodBlock = testTest.getTraceMethodBlockLists().get(j);
//					System.out.println("\t" + traceMethodBlock.getTraceLists().get(0).getMname() + ":" + traceMethodBlock.getTraceLists().get(0).getSeqNum());
//				}
//			}
			
			// 3. not need method delete
			ArrayList<Test> testTestLists = testClass.getTestLists();
			for(int testNum = 0; testNum < testTestLists.size(); testNum++) {
				int assertSeqNum = 0;
				Test testTest = testTestLists.get(testNum);
				ArrayList<Trace> testTraceLists = testTest.getTraceMethodBlock().getTraceLists();
				
				for(int testTraceNum = 0; testTraceNum < testTraceLists.size(); testTraceNum++) {
					Trace trace = testTraceLists.get(testTraceNum);
					
					if(trace.getEvent().equals("CALL") && trace.getAttr().getName().equals("assertEquals")) {
						assertSeqNum = trace.getSeqNum();
						break;
					}
				}
				
				if(assertSeqNum != 0) {
					ArrayList<TraceMethodBlock> assertTraceMethodBlockLists = testTest.getTraceMethodBlockLists();
					int traceMethodBlockLength = assertTraceMethodBlockLists.size();
					for(int blockNum = 0; blockNum < traceMethodBlockLength; blockNum++) {
						if(assertTraceMethodBlockLists.get(blockNum).getTraceLists().get(0).getSeqNum() > assertSeqNum) {
							assertTraceMethodBlockLists.remove(blockNum);
							blockNum -= 1;
							traceMethodBlockLength -=1;
						}
					}
				}
				
			}
			
			// delete assertion first method
			for(int testNum = 0; testNum < testClass.getTestLists().size(); testNum++) {
				Test testTest = testClass.getTestLists().get(testNum);
				
				if(testTest.getAssertionLists().size() > 0) {
					TestAssertion testAssertion = testTest.getAssertionLists().get(0);
					if(testAssertion.getVariable().equals("")) {
						int lastMethodBlockNum = 0;
						int lastMethodBlockSeqNum = 0;
						for(int blockNum = 0; blockNum < testTest.getTraceMethodBlockLists().size(); blockNum++) {
							if(testTest.getTraceMethodBlockLists().get(blockNum).getTraceLists().get(0).getSeqNum() > lastMethodBlockSeqNum) {
								lastMethodBlockNum = blockNum;
								lastMethodBlockSeqNum = testTest.getTraceMethodBlockLists().get(blockNum).getTraceLists().get(0).getSeqNum();
							}
						}
						
						testTest.getTraceMethodBlockLists().remove(lastMethodBlockNum);
					}
				}
			}
			
			// sort Method Block
			for(int testNum = 0; testNum < testClass.getTestLists().size(); testNum++) {
				Test testTest = testClass.getTestLists().get(testNum);
				
				ArrayList<TraceMethodBlock> sortTraceMethodBlockLists = testTest.getTraceMethodBlockLists();
				for(int i = 0; i < sortTraceMethodBlockLists.size(); i++) {
					for(int j = 0; j < sortTraceMethodBlockLists.size() - 1; j++) {
						TraceMethodBlock frontTraceMethodBlock = sortTraceMethodBlockLists.get(j);
						TraceMethodBlock backTraceMethodBlock = sortTraceMethodBlockLists.get(j+1);
						
						if(frontTraceMethodBlock.getTraceLists().get(0).getSeqNum() > backTraceMethodBlock.getTraceLists().get(0).getSeqNum()) {
							sortTraceMethodBlockLists.set(j, backTraceMethodBlock);
							sortTraceMethodBlockLists.set(j+1, frontTraceMethodBlock);
						}
					}
				}
			}
			
//			System.out.println("method block:");
//			for(int i = 0; i < testClass.getTestLists().size(); i++) {
//				Test testTest = testClass.getTestLists().get(i);
//				System.out.println(testTest.getMethodName());
//				
//				for(int j = 0; j < testTest.getTraceMethodBlockLists().size(); j++) {
//					TraceMethodBlock traceMethodBlock = testTest.getTraceMethodBlockLists().get(j);
//					System.out.println("\t" + traceMethodBlock.getTraceLists().get(0).getMname() + ":" + traceMethodBlock.getTraceLists().get(0).getSeqNum());
//				}
//			}
		}
	}
}
